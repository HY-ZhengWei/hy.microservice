package org.hy.microservice.common.ipSafe;

import org.hy.microservice.common.BaseViewMode;





/**
 * 系统安全访问IP黑白名单
 * 
 * @author      ZhengWei(HY)
 * @createDate  2023-05-26
 * @version     v1.0
 */
public class IPSafeConfig extends BaseViewMode
{
    
    private static final long serialVersionUID = 8155182081994238170L;
    
    
    /** 黑名单 */
    public  static final String $Type_BackList  = "blacklist";
    
    /** 白名单 */
    public  static final String $Type_WhiteList = "whitelist";
    
    
    
    /** 主键 */
    private String id;
    
    /** IP类型 */
    private String ipType;
    
    /** IP地址 */
    private String ip;
    
    /** 备注注解 */
    private String comment;

    
    
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
     * 获取：IP类型
     */
    public String getIpType()
    {
        return ipType;
    }

    
    /**
     * 设置：IP类型
     * 
     * @param i_IpType IP类型
     */
    public void setIpType(String i_IpType)
    {
        this.ipType = i_IpType;
    }

    
    /**
     * 获取：IP地址
     */
    public String getIp()
    {
        return ip;
    }

    
    /**
     * 设置：IP地址
     * 
     * @param i_Ip IP地址
     */
    public void setIp(String i_Ip)
    {
        this.ip = i_Ip;
    }

    
    /**
     * 获取：备注注解
     */
    public String getComment()
    {
        return comment;
    }

    
    /**
     * 设置：备注注解
     * 
     * @param i_Comment 备注注解
     */
    public void setComment(String i_Comment)
    {
        this.comment = i_Comment;
    }
    
}
