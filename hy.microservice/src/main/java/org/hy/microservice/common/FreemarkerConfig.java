package org.hy.microservice.common;

import org.hy.common.app.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;





/**
 * Freemarker配置
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-01
 * @version     v1.0
 */
@Configuration
public class FreemarkerConfig
{
 
    // @Autowired
    // private FreeMarkerProperties properties;
    
    @Autowired
    @Qualifier("MS_Common_Freemarker_TemplateLoaderPath")
    private Param templateLoaderPath;
    
 
    
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer()
    {
        FreeMarkerConfigurer v_Configurer = new FreeMarkerConfigurer();
        v_Configurer.setTemplateLoaderPaths("classpath:" + this.templateLoaderPath.getValue());
        v_Configurer.setDefaultEncoding("UTF-8");
        return v_Configurer;
    }
 
}