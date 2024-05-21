package org.hy.microservice.common.heartbeat.task;





/**
 * 任务执行对象的接口。本类将由ITask构建出来
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-05-21
 * @version     v1.0
 */
public interface ITaskX
{
    
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
