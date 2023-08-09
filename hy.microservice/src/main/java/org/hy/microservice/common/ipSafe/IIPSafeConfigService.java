package org.hy.microservice.common.ipSafe;

import java.util.List;

import org.hy.common.TablePartitionRID;





/**
 * 系统安全访问IP黑白名单的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-05-26
 * @version     v1.0
 */
public interface IIPSafeConfigService
{
    
    /**
     * 新增系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param io_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    public IPSafeConfig insert(IPSafeConfig io_IPSafeConfig);
    
    
    
    /**
     * 更新系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    public IPSafeConfig update(IPSafeConfig i_IPSafeConfig);
    
    
    
    /**
     * 按主键，查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    public IPSafeConfig queryByID(IPSafeConfig i_IPSafeConfig);
    
    
    
    /**
     * 按主键，查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_ID  主键
     * @return
     */
    public IPSafeConfig queryByID(String i_ID);
    
    
    
    /**
     * 按类型，查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_IPSafeConfig  统安全访问IP黑白名单
     * @return
     */
    public List<IPSafeConfig> queryByIPType(IPSafeConfig i_IPSafeConfig);
    
    
    
    /**
     * 按类型，查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @return
     */
    public TablePartitionRID<String ,IPSafeConfig> queryAll();
    
    
    
    /**
     * 刷新黑白名单缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @return
     */
    public TablePartitionRID<String ,IPSafeConfig> cacheIPSafesRefurbish();
    
    
    
    /**
     * 添加IP黑白名单的命中信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-08
     * @version     v1.0
     *
     * @param i_IpSafeKey
     * @param i_IPSafeType
     */
    public void putIPSafeHit(String i_IpSafeKey ,String i_IPSafeType);
    
    
    
    /**
     * 获取IP黑白名单的命中信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-08
     * @version     v1.0
     *
     * @param i_IpSafeKey
     * @return
     */
    public String getIPSafeHit(String i_IpSafeKey);
    
}
