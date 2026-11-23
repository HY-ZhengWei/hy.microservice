package org.hy.microservice.common.probe.data;

import java.util.List;
import java.util.Map;

import org.hy.common.TablePartitionRID;

import org.hy.microservice.common.probe.client.AssetID;
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
     * 更新数据资产状态
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-15
     * @version     v1.0
     *
     * @param i_TableName   表名
     * @param i_AssetIDs    多个数据资产编号
     * @param i_AssetState  未同步false； 已同步true
     * @param i_UserID      更新用户ID
     * @return              更新成功数量
     */
    public int updateAssetStates(String i_TableName ,List<String> i_AssetIDs ,boolean i_AssetState ,String i_UserID);
    
    
    
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
    
    
    
    /**
     * 按用户主键查数据资产编号
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-22
     * @version     v1.0
     *
     * @param <D>
     * @param i_SchemaName  数据探针的结构
     * @param i_Data        数据
     * @return
     */
    public String queryAssetIDByPK(ProbeSchemaName i_SchemaName ,Object i_Data);
    
    
    
    /**
     * 查当前表中最大的数据资产编号
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-12
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @param i_Source      数据资产编号中的来源
     * @return
     */
    public AssetID queryMaxAssetID(ProbeSchemaName i_SchemaName ,String i_Source);
    
    
    
    /**
     * 查询需要同步的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-13
     * @version     v1.0
     *
     * @param i_SchemaName  数据探针的结构
     * @return              按用户ID分区，按数据资产编号为主键
     */
    public TablePartitionRID<String ,Map<String ,Object>> queryRequireSync(ProbeSchemaName i_SchemaName);
    
}
