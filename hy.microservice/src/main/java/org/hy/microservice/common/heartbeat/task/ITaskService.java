package org.hy.microservice.common.heartbeat.task;

import java.util.List;





/**
 * 心跳任务业务的统一接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-05-20
 * @version     v1.0
 */
public interface ITaskService
{
    
    /**
     * 获取所有任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public List<ITask> getTasks();
    
}
