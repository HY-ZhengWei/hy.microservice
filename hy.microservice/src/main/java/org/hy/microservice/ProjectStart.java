package org.hy.microservice;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.PartitionMap;
import org.hy.common.TablePartition;
import org.hy.common.xml.plugins.AppBaseServlet;
import org.hy.common.xml.plugins.XJavaSpringAnnotationConfigServletWebServerApplicationContext;
import org.hy.common.xml.plugins.analyse.AnalyseObjectServlet;
import org.hy.common.xml.plugins.analyse.AnalysesServlet;
import org.hy.microservice.common.ProjectStartBase;
import org.hy.microservice.common.VueServlet;
import org.hy.microservice.common.config.XJavaSpringInitialzer;
import org.hy.microservice.common.operationLog.OperationLogApi;
import org.hy.microservice.common.operationLog.OperationLogModule;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;





/**
 * 微服务的启动类
 *
 * @author      ZhengWei(HY)、马龙
 * @createDate  2020-11-19
 * @version     v1.0
 *              v2.0  2021-02-19  添加：支持SpringBoot 2.4.0版本
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class ProjectStart extends SpringBootServletInitializer
{
    public static final Map<String ,OperationLogModule>       $RequestMappingModules = new HashMap<String ,OperationLogModule>();
    
    public static final PartitionMap<String ,OperationLogApi> $RequestMappingMethods = new TablePartition<String ,OperationLogApi>();
    
    
    
    @SuppressWarnings("unused")
    public static void main(String[] args)
    {
        SpringApplication v_SpringApp = new SpringApplication(ProjectStart.class);
        v_SpringApp.addInitializers(new XJavaSpringInitialzer());
        v_SpringApp.setApplicationContextFactory(ApplicationContextFactory.ofContextClass(XJavaSpringAnnotationConfigServletWebServerApplicationContext.class));
        ConfigurableApplicationContext v_CAC = v_SpringApp.run(args);
    }
    
    
    
    /**
     * Tomcat 启动Spring Boot
     *
     * @author      ZhengWei(HY)
     * @createDate  2019-01-13
     * @version     v1.0
     *
     * @param i_Application
     * @return
     *
     * @see org.springframework.boot.web.servlet.support.SpringBootServletInitializer#run(org.springframework.boot.SpringApplication)
     */
    @Override
    protected WebApplicationContext run(SpringApplication i_Application)
    {
        return ProjectStartBase.run(i_Application);
    }
    
    

    /**
     * 注册Vue独立处理机制
     * 
     * @author      ZhengWei(HY)
     * @createDate  2020-11-20
     * @version     v1.0
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<VueServlet> vueServlet()
    {
        return ProjectStartBase.vueServlet();
    }
    
    
    
    /**
     * 注册App接口的基础的Servlet
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-07
     * @version     v1.0
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<AppBaseServlet> appServlet()
    {
        return ProjectStartBase.appServlet();
    }
    
    
    
    /**
     * 注册分析中心
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-24
     * @version     v1.0
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<AnalysesServlet> analysesServlet()
    {
        return ProjectStartBase.analysesServlet();
    }
    
    
    
    /**
     * 注册Java对象池分析
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-24
     * @version     v1.0
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<AnalyseObjectServlet> analyseObjectServlet()
    {
        return ProjectStartBase.analyseObjectServlet();
    }
    
    
    
    /**
     * 设置匹配的请求后缀。不设置的话，后缀为空，也没有"点"。
     *
     * @author      ZhengWei(HY)
     * @createDate  2022-10-25
     * @version     v1.0
     * 
     * @param i_DispatcherServlet
     * @return
     * @see SuffixRequestMappingHandlerMapping 还需启用它
     */
//    @Bean
//    public ServletRegistrationBean<DispatcherServlet> servletRegistrationBean(DispatcherServlet i_DispatcherServlet)
//    {
//        ServletRegistrationBean<DispatcherServlet> v_Bean = new ServletRegistrationBean<DispatcherServlet>(i_DispatcherServlet);
//        v_Bean.addUrlMappings("*.page");
//        return v_Bean;
//    }
    
}
