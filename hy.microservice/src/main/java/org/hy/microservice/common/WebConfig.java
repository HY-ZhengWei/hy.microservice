package org.hy.microservice.common;

import javax.annotation.Resource;

import org.hy.common.xml.XJava;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;





/**
 * WebConfig：服务Web配置
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-11-20
 * @version     v1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer
{

    @Resource
    private CommonInterceptor commonInterceptor;
    
    
    
    /**
     * 添加拦截器
     *
     * @author      ZhengWei(HY)
     * @createDate  2020-11-20
     * @version     v1.0
     *
     * @param registry
     *
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(commonInterceptor).addPathPatterns("/" + XJava.getParam("MS_Common_ProjectName").getValue() + "/**");
        registry.addInterceptor(commonInterceptor).addPathPatterns("/" + XJava.getParam("MS_Common_ServiceName").getValue() + "/**");
    }



    /**
     * 添加静态资源
     *
     * @author      ZhengWei(HY)
     * @createDate  2020-11-20
     * @version     v1.0
     *
     * @param i_Registry
     *
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry)
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry i_Registry)
    {
        // 配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
        // i_Registry.addResourceHandler("/ms/**").addResourceLocations("file:./ms/" ,"file:/ms/" ,"/ms/" ,"classpath:/ms/");
        WebMvcConfigurer.super.addResourceHandlers(i_Registry);
    }
    

    
//    在Spring MVC 5.3 之后不生效
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer)
//    {
//        configurer.setUseSuffixPatternMatch(false);
//        configurer.setUseRegisteredSuffixPatternMatch(true);
//    }
//
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer.favorPathExtension(false);
//    }

}
