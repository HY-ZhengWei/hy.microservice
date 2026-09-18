package org.hy.microservice.common.prometheus;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.xcql.Connection;
import org.hy.common.xcql.DataSourceCQL;
import org.hy.common.xml.XJava;
import org.neo4j.driver.Result;
import org.springframework.boot.health.contributor.AbstractReactiveHealthIndicator;
import org.springframework.boot.health.contributor.Health;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;





/**
 * 代替原生Neo4j健康指示器（流式的）
 * 
 * 注：如果配置Spring "management.health.neo4j.enabled" = false 时，我们并没有启动Neo4j时，
 *    健康检查（http://127.0.0.1:8080/服务名/msActuator/health）页面的状态为DOWN，这并不符合我们的期望。
 *    这也是此类的主要作用：可自定义是否监控Neo4j的健康。
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-09-15
 * @version     v1.0
 */
@Component("neo4jHealthIndicator")
public class Neo4jHealthIndicator extends AbstractReactiveHealthIndicator
{

    public Neo4jHealthIndicator()
    {
        super();
    }
    
    
    
    @Override
    protected Mono<Health> doHealthCheck(Health.Builder io_Builder)
    {
        Map<String ,DataSourceCQL> v_DataSourceCQLs = XJava.getObjects(DataSourceCQL.class);
        
        // 虚拟Neo4j健康状态为UP
        if ( Help.isNull(v_DataSourceCQLs) )
        {
            Map<String, Object> v_Details = Map.of(
                    "url", "127.0.0.1:7687",
                    "database", DataSourceCQL.$DBType_Neo4j,
                    "comment", "未配置图数据库",
                    "status", "UP"
            );
            
            return Mono.fromSupplier(() -> io_Builder.up().withDetails(v_Details).build());
        }
        // 实际图数据库的的健康状态
        else
        {
            return Mono.fromSupplier(() -> 
            {
                Map<String, Object> v_AllDetails = new HashMap<String ,Object>();
                boolean             v_AllOK      = true;
                
                for (DataSourceCQL v_DataSourceCQL : v_DataSourceCQLs.values())
                {
                    String     v_Status     = "DOWN";
                    Connection v_Connection = null;
                    try
                    {
                        if ( v_DataSourceCQL.isValid() )
                        {
                            v_Connection = v_DataSourceCQL.getConnection();
                            if ( v_Connection != null )
                            {
                                Result v_Result = v_Connection.run("MATCH (n) RETURN 1");
                                if ( v_Result != null )
                                {
                                    v_Status = "UP";
                                }
                            }
                            else
                            {
                                v_AllOK = false;
                            }
                        }
                    }
                    catch (Exception exce)
                    {
                        v_AllOK  = false;
                        v_Status = "DOWN";
                        
                        if ( v_Connection != null )
                        {
                            try
                            {
                                v_Connection.close();
                            }
                            catch (Exception e)
                            {
                                // Nothing.
                            }
                            v_Connection = null;
                        }
                    }
                    
                    Map<String, Object> v_Details = Map.of(
                            "url", v_DataSourceCQL.getUrl(),
                            "database", v_DataSourceCQL.getDatabase(),
                            "comment", Help.NVL(v_DataSourceCQL.getComment()),
                            "status", v_Status
                    );
                    
                    v_AllDetails.put(v_DataSourceCQL.getXJavaID() ,v_Details);
                }
                
                return (v_AllOK ? io_Builder.up() : io_Builder.down()).withDetails(v_AllDetails).build();
            });
        }
    }
    
}
