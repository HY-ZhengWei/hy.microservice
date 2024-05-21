package org.hy.microservice.common.heartbeat;

import java.util.List;
import java.util.Map;





/**
 * 心跳的业务层
 * 
 * @author      ZhengWei(HY)
 * @createDate  2024-01-29
 * @version     v1.0
 */
public interface IHeartbeatService
{
    
    /**
     * 我认领的所有任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-30
     * @version     v1.0
     *
     * @return
     */
    public Map<String ,String> myClaimTasks();
    
    
    
    /**
     * 保存边缘心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     * 
     * @return
     */
    public void heartbeat();
    
    
    
    /**
     * 新增、修改边缘服务的心跳
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     *
     * @param i_Heartbeat  边缘服务的心跳
     * @return
     */
    public Heartbeat save(Heartbeat i_Heartbeat);
    
    
    
    /**
     * 按IP查找边缘计算服务的心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     * 
     * @param i_EdgeIP    边缘服务的IP
     * @return
     */
    public Heartbeat queryByEdge(String i_EdgeIP);
    
    
    
    /**
     * 查询在线的边缘服务。即，仅查询最后多少秒几的心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     * 
     * @param i_Second  秒数
     * @return
     */
    public List<Heartbeat> queryByValids(Integer i_Second);
    
    
    
    /**
     * 查找边缘计算服务的心跳列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-29
     * @version     v1.0
     * 
     * @param i_Heartbeat  边缘服务的心跳
     * @return
     */
    public List<Heartbeat> query(Heartbeat i_Heartbeat);
    
}
