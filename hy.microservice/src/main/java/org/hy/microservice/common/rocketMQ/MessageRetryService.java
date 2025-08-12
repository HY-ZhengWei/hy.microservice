package org.hy.microservice.common.rocketMQ;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;

import java.util.Map;





/**
 * @Author: lihao
 * @Date: 2025-08-08 21:01
 * @Description: 失败消息重试服务类
 */
@Xjava
public class MessageRetryService {

    private Logger logger = new Logger(MessageRetryService.class);

    @Xjava
    private RedisMessageService redisMessageService;



    /**
     * @Title: retrySendFailedMessages
     * @Author: lihao
     * @Date: 2025-08-08 20:59
     * @Params: []
     * @Return: void
     * @Description: 每3分钟重试发送失败的消息
     */
    public void retrySendFailedMessages()
    {
        logger.debug("开始重试发送失败的消息...");
        Map<String ,MessageEntity> failedMessageIds = redisMessageService.getSendFailedMessageIds();
        if ( failedMessageIds == null || failedMessageIds.isEmpty() )
        {
            logger.debug("没有需要重试的发送失败消息");
            return;
        }
        for (Map.Entry<String ,MessageEntity> entry : failedMessageIds.entrySet())
        {
            String messageId = entry.getKey();
            MessageEntity message = entry.getValue();
            if ( message == null || !message.canRetry() )
            {
                if ( message != null && !message.canRetry() )
                {
                    logger.debug("消息达到最大重试次数，不再重试: " + messageId);
                    // 可以在这里添加告警逻辑
                }
                continue;
            }
            try
            {
                String destination = message.getTopic() + ":" + message.getTag();
                RocketMQTemplate rocketMQTemplate = (RocketMQTemplate) XJava.getObject("MS_MQ_TEMPLATE");
                SendResult result = rocketMQTemplate.syncSend(destination ,message.getContent());
                if ( result != null )
                {
                    redisMessageService.deleteSendFailedMessage(messageId);
                    logger.debug("消息发送重试成功: " + messageId + ", 重试次数: " + message.getRetryCount());
                }
                else
                {
                    message.incrementRetryCount();
                    redisMessageService.updateSendFailedMessage(message);
                    logger.debug("消息发送重试失败: " + messageId + ", 重试次数: " + message.getRetryCount());
                }
            }
            catch (Exception e)
            {
                message.incrementRetryCount();
                redisMessageService.updateSendFailedMessage(message);
                logger.error("消息发送重试异常: " + messageId + ", 原因: " + e.getMessage());
            }
        }
    }



    /**
     * @Title: retryConsumeFailedMessages
     * @Author: lihao
     * @Date: 2025-08-08 21:01
     * @Params: []
     * @Return: void
     * @Description: 每5分钟重试试消费失败的消息
     */
    public void retryConsumeFailedMessages()
    {
        logger.debug("开始重试试消费失败的消息...");
        Map<String ,MessageEntity> failedMessageIds = redisMessageService.getConsumeFailedMessageIds();
        if ( failedMessageIds == null || failedMessageIds.isEmpty() )
        {
            logger.debug("没有需要重试的消费失败消息");
            return;
        }
        for (Map.Entry<String ,MessageEntity> entry : failedMessageIds.entrySet())
        {
            String messageId = entry.getKey();
            MessageEntity message = entry.getValue();
            if ( message == null || !message.canRetry() )
            {
                if ( message != null && !message.canRetry() )
                {
                    logger.debug("消息达到最大重试次数，不再重试: " + messageId);
                    // 可以在这里添加告警逻辑
                }
                continue;
            }
            try
            {
                // 重新处理消息
                processMessage(message.getContent());
                redisMessageService.deleteConsumeFailedMessage(messageId);
                logger.debug("消息消费重试成功: " + messageId + ", 重试次数: " + message.getRetryCount());
            }
            catch (Exception e)
            {
                message.incrementRetryCount();
                redisMessageService.updateConsumeFailedMessage(message);
                logger.error("消息消费重试失败: " + messageId + ", 原因: " + e.getMessage());
            }
        }
    }



    // 重新处理消息的逻辑
    private void processMessage(String message) throws Exception
    {
        // 和消费者中的处理逻辑一致
        logger.debug("重新处理消息: " + message);
        // 模拟业务处理
    }
}
