package org.hy.microservice.common.probe.data;





/**
 * 缓存层：数据探针的数据
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-06
 * @version     v1.0
 */
public interface IProbeDataCache
{
    
    /**
     * 数据弹出队列
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-06
     * @version     v1.0
     *
     * @return
     */
    public ProbeData get();
    
    
    
    /**
     * 数据压入队列
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @param i_ProbeData  数据探针的数据
     */
    public void push(ProbeData i_ProbeData);
    
}
