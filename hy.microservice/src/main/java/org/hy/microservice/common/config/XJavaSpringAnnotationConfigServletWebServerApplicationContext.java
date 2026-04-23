package org.hy.microservice.common.config;

import org.hy.common.xml.plugins.XJavaSpringBootLoadingText;
import org.hy.common.xml.plugins.XJavaSpringObjectFactotry;
import org.springframework.boot.web.server.servlet.context.AnnotationConfigServletWebServerApplicationContext;





/**
 * XJava对接Spring Boot的第3步（共4步），使Spring可以通过 @Autowired 或 @Resource 注解注入XJava对象池中的对象。
 * 
 * @author      ZhengWei(HY)
 * @createDate  2018-11-08
 * @version     v1.0
 *              v2.0  2026-04-18  升级：通过动态Java支持SpringBoot 4.x
 */
public class XJavaSpringAnnotationConfigServletWebServerApplicationContext extends AnnotationConfigServletWebServerApplicationContext
{
    public XJavaSpringAnnotationConfigServletWebServerApplicationContext() 
    {
        super(new XJavaSpringObjectFactotry());
        System.out.println(XJavaSpringBootLoadingText.getText(XJavaSpringBootLoadingText.getVersion()));
    }
}
