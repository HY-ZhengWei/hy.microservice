package org.hy.microservice.common.rocketMQ;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.hy.common.StringHelp;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;





@Xjava
public class RocketMQProducer
{

    private Logger              logger = new Logger(RocketMQProducer.class);

    @Xjava
    private RedisMessageService redisMessageService;



    /**
     * @Title: sendMessage
     * @Author: lihao
     * @Date: 2025-08-08 21:05
     * @Params: [topic, tag, content]
     * @Return: SendResult
     * @Description: 发送消息同步方法
     */
    public SendResult sendMessage(String topic ,String tag ,String content)
    {
        String messageId = StringHelp.getUUID9n();
        MessageEntity message = new MessageEntity(messageId ,topic ,tag ,content);
        String destination = topic + ":" + tag;
        try
        {
            RocketMQTemplate rocketMQTemplate = (RocketMQTemplate) XJava.getObject("MS_MQ_TEMPLATE");
            // return new CacheRemote<Data>((IRedis) ,i_DataClass);
            SendResult result = rocketMQTemplate.syncSend(destination ,content);
            if ( result == null || result.getSendStatus() == null )
            {
                throw new Exception("消息发送状态未知");
            }
            redisMessageService.saveSendFailedMessage(message);
            logger.debug("消息发送成功: " + messageId);
            return result;
        }
        catch (Exception e)
        {
            logger.error("消息发送失败，存入Redis: " + messageId + ", 原因: " + e.getMessage());
            redisMessageService.saveSendFailedMessage(message);
        }
        return null;
    }



    /**
     * @Title: sendAsyncMessage
     * @Author: lihao
     * @Date: 2025-08-08 21:05
     * @Params: [topic, tag, content]
     * @Return: void
     * @Description: 异步发送消息
     */
    public void sendAsyncMessage(String topic ,String tag ,String content)
    {
        String messageId = StringHelp.getUUID9n();
        ;
        MessageEntity message = new MessageEntity(messageId ,topic ,tag ,content);
        String destination = topic + ":" + tag;
        RocketMQTemplate rocketMQTemplate = (RocketMQTemplate) XJava.getObject("MS_MQ_TEMPLATE");
        try
        {
            rocketMQTemplate.asyncSend(destination ,content ,new SendCallback() {

                @Override
                public void onSuccess(SendResult sendResult)
                {
                    logger.debug("异步消息发送成功: " + messageId);
                }



                @Override
                public void onException(Throwable e)
                {
                    logger.error("异步消息发送失败，存入Redis: " + messageId + ", 原因: " + e.getMessage());
                    redisMessageService.saveSendFailedMessage(message);
                }
            });
        }
        catch (Exception e)
        {
            logger.error("异步消息发送异常，存入Redis: " + messageId + ", 原因: " + e.getMessage());
            redisMessageService.saveSendFailedMessage(message);
        }
    }
    
}
