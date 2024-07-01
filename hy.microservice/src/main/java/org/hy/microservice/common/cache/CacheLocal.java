package org.hy.microservice.common.cache;

import org.hy.common.xml.XJava;





/**
 * 本地缓存
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-01
 * @version     v1.0
 * @param <Data>  缓存的数据对象
 */
public class CacheLocal<Data> implements ICache<Data>
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
     * @param i_ID        主键IP（要求：全域、全库、全表均是惟一的）
     * @param i_Data      数据
     */
    @Override
    public void save(String i_DataBase ,String i_Table ,String i_ID ,Data i_Data)
    {
        XJava.putObject(i_ID ,i_Data);
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
     * @param i_ID        主键IP（要求：全域、全库、全表均是惟一的）
     * @param i_Data      数据
     * @param i_Second    过期时长(单位：秒)。指当前时刻过i_Second秒后过期失效。
     */
    @Override
    public void save(String i_DataBase ,String i_Table ,String i_ID ,Data i_Data ,long i_Second)
    {
        XJava.putObject(i_ID ,i_Data ,i_Second);
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
     * @param i_ID        主键IP（要求：全域、全库、全表均是惟一的）
     * @return            返回删除的数据
     */
    @SuppressWarnings("unchecked")
    @Override
    public Data remove(String i_DataBase ,String i_Table ,String i_ID)
    {
        Data v_Old = (Data) XJava.getObject(i_ID);
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
     * @param i_ID        主键IP（要求：全域、全库、全表均是惟一的）
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public Data get(String i_DataBase ,String i_Table ,String i_ID)
    {
        return (Data) XJava.getObject(i_ID ,false);
    }
    
}
