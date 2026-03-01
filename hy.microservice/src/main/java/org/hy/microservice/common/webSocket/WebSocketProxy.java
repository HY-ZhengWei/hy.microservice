package org.hy.microservice.common.webSocket;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

import org.hy.common.Help;
import org.hy.common.xml.log.Logger;





/**
 * WebSocket消息的代理转发类
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-08-30
 * @version     v1.0
 */
@ClientEndpoint
public class WebSocketProxy
{
    
    private static Logger $Logger = Logger.getLogger(WebSocketProxy.class);
    
    
    /** 服务类型 */
    private String          serviceType;
    
    /** 代理的连接远程WebSocket配置 */
    private WebSocketConfig remoteConfig;
    
    
    
    /**
     * 构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-08-30
     * @version     v1.0
     *
     * @param i_ServiceType    代理后的服务类型名称
     * @param i_RemoteConfig    WebSocket服务连接配置
     */
    public WebSocketProxy(String i_ServiceType ,WebSocketConfig i_RemoteConfig)
    {
        if ( i_RemoteConfig == null )
        {
            throw new NullPointerException("WebSocketConfig is null.");
        }
        if ( Help.isNull(i_RemoteConfig.getServiceType()) )
        {
            throw new NullPointerException("RemoteConfig.serviceType is null.");
        }
        this.remoteConfig = i_RemoteConfig;
        this.serviceType  = Help.NVL(i_ServiceType ,this.remoteConfig.getServiceType());
    }
    
    
    
    @OnOpen
    public void onOpen(Session i_Session)
    {
        $Logger.info("成功连接远程WebSocket[" + this.remoteConfig + "]");
    }



    /**
     * 收到客户端消息后调用的方法
     * 
     * @param i_Message      客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String i_Message)
    {
        WebSocketServer.pushMessages(this.serviceType ,i_Message);
    }



    @OnClose
    public void onClose(Session i_Session ,CloseReason i_CloseReason)
    {
        $Logger.info("关闭远程WebSocket[" + this.remoteConfig + "]，原因：" + i_CloseReason.getReasonPhrase());
    }



    @OnError
    public void onError(Session i_Session ,Throwable i_Throwable)
    {
        $Logger.error("远程WebSocket通信错误[" + this.remoteConfig + "]: " + i_Throwable.getMessage() ,i_Throwable);
    }
    
}
