package org.hy.microservice.common.operationLog;

import java.io.Serializable;
import java.util.List;

import org.hy.common.xml.annotation.Xjava;





/**
 * 系统操作日志的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-11
 * @version     v1.0
 */
@Xjava
public class OperationLogService implements IOperationLogService ,Serializable
{
    private static final long serialVersionUID = 1986705138017017708L;
    
    
    
    @Xjava
    private OperationLogCache operationLogCache;
    
    @Xjava
    private IOperationLogDAO  operationLogDAO;
    
    
    
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
     * @param i_OperationLog
     * @return
     */
    @Override
    public List<OperationLog> queryList(OperationLog i_OperationLog)
    {
        return this.operationLogDAO.query(i_OperationLog);
    }
    
}
