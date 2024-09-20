package org.hy.microservice.common.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hy.common.Help;
import org.hy.common.db.DataSourceGroup;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;





/**
 * 注册动态多数据源
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-09-20
 * @version     v1.0
 */
@Configuration
@Component
@EnableTransactionManagement
public class DynamicDataSourceConfig
{

    private static final Logger $Logger = new Logger(DynamicDataSourceConfig.class);
    
    
    
    /** 默认数据源 */
    @Autowired
    @Qualifier("DSG_MS_Common")
    private DataSourceGroup defaultDSG;



    /**
     * 注册所有数据源
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @return
     */
    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource()
    {
        Map<String ,DataSourceGroup> v_DSGMap = XJava.getObjects(DataSourceGroup.class);

        if ( Help.isNull(v_DSGMap) )
        {
            RuntimeException v_Exception = new RuntimeException("未配置或未发现任何数据源");
            $Logger.error(v_Exception);
            throw v_Exception;
        }
        
        Map<Object, Object> v_DataSources = new HashMap<Object, Object>();
        for (Map.Entry<String ,DataSourceGroup> v_Item : v_DSGMap.entrySet())
        {
            v_DataSources.put(v_Item.getKey() ,v_Item.getValue().get(0));
        }
        
        // 注册所有的数据源，并且设置默认数据源
        return new DynamicDataSource(this.defaultDSG.get(0), v_DataSources);
    }
    
    
    
    /**
     * 注册数据源会话工厂及Mappers配置文件的位置路径
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @param i_DynamicDataSource  动态多数据源
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource i_DynamicDataSource) throws Exception
    {
        SqlSessionFactoryBean v_FactoryBean = new SqlSessionFactoryBean();
        v_FactoryBean.setDataSource(i_DynamicDataSource);
        
        PathMatchingResourcePatternResolver v_Resolver = new PathMatchingResourcePatternResolver();
        Resource[] v_Resources = v_Resolver.getResources("classpath*:mappers/**/*.xml");
        
        if( !Help.isNull(v_Resources) )
        {
            v_FactoryBean.setMapperLocations(v_Resources);
        }
        else
        {
            $Logger.warn("classpath*:mappers中未发现配置文件");
        }
        
        return v_FactoryBean.getObject();
    }

}
