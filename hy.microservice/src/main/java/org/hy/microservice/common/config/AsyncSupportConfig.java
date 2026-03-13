package org.hy.microservice.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.hy.microservice.common.LogFilter;





/**
 * 开启异步支持。配合SpringAI回显问题时，一行一行的显示（即对Flux<String>的支持）
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-03-04
 * @version     v1.0
 */
@Configuration
public class AsyncSupportConfig
{

    /**
     * 为自定义 LogFilter 开启异步支持（如果过滤器不是通过@WebFilter注册）
     */
    @Bean
    public FilterRegistrationBean<LogFilter> logFilterRegistration()
    {
        FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogFilter());
        registration.addUrlPatterns("/*");
        registration.setAsyncSupported(true);  // 开启过滤器异步支持
        return registration;
    }
    
}
