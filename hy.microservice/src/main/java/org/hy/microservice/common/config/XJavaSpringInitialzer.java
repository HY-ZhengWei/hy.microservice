package org.hy.microservice.common.config;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.xml.XJava;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;





/**
 * SpringBoot启动时，拉起XJava的初始化
 *
 * @author      ZhengWei(HY)、马龙
 * @createDate  2020-11-19
 * @version     v1.0
 */
public class XJavaSpringInitialzer implements ApplicationContextInitializer<ConfigurableApplicationContext>
{
    
    @Override
    public void initialize(ConfigurableApplicationContext i_ApplicationContext)
    {
        new XJavaInit();
        i_ApplicationContext.getEnvironment().getPropertySources().addFirst(this.initializePrometheus());
    }
    
    
    
    /**
     * 初始化普罗米修斯Prometheus监控的配置
     * 
     * 指标metrics访问地址：
     *    http://127.0.0.1:8080/服务名/msActuator
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-09-14
     * @version     v1.0
     *
     * @return
     */
    private MapPropertySource initializePrometheus()
    {
        Map<String, Object> v_Propertys = new HashMap<String, Object>();
        
        v_Propertys.put("server.tomcat.mbeanregistry.enabled"       ,true);                                                      // 开启 Tomcat MBean 注册
        v_Propertys.put("management.metrics.tags.application"       ,XJava.getParam("MS_Common_ServiceName").getValue());        // 配置全局指标标签
        v_Propertys.put("management.endpoints.web.base-path"        ,XJava.getParam("MS_Common_ProemtheusUrl").getValue());      // 配置 Actuator URL 地址
        v_Propertys.put("management.endpoints.web.exposure.include" ,XJava.getParam("MS_Common_ProemtheusEndpoint").getValue()); // 配置 Actuator Web 端点暴露
        v_Propertys.put("management.endpoint.health.show-details"   ,"always");                                                  // 健康页面显示详情
        v_Propertys.put("management.endpoint.prometheus.enabled"    ,true);                                                      // 显式启用 Prometheus 导出
        v_Propertys.put("management.endpoint.info.enabled"          ,true);                                                      // 
        v_Propertys.put("management.info.defaults.enabled"          ,true);                                                      // 开启默认info采集器
                
        // 添加自定义属性源，优先级高于application.yml
        return new MapPropertySource("actuator-custom-props", v_Propertys);
    }
    
}
