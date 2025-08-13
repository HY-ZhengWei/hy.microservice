package org.hy.microservice.common.cache;

import org.hy.common.redis.IRedis;
import org.hy.common.xml.XJava;





/**
 * 缓存的简单工厂，构建本地缓存或远程缓存的统一方法类
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-01
 * @version     v1.0
 */
public class CacheFactory
{
    
    /**
     * 构建缓存实例对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param <Data>            缓存数据对象
     * @param i_UseRemoteCache  是否启用远程缓存。即，工厂构建的产品类型
     * @param i_DataClass       缓存数据对象的元类型
     * @return
     */
    public static <Data> ICache<Data> newInstanceOf(boolean i_UseRemoteCache ,Class<Data> i_DataClass)
    {
        if ( i_UseRemoteCache )
        {
            return new CacheRemote<Data>((IRedis) XJava.getObject("RedisOperation_MS_Common") ,i_DataClass);
        }
        else
        {
            return new CacheLocal<Data>(i_DataClass);
        }
    }
    
    
    
    private CacheFactory()
    {
        // 本类不可被 new
    }
    
}
