package org.hy.microservice.common.prometheus;

import org.hy.common.XJavaID;

import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;





/**
 * 指标注册器
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-09-16
 * @version     v1.0
 */
public class PrometheusRegistry implements XJavaID
{
    
    /** 指标注册器 */
    private PrometheusMeterRegistry registry;
    
    /** 逻辑ID */
    private String                  xid;
    
    /** 备注说明 */
    private String                  comment;
    
    
    
    public PrometheusRegistry()
    {
        this.registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }
    
    
    
    /**
     * 获取指标注册器
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-09-16
     * @version     v1.0
     *
     * @return
     */
    public PrometheusMeterRegistry getRegistry()
    {
        return this.registry;
    }
    
    
    
    /**
     * 获取指标数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-09-16
     * @version     v1.0
     *
     * @return
     */
    public String scrape() 
    {
        return this.registry.scrape();
    }



    @Override
    public void setXJavaID(String i_XJavaID)
    {
        this.xid = i_XJavaID;
    }



    @Override
    public String getXJavaID()
    {
        return this.xid;
    }



    @Override
    public void setComment(String i_Comment)
    {
        this.comment = i_Comment;
    }



    @Override
    public String getComment()
    {
        return this.comment;
    }
    
}
