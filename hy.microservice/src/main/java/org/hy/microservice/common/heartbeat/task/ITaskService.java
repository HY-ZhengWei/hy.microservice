package org.hy.microservice.common.heartbeat.task;

import java.util.List;

import org.hy.common.Return;





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
    
    
    
    /**
     * 更新执行任务。
     *    适用场景01：当执行任务的真实对象未初始化时，可在此方法内初始化。
     *    适用场景02：当执行任务的真实对象配置发生变化时，可在些方法内更新。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     * 
     * @param i_Task  任务信息
     * @return  返回更新结果。
     *             返回true 表示：执行任务的真实对象发生了变化（创建、修改）
     *             返回false表示：执行任务的真实对象未改变
     */
    public Return<ITaskX> refresh(ITask i_Task);
    
    
    
    /**
     * 更新认领任务的服务IP
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @param i_ITaskX   任务执行实例对象
     * @param i_ClaimIP  认领任务的服务IP
     */
    public void updateClaimIP(ITaskX i_ITaskX ,String i_ClaimIP);
    
}
