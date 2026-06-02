package org.hy.microservice.common.webSocket;

import java.io.IOException;
import java.net.URI;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import org.hy.common.Help;
import org.hy.common.callflow.event.websocket.WSPullData;
import org.hy.common.callflow.event.websocket.WSPullExecuter;
import org.hy.common.xml.log.Logger;





/**
 * WebSocket消息接收的执行者
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-08-30
 * @version     v1.0
 */
@ClientEndpoint
public class WebSocketPullExecuter implements WSPullExecuter
{
    
    private static Logger $Logger = Logger.getLogger(WebSocketPullExecuter.class);
    
    
    
    private WSPullData data;
    
    /** 连接会话 */
    private Session    session;
    
    
    
    /**
     * 构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-08-30
     * @version     v1.0
     */
    public WebSocketPullExecuter()
    {
    }
    
    
    
    /**
     * 获取连接会话
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-06-01
     * @version     v1.0
     *
     * @return
     */
    public Session getSession()
    {
        return session;
    }
    
    
    
    /**
     * 初始化点拉元素的执行者
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-08-30
     * @version     v1.0
     *
     * @param i_WSPullData  点拉元素的执行数据
     * @return
     */
    public boolean init(WSPullData i_WSPullData)
    {
        this.data = i_WSPullData;
        if ( this.data == null )
        {
            $Logger.error("连接远程WebSocket配置为空");
            return false;
        }
        if ( Help.isNull(this.data.getWsURL()) )
        {
            $Logger.error("连接远程WebSocket地址为空");
            return false;
        }
        
        try
        {
            WebSocketContainer v_Container = ContainerProvider.getWebSocketContainer();
            URI                v_URI       = URI.create(this.data.getWsURL());
            
            this.session = v_Container.connectToServer(this ,v_URI);
            return true;
        }
        catch (Exception error)
        {
            $Logger.error("连接远程WebSocket[" + this.data.getWsURL() + "]服务失败: " + error.getMessage() ,error);
        }
        
        return true;
    }
    
    
    
    /**
     * 发消息（文本消息）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-06-01
     * @version     v1.0
     *
     * @param i_Text  文本消息
     * @return  是否发送成功
     */
    public boolean sendText(String i_Text)
    {
        try
        {
            this.session.getBasicRemote().sendText(i_Text);
            return true;
        }
        catch (IOException exce)
        {
            $Logger.error(exce);
            return false;
        }
    }
    
    
    
    /**
     * 关闭会话释放资源
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-06-02
     * @version     v1.0
     *
     */
    public void close()
    {
        if ( this.session != null )
        {
            try
            {
                this.session.close();
            }
            catch (IOException exce)
            {
                $Logger.error(exce);
            }
            
            this.session = null;
        }
    }
    
    
    
    @OnOpen
    public void onOpen(Session i_Session)
    {
        $Logger.info("成功连接远程WebSocket[" + this.data.getWsURL() + "]");
    }



    /**
     * 收到客户端消息后调用的方法
     * 
     * @param i_Message      客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String i_Message)
    {
        this.onMessage(this.data ,i_Message);
    }
    


    @OnClose
    public void onClose(Session i_Session ,CloseReason i_CloseReason)
    {
        $Logger.info("关闭远程WebSocket[" + this.data.getWsURL() + "]，原因：" + i_CloseReason.getReasonPhrase());
    }



    @OnError
    public void onError(Session i_Session ,Throwable i_Throwable)
    {
        $Logger.error("远程WebSocket通信错误[" + this.data.getWsURL() + "]: " + i_Throwable.getMessage() ,i_Throwable);
    }
    
}
