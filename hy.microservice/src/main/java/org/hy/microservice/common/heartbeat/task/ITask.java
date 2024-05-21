package org.hy.microservice.common.heartbeat.task;

import org.hy.common.Date;





/**
 * 任务接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-05-20
 * @version     v1.0
 */
public interface ITask
{
    
    /**
     * 更新执行任务。
     *    适用场景01：当执行任务的真实对象未初始化时，可在此方法内初始化。
     *    适用场景02：当执行任务的真实对象配置发生变化时，可在些方法内更新。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return  返回更新结果。
     *             返回true 表示：执行任务的真实对象发生了变化（创建、修改）
     *             返回false表示：执行任务的真实对象未改变
     */
    public boolean refresh();
    
    
    
    /**
     * 执行任务是否运行正常
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public boolean isRunOK();
    
    
    
    /**
     * 更新认领任务的服务IP
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @param i_ClaimIP  认领任务的服务IP
     */
    public void updateClaimIP(String i_ClaimIP);
    
    
    
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
     * 实时推送是否生效
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public boolean isWebPush();
    
    
    
    /**
     * 实时推送的名称。即暴露给外界使用的WebSocket服务名称
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public String getWebPushName();
    
    
    
    /**
     * 实时推送间隔时长（单位：秒）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public Integer getWebPushInterval();
    
    
    
    /**
     * 实时推送的执行方法名称
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public String getWebPushExecuteName();
    
    
    
    /**
     * 写库XSQL是否生效
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public boolean isXsql();
    
    
    
    /**
     * 写库XSQL的XID。msData服务上提供写库操作的XSQL的XID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public String getXsqlXID();
    
    
    
    /**
     * 写库XSQL间隔时长（单位：分钟）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public Integer getXsqlInterval();
    
    
    
    /**
     * 写库XSQL的执行方法名称
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-20
     * @version     v1.0
     *
     * @return
     */
    public String getXsqlExecuteName();
    
    
    
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
