package org.hy.microservice.common.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;





/**
 * JPA兼容梦达DM8数据库
 *
 * @author      李浩、ZhengWei(HY)
 * @createDate  2026-04-13
 * @version     v1.0
 */
@Configuration
public class JpaCompatibilityConfig
{

    private static final Logger $Logger = new Logger(JpaCompatibilityConfig.class);
    
    
    
    /**
     * 注意：项目实际使用 MyBatis 作为持久层， 此 EntityManagerFactory 仅用于满足依赖注入，不实际执行 JPA 操作。
     */
    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource i_DataSource)
    {
        LocalContainerEntityManagerFactoryBean v_FactoryBean = new LocalContainerEntityManagerFactoryBean();
        
        // 关键：注入数据源
        v_FactoryBean.setDataSource(i_DataSource);
        // 设置持久化提供者
        v_FactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        // 设置要扫描的实体类包路径
        v_FactoryBean.setPackagesToScan(XJava.getParam("MS_Common_RootPackageName").getValue().split(","));
        
        // JPA 属性配置 - 达梦方言
        Map<String ,Object> v_JPA = new HashMap<>();
        v_JPA.put("hibernate.dialect"                                 ,"org.hibernate.dialect.DamengDialect"); // 达梦方言。实测无效，改用下面的
        v_JPA.put("hibernate.dialect"                                 ,"org.hibernate.dialect.H2Dialect");     // 用 H2 通用方言
        v_JPA.put("hibernate.dialect"                                 ,"org.hibernate.dialect.MySQLDialect");  // 用 MySQL 方言
        v_JPA.put("hibernate.hbm2ddl.auto"                            ,"none");                                // 不执行 DDL 操作
        v_JPA.put("hibernate.temp.use_jdbc_metadata_defaults"         ,"false");                               // 关闭元数据校验
        v_JPA.put("hibernate.connection.provider_disables_autocommit" ,"true");
        v_FactoryBean.setJpaPropertyMap(v_JPA);
        
        // 初始化
        v_FactoryBean.afterPropertiesSet();
        return v_FactoryBean.getObject();
    }
    
    
    
    /**
     * 是否为达梦数据库
     * 
     * @author      ZhengWei(HY)
     * @createDate  2016-04-13
     * @version     v1.0
     *
     */
    public boolean isDaMeng(DataSource i_DataSource)
    {
        Connection v_Conn = null;
        try
        {
            v_Conn = i_DataSource.getConnection();
            if ( v_Conn == null )
            {
                return false;
            }
            DatabaseMetaData v_DBMetaData = v_Conn.getMetaData();
            
            String v_DBName = v_DBMetaData.getDatabaseProductName().toUpperCase();
            if ( v_DBName.indexOf("DM DBMS") >= 0 )
            {
                return true;
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        finally
        {
            try
            {
                if ( v_Conn != null )
                {
                    v_Conn.close();
                    v_Conn = null;
                }
            }
            catch (Exception exce)
            {
                throw new RuntimeException(exce.getMessage());
            }
            finally
            {
                v_Conn = null;
            }
        }
        
        return false;
    }
    
}
