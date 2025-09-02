package org.hy.microservice.common.operationLog;

import java.io.Serializable;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.XJava;
import org.hy.common.xml.XSQL;
import org.hy.common.xml.annotation.Xjava;





/**
 * 系统操作日志的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-11
 * @version     v1.0
 *              v2.0  2025-08-07  添加：从名称name中解析出日志表名称的后缀。
 *                                     可支持不同业务的日志保存在不同的表中。
 *              v3.0  2025-09-02  添加：分页查询
 */
@Xjava
public class OperationLogService implements IOperationLogService ,Serializable
{
    private static final long serialVersionUID = 1986705138017017708L;
    
    
    
    @Xjava
    private OperationLogCache operationLogCache;
    
    @Xjava
    private IOperationLogDAO  operationLogDAO;
    
    @Xjava(ref="MS_Common_PagePerCount")
    private Param           pagePerCount;
    
    
    
    /**
     * 创建日志表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-08-07
     * @version     v1.0
     *
     * @param i_LogName  日志表的后缀
     * @return           返回值表示是否有真实创建日志表
     */
    public synchronized boolean create(String i_LogName)
    {
        if ( Help.isNull(i_LogName) )
        {
            return false;
        }
        
        if ( XJava.getXSQL("XSQL_Common_Create_OperationLog_Template_" + i_LogName) == null )
        {
            XSQL v_XSQL = XJava.getXSQL("XSQL_Common_Create_OperationLog_Template" ,true);
            
            v_XSQL.setXJavaID("XSQL_Common_Create_OperationLog_" + i_LogName);
            v_XSQL.setContent(StringHelp.replaceAll(v_XSQL.getContent().getSqlText() ,"Template" ,i_LogName));
            v_XSQL.setCreate("TOperationLog" + i_LogName);
            
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    
    /**
     * 新增系统操作日志
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-04-10
     * @version     v1.0
     *
     * @param i_OperationLog  操作日志数据
     * @return
     */
    @Override
    public boolean insert(OperationLog i_OperationLog)
    {
        i_OperationLog.setId(Help.NVL(i_OperationLog.getId() ,StringHelp.getUUID()));
        i_OperationLog.appendCacheToDBFlag(OperationLogCache.$C2DB_Append_Insert);
        this.operationLogCache.putLog(i_OperationLog);
        return true;
    }
    
    
    
    /**
     * 更新完成时间、响应信息等
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-04-11
     * @version     v1.0
     *
     * @param i_OperationLog  操作日志数据
     * @return
     */
    @Override
    public boolean update(OperationLog i_OperationLog)
    {
        i_OperationLog.appendCacheToDBFlag(OperationLogCache.$C2DB_Append_Update);
        this.operationLogCache.putLog(i_OperationLog);
        return true;
    }
    
    

    /**
     * 查询系统操作日志
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-04-11
     * @version     v1.0
     *
     * @param i_OperationLog  操作日志数据
     * @return
     */
    @Override
    public List<OperationLog> queryList(OperationLog i_OperationLog)
    {
        return this.operationLogDAO.query(i_OperationLog);
    }
    
    
    
    /**
     * 查询系统操作日志（分页查询）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-02
     * @version     v1.0
     * 
     * @param io_OperationLog  操作日志数据
     * @return
     */
    @Override
    public List<OperationLog> queryListByPage(OperationLog io_OperationLog)
    {
        long v_DefPagePerCount = this.pagePerCount.getValueLong();
        
        io_OperationLog.setPageIndex(Help.NVL(io_OperationLog.getPageIndex() ,1L));
        io_OperationLog.setPagePerCount(Help.min(Help.NVL(io_OperationLog.getPagePerCount() ,v_DefPagePerCount) ,v_DefPagePerCount));
        return this.operationLogDAO.queryByPage(io_OperationLog);
    }
    
    
    
    /**
     * 查询系统操作日志的总记录数
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-02
     * @version     v1.0
     *
     * @param i_OperationLog  操作日志数据
     * @return
     */
    @Override
    public Long queryCount(OperationLog i_OperationLog)
    {
        return this.operationLogDAO.queryCount(i_OperationLog);
    }
    
}
