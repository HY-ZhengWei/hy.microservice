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
    
}
