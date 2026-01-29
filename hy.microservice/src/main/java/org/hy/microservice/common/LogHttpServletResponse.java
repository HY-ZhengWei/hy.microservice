package org.hy.microservice.common;

import javax.servlet.http.HttpServletResponse;

import org.hy.microservice.common.operationLog.OperationLog;





/**
 * 面向最终用户的控制层Controller的响应对象
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-01-29
 * @version     v1.0
 */
public interface LogHttpServletResponse extends HttpServletResponse
{
    
    /**
     * 获取：操作日志，允许最终用户的控制层Controller对操作日志的可读可控
     */
    public OperationLog getOperationLog();
    
}
