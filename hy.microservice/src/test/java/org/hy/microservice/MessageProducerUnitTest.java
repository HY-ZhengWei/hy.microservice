package org.hy.microservice;

import org.hy.microservice.common.rocketMQ.RocketMQProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.rocketmq.client.producer.SendResult;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @ClassName: MessageProducerUnitTest
 * @Author: lihao
 * @Date: 2025/8/8 12:01
 * @Description 必须描述类做什么事情，实现什么功能
 */



@SpringBootTest
public class MessageProducerUnitTest {

    @Autowired
    private RocketMQProducer messageProducer;

    // @Test
    public void testSendMessageToCluster() {
        // 发送测试消息
        String topic = "LPS_Test_Topice";
        String tag = "LPS_tag";
        String content = "集成测试消息：" + System.currentTimeMillis();

        // 执行发送
        SendResult result = messageProducer.sendMessage(topic, tag, content);

        // 验证结果
        assertNotNull(result, "消息发送结果不应为null");
        assertEquals("SEND_OK", result.getSendStatus().name(), "消息发送状态应为成功");
        System.out.println("消息发送成功，消息ID：" + result.getMsgId());
    }
}
