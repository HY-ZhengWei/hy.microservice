package org.hy.microservice.common.operationLog;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xsql;





/**
 * 记录系统操作日志的DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-10
 * @version     v1.0
 */
@Xjava(id="OperationLogDAO" ,value=XType.XSQL)
public interface IOperationLogDAO
{
    
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
    @Xsql(id="XSQL_Common_OperationLog_Insert" ,batch=false)
    public int insert(OperationLog i_OperationLog);
    
    
    
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
    @Xsql(id="XSQL_Common_OperationLog_Update" ,batch=false)
    public int update(OperationLog i_OperationLog);
    
    
    
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
    @Xsql("XSQL_Common_OperationLog_Query")
    public List<OperationLog> query(OperationLog i_OperationLog);
    
}
