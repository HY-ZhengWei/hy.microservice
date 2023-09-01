package org.hy.microservice.common;

import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;





/**
 * Freemarker配置
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-01
 * @version     v1.0
 */
// @Configuration
public class FreemarkerConfig
{
 
    // @Autowired
    private FreeMarkerProperties properties;
 
    
    // @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer()
    {
        FreeMarkerConfigurer v_Configurer = new FreeMarkerConfigurer();
        v_Configurer.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
        v_Configurer.setDefaultEncoding("UTF-8");
        return v_Configurer;
    }
 
}