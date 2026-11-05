package org.hy.microservice.common.probe.schema;

import java.util.Map;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 操作DAO层：数据探针的结构中本体逻辑名称
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-26
 * @version     v1.0
 */
@Xjava(id="ProbeSchemaNameDAO" ,value=XType.XSQL)
public interface IProbeSchemaNameDAO
{
    
    /**
     * 查询数据探针的结构中本体逻辑名称列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-26
     * @version     v1.0
     * 
     * @return
     */
    @Xsql("XSQL_Common_ProbeSchemaName_Query")
    public Map<String ,ProbeSchemaName> queryList();
    
    
    
    /**
     * 查询数据探针的结构中本体逻辑名称列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-26
     * @version     v1.0
     * 
     * @param i_ProbeSchema  数据探针的结构中本体逻辑名称
     * @return
     */
    @Xsql(id="XSQL_Common_ProbeSchemaName_Query")
    public Map<String ,ProbeSchemaName> queryList(ProbeSchemaName i_ProbeSchemaName);
    
    
    
    /**
     * 按数据探针的结构ID查询数据探针的结构中本体逻辑名称列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-26
     * @version     v1.0
     * 
     * @param i_SchemaID  数据探针的结构ID
     * @return            Map.key 为本体结构串码metaCode
     */
    @Xsql(id="XSQL_Common_ProbeSchemaName_Query")
    public Map<String ,ProbeSchemaName> queryBySchemaID(String i_SchemaID);
    
    
    
    /**
     * 查询数据探针的结构中本体逻辑名称列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-26
     * @version     v1.0
     * 
     * @param i_ProbeSchema  数据探针的结构中本体逻辑名称
     * @return
     */
    @Xsql(id="XSQL_Common_ProbeSchemaName_Query_ByPage" ,paging=true)
    public Map<String ,ProbeSchemaName> queryByPage(ProbeSchemaName i_ProbeSchemaName);
    
    
    
    /**
     * 查询数据探针的结构中本体逻辑名称列表的总记录数
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-26
     * @version     v1.0
     *
     * @return
     */
    @Xsql(id="XSQL_Common_ProbeSchemaName_Count")
    public Long queryCount(ProbeSchemaName i_ProbeSchema);
    
    
    
    /**
     * 按ID查询数据探针的结构中本体逻辑名称
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-26
     * @version     v1.0
     * 
     * @param i_ID  数据探针的结构中本体逻辑名称ID
     * @return
     */
    @Xsql(id="XSQL_Common_ProbeSchemaName_Query" ,returnOne=true)
    public ProbeSchemaName queryByID(@Xparam("id") String i_ID);
    
    
    
    /**
     * 按本体结构串码查询数据探针的结构中本体逻辑名称
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-26
     * @version     v1.0
     * 
     * @param i_MetaCode  本体结构串码
     * @return
     */
    @Xsql(id="XSQL_Common_ProbeSchemaName_Query" ,returnOne=true)
    public ProbeSchemaName queryByMetaCode(@Xparam("metaCode") String i_MetaCode);
    
}
