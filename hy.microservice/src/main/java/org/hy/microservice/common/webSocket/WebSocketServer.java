package org.hy.microservice.common.webSocket;

import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.hy.common.Help;
import org.hy.common.TablePartitionRID;
import org.hy.common.xml.log.Logger;
import org.springframework.stereotype.Component;





/**
 * WebSocket的服务端
 *
 * @author      ZhengWei(HY)
 * @createDate  2022-07-28
 * @version     v1.0
 *              v2.0  2023-04-17  添加：用户编号 & 访问票据
 */
@ServerEndpoint("/report/{serviceType}/{userID}")
@Component
public class WebSocketServer
{
    private static Logger                                     $Logger       = Logger.getLogger(WebSocketServer.class);
    
    private static TablePartitionRID<String ,WebSocketClient> $WebSocketMap = new TablePartitionRID<String ,WebSocketClient>();

    
    
    private WebSocketClient client;
    
    
    
    /**
     * 连接建立成功调用的方法
     * 
     * @param i_Session       可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @param i_ServiceType   服务类型
     */
    @OnOpen
    public void onOpen(Session i_Session
                      ,@PathParam("serviceType") String i_ServiceType
                      ,@PathParam("userID")      String i_UserID)
    {
        if ( i_Session == null )
        {
            return;
        }
        
        if ( Help.isNull(i_ServiceType) )
        {
            return;
        }
        
        if ( Help.isNull(i_UserID) )
        {
            return;
        }
        
        this.client = new WebSocketClient(i_UserID ,i_Session.getId() ,i_Session ,i_ServiceType ,this);
        if ( i_Session.getRequestParameterMap() != null
          && !Help.isNull(i_Session.getRequestParameterMap().get("token")) )
        {
            this.client.setToken(i_Session.getRequestParameterMap().get("token").get(0));
        }
        $WebSocketMap.putRow(i_ServiceType ,this.client.getSessionID() ,this.client);
        
        $Logger.info("WebSocket onOpen：" + this.client.getServiceType() + ":" + this.client.getSessionID() + ":" + this.client.getUserID());
    }
    
    
    
    /**
     * 连接关闭调用的方法
     * 
     * @param i_ServiceType  服务类型
     */
    @OnClose
    public void onClose(@PathParam("serviceType") String i_ServiceType
                       ,@PathParam("userID")      String i_UserID
                       ,@PathParam("token")       String i_Token)
    {
        if ( this.client == null )
        {
            return;
        }
        
        if ( Help.isNull(i_ServiceType) )
        {
            return;
        }
        
        if ( Help.isNull(i_UserID) )
        {
            return;
        }
        
        WebSocketClient v_WebSocketClient = $WebSocketMap.get(i_ServiceType).get(this.client.getSessionID());
        
        if ( v_WebSocketClient != null )
        {
            $WebSocketMap.removeRow(i_ServiceType ,this.client.getSessionID());
        }
        
        $Logger.info("WebSocket onClose：" + this.client.getServiceType() + ":" + this.client.getSessionID() + ":" + this.client.getUserID());
    }
    
    

    /**
     * 收到客户端消息后调用的方法
     * 
     * @param i_Message      客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String i_Message)
    {
        if ( Help.isNull(i_Message) )
        {
            return;
        }
        
        try
        {
            this.client.getSession().getBasicRemote().sendText(i_Message);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        $Logger.debug("WebSocket onMessage：" + this.client.getServiceType() + "=" + i_Message);
    }

    
    
    /**
     * 发生错误时调用
     * 
     * @param i_Session      会话
     * @param i_Error        异常
     * @param i_ServiceType  服务类型
     * @param i_UserID       用户编号
     */
    @OnError
    public void onError(Session                          i_Session
    		           ,Throwable                        i_Error
                       ,@PathParam("serviceType") String i_ServiceType
                       ,@PathParam("userID")      String i_UserID)
    {
        $Logger.error("WebSocket error：" + this.client.getServiceType() + ":" + this.client.getSessionID() + ":" + this.client.getUserID() ,i_Error);
    }
    
    
    
    /**
     * 获取某种服务类型的所有客户端
     * 
     * @param i_ServiceType  服务类型
     * @return
     */
    public static Map<String ,WebSocketClient> getClients(String i_ServiceType)
    {
        if ( Help.isNull(i_ServiceType) )
        {
            return null;
        }
        
        return $WebSocketMap.get(i_ServiceType);
    }
    
    
    
    
    /**
     * 向客户端群发消息。
     * 
     * @param i_ServiceType  服务类型
     * @param i_NewMessage   仅有变化的消息
     */
    public static void pushMessages(String i_ServiceType ,String i_NewMessage)
    {
        pushMessages(i_ServiceType ,i_NewMessage ,i_NewMessage);
    }
    
    
    
    /**
     * 向客户端群发消息。
     * 
     * 首次接入的客户端，将发送全部消息，之后将只发有变化的消息
     * 
     * @param i_ServiceType  服务类型
     * @param i_NewMessage   仅有变化的消息
     * @param i_AllMessage   全部消息
     */
    public static void pushMessages(String i_ServiceType ,String i_NewMessage ,String i_AllMessage)
    {
        if ( Help.isNull(i_ServiceType) )
        {
            return;
        }
        
        Map<String ,WebSocketClient> v_Clients = $WebSocketMap.get(i_ServiceType);
        if ( Help.isNull(v_Clients) )
        {
            return;
        }
        
        for (WebSocketClient v_Client : v_Clients.values())
        {
            if ( v_Client.getSendCount() <= 0 )
            {
                v_Client.pushMessage(Help.NVL(i_AllMessage));
            }
            else
            {
                v_Client.pushMessage(Help.NVL(i_NewMessage));
            }
        }
    }
    
    
    
    /**
     * 向客户端群发消息。
     * 
     * 首次接入的客户端，将发送全部消息，之后将只发有变化的消息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-24
     * @version     v1.0
     *
     * @param i_Message  消息接口
     */
    public static void pushMessages(WebSocketMessage i_Message)
    {
        if ( i_Message == null )
        {
            return;
        }
        if ( Help.isNull(i_Message.getServiceType()) )
        {
            return;
        }
        
        Map<String ,WebSocketClient> v_Clients = $WebSocketMap.get(i_Message.getServiceType());
        if ( Help.isNull(v_Clients) )
        {
            return;
        }
        
        for (WebSocketClient v_Client : v_Clients.values())
        {
            if ( v_Client.getSendCount() <= 0 )
            {
                v_Client.pushMessage(Help.NVL(i_Message.getAllMessage()));
            }
            else
            {
                v_Client.pushMessage(Help.NVL(i_Message.getMessage()));
            }
        }
    }
    
}