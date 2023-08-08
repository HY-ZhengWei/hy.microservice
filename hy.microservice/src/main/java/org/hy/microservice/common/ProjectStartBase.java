package org.hy.microservice.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.StringHelp;
import org.hy.common.TablePartition;
import org.hy.common.xml.XJava;
import org.hy.common.xml.plugins.AppBaseServlet;
import org.hy.common.xml.plugins.XJavaSpringAnnotationConfigServletWebServerApplicationContext;
import org.hy.common.xml.plugins.analyse.AnalyseObjectServlet;
import org.hy.common.xml.plugins.analyse.AnalysesServlet;
import org.hy.microservice.common.config.XJavaSpringInitialzer;
import org.hy.microservice.common.operationLog.OperationLogApi;
import org.hy.microservice.common.operationLog.OperationLogModule;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.common.collect.ImmutableMap;





/**
 * 微服务的启动类
 *
 * @author      ZhengWei(HY)、马龙
 * @createDate  2020-11-19
 * @version     v1.0
 *              v2.0  2021-02-19  添加：支持SpringBoot 2.4.0版本
 */
public class ProjectStartBase
{
    public static final Map<String ,OperationLogModule>       $RequestMappingModules = new HashMap<String ,OperationLogModule>();
    
    public static final PartitionMap<String ,OperationLogApi> $RequestMappingMethods = new TablePartition<String ,OperationLogApi>();
    
    
    
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
    public static WebApplicationContext run(SpringApplication i_Application)
    {
        i_Application.addInitializers(new XJavaSpringInitialzer());
        i_Application.setApplicationContextFactory(ApplicationContextFactory.ofContextClass(XJavaSpringAnnotationConfigServletWebServerApplicationContext.class));
        
        WebApplicationContext v_WebAppContext = (WebApplicationContext) i_Application.run();
        
        // 获取Spring所有RequestMapping类
        Map<String ,Object> v_Beans = v_WebAppContext.getBeansWithAnnotation(RequestMapping.class);
        for (Map.Entry<String ,Object> v_Item : v_Beans.entrySet())
        {
            Class<?>       v_AnnoClass      = v_Item.getValue().getClass();
            RequestMapping v_RequestMapping = v_AnnoClass.getAnnotation(RequestMapping.class);
            
            if ( Help.isNull(v_RequestMapping.name()) )
            {
                continue;
            }
            
            OperationLogModule v_OModule = new OperationLogModule();
            v_OModule.setModuleCode(v_RequestMapping.value()[0]);
            v_OModule.setModuleName(v_RequestMapping.name());
            $RequestMappingModules.put(v_OModule.getModuleCode() ,v_OModule);
        }
        
        // 获取Spring所有RequestMapping方法
        AbstractHandlerMethodMapping<RequestMappingInfo> v_Methods   = v_WebAppContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo ,HandlerMethod>           v_MethodMap = v_Methods.getHandlerMethods();
        v_MethodMap.forEach((k ,v) ->
        {
            final Set<String> v_Patterns = k.getPatternValues();// getPatternsCondition().getPatterns();
            for (String v_Pattern : v_Patterns)
            {
                if ( Help.isNull(v_Pattern) || Help.isNull(k.getName()) )
                {
                    continue;
                }
                
                String [] v_Url   = v_Pattern.split("/");
                String [] v_Names = k.getName().split("#");
                if ( Help.isNull(v_Names) || v_Names.length < 2 )
                {
                    continue;
                }
                
                if ( Help.isNull(v_Url) || v_Url.length < 2 )
                {
                    continue;
                }
                
                OperationLogApi v_OApi = new OperationLogApi();
                v_OApi.setModuleCode(v_Url[1]);
                v_OApi.setModuleName($RequestMappingModules.get(v_OApi.getModuleCode()).getModuleName());
                v_OApi.setUrl(v_Pattern);
                v_OApi.setUrlName(v_Names[1]);
                
                $RequestMappingMethods.putRow(v_OApi.getModuleCode() ,v_OApi);
            }
        });

        return v_WebAppContext;
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
    public static ServletRegistrationBean<VueServlet> vueServlet()
    {
        return new ServletRegistrationBean<VueServlet>(new VueServlet() ,"/ms/*");
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
    public static ServletRegistrationBean<AppBaseServlet> appServlet()
    {
        return new ServletRegistrationBean<AppBaseServlet>(new AppBaseServlet() ,"/app/*");
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
    public static ServletRegistrationBean<AnalysesServlet> analysesServlet()
    {
        return new ServletRegistrationBean<AnalysesServlet>(new AnalysesServlet() ,"/analyses/*");
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
    public static ServletRegistrationBean<AnalyseObjectServlet> analyseObjectServlet()
    {
        ServletRegistrationBean<AnalyseObjectServlet> v_SRB = new ServletRegistrationBean<AnalyseObjectServlet>(new AnalyseObjectServlet() ,"/analyses/analyseObject/*");
        
        v_SRB.setInitParameters(ImmutableMap.of("password", StringHelp.md5(XJava.getParam("MS_Common_Analyses_Password").getValue() ,StringHelp.$MD5_Type_Hex)));
        
        return v_SRB;
    }

}
