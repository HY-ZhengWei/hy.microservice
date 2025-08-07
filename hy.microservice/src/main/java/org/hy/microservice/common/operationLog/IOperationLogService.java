package org.hy.microservice.common.operationLog;

import java.util.List;





/**
 * 系统操作日志的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-11
 * @version     v1.0
 */
public interface IOperationLogService
{
    
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
    public boolean create(String i_LogName);
    
    
    
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
    public boolean insert(OperationLog i_OperationLog);
    
    
    
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
    public boolean update(OperationLog i_OperationLog);
    
    
    
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
    public List<OperationLog> queryList(OperationLog i_OperationLog);
    
}
