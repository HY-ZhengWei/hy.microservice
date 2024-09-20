package org.hy.microservice.common.mybatis;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;





/**
 * 动态多数据源
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-09-20
 * @version     v1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource
{

    /** 使用ThreadLocal来存储当前线程的数据源名称，保证多线程情况下，各自的数据源互不影响 */
    private static final ThreadLocal<String> $ContextHolder = new ThreadLocal<String>();
    
    private static final Map<Object ,Object> $DataSources   = new HashMap<Object ,Object>();



    /**
     * 构造器
     *
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @param i_DefaultDataSource  默认数据源
     * @param i_DataSources        所有数据源。Key为数据源的名称，即XID
     */
    public DynamicDataSource(DataSource i_DefaultDataSource ,Map<Object ,Object> i_DataSources)
    {
        super.setDefaultTargetDataSource(i_DefaultDataSource);
        super.setTargetDataSources(i_DataSources);
        super.afterPropertiesSet();
        
        $DataSources.putAll(i_DataSources);
    }



    /**
     * 返回当前数据源
     *
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey()
    {
        return getDataSource();
    }


    
    /**
     * 设置当前线程中用的数据源
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @param i_DataSource  数据源名称，即XID
     */
    public static void setDataSource(String i_DataSource)
    {
        $ContextHolder.set(i_DataSource);
    }



    /**
     * 获取当前线程中用的数据源
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @return
     */
    public static String getDataSource()
    {
        return $ContextHolder.get();
    }



    /**
     * 清除当前线程中用的数据源
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     */
    public static void clearDataSource()
    {
        $ContextHolder.remove();
    }
    
    
    
    /**
     * 判定数据源名称
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @param i_DataSource  数据源名称，即XID
     * @return
     */
    public static boolean exists(String i_DataSource)
    {
        return $DataSources.containsKey(i_DataSource);
    }
    
}