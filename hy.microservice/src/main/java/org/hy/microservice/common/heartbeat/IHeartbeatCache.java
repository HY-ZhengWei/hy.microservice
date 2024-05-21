package org.hy.microservice.common.heartbeat;

import java.util.List;





/**
 * 心跳的缓存层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-03-21
 * @version     v1.0
 */
public interface IHeartbeatCache
{
    
    
    /**
     * 保存边缘心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-21
     * @version     v1.0
     * 
     * @param io_Heartbeat  边缘服务的心跳
     * @return
     */
    public boolean save(Heartbeat io_Heartbeat);
    
    
    
    /**
     * 查询在线的边缘服务。即，仅查询最后多少秒几的心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-21
     * @version     v1.0
     * 
     * @param i_Second  秒数
     * @return
     */
    public List<Heartbeat> queryByValids(int i_Second);
    
    
    
    /**
     * 按EdgeIP边缘服务IP地址删除心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-29
     * @version     v1.0
     *
     * @param i_EdgeIP
     */
    public void delEdgeIP(String i_EdgeIP);
    
    
    
    /**
     * 删除过期边缘心跳
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-26
     * @version     v1.0
     */
    public void delExpire();
    
    
    
    /**
     * 删除过期边缘心跳
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-26
     * @version     v1.0
     *
     * @param i_Day  过期多少天
     */
    public void delExpire(int i_Day);
    
}
