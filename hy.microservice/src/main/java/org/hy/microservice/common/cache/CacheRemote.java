package org.hy.microservice.common.cache;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.hy.common.redis.IRedis;





/**
 * 远程缓存
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-01
 * @version     v1.0
 * @param <Data>  缓存的数据对象
 */
public class CacheRemote<Data> implements ICache<Data>
{
    
    private IRedis      redis;
    
    private Class<Data> dataClass;
    
    
    
    public CacheRemote(IRedis i_Redis)
    {
        this.redis     = i_Redis;
        this.dataClass = this.parserDataType();
    }
    
    
    
    public CacheRemote(IRedis i_Redis ,Class<Data> i_DataClass)
    {
        this.redis     = i_Redis;
        this.dataClass = i_DataClass;
    }
    
    
    
    /**
     * 解析泛型<Data>的真实元类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private Class<Data> parserDataType()
    {
        Type v_SuperClass = getClass().getGenericSuperclass();
        if (v_SuperClass instanceof ParameterizedType)
        {
            ParameterizedType v_ParameterizedType = (ParameterizedType) v_SuperClass;
            Type[] v_TypeArgs = v_ParameterizedType.getActualTypeArguments();
            if ( v_TypeArgs.length > 0 && v_TypeArgs[0] instanceof Class )
            {
                return (Class<Data>) v_TypeArgs[0];
            }
        }
        
        return null;
    }
    
    
    
    /**
     * 保存数据（创建&更新）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param i_DataBase  数据库名称
     * @param i_Table     表名称
     * @param i_ID        主键ID（要求：全域、全库、全表均是惟一的）
     * @param i_Data      数据
     */
    @Override
    public void save(String i_DataBase ,String i_Table ,String i_ID ,Data i_Data)
    {
        this.redis.save(i_DataBase ,i_Table ,i_ID ,i_Data ,true);
    }
    
    
    
    /**
     * 保存数据（创建&更新）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param i_DataBase  数据库名称
     * @param i_Table     表名称
     * @param i_ID        主键ID（要求：全域、全库、全表均是惟一的）
     * @param i_Data      数据
     * @param i_Second    过期时长(单位：秒)。指当前时刻过i_Second秒后过期失效。
     */
    @Override
    public void save(String i_DataBase ,String i_Table ,String i_ID ,Data i_Data ,long i_Second)
    {
        this.redis.save(i_DataBase ,i_Table ,i_ID ,i_Data ,true);
        // TODO 过期时长的功能须等待 Redis包的实现
    }
    
    
    
    /**
     * 删除数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param i_DataBase  数据库名称
     * @param i_Table     表名称
     * @param i_ID        主键ID（要求：全域、全库、全表均是惟一的）
     * @return            返回删除的数据
     */
    @Override
    public Data remove(String i_DataBase ,String i_Table ,String i_ID)
    {
        Data v_Old = this.redis.getRow(i_ID ,this.dataClass);
        if ( v_Old != null )
        {
            this.redis.delete(i_DataBase ,i_Table ,i_ID);
        }
        return v_Old;
    }
    
    
    
    /**
     * 获取数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param i_DataBase  数据库名称
     * @param i_Table     表名称
     * @param i_ID        主键ID（要求：全域、全库、全表均是惟一的）
     * @return
     */
    @Override
    public Data get(String i_DataBase ,String i_Table ,String i_ID)
    {
        return this.redis.getRow(i_ID ,this.dataClass);
    }
    
}
