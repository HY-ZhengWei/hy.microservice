package org.hy.microservice.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.TablePartitionRID;
import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.execute.ExecuteResult;
import org.hy.common.callflow.language.JavaConfig;
import org.hy.common.callflow.language.java.CacheJavaInfo;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.AppBaseServlet;
import org.hy.common.xml.plugins.XJavaSpringBootLoadingText;
import org.hy.common.xml.plugins.analyse.AnalyseObjectServlet;
import org.hy.common.xml.plugins.analyse.AnalysesServlet;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.server.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.common.collect.ImmutableMap;
import org.hy.microservice.common.config.XJavaSpringAnnotationConfigServletWebServerApplicationContext;
import org.hy.microservice.common.config.XJavaSpringInitialzer;
import org.hy.microservice.common.operationLog.IOperationLogService;
import org.hy.microservice.common.operationLog.OperationLogApi;
import org.hy.microservice.common.operationLog.OperationLogModule;





/**
 * 微服务的启动类
 *
 * @author      ZhengWei(HY)、马龙
 * @createDate  2020-11-19
 * @version     v1.0
 *              v2.0  2021-02-19  添加：支持SpringBoot 2.4.0版本
 *              v3.0  2025-08-07  添加：从名称name中解析出日志表名称的后缀。
 *                                     可支持不同业务的日志保存在不同的表中。合作解决人：李浩 
 *              v4.0  2026-04-01  修正：对类级RequestMapping(value="/xx/yy/zz")路径的支持。合作解决人：李浩 
 *              v5.0  2026-04-18  升级：按运行时动态支持SpringBoot 3.5.11 和 4.0.5
 */
public class ProjectStartBase
{
    private static final Logger                                    $Logger                = new Logger(ProjectStartBase.class ,true);
    
    /** 系统模块的集合。Map.key 为模块编码，即配置了 @RequestMapping(value="模块编码" ,name="模块名称") name属性的映射类 */
    public static final Map<String ,OperationLogModule>            $RequestMappingModules = new HashMap<String ,OperationLogModule>();
    
    /**
     * 系统接口的集合。
     * Map.分区 为模块编码，即配置了 @RequestMapping(value="接口编码" ,name="接口名称") name属性的映射方法
     * Map.key  为/模块编码/接口编码，即 URL访问路径
     **/
    public static final TablePartitionRID<String ,OperationLogApi> $RequestMappingMethods = new TablePartitionRID<String ,OperationLogApi>();
    
    /** 资源路径中分割日志名称的分割符 */
    public static final String                                     $UrlSplit              = "/";
    
    /** 名称name中分割日志名称的分割符 */
    public static final String                                     $NameLogSplit          = "/";
    
    
    
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
        XJava.putObject("MS_Common_WebApplicationContext" ,v_WebAppContext);
        
        // RequestMappingHandlerMapping v_RequestMappingHandler = v_WebAppContext.getBean(RequestMappingHandlerMapping.class);
        // RequestMappingInfo requestMappingInfo = RequestMappingInfo.paths("/lmcTest").methods(RequestMethod.GET).build();
        // v_RequestMappingHandler.registerMapping(requestMappingInfo, "adapterController", AdapterController.class.getDeclaredMethod("myTest"));
        
        // 获取Spring所有RequestMapping类
        Map<String ,Object> v_Beans = v_WebAppContext.getBeansWithAnnotation(RequestMapping.class);
        for (Map.Entry<String ,Object> v_Item : v_Beans.entrySet())
        {
            Class<?> v_AnnoClass = v_Item.getValue().getClass();
            if ( v_AnnoClass == null )
            {
                continue;
            }
            
            RequestMapping v_RequestMapping = v_AnnoClass.getAnnotation(RequestMapping.class);
            if ( v_RequestMapping == null || Help.isNull(v_RequestMapping.name()) )
            {
                continue;
            }
            
            // 将Spring的Web接口注入到XJava中
            XJava.putObject(v_Item.getKey(), v_Item.getValue());
            
            OperationLogModule v_OModule = new OperationLogModule();
            v_OModule.setModuleCode(v_RequestMapping.value()[0]);
            v_OModule.setModuleName(v_RequestMapping.name());
            if ( v_OModule.getModuleCode().startsWith($UrlSplit) )
            {
                // 前缀不以/开头
                v_OModule.setModuleCode(v_OModule.getModuleCode().substring(1));
            }
            
            int v_LastIndex = v_OModule.getModuleName().lastIndexOf($NameLogSplit);
            if ( v_LastIndex > 0 && v_LastIndex < v_OModule.getModuleName().length() - 1 )
            {
                v_OModule.setLogName(   v_OModule.getModuleName().substring(v_LastIndex + 1));
                v_OModule.setModuleName(v_OModule.getModuleName().substring(0 ,v_LastIndex));
            }
            else
            {
                v_OModule.setLogName("");
                v_OModule.setModuleName(v_OModule.getModuleName());
            }
            
            $RequestMappingModules.put(v_OModule.getModuleCode() ,v_OModule);
        }
        
        // 获取Spring所有RequestMapping方法
        AbstractHandlerMethodMapping<RequestMappingInfo> v_Methods    = v_WebAppContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo ,HandlerMethod>           v_MethodMap  = v_Methods.getHandlerMethods();
        IOperationLogService                             v_LogService = (IOperationLogService) XJava.getObject("OperationLogService");
        v_MethodMap.forEach((k ,v) ->
        {
            final Set<String> v_Patterns = k.getPatternValues();// getPatternsCondition().getPatterns();
            for (String v_Pattern : v_Patterns)
            {
                if ( Help.isNull(v_Pattern) || Help.isNull(k.getName()) )
                {
                    continue;
                }
                
                String [] v_Names = k.getName().split("#");
                if ( Help.isNull(v_Names) || v_Names.length < 2 )
                {
                    continue;
                }
                
                int v_LastIndex = v_Pattern.lastIndexOf($UrlSplit);
                if ( v_LastIndex <= 0 || v_LastIndex >= v_Pattern.length() - 1 )
                {
                    continue;
                }
                
                String             v_ModuleCode = v_Pattern.substring(1 ,v_LastIndex);
                OperationLogModule v_OLModule   = $RequestMappingModules.get(v_ModuleCode);
                if ( v_OLModule == null )
                {
                    continue;
                }
                
                OperationLogApi v_OApi = new OperationLogApi();
                v_OApi.setModuleCode(v_ModuleCode);
                v_OApi.setModuleName(v_OLModule.getModuleName());
                v_OApi.setUrl(v_Pattern);
                v_OApi.setUrlName(v_Names[1]);
                v_OApi.setUrlType("http");
                
                v_LastIndex = v_OApi.getUrlName().lastIndexOf($NameLogSplit);
                if ( v_LastIndex > 0 && v_LastIndex < v_OApi.getUrlName().length() - 1 )
                {
                    v_OApi.setLogName(v_OApi.getUrlName().substring(v_LastIndex + 1));
                    v_OApi.setUrlName(v_OApi.getUrlName().substring(0 ,v_LastIndex));
                }
                else
                {
                    v_OApi.setLogName(v_OLModule.getLogName());
                    v_OApi.setUrlName(v_OApi.getUrlName());
                }
                
                v_LogService.create(v_OApi.getLogName());
                
                $RequestMappingMethods.putRow(v_OApi.getModuleCode() ,v_OApi.getUrl() ,v_OApi);
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
    
    /**
     * 按运行时中不同的SpringBoot版本，运行创建 AnnotationConfigServletWebServerApplicationContext 的实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-04-18
     * @version     v1.0
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class<AnnotationConfigServletWebServerApplicationContext> getAnnotationConfigServletWebServerApplicationContext()
    {
        Class<AnnotationConfigServletWebServerApplicationContext> v_Ret               = null;
        JavaConfig                                                v_JConfig           = new JavaConfig();
        int                                                       v_SpringBootVersion = XJavaSpringBootLoadingText.getVersion();
        v_JConfig.setXid("XJavaSpringBoot");
        v_JConfig.setJava(StringHelp.replaceFirst(XJavaSpringBootLoadingText.getJava(v_SpringBootVersion) ,"package org.hy.common.xml.plugins" ,"package com.lps.microservice.callflow"));
        v_JConfig.setReturnID("XAnnotationConfigServletWebServerApplicationContext");
        
        Return<Object> v_CheckRet = CallFlow.getHelpCheck().check(v_JConfig);
        if ( !v_CheckRet.get() )
        {
            $Logger.error(v_CheckRet.getParamStr());  // 打印静态检查不合格的原因
            return null;
        }
        
        // 没有此步下面的执行编排无法成功，因为XJava在初始本对象的过程还没有完成呢
        XJava.putObject(v_JConfig.getXid() ,v_JConfig);
        
        Map<String ,Object> v_Context = new HashMap<String ,Object>();
        ExecuteResult       v_Result  = CallFlow.execute(v_JConfig ,v_Context);
        if ( !v_Result.isSuccess() )
        {
            StringBuilder v_ErrorLog = new StringBuilder();
            v_ErrorLog.append("Error XID = " + v_Result.getExecuteXID()).append("\n");
            v_ErrorLog.append("Error Msg = " + v_Result.getException().getMessage());
            if ( v_Result.getException() instanceof TimeoutException )
            {
                v_ErrorLog.append("is TimeoutException");
            }
            $Logger.error(v_ErrorLog.toString() ,v_Result.getException());
        }
        else
        {
            CacheJavaInfo v_CacheJavaInfo = (CacheJavaInfo) v_Context.get("XAnnotationConfigServletWebServerApplicationContext");
            if ( v_CacheJavaInfo != null )
            {
                v_Ret = (Class<AnnotationConfigServletWebServerApplicationContext>) v_CacheJavaInfo.getClazz();
            }
        }
        
        v_Context.clear();
        v_Context = null;
        return v_Ret;
    }

}
