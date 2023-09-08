package org.hy.microservice.common.graph;

import org.hy.microservice.common.BaseViewMode;





/**
 * 图谱数显对象
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-08
 * @version     v1.0
 */
public class GraphInfo extends BaseViewMode
{

    private static final long serialVersionUID = 2151980588035344724L;
    
    
    /** 图谱数据的URL地址（节点与连接线的关系数据）。一般为 json文件的URL */
    private String dataUrl;
    
    /** WebSocket的服务是不是为安全协议（值为：s），还是普通协议（值为：-） */
    private String webSProtocol;
    
    /** WebSocket的服务IP地址 */
    private String webSHost;
    
    /** WebSocket的服务名称 */
    private String webSName;
    
    /** WebSocket的服务ID（连接线实时动态数据） */
    private String webSID;
    
    /** 风格类型 */
    private String styleType;
    
    
    
    public GraphInfo()
    {
        this.webSProtocol = "";
        this.styleType    = "";
    }

    
    
    /**
     * 获取：图谱数据的URL地址（节点与连接线的关系数据）。一般为 json文件的URL
     */
    public String getDataUrl()
    {
        return dataUrl;
    }

    
    /**
     * 设置：图谱数据的URL地址（节点与连接线的关系数据）。一般为 json文件的URL
     * 
     * @param i_DataUrl 图谱数据的URL地址（节点与连接线的关系数据）。一般为 json文件的URL
     */
    public void setDataUrl(String i_DataUrl)
    {
        this.dataUrl = i_DataUrl;
    }

    
    /**
     * 获取：WebSocket的服务是不是为安全协议（值为：s），还是普通协议（值为：空字符串）
     */
    public String getWebSProtocol()
    {
        return webSProtocol;
    }

    
    /**
     * 设置：WebSocket的服务是不是为安全协议（值为：s），还是普通协议（值为：空字符串）
     * 
     * @param i_WebSProtocol WebSocket的服务是不是为安全协议（值为：s），还是普通协议（值为：空字符串）
     */
    public void setWebSProtocol(String i_WebSProtocol)
    {
        this.webSProtocol = i_WebSProtocol;
    }

    
    /**
     * 获取：WebSocket的服务IP地址
     */
    public String getWebSHost()
    {
        return webSHost;
    }

    
    /**
     * 设置：WebSocket的服务IP地址
     * 
     * @param i_WebSHost WebSocket的服务IP地址
     */
    public void setWebSHost(String i_WebSHost)
    {
        this.webSHost = i_WebSHost;
    }

    
    /**
     * 获取：WebSocket的服务名称
     */
    public String getWebSName()
    {
        return webSName;
    }

    
    /**
     * 设置：WebSocket的服务名称
     * 
     * @param i_WebSName WebSocket的服务名称
     */
    public void setWebSName(String i_WebSName)
    {
        this.webSName = i_WebSName;
    }

    
    /**
     * 获取：WebSocket的服务ID（连接线实时动态数据）
     */
    public String getWebSID()
    {
        return webSID;
    }

    
    /**
     * 设置：WebSocket的服务ID（连接线实时动态数据）
     * 
     * @param i_WebSID WebSocket的服务ID（连接线实时动态数据）
     */
    public void setWebSID(String i_WebSID)
    {
        this.webSID = i_WebSID;
    }

    
    /**
     * 获取：风格类型
     */
    public String getStyleType()
    {
        return styleType;
    }

    
    /**
     * 设置：风格类型
     * 
     * @param i_StyleType 风格类型
     */
    public void setStyleType(String i_StyleType)
    {
        this.styleType = i_StyleType;
    }

}
