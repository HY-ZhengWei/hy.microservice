package org.hy.microservice.common.operationLog;

import org.hy.common.Help;
import org.hy.common.Queue;
import org.hy.common.app.Param;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;





/**
 * 操作日志的缓存处理机制
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-08-11
 * @version     v1.0
 *              v2.0  2024-01-04  添加：每次写入操作日志后释放CPU资源的间隔时长（单位：毫秒）
 *              v3.0  2026-02-15  添加：多线程写入日志
 */
@Xjava
public class OperationLogCache
{
    
    private static final Logger              $Logger                    = new Logger(OperationLogCache.class);
    
    /** 日志缓存 */
    private static final Queue<OperationLog> $LogCaches                 = new Queue<OperationLog>();
    
    
    
    /** 追加Insert处理环节的状态值 */
    public  static final int                 $C2DB_Append_Insert        = 1;
    
    /** 追加Update处理环节的状态值 */
    public  static final int                 $C2DB_Append_Update        = 10;
    
    
    
    /**  1: 日志首次添加到缓存，准备Insert到DB */
    public  static final int                 $C2DB_FirstCache           = 1;
    
    /**  2: 成功Insert到DB */
    public  static final int                 $C2DB_InsertDB             = 2;
    
    /** 11: 日志二次添加到缓存，还未Insert到DB，准备Update到DB */
    public  static final int                 $C2DB_SecondCache_NoInsert = 11;
    
    /** 12: 日志二次添加到缓存，成功Insert到DB，准备Update到DB */
    public  static final int                 $C2DB_SecondCache          = 12;
    
    /** 21: 还未Insert到DB、Update改为Insert到DB成功 */
    public  static final int                 $C2DB_FinishInsert         = 21;
    
    /** 22: 成功Insert到DB，成功Update到DB */
    public  static final int                 $C2DB_FinishUpdate         = 22;
    
    
    
    static
    {
        XJava.putObject("MS_Common_OperationLogCache" ,$LogCaches);
    }
    
    
    
    /** 日志写入任务的最大线程数量 */
    @Xjava(ref="MS_Common_OperationLog_MaxThreadCount")
    Param                     maxThreadCount;
    
    /** 每次写入操作日志后释放CPU资源的间隔时长（单位：毫秒），小于等于0时无效 */
    @Xjava(ref="MS_Common_OperationLog_WriteSleep")
    Param                     writeSleep;
    
    @Xjava
    private IOperationLogDAO  operationLogDAO;
    
    private int               isRunning;
    
    
    
    public OperationLogCache()
    {
        this.isRunning = 0;
    }
    
    
    
    /**
     * 日志向缓存中添加
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-11
     * @version     v1.0
     *
     * @param i_OperationLog
     */
    public synchronized void putLog(OperationLog i_OperationLog)
    {
        if ( i_OperationLog.getCacheToDBFlag() == $C2DB_SecondCache_NoInsert )
        {
            // 不用二次再添加到缓存中，Insert操作一次，不用再Update了
            return;
        }
        
        $LogCaches.put(i_OperationLog);
    }
    
    
    
    /**
     * 延时单线程队列周期性的处理日志的持久化
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-11
     * @version     v1.0
     *
     * @return
     */
    public void saveLogs()
    {
        int v_MaxThreadCount = Help.max(Help.NVL(this.maxThreadCount.getValueInt()) ,1);
        synchronized (this)
        {
            if ( this.isRunning >= v_MaxThreadCount )
            {
                return;
            }
            else
            {
                if ( this.isRunning < 0 )
                {
                    this.isRunning  = 1;
                }
                else
                {
                    this.isRunning += 1;
                }
            }
        }
        
        try
        {
            OperationLog v_OLog = $LogCaches.get();
            while ( v_OLog != null )
            {
                if ( v_OLog.getCacheToDBFlag() == $C2DB_FirstCache )
                {
                    if ( this.operationLogDAO.insert(v_OLog) >= 1 )
                    {
                        v_OLog.appendCacheToDBFlag($C2DB_Append_Insert);
                    }
                    else
                    {
                        // 异常时，退出循环，等待下一个日志处理周期的来临
                        this.saveLogError(v_OLog);
                        return;
                    }
                }
                else if ( v_OLog.getCacheToDBFlag() == $C2DB_SecondCache_NoInsert )
                {
                    if ( this.operationLogDAO.insert(v_OLog) >= 1 )
                    {
                        v_OLog.appendCacheToDBFlag($C2DB_Append_Update);
                        v_OLog.appendCacheToDBFlag($C2DB_Append_Insert);
                    }
                    else
                    {
                        // 异常时，退出循环，等待下一个日志处理周期的来临
                        this.saveLogError(v_OLog);
                        return;
                    }
                }
                else if ( v_OLog.getCacheToDBFlag() == $C2DB_SecondCache )
                {
                    if ( this.operationLogDAO.update(v_OLog) >= 1 )
                    {
                        v_OLog.appendCacheToDBFlag($C2DB_Append_Update);
                    }
                    else
                    {
                        // 异常时，退出循环，等待下一个日志处理周期的来临
                        this.saveLogError(v_OLog);
                        return;
                    }
                }
                
                if ( this.writeSleep.getValueInt() > 0 )
                {
                    Thread.sleep(this.writeSleep.getValueInt());
                }
                v_OLog = $LogCaches.get();
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        finally
        {
            synchronized (this)
            {
                this.isRunning -= 1;
            }
        }
    }
    
    
    
    /**
     * 保存日志异常时的处理方法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-11
     * @version     v1.0
     *
     * @param io_OperationLog
     */
    private void saveLogError(OperationLog io_OperationLog)
    {
        // 尝试过3次异常时，将放弃保存日志
        if ( io_OperationLog.getCacheToDBErrCount() < 3 )
        {
            io_OperationLog.setCacheToDBErrCount(io_OperationLog.getCacheToDBErrCount() + 1);
            $LogCaches.put(io_OperationLog);
        }
    }
    
}
