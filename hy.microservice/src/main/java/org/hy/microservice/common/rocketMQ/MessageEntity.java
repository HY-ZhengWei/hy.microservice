package org.hy.microservice.common.rocketMQ;

import org.hy.microservice.common.BaseData;
import org.hy.common.Date;





/**
 * @Author: lihao
 * @Date: 2025-08-08 20:55
 * @Description: 消息实体类
 */
public class MessageEntity  extends BaseData 
{

    private static final long serialVersionUID = 9179169560460186309L;

    /** 主键ID */
    private String            id;

    /** 主题 */
    private String            topic;

    /** 标签 */
    private String            tag;

    /** 消息内容 */
    private String            content;

    /** 重试次数 */
    private int               retryCount       = 0;

    /** 创建时间 */
    private Date              createTime;

    /** 最后一次推送时间 */
    private Date              lastRetryTime;

    /** 最大重推次数 */
    private int               maxRetryCount    = 5;



    /** 构造函数 */
    public MessageEntity()
    {
    }



    public MessageEntity(String id ,String topic ,String tag ,String content)
    {
        this.id = id;
        this.topic = topic;
        this.tag = tag;
        this.content = content;
        this.createTime = new Date();
    }



    public boolean canRetry()
    {
        return retryCount < maxRetryCount;
    }



    public void incrementRetryCount()
    {
        this.retryCount++;
        this.lastRetryTime = new Date();
    }



    public String getId()
    {
        return id;
    }



    public void setId(String id)
    {
        this.id = id;
    }



    public String getTopic()
    {
        return topic;
    }



    public void setTopic(String topic)
    {
        this.topic = topic;
    }



    public String getTag()
    {
        return tag;
    }



    public void setTag(String tag)
    {
        this.tag = tag;
    }



    public String getContent()
    {
        return content;
    }



    public void setContent(String content)
    {
        this.content = content;
    }



    public int getRetryCount()
    {
        return retryCount;
    }



    public void setRetryCount(int retryCount)
    {
        this.retryCount = retryCount;
    }



    public Date getCreateTime()
    {
        return createTime;
    }



    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }



    public Date getLastRetryTime()
    {
        return lastRetryTime;
    }



    public void setLastRetryTime(Date lastRetryTime)
    {
        this.lastRetryTime = lastRetryTime;
    }



    public int getMaxRetryCount()
    {
        return maxRetryCount;
    }



    public void setMaxRetryCount(int maxRetryCount)
    {
        this.maxRetryCount = maxRetryCount;
    }

//    @Override
//    public String toString() {
//        return "MessageEntity{" +
//                "id='" + id + '\'' +
//                ", topic='" + topic + '\'' +
//                ", tag='" + tag + '\'' +
//                ", content='" + content + '\'' +
//                ", retryCount=" + retryCount +
//                ", createTime=" + createTime +
//                ", lastRetryTime=" + lastRetryTime +
//                ", maxRetryCount=" + maxRetryCount +
//                '}';
//    }
}
