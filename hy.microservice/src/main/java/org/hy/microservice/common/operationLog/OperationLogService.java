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
    private IOperationLogDAO operationLogDAO;
    
    

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
