package org.hy.microservice.common.config;

import org.hy.common.xml.XJava;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import jakarta.servlet.MultipartConfigElement;





/**
 * Multipart 配置类 - 用于配置文件上传支持
 * 
 * Author:  LiHao、ZhengWei(HY)
 * Date:    2026-03-11
 * Version: 1.0
 */
@Configuration
public class FileUploadMultipartConfig
{

    /**
     * 配置Multipart文件上传参数（核心）
     * 
     * @author      LiHao、ZhengWei(HY)
     * @createDate  2026-03-12
     * @version     v1.0
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() 
    {
        MultipartConfigFactory v_Factory = new MultipartConfigFactory();
        
        v_Factory.setMaxFileSize(   DataSize.ofMegabytes(XJava.getParam("MS_Common_FileUpload_MaxFileSize")   .getValueInt()));
        v_Factory.setMaxRequestSize(DataSize.ofMegabytes(XJava.getParam("MS_Common_FileUpload_MaxRequestSize").getValueInt()));
        v_Factory.setFileSizeThreshold(DataSize.ofBytes(0));       // 文件写入磁盘的阈值：0（默认值，可省略）
        
        return v_Factory.createMultipartConfig();
    }
    


    /**
     * 配置Multipart文件上传的延迟解析
     * 
     * 按需解析，节省服务器资源；
     * 大文件上传时，请求入口不阻塞，性能更优；
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-03-12
     * @version     v1.0
     *
     * @return
     */
    @Bean
    public MultipartResolver multipartResolver()
    {
        StandardServletMultipartResolver v_Resolver = new StandardServletMultipartResolver();
        v_Resolver.setResolveLazily(true);
        return v_Resolver;
    }

    
    
    /**
     * 配置Tomcat容器参数
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-03-12
     * @version     v1.0
     *
     * @return
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() 
    {
        TomcatServletWebServerFactory v_TomcatFactory = new TomcatServletWebServerFactory();
        v_TomcatFactory.addConnectorCustomizers(io_Connector -> 
        {
            // 禁用Tomcat的maxSwallowSize限制（对应max-swallow-size=-1）
            // Tomcat 10+/11+ 适配：设置maxSwallowSize为-1
            io_Connector.setProperty("maxSwallowSize" ,"-1");
        });
        return v_TomcatFactory;
    }
    
}
