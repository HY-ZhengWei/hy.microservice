package org.hy.microservice.common.webSocket;





/**
 * WebSocket消息接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-08-24
 * @version     v1.0
 */
public interface WebSocketMessage
{
    
    /**
     * 获取消息的服务类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-24
     * @version     v1.0
     *
     * @return
     */
    public String getServiceType();
    
    
    
    /**
     * 获取最新的消息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-24
     * @version     v1.0
     *
     * @return
     */
    public String getMessage();
    
    
    
    /**
     * 获取所有消息（一般在首次接入的客户端，将发送全部消息）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-24
     * @version     v1.0
     *
     * @return
     */
    public String getAllMessage();
    
    
    
    /**
     * 获取首次的初始化消息。需配合WebSocketServer事件监听器一同使用。
     * 
     * 注：当本方法生效时，getAllMessage() 方法将不会被执行
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-25
     * @version     v1.0
     *
     * @return
     */
    public String getInitMessage();
    
}
