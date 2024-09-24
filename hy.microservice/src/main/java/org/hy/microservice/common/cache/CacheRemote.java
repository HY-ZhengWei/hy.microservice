package org.hy.microservice.common.cache;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.hy.common.redis.IRedis;





/**
 * 远程缓存
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-01
 * @version     v1.0
 * @param <Data>  缓存的数据对象
 *              v2.0  2024-09-20  添加：getRowsList 和 getRowsMap 全表数据获取的方法
 *              v3.0  2024-09-23  添加：开放字符串的get、set方法
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
        this.redis.save(i_DataBase ,i_Table ,i_ID ,i_Data ,true ,i_Second);
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
            Long v_Count = this.redis.delete(i_DataBase ,i_Table ,i_ID);
            if ( v_Count == null || v_Count <= 0 )
            {
                return null;
            }
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
    
    
    
    /**
     * 获取全表数据（Map结构）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @param i_DataBase  数据库名称
     * @param i_Table     表名称
     * @return            Map.key行主键，Map.value行数据
     */
    @Override
    public Map<String ,Data> getRowsMap(String i_DataBase ,String i_Table)
    {
        return this.redis.getRows(i_DataBase ,i_Table ,this.dataClass);
    }
    
    
    
    /**
     * 获取全表数据（List结构）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @param i_DataBase  数据库名称
     * @param i_Table     表名称
     * @return
     */
    @Override
    public List<Data> getRowsList(String i_DataBase ,String i_Table)
    {
        return this.redis.getRowsList(i_DataBase ,i_Table ,this.dataClass);
    }
    
    
    
    /**
     * 设置数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-23
     * @version     v1.0
     *
     * @param i_Key    关键字
     * @param i_Value  数据
     * @return         成功返回true
     */
    @Override
    public Boolean set(String i_Key ,String i_Value)
    {
        return this.redis.set(i_Key ,i_Value);
    }
    
    
    
    /**
     * 设置数据，并且设定过期时长
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-23
     * @version     v1.0
     *
     * @param i_Key         关键字
     * @param i_Value       数据
     * @param i_ExpireTime  过期时间（单位：秒）
     * @return              成功返回true
     */
    @Override
    public Boolean setex(String i_Key ,String i_Value ,Long i_ExpireTime)
    {
        return this.redis.setex(i_Key ,i_Value ,i_ExpireTime);
    }
    
    
    
    /**
     * 设置数据，仅在关键字不存在时设置数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-23
     * @version     v1.0
     *
     * @param i_Key    关键字
     * @param i_Value  数据
     * @return         是否设置数据
     */
    @Override
    public Boolean setnx(String i_Key ,String i_Value)
    {
        return this.redis.setnx(i_Key ,i_Value);
    }
    
    
    
    /**
     * 获取数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-23
     * @version     v1.0
     *
     * @param i_Key  关键字
     * @return
     */
    @Override
    public String get(String i_Key)
    {
        return this.redis.get(i_Key);
    }
    
    
    
    /**
     * 获取数据并删除
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-23
     * @version     v1.0
     *
     * @param i_Key  关键字
     * @return
     */
    @Override
    public String getdel(String i_Key)
    {
        return this.redis.getdel(i_Key);
    }
    
    
    
    /**
     * 删除数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-23
     * @version     v1.0
     *
     * @param i_Keys  一个或多个关键字
     * @return        返回删除数据的数量
     */
    @Override
    public Long del(String ... i_Keys)
    {
        return this.redis.del(i_Keys);
    }
    
}
