package org.hy.microservice.common.probe.schema;

import java.util.List;

import org.hy.microservice.common.BaseData;





/**
 * 数据层：数据探针的结构中本体逻辑名称
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-26
 * @version     v1.0
 */
public class ProbeSchemaName extends BaseData
{

    private static final long serialVersionUID = -3416198086639209447L;
    
    
    
    /** 主键 */
    private String                id;
    
    /** 归类ID */
    private String                schemaID;
    
    /** 逻辑名称 */
    private String                tableName;
    
    /** 本体结构串码 */
    private String                metaCode;
    
    /** 本体结构 */
    private List<ProbeSchemaMeta> metas;

    
    
    /**
     * 获取：主键
     */
    public String getId()
    {
        return id;
    }

    
    /**
     * 设置：主键
     * 
     * @param i_Id 主键
     */
    public void setId(String i_Id)
    {
        this.id = i_Id;
    }

    
    /**
     * 获取：归类ID
     */
    public String getSchemaID()
    {
        return schemaID;
    }

    
    /**
     * 设置：归类ID
     * 
     * @param i_SchemaID 归类ID
     */
    public void setSchemaID(String i_SchemaID)
    {
        this.schemaID = i_SchemaID;
    }

    
    /**
     * 获取：逻辑名称
     */
    public String getTableName()
    {
        return tableName;
    }

    
    /**
     * 设置：逻辑名称
     * 
     * @param i_TableName 逻辑名称
     */
    public void setTableName(String i_TableName)
    {
        this.tableName = i_TableName;
    }

    
    /**
     * 获取：本体结构串码
     */
    public String getMetaCode()
    {
        return metaCode;
    }

    
    /**
     * 设置：本体结构串码
     * 
     * @param i_MetaCode 本体结构串码
     */
    public void setMetaCode(String i_MetaCode)
    {
        this.metaCode = i_MetaCode;
    }

    
    /**
     * 获取：本体结构
     */
    public List<ProbeSchemaMeta> getMetas()
    {
        return metas;
    }

    
    /**
     * 设置：本体结构
     * 
     * @param i_Metas 本体结构
     */
    public void setMetas(List<ProbeSchemaMeta> i_Metas)
    {
        this.metas = i_Metas;
    }
    
}
