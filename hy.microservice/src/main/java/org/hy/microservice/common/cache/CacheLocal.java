package org.hy.microservice.common.cache;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.xml.XJava;





/**
 * 本地缓存
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-01
 * @version     v1.0
 * @param <Data>  缓存的数据对象
 *              v2.0  2024-09-20  添加：getRowsList 和 getRowsMap 全表数据获取的方法
 *              v3.0  2024-09-23  添加：开放字符串的get、set方法
 */
public class CacheLocal<Data> implements ICache<Data>
{
    
    private static final String $Level = ">";
    
    private Class<Data> dataClass;
    
    
    
    public CacheLocal()
    {
        this.dataClass = this.parserDataType();
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
        XJava.putObject(i_DataBase + $Level + i_Table + $Level + i_ID ,i_Data);
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
        XJava.putObject(i_DataBase + $Level + i_Table + $Level + i_ID ,i_Data ,i_Second);
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
    @SuppressWarnings("unchecked")
    @Override
    public Data remove(String i_DataBase ,String i_Table ,String i_ID)
    {
        Data v_Old = (Data) XJava.getObject(i_DataBase + $Level + i_Table + $Level + i_ID);
        if ( v_Old != null )
        {
            XJava.remove(i_ID);
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
    @SuppressWarnings("unchecked")
    @Override
    public Data get(String i_DataBase ,String i_Table ,String i_ID)
    {
        return (Data) XJava.getObject(i_DataBase + $Level + i_Table + $Level + i_ID ,false);
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
    @SuppressWarnings("unchecked")
    @Override
    public Map<String ,Data> getRowsMap(String i_DataBase ,String i_Table)
    {
        Map<String ,Object> v_Datas = XJava.getObjects(i_DataBase + $Level + i_Table + $Level ,false);
        Map<String ,Data>   v_Ret   = new HashMap<String ,Data>();
        
        if ( !Help.isNull(v_Datas) )
        {
            for (Map.Entry<String ,Object> v_Item : v_Datas.entrySet())
            {
                if ( v_Item.getValue() != null && v_Item.getValue().getClass().equals(this.dataClass) )
                {
                    v_Ret.put(v_Item.getKey() ,(Data) v_Item.getValue());
                }
            }
            
            v_Datas.clear();
            v_Datas = null;
        }
        
        return v_Ret;
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
    @SuppressWarnings("unchecked")
    @Override
    public List<Data> getRowsList(String i_DataBase ,String i_Table)
    {
        Map<String ,Object> v_Datas = XJava.getObjects(i_DataBase + $Level + i_Table + $Level ,false);
        List<Data>          v_Ret   = new ArrayList<Data>();
        
        if ( !Help.isNull(v_Datas) )
        {
            for (Map.Entry<String ,Object> v_Item : v_Datas.entrySet())
            {
                if ( v_Item.getValue() != null && v_Item.getValue().getClass().equals(this.dataClass) )
                {
                    v_Ret.add((Data) v_Item.getValue());
                }
            }
            
            v_Datas.clear();
            v_Datas = null;
        }
        
        return v_Ret;
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
        XJava.putObject(i_Key ,i_Value);
        return true;
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
        XJava.putObject(i_Key ,i_Value ,i_ExpireTime);
        return true;
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
    public synchronized Boolean setnx(String i_Key ,String i_Value)
    {
        if ( XJava.getObject(i_Key) == null )
        {
            XJava.putObject(i_Key ,i_Value);
            return true;
        }
        else
        {
            return false;
        }
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
        Object v_Value = XJava.getObject(i_Key);
        if ( v_Value != null )
        {
            return v_Value.toString();
        }
        else
        {
            return null;
        }
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
    public synchronized String getdel(String i_Key)
    {
        String v_Value = this.get(i_Key);
        XJava.remove(v_Value);
        return v_Value;
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
    public synchronized Long del(String ... i_Keys)
    {
        if ( !Help.isNull(i_Keys) )
        {
            long v_Count = 0L;
            for (String v_Key : i_Keys)
            {
                XJava.remove(v_Key);
                v_Count++;
            }
            
            return v_Count;
        }
        else
        {
            return 0L;
        }
    }
    
}
