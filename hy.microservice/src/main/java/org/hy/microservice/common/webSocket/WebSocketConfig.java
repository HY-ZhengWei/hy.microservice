package org.hy.microservice.common.webSocket;

import org.hy.common.Help;





/**
 * WebSocket服务连接配置
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-08-29
 * @version     v1.0
 */
public class WebSocketConfig
{
    
    /** WebSocket类型（wss还是ws协议） */
    private String protocol;
    
    /** WebSocket服务端的IP地址及端口 */
    private String host;
    
    /** HTTP请求地址。不含"http://IP:Port/"。不含请求参数  */
    private String url;
    
    /** 服务类型 */
    private String serviceType;
    
    /** 用户编号 */
    private String userID;
    
    
    
    /**
     * 获取：WebSocket类型（wss还是ws协议）
     */
    public String getProtocol()
    {
        return protocol;
    }

    
    /**
     * 设置：WebSocket类型（wss还是ws协议）
     * 
     * @param i_Protocol WebSocket类型（wss还是ws协议）
     */
    public WebSocketConfig setProtocol(String i_Protocol)
    {
        this.protocol = i_Protocol;
        return this;
    }

    
    /**
     * 获取：WebSocket服务端的IP地址及端口
     */
    public String getHost()
    {
        return host;
    }

    
    /**
     * 设置：WebSocket服务端的IP地址及端口
     * 
     * @param i_Host WebSocket服务端的IP地址及端口
     */
    public WebSocketConfig setHost(String i_Host)
    {
        this.host = i_Host;
        return this;
    }
    
    
    /**
     * 获取：HTTP请求地址。不含"http://IP:Port/"。不含请求参数
     */
    public String getUrl()
    {
        return url;
    }

    
    /**
     * 设置：HTTP请求地址。不含"http://IP:Port/"。不含请求参数
     * 
     * @param i_Url HTTP请求地址。不含"http://IP:Port/"。不含请求参数
     */
    public WebSocketConfig setUrl(String i_Url)
    {
        this.url = i_Url;
        return this;
    }
    
    
    /**
     * 获取：服务类型
     */
    public String getServiceType()
    {
        return serviceType;
    }

    
    /**
     * 设置：服务类型
     * 
     * @param i_ServiceType 服务类型
     */
    public WebSocketConfig setServiceType(String i_ServiceType)
    {
        this.serviceType = i_ServiceType;
        return this;
    }


    /**
     * 获取：用户编号
     */
    public String getUserID()
    {
        return userID;
    }

    
    /**
     * 设置：用户编号
     * 
     * @param i_UserID 用户编号
     */
    public WebSocketConfig setUserID(String i_UserID)
    {
        this.userID = i_UserID;
        return this;
    }
    
    
    /**
     * 生成WebSocket连接URL地址
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-08-29
     * @version     v1.0
     *
     * @return
     */
    public String toWebSocketURL()
    {
        StringBuilder v_Buffer = new StringBuilder();
        
        v_Buffer.append(Help.NVL(this.getProtocol() ,"ws").toLowerCase());
        v_Buffer.append("://");
        v_Buffer.append(Help.NVL(this.getHost() ,"127.0.0.1"));
        
        if ( Help.isNull(this.getUrl()) )
        {
            v_Buffer.append("/msCommon/report");
        }
        else if ( this.getUrl().startsWith("/") )
        {
            v_Buffer.append(this.getUrl());
        }
        else
        {
            v_Buffer.append("/").append(this.getUrl());
        }
        
        if ( !Help.NVL(this.getUrl() ,"").endsWith("/") )
        {
            v_Buffer.append("/");
        }
        
        v_Buffer.append(Help.NVL(this.getServiceType() ,"Default"));
        v_Buffer.append("/");
        v_Buffer.append(Help.NVL(this.getUserID() ,"ms"));
        
        return v_Buffer.toString();
    }


    @Override
    public String toString()
    {
        return toWebSocketURL();
    }
    
}
