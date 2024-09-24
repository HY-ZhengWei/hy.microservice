package org.hy.microservice.common.cache;

import java.util.List;
import java.util.Map;





/**
 * 缓存接口
 *   可实现本地缓存与远程缓存的无缝切换。
 * 
 *   本地缓存的代表为：XJava
 *   远程缓存的代表为：Redis
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-01
 * @version     v1.0
 *              v2.0  2024-09-20  添加：getRowsList 和 getRowsMap 全表数据获取的方法
 *              v3.0  2024-09-23  添加：开放字符串的get、set方法
 */
public interface ICache<Data>
{
    
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
    public void save(String i_DataBase ,String i_Table ,String i_ID ,Data i_Data);
    
    
    
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
    public void save(String i_DataBase ,String i_Table ,String i_ID ,Data i_Data ,long i_Second);
    
    
    
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
    public Data remove(String i_DataBase ,String i_Table ,String i_ID);
    
    
    
    /**
     * 删除内存表。会同时删除表数据、表关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-24
     * @version     v1.0
     *
     * @param i_Database   库名称
     * @param i_Table      表名称
     */
    public boolean dropTable(String i_Database ,String i_Table);
    
    
    
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
    public Data get(String i_DataBase ,String i_Table ,String i_ID);
    
    
    
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
    public Map<String ,Data> getRowsMap(String i_DataBase ,String i_Table);
    
    
    
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
    public List<Data> getRowsList(String i_DataBase ,String i_Table);
    
    
    
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
    public Boolean set(String i_Key ,String i_Value);
    
    
    
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
    public Boolean setex(String i_Key ,String i_Value ,Long i_ExpireTime);
    
    
    
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
    public Boolean setnx(String i_Key ,String i_Value);
    
    
    
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
    public String get(String i_Key);
    
    
    
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
    public String getdel(String i_Key);
    
    
    
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
    public Long del(String ... i_Keys);
    
}
