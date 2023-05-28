package org.hy.microservice.common.operationLog;

import org.hy.common.Date;
import org.hy.microservice.common.BaseViewMode;

import com.fasterxml.jackson.annotation.JsonFormat;





/**
 * 系统操作日志
 * 
 * @author      ZhengWei(HY)
 * @createDate  2023-04-10
 * @version     v1.0
 */
public class OperationLog extends BaseViewMode
{

    private static final long serialVersionUID = -5413499847764917172L;

    /** 主键 */
    private String                  id;
    
    /** 系统编号 */
    private String                  systemCode;
    
    /** 模块编号 */
    private String                  moduleCode;
                                    
    /** 操作的URL地址 */
    private String                  url;
        
    /** 操作的请求参数 */
    private String                  urlRequest;
    
    /** 操作的请求体数据 */
    private String                  urlRequestBody;
    
    /** 操作的响应信息 */
    private String                  urlResponse;
                   
    /** 操作的响应结果编号 */
    private String                  resultCode;
    
    /** 操作人ID */
    private String                  userID;
    
    /** 操作人IP地址 */
    private String                  userIP;
                   
    /** 请求时间 */
    private Long                    requestTime;
    
    /** 响应时间 */
    private Long                    responseTime;
    
    /** 操作时长（单位：毫秒） */
    private Long                    timeLen;
    
    /** 攻击类型 */
    private String                  attackType;
    

    
    public OperationLog()
    {
        this.requestTime = new Date().getTime();
    }


    
    /**
     * 获取：主键
     */
    public String getId()
    {
        return id;
    }


    
    /**
     * 设置：主键
     * 
     * @param i_Id 主键
     */
    public void setId(String i_Id)
    {
        this.id = i_Id;
    }


    
    /**
     * 获取：系统编号
     */
    public String getSystemCode()
    {
        return systemCode;
    }


    
    /**
     * 设置：系统编号
     * 
     * @param i_SystemCode 系统编号
     */
    public void setSystemCode(String i_SystemCode)
    {
        this.systemCode = i_SystemCode;
    }


    
    /**
     * 获取：模块编号
     */
    public String getModuleCode()
    {
        return moduleCode;
    }


    
    /**
     * 设置：模块编号
     * 
     * @param i_ModuleCode 模块编号
     */
    public void setModuleCode(String i_ModuleCode)
    {
        this.moduleCode = i_ModuleCode;
    }


    
    /**
     * 获取：操作的URL地址
     */
    public String getUrl()
    {
        return url;
    }


    
    /**
     * 设置：操作的URL地址
     * 
     * @param i_Url 操作的URL地址
     */
    public void setUrl(String i_Url)
    {
        this.url = i_Url;
    }


    
    /**
     * 获取：操作的请求参数
     */
    public String getUrlRequest()
    {
        return urlRequest;
    }


    
    /**
     * 设置：操作的请求参数
     * 
     * @param i_UrlRequest 操作的请求参数
     */
    public void setUrlRequest(String i_UrlRequest)
    {
        this.urlRequest = i_UrlRequest;
    }


    
    /**
     * 获取：操作的请求体数据
     */
    public String getUrlRequestBody()
    {
        return urlRequestBody;
    }


    
    /**
     * 设置：操作的请求体数据
     * 
     * @param i_UrlRequestBody 操作的请求体数据
     */
    public void setUrlRequestBody(String i_UrlRequestBody)
    {
        this.urlRequestBody = i_UrlRequestBody;
    }


    
    /**
     * 获取：操作的响应信息
     */
    public String getUrlResponse()
    {
        return urlResponse;
    }



    /**
     * 设置：操作的响应信息
     * 
     * @param i_UrlResponse 操作的响应信息
     */
    public void setUrlResponse(String i_UrlResponse)
    {
        this.urlResponse = i_UrlResponse;
    }


    
    /**
     * 获取：操作的响应结果编号
     */
    public String getResultCode()
    {
        return resultCode;
    }


    
    /**
     * 设置：操作的响应结果编号
     * 
     * @param i_ResultCode 操作的响应结果编号
     */
    public void setResultCode(String i_ResultCode)
    {
        this.resultCode = i_ResultCode;
    }


    
    /**
     * 获取：操作人ID
     */
    @Override
    public String getUserID()
    {
        return userID;
    }


    
    /**
     * 设置：操作人ID
     * 
     * @param i_UserID 操作人ID
     */
    @Override
    public void setUserID(String i_UserID)
    {
        this.userID = i_UserID;
    }


    
    /**
     * 获取：操作人IP地址
     */
    public String getUserIP()
    {
        return userIP;
    }



    /**
     * 设置：操作人IP地址
     * 
     * @param i_UserIP 操作人IP地址
     */
    public void setUserIP(String i_UserIP)
    {
        this.userIP = i_UserIP;
    }


    
    /**
     * 获取：请求时间
     */
    public Long getRequestTime()
    {
        return requestTime;
    }
    
    
    /**
     * 获取：请求时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS" ,timezone = "GMT+8")
    public Date getRequestDate()
    {
        return new Date(requestTime);
    }

    
    /**
     * 设置：请求时间
     * 
     * @param i_RequestTime 请求时间
     */
    public void setRequestTime(Long i_RequestTime)
    {
        this.requestTime = i_RequestTime;
    }

    
    /**
     * 获取：响应时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS" ,timezone = "GMT+8")
    public Date getResponseDate()
    {
        if ( this.responseTime == null || this.responseTime == 0 )
        {
            return null;
        }
        else
        {
            return new Date(responseTime);
        }
    }
    
    
    /**
     * 获取：响应时间
     */
    public Long getResponseTime()
    {
        return responseTime;
    }


    
    /**
     * 设置：响应时间
     * 
     * @param i_ResponseTime 响应时间
     */
    public void setResponseTime(Long i_ResponseTime)
    {
        this.responseTime = i_ResponseTime;
    }


    
    /**
     * 获取：操作时长（单位：毫秒）
     */
    public Long getTimeLen()
    {
        return timeLen;
    }


    
    /**
     * 设置：操作时长（单位：毫秒）
     * 
     * @param i_TimeLen 操作时长（单位：毫秒）
     */
    public void setTimeLen(Long i_TimeLen)
    {
        this.timeLen = i_TimeLen;
    }


    
    /**
     * 获取：攻击类型
     */
    public String getAttackType()
    {
        return attackType;
    }


    
    /**
     * 设置：攻击类型
     * 
     * @param i_AttackType 攻击类型
     */
    public void setAttackType(String i_AttackType)
    {
        this.attackType = i_AttackType;
    }

}
