package org.hy.microservice.common.cache;





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
    
}
