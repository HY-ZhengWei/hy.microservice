package org.hy.microservice.common.probe.data;

import org.hy.common.app.Param;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.common.cache.CacheFactory;
import org.hy.microservice.common.cache.ICache;





/**
 * 缓存层：数据探针的数据
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-06
 * @version     v1.0
 */
@Xjava
public class ProbeDataCache implements IProbeDataCache
{
    
    /** 缓存的表名称 */
    private static final String $RTable = "TProbeData";
    
    
    
    /** 缓存对象 */
    private ICache<ProbeData> cache;
    
    /** 缓存的数据库名称。默认使用微服务的服务名称 */
    @Xjava(ref="MS_Common_ServiceName")
    private Param             cacheDatabase;
    
    
    
    public ProbeDataCache()
    {
        boolean v_UseRemote = "启用".equals(XJava.getParam("MS_Common_IsRedis").getValue());
        this.cache = CacheFactory.newInstanceOf(v_UseRemote ,ProbeData.class);
    }
    
    
    
    /**
     * 数据弹出队列
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-06
     * @version     v1.0
     *
     * @return
     */
    @Override
    public ProbeData get()
    {
        return this.cache.getQueue(this.cacheDatabase.getValue() ,$RTable);
    }
    
    
    
    /**
     * 数据压入队列
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @param i_ProbeData  数据探针的数据
     */
    @Override
    public void push(ProbeData i_ProbeData)
    {
        this.cache.putQueue(this.cacheDatabase.getValue() ,$RTable ,i_ProbeData.getAssetID() ,i_ProbeData);
    }
    
}
