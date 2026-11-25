package org.hy.microservice.common.probe.client;

import org.hy.microservice.common.probe.schema.ProbeSchemaMetaDomain;





/**
 * 数据探针的客户端数据接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-22
 * @version     v1.0
 */
public interface ProbeClientData
{
    
    
    /**
     * 获取原始数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-22
     * @version     v1.0
     *
     * @return
     */
    public Object getData();
    
    
    
    /**
     * 通过关键字获取字段数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-22
     * @version     v1.0
     *
     * @param i_Meta  数据探针的本体结构。有两种可种，
     *                一种是Java对象成员属性的下标。
     *                一种是Map对象的key值
     * @return
     */
    public Object getFiledValue(ProbeSchemaMetaDomain i_Meta);
    
}
