package org.hy.microservice;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.redis.type.RedisMapType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.common.cache.CacheFactory;
import org.hy.microservice.common.cache.ICache;






/**
 * 测试单元：通用Map结构，以支持动态编程
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-02-06
 * @version     v1.0
 */
@Xjava
public class JU_RedisMap
{
    
    /** 缓存对象 */
    @SuppressWarnings("rawtypes")
    private ICache<HashMap> cache;
    
    
    
    public JU_RedisMap()
    {
        this.cache = CacheFactory.newInstanceOf(true ,HashMap.class);
    }
    
    
    
    @SuppressWarnings({"rawtypes" ,"unchecked"})
    public void test()
    {
        String                  v_DBName  = "TestDB";
        String                  v_TabName = "Test";
        HashMap<String ,Object> v_RowData = new HashMap<String ,Object>();
        
        RedisMapType.save(v_DBName ,v_TabName ,"F01" ,String.class);
        RedisMapType.save(v_DBName ,v_TabName ,"F02" ,Integer.class);
        RedisMapType.save(v_DBName ,v_TabName ,"F03" ,Date.class);
        
        v_RowData.put("F01" ,"V01");
        v_RowData.put("F02" ,123456);
        v_RowData.put("F03" ,new Date());
        
        this.cache.save(v_DBName ,v_TabName ,"Row01" ,v_RowData);
        this.cache.save(v_DBName ,v_TabName ,"Row02" ,v_RowData);
        
        Map<String ,HashMap> v_Rows = this.cache.getRowsMap(v_DBName ,v_TabName);
        for (Map.Entry<String ,HashMap> v_Row : v_Rows.entrySet())
        {
            System.out.println(v_Row.getKey() + ":");
            HashMap<String ,Object> v_Datas = v_Row.getValue();
            
            for (Map.Entry<String ,Object> v_Data : v_Datas.entrySet())
            {
                System.out.println("\t" + v_Data.getKey() + "=" + v_Data.getValue() + ":" + v_Data.getValue().getClass().getSimpleName());
            }
        }
    }
    
}
