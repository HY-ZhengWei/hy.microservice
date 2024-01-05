package org.hy.microservice.common.openapi;

import org.hy.microservice.common.BaseViewMode;





/**
 * 系统操作日志
 * 
 * @author      ZhengWei(HY)
 * @createDate  2023-12-27
 * @version     v1.0
 */
public class OpenApiConfig extends BaseViewMode
{

    private static final long serialVersionUID = -1820383376053890059L;
    
    
    /** URL地址 */
    private String  url;
    
    /** 每分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值 */
    private Long    maxCountMinute;
    
    /** 每10分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值 */
    private Long    maxCountMinute10;
    
    /** 保存简单信息的解析对象的名称。当本属性有值时，表示保存简单信息 */
    private String  SimpleClassName;

    
    /**
     * 获取：URL地址
     */
    public String getUrl()
    {
        return url;
    }

    
    /**
     * 设置：URL地址
     * 
     * @param i_Url URL地址
     */
    public void setUrl(String i_Url)
    {
        this.url = i_Url;
    }

    
    /**
     * 获取：每分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值
     */
    public Long getMaxCountMinute()
    {
        return maxCountMinute;
    }

    
    /**
     * 设置：每分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值
     * 
     * @param i_MaxCountMinute 每分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值
     */
    public void setMaxCountMinute(Long i_MaxCountMinute)
    {
        this.maxCountMinute = i_MaxCountMinute;
    }

    
    /**
     * 获取：每10分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值
     */
    public Long getMaxCountMinute10()
    {
        return maxCountMinute10;
    }

    
    /**
     * 设置：每10分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值
     * 
     * @param i_MaxCountMinute10 每10分钟最大允许的请求量。等于0 表示不限制访问量； 小与0 表示取默认值
     */
    public void setMaxCountMinute10(Long i_MaxCountMinute10)
    {
        this.maxCountMinute10 = i_MaxCountMinute10;
    }

    
    /**
     * 获取：保存简单信息的解析对象的名称。当本属性有值时，表示保存简单信息
     */
    public String getSimpleClassName()
    {
        return SimpleClassName;
    }

    
    /**
     * 设置：保存简单信息的解析对象的名称。当本属性有值时，表示保存简单信息
     * 
     * @param i_SimpleClassName 保存简单信息的解析对象的名称。当本属性有值时，表示保存简单信息
     */
    public void setSimpleClassName(String i_SimpleClassName)
    {
        SimpleClassName = i_SimpleClassName;
    }
    
}
