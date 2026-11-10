package org.hy.microservice.common.probe;

import org.hy.common.Return;

import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;





/**
 * 业务层：数据探针
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public interface IProbeService
{
    
    
    /**
     * 按归类查询数据探针的结构（带缓存的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-06
     * @version     v1.0
     *
     * @param i_Type  本体结构的归类
     * @return
     */
    public ProbeSchemaDomain queryByType(String i_Type);
    
    
    
    /**
     * 保存数据探针的本体结构
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     *
     * @param io_Schema  本体结构
     * @return           Return.value     表示是否成功
     *                   Return.paramStr  表示异常消息
     *                   Return.paramObj  表示成功时的数据探针的本体结构
     */
    public Return<ProbeSchemaDomain> saveSchema(ProbeSchemaDomain io_Schema);
    
    
    
    /**
     * 数据探针的数据保存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-12
     * @version     v1.0
     *
     * @param i_Schema        数据探针的结构
     * @param i_ProbeRequest  数据探针的请求
     * @return                数据探针的响应
     */
    public ProbeResponseDomain dataSaves(ProbeSchemaDomain i_Schema ,ProbeRequestDomain i_ProbeRequest);
    
    
    
    /**
     * 数据探针的数据删除
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @param i_Schema        数据探针的结构
     * @param i_ProbeRequest  数据探针的请求
     * @return                数据探针的响应
     */
    public ProbeResponseDomain dataDels(ProbeSchemaDomain i_Schema ,ProbeRequestDomain i_ProbeRequest);
    
}
