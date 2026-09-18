package org.hy.microservice.common.prometheus;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.endpoint.annotation.EndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpointDiscoverer;
import org.springframework.boot.servlet.autoconfigure.actuate.web.ServletEndpointManagementContextConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;





// 2026-09-16 无法动态创建 @Endpoint 标记的类，原因是 Spring 底层未开放
public class DynamicEndpoint
{

    WebEndpointDiscoverer v1;
    WebMvcConfigurer v3;
    EndpointDiscoverer v4;
    ServletEndpointDiscoverer v5;    
    EndpointDiscoverer v51;                 // 中的 getEndpoints() 方法是重点
    ServletEndpointManagementContextConfiguration v6;
    WebEndpointAutoConfiguration v7;
    
}
