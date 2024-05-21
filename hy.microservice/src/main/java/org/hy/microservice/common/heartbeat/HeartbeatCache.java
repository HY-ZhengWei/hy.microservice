package org.hy.microservice.common.heartbeat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.redis.IRedis;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;





/**
 * 心跳的缓存层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-03-21
 * @version     v1.0
 *              v2.0  2024-04-12  添加：去除重复的主机IP（K8s节点IP），确保对外服务的IP地址，固定不变，也不会有重复造成的冲突
 */
@Xjava
public class HeartbeatCache implements IHeartbeatCache
{
    
    /** Redis对象的XID */
    private static final String $RedisXID = "RedisOperation_MS_Common";
    
    /** Redis表名称 */
    private static final String $RTable   = "Heartbeat";
    
    
    
    /** Redis数据库名称。默认使用微服务的服务名称 */
    @Xjava(ref="MS_Common_ServiceName")
    private Param  redisDatabaseName;
    
    @Xjava(ref="MS_Common_Heartbeat_RedisDelExpire")
    private Param  redisDelExpire;
    
    
    
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
    @Override
    public boolean save(Heartbeat io_Heartbeat)
    {
        // 未将Redis定义为类的成员变量原因是：有可能业务尚没有配置Redis。不用强行绑定要求必须使用Redis
        IRedis v_Redis = (IRedis) XJava.getObject($RedisXID);
        
        io_Heartbeat.setUpdateTime(v_Redis.getNowTime());
        if ( io_Heartbeat.getUpdateTime() == null )
        {
            io_Heartbeat.setUpdateTime(new Date());
        }
        else
        {
            io_Heartbeat.setUpdateTime(new Date(io_Heartbeat.getUpdateTime().getTime() + 8 * 60 * 60 * 1000));
        }
        
        Date v_InvalidTime = io_Heartbeat.getInvalidTime();
        if ( v_InvalidTime != null )
        {
            // 确保仅首次写入无效时间，之后将不在记录无效时间
            v_Redis.insert("msModbus" ,"Heartbeat" ,io_Heartbeat.getEdgeIP() ,"invalidTime" ,io_Heartbeat.getInvalidTime().getFull());
            io_Heartbeat.setInvalidTime(null);
        }
        
        // 为了预防不同微服务模块的边缘计算IP雷同的情况。在Redis.key由服务名称+边缘计算IP组成。
        if ( v_Redis.save(this.redisDatabaseName.getValue() ,$RTable ,this.redisDatabaseName.getValue() + io_Heartbeat.getEdgeIP() ,io_Heartbeat) >= 1 )
        {
            io_Heartbeat.setInvalidTime(v_InvalidTime);
            return true;
        }
        else
        {
            io_Heartbeat.setInvalidTime(v_InvalidTime);
            return false;
        }
    }
    
    
    
    /**
     * 查询在线的边缘服务。即，仅查询最后多少秒几的心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-21
     * @version     v1.0
     *              v2.0  2024-04-12  添加：去除重复的主机IP（K8s节点IP），确保对外服务的IP地址，固定不变，也不会有重复造成的冲突
     * 
     * @param i_Second  秒数
     * @return
     */
    @Override
    public List<Heartbeat> queryByValids(int i_Second)
    {
        // 未将Redis定义为类的成员变量原因是：有可能业务尚没有配置Redis。不用强行绑定要求必须使用Redis
        IRedis v_Redis = (IRedis) XJava.getObject($RedisXID);
        
        Date v_RedisNowTime = v_Redis.getNowTime();
        if ( v_RedisNowTime == null )
        {
            return null;
        }
        else
        {
            v_RedisNowTime = new Date(v_RedisNowTime.getTime() + 8 * 60 * 60 * 1000);
        }
        
        List<Heartbeat> v_Heartbeats = v_Redis.getRowsList(this.redisDatabaseName.getValue() ,$RTable ,Heartbeat.class);
        
        if ( !Help.isNull(v_Heartbeats) )
        {
            // 过滤失效的服务
            for (int x=v_Heartbeats.size()-1; x>=0; x--)
            {
                Heartbeat v_Item = v_Heartbeats.get(x);
                
                if ( v_Item.getIsValid() != 1 )
                {
                    v_Heartbeats.remove(x);
                    continue;
                }
                
                if ( v_RedisNowTime.differ(v_Item.getUpdateTime()) >= i_Second * -1000 )
                {
                    v_Heartbeats.remove(x);
                    continue;
                }
                
                if ( v_RedisNowTime.differ(new Date(v_Item.getOsTime())) >= i_Second * -1000 )
                {
                    v_Heartbeats.remove(x);
                    continue;
                }
            }
            
            if ( Help.isNull(v_Heartbeats) )
            {
                return v_Heartbeats;
            }
            
            // 去除重复的主机IP（K8s节点IP），确保对外服务的IP地址，固定不变，也不会有重复造成的冲突
            Help.toSort(v_Heartbeats ,"hostIP" ,"edgeStartTime DESC");
            Map<String ,Integer> v_DistinctHostIP = new HashMap<String ,Integer>();
            for (int x=v_Heartbeats.size()-1; x>=0; x--)
            {
                Heartbeat v_Item      = v_Heartbeats.get(x);
                Integer   v_ListIndex = v_DistinctHostIP.get(v_Item.getHostIP());
                if ( v_ListIndex == null )
                {
                    v_DistinctHostIP.put(v_Item.getHostIP() ,v_ListIndex);
                }
                else
                {
                    v_Heartbeats.remove(x);
                }
            }
            v_DistinctHostIP.clear();
            v_DistinctHostIP = null;
            
            Help.toSort(v_Heartbeats ,"edgeStartTime" ,"edgeIP");
        }
        
        return v_Heartbeats;
    }
    
    
    
    /**
     * 按EdgeIP边缘服务IP地址删除心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-29
     * @version     v1.0
     *
     * @param i_EdgeIP
     */
    @Override
    public void delEdgeIP(String i_EdgeIP)
    {
        // 未将Redis定义为类的成员变量原因是：有可能业务尚没有配置Redis。不用强行绑定要求必须使用Redis
        IRedis v_Redis = (IRedis) XJava.getObject($RedisXID);
        
        // 为了预防不同微服务模块的边缘计算IP雷同的情况。在Redis.key由服务名称+边缘计算IP组成。
        v_Redis.delete(this.redisDatabaseName.getValue() ,$RTable ,this.redisDatabaseName.getValue() + i_EdgeIP);
    }
    
    
    
    /**
     * 删除过期边缘心跳
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-26
     * @version     v1.0
     */
    @Override
    public void delExpire()
    {
        this.delExpire(this.redisDelExpire.getValueInt());
    }
    
    
    
    /**
     * 删除过期边缘心跳
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-26
     * @version     v1.0
     *
     * @param i_Day  过期多少天
     */
    @Override
    public void delExpire(int i_Day)
    {
        // 未将Redis定义为类的成员变量原因是：有可能业务尚没有配置Redis。不用强行绑定要求必须使用Redis
        IRedis v_Redis = (IRedis) XJava.getObject($RedisXID);
        
        Map<String ,Heartbeat> v_AllDatas = v_Redis.getRows(this.redisDatabaseName.getValue() ,$RTable ,Heartbeat.class);
        if ( Help.isNull(v_AllDatas) )
        {
            return;
        }
        
        Date v_RedisNowTime = v_Redis.getNowTime();
        if ( v_RedisNowTime == null )
        {
            return;
        }
        
        long v_MaxExpireTime = i_Day * 24 * 60 * 60 * 1000;
        for (Heartbeat v_Item : v_AllDatas.values())
        {
            if ( v_RedisNowTime.differ(v_Item.getUpdateTime()) > v_MaxExpireTime )
            {
                this.delEdgeIP(v_Item.getEdgeIP());
            }
        }
    }
    
}
