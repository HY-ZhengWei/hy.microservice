package org.hy.microservice.common.rocketMQ;

import org.hy.microservice.common.cache.CacheFactory;
import org.hy.microservice.common.cache.ICache;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;

import java.util.Map;





@Xjava
public class RedisMessageService
{

    private static final String   SEND_FAIL_KEY    = "rocketmq:send:fail:";

    private static final String   CONSUME_FAIL_KEY = "rocketmq:consume:fail:";
    // private static final long EXPIRATION_DAYS = 7;

    /** 缓存的表名称 */
    private static final String   $RTable          = "TRocketProducerConfig";

    /** 缓存的数据库实例名称 */
    private static final String   $RDataSource     = "rocketMQ_DB";

    private ICache<MessageEntity> redisCache;



    public RedisMessageService()
    {
        boolean v_UseRemote = "启用".equals(XJava.getParam("MS_Common_IsHeartbeat").getValue());
        this.redisCache = CacheFactory.newInstanceOf(v_UseRemote ,MessageEntity.class);
    }



    /**
     * @Title: saveSendFailedMessage
     * @Author: lihao
     * @Date: 2025-08-11 8:23
     * @Params: [message]
     * @Return: void
     * @Description: 存储发送失败的消息
     */
    public void saveSendFailedMessage(MessageEntity message)
    {
        String key = SEND_FAIL_KEY + message.getId();
        this.redisCache.save($RDataSource ,$RTable ,key ,message);
    }



    /**
     * @Title: saveConsumeFailedMessage
     * @Author: lihao
     * @Date: 2025-08-10 10:35
     * @Params: [message]
     * @Return: void
     * @Description: 存储消费失败的消息
     */
    public void saveConsumeFailedMessage(MessageEntity message)
    {
        String key = CONSUME_FAIL_KEY + message.getId();
        this.redisCache.save($RDataSource ,$RTable ,key ,message);
    }



    /**
     * @Title: getSendFailedMessageIds
     * @Author: lihao
     * @Date: 2025-08-10 10:35
     * @Params: []
     * @Return: Set<String>
     * @Description: 获取所有发送失败的消息ID
     */
    public Map<String ,MessageEntity> getSendFailedMessageIds()
    {
        return redisCache.getRowsMap($RDataSource ,$RTable);
    }



    /**
     * @Title: getSendFailedMessageIds1
     * @Author: lihao
     * @Date: 2025-08-10 10:36
     * @Params: []
     * @Return: Map<String,MessageEntity>
     * @Description: 获取所有发送失败的消息ID
     */
    public Map<String ,MessageEntity> getSendFailedMessageIds1()
    {
        return redisCache.getRowsMap($RDataSource ,$RTable);
    }



    /**
     * @Title: getConsumeFailedMessageIds
     * @Author: lihao
     * @Date: 2025-08-10 10:36
     * @Params: []
     * @Return: Set<String>
     * @Description: 获取所有消费失败的消息ID
     */
    public Map<String ,MessageEntity> getConsumeFailedMessageIds()
    {
        return redisCache.getRowsMap($RDataSource ,$RTable);
    }



    /**
     * @Title: getSendFailedMessage
     * @Author: lihao
     * @Date: 2025-08-10 10:36
     * @Params: [id]
     * @Return: MessageEntity
     * @Description: 获取发送失败的消息
     */
    public MessageEntity getSendFailedMessage(String id)
    {
        return (MessageEntity) redisCache.get($RDataSource ,$RTable ,id);
    }



    /**
     * @Title: getConsumeFailedMessage
     * @Author: lihao
     * @Date: 2025-08-10 10:36
     * @Params: [id]
     * @Return: MessageEntity
     * @Description: 获取消费失败的消息
     */
    public MessageEntity getConsumeFailedMessage(String id)
    {
        return (MessageEntity) redisCache.get($RDataSource ,$RTable ,id);
    }



    /**
     * @Title: deleteSendFailedMessage
     * @Author: lihao
     * @Date: 2025-08-11 8:22
     * @Params: [id]
     * @Return: void
     * @Description: 删除发送失败的消息
     */
    public void deleteSendFailedMessage(String id)
    {
        redisCache.del($RDataSource ,$RTable ,id);
    }



    /**
     * @Title: deleteConsumeFailedMessage
     * @Author: lihao
     * @Date: 2025-08-11 8:22
     * @Params: [id]
     * @Return: void
     * @Description: 删除消费失败的消息
     */
    public void deleteConsumeFailedMessage(String id)
    {
        redisCache.del($RDataSource ,$RTable ,id);
    }



    /**
     * @Title: updateSendFailedMessage
     * @Author: lihao
     * @Date: 2025-08-11 8:22
     * @Params: [message]
     * @Return: void
     * @Description: 更新消息
     */
    public void updateSendFailedMessage(MessageEntity message)
    {
        saveSendFailedMessage(message);
    }



    /**
     * @Title: updateConsumeFailedMessage
     * @Author: lihao
     * @Date: 2025-08-11 8:22
     * @Params: [message]
     * @Return: void
     * @Description: 更新消费失败的消息
     */
    public void updateConsumeFailedMessage(MessageEntity message)
    {
        saveConsumeFailedMessage(message);
    }
}
