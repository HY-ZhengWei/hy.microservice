package org.hy.microservice.common.webSocket;

import jakarta.websocket.Session;





/**
 * WebSocket的客户端
 *
 * @author      ZhengWei(HY)
 * @createDate  2022-07-29
 * @version     v1.0
 *              v2.0  2023-04-17  添加：用户编号
 */
public class WebSocketClient
{
    /** 用户编号 */
    private String          userID;
    
    /** 访问票据 */
    private String          token;
    
    /** 会话ID */
    private String          sessionID;
                            
    /** 会话对象 */
    private Session         session;
                            
    /** 服务类型 */
    private String          serviceType;
    
    /** 服务端 */
    private WebSocketServer server;
    
    /** 向客户端发送消息的次数 */
    private long            sendCount;
    
    
    
    public WebSocketClient(String i_UserID ,String i_SessionID ,Session i_Session ,String i_ServiceType ,WebSocketServer i_Server)
    {
        this.userID      = i_UserID;
        this.sessionID   = i_SessionID;
        this.session     = i_Session;
        this.serviceType = i_ServiceType;
        this.server      = i_Server;
        this.sendCount   = 0L;
    }


    
    /**
     * 获取：访问票据
     */
    public String getToken()
    {
        return token;
    }


    
    /**
     * 设置：访问票据
     * 
     * @param i_Token 访问票据
     */
    public void setToken(String i_Token)
    {
        this.token = i_Token;
    }



    /**
     * 获取：用户编号
     */
    public String getUserID()
    {
        return userID;
    }


    
    /**
     * 获取：会话ID
     */
    public String getSessionID()
    {
        return sessionID;
    }


    
    /**
     * 获取：会话对象
     */
    public Session getSession()
    {
        return session;
    }


    
    /**
     * 获取：服务类型
     */
    public String getServiceType()
    {
        return serviceType;
    }


    
    /**
     * 获取：服务端
     */
    public WebSocketServer getServer()
    {
        return server;
    }



    /**
     * 获取：向客户端发送消息的次数
     */
    public synchronized long getSendCount()
    {
        return sendCount;
    }


    
    /**
     * 设置：向客户端发送消息的次数
     */
    private synchronized void setSendCount(long sendCount)
    {
        this.sendCount = sendCount;
    }
    
    
    
    /**
     * 推送消息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-07-29
     * @version     v1.0
     *
     * @param i_Message
     */
    public void pushMessage(String i_Message)
    {
        this.getServer().sendMessage(i_Message);
        this.setSendCount(this.getSendCount() + 1L);
    }
    
}
