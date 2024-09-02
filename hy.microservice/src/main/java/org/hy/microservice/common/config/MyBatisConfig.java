package org.hy.microservice.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hy.common.db.DataSourceGroup;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;





/**
 * MyBatis配置的加载
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-08-29
 * @version     v1.0
 */
@Configuration
public class MyBatisConfig
{
    
    @Autowired
    @Qualifier("DSG_MS_Common")
    private DataSourceGroup dsGroup;
    
    

    @Bean
    public DataSource dataSource()
    {
        return dsGroup.get(0);
    }

    
    
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource i_DataSource) throws Exception
    {
        SqlSessionFactoryBean v_FactoryBean = new SqlSessionFactoryBean();
        v_FactoryBean.setDataSource(i_DataSource);
        Resource[] v_Resources = new Resource[]{new ClassPathResource("mappers/*.xml")};
        v_FactoryBean.setMapperLocations(v_Resources);
        return v_FactoryBean.getObject();
    }
}
