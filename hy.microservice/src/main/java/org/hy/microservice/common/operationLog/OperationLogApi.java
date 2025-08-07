package org.hy.microservice.common.operationLog;





/**
 * 操作日志中的系统接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-12
 * @version     v1.0
 */
public class OperationLogApi extends OperationLogModule
{

    private static final long serialVersionUID = 2001040419147740574L;
    
    /** API接口的资源路径 */
    private String url;
    
    /** API接口的名称 */
    private String urlName;
    
    /** 日志名称。即日志表名称的后缀 */
    private String logName;

    
    
    /**
     * 获取：API接口的资源路径
     */
    public String getUrl()
    {
        return url;
    }

    
    /**
     * 设置：API接口的资源路径
     * 
     * @param i_Url API接口的资源路径
     */
    public void setUrl(String i_Url)
    {
        this.url = i_Url;
    }

    
    /**
     * 获取：API接口的名称
     */
    public String getUrlName()
    {
        return urlName;
    }

    
    /**
     * 设置：API接口的名称
     * 
     * @param i_UrlName API接口的名称
     */
    public void setUrlName(String i_UrlName)
    {
        this.urlName = i_UrlName;
    }
    
    
    /**
     * 获取：日志名称。即日志表名称的后缀
     */
    public String getLogName()
    {
        return logName;
    }

    
    /**
     * 设置：日志名称。即日志表名称的后缀
     * 
     * @param i_LogName 日志名称。即日志表名称的后缀
     */
    public void setLogName(String i_LogName)
    {
        this.logName = i_LogName;
    }
    
}
