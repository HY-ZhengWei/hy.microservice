package org.hy.microservice.common.prometheus;

import java.util.concurrent.TimeUnit;

import org.hy.common.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.TimeGauge;





/**
 * 微服务自身与业务相关的监控 Web 端点暴露
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-09-16
 * @version     v1.0
 */
@Component
@Endpoint(id="xmetrics")
public class MicroserviceMetricsEndpoint
{
    
    private PrometheusRegistry registry;
    
    /** 最后一次监控时间 */
    private Date               lastTime;
    
    /** 最后一次监控次数 */
    private Counter            lastCounter;
    
    
    
    @Autowired
    public MicroserviceMetricsEndpoint(@Qualifier("MS_Common_Registry") PrometheusRegistry i_Registry)
    {
        this.registry    = i_Registry;
        this.lastTime    = new Date();
        this.lastCounter = Counter.builder("ms_last_count").description("最后一次监控次数").register(this.registry.getRegistry());
        TimeGauge.builder("ms_last_time" ,this ,TimeUnit.MILLISECONDS ,o -> o.getLastTime()).description("最后一次监控时间").register(this.registry.getRegistry());
    }
    
    
    
    @ReadOperation(produces = "text/plain;version=0.0.4;charset=utf-8")
    public String scrape()
    {
        String v_Ret = this.registry.scrape();
        this.lastCounter.increment();
        this.lastTime = new Date();
        return v_Ret;
    }
    
    
    
    public long getLastTime()
    {
        return this.lastTime.getTime();
    }
    
}
