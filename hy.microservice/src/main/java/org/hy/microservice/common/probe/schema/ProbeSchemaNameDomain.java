package org.hy.microservice.common.probe.schema;

import java.util.List;

import org.hy.microservice.common.BaseDomain;





/**
 * 领域模型：数据探针的结构中本体逻辑名称
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-26
 * @version     v1.0
 */
public class ProbeSchemaNameDomain extends BaseDomain<ProbeSchemaName>
{
    
    private static final long serialVersionUID = -554786264695830782L;

    

    public ProbeSchemaNameDomain()
    {
        this(new ProbeSchemaName());
    }
    
    
    
    public ProbeSchemaNameDomain(ProbeSchemaName i_Data)
    {
        super(i_Data);
    }
    
    
    
    /**
     * 获取：主键
     */
    public String getId()
    {
        return this.data.getId();
    }
    
    
    /**
     * 设置：主键
     * 
     * @param i_Id 主键
     */
    public void setId(String i_Id)
    {
        this.data.setId(i_Id);
    }
    
    
    /**
     * 获取：归类ID
     */
    public String getSchemaID()
    {
        return this.data.getSchemaID();
    }

    
    /**
     * 设置：归类ID
     * 
     * @param i_SchemaID 归类ID
     */
    public void setSchemaID(String i_SchemaID)
    {
        this.data.setSchemaID(i_SchemaID);
    }
    
    
    /**
     * 获取：逻辑名称
     */
    public String getTableName()
    {
        return this.data.getTableName();
    }

    
    /**
     * 设置：逻辑名称
     * 
     * @param i_TableName 逻辑名称
     */
    public void setTableName(String i_TableName)
    {
        this.data.setTableName(i_TableName);
    }

    
    /**
     * 获取：本体结构串码
     */
    public String getMetaCode()
    {
        return this.data.getMetaCode();
    }

    
    /**
     * 设置：本体结构串码
     * 
     * @param i_MetaCode 本体结构串码
     */
    public void setMetaCode(String i_MetaCode)
    {
        this.data.setMetaCode(i_MetaCode);
    }

    
    /**
     * 获取：本体结构
     */
    public List<ProbeSchemaMeta> getMetas()
    {
        return this.data.getMetas();
    }

    
    /**
     * 设置：本体结构
     * 
     * @param i_Metas 本体结构
     */
    public void setMetas(List<ProbeSchemaMeta> i_Metas)
    {
        this.data.setMetas(i_Metas);
    }
    
}
