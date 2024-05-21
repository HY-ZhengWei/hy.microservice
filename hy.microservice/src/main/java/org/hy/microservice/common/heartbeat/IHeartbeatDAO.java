package org.hy.microservice.common.heartbeat;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 边缘服务的心跳的操作DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-29
 * @version     v1.0
 */
@Xjava(id="HeartbeatDAO" ,value=XType.XSQL)
public interface IHeartbeatDAO
{
	
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
    @Xsql("GXSQL_Common_Heartbeat_Save")
    public boolean save(Heartbeat i_Heartbeat);
    
    
    
    /**
     * 按EdgeIP边缘服务IP地址删除心跳信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-03-29
     * @version     v1.0
     *
     * @param i_EdgeIP
     */
    @Xsql("XSQL_Common_Heartbeat_Del")
    public void delEdgeIP(@Xparam("edgeIP") String i_EdgeIP);
    
    
    
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
    @Xsql(id="XSQL_Common_Heartbeat_Query" ,returnOne=true)
    public Heartbeat queryByEdge(@Xparam("edgeIP") String i_EdgeIP);
    
    
    
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
    @Xsql("XSQL_Common_Heartbeat_Query_DistinctHostIP")
    public List<Heartbeat> queryByValids(@Xparam("second") Integer i_Second);
    
    
    
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
    @Xsql("XSQL_Common_Heartbeat_Query")
    public List<Heartbeat> query(Heartbeat i_Heartbeat);
    
}
