package org.hy.microservice.common.prometheus;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.xml.XJava;
import org.hy.common.xml.XSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.TimeGauge;





/**
 * XSQL 监控 Web 端点暴露
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-09-15
 * @version     v1.0
 */
@Component
@Endpoint(id="xsql")
public class XSQLMetricsEndpoint
{
    
    private PrometheusRegistry registry;
    
    
    
    @Autowired
    public XSQLMetricsEndpoint(@Qualifier("MS_Common_Registry_XSQL") PrometheusRegistry i_Registry)
    {
        this.registry = i_Registry;
        
        Map<String ,XSQL> v_XSQLs = XJava.getObjects(XSQL.class ,false);
        for (XSQL v_XSQL : v_XSQLs.values())
        {
            if ( Help.isNull(v_XSQL.getXJavaID()) )
            {
                continue;
            }
            
            Gauge    .builder("xsql_request_count"        ,v_XSQL ,XSQL::getRequestCount)                             .tag("xid" ,Help.NVL(v_XSQL.getXJavaID())).description("请求次数")         .register(this.registry.getRegistry());
            Gauge    .builder("xsql_success_count"        ,v_XSQL ,XSQL::getSuccessCount)                             .tag("xid" ,Help.NVL(v_XSQL.getXJavaID())).description("请求成功次数")      .register(this.registry.getRegistry());
            Gauge    .builder("xsql_io_row_count"         ,v_XSQL ,XSQL::getIoRowCount)                               .tag("xid" ,Help.NVL(v_XSQL.getXJavaID())).description("读写行数")         .register(this.registry.getRegistry());
            Gauge    .builder("xsql_success_time_len_max" ,v_XSQL ,XSQL::getSuccessTimeLenMax)                        .tag("xid" ,Help.NVL(v_XSQL.getXJavaID())).description("请求成功最大用时时长").register(this.registry.getRegistry());
            TimeGauge.builder("xsql_execute_time"         ,this.getExecuteTime(v_XSQL) ,TimeUnit.MILLISECONDS ,o -> o).tag("xid" ,Help.NVL(v_XSQL.getXJavaID())).description("最后执行时间")      .register(this.registry.getRegistry());
        }
    }
    
    
    
    @ReadOperation(produces = "text/plain;version=0.0.4;charset=utf-8")
    public String scrape()
    {
        return this.registry.scrape();
    }
    
    
    
    public long getExecuteTime(XSQL i_XSQL)
    {
        Date v_ExecuteTime = i_XSQL.getExecuteTime();
        return v_ExecuteTime == null ? 0L : v_ExecuteTime.getTime();
    }
    
}
