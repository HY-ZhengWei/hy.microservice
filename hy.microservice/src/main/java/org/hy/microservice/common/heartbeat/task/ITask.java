package org.hy.microservice.common.heartbeat.task;

import org.hy.common.Date;





/**
 * 任务接口。本类是与数据库交互有对象
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-05-20
 * @version     v1.0
 */
public interface ITask
{
    
    /**
     * 任务的逻辑ID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public String getXid();
    
    
    
    /**
     * 任务创建时间
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public Date getCreateTime();
    
    
    
    /**
     * 任务备注说明
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public String getComment();
    
}
