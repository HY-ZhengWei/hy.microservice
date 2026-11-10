package org.hy.microservice.common.probe.data;

import org.hy.microservice.common.probe.schema.ProbeSchemaName;





/**
 * 操作DAO层：数据探针的数据
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public interface IProbeDataDAO
{
    
    /**
     * 数据探针的数据持久化入库
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-09
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @param i_Data        数据探针的数据
     * @return              是否保存成功
     */
    public boolean save(ProbeSchemaName i_SchemaName ,ProbeData i_Data);
    
}
