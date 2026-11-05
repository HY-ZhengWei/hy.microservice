package org.hy.microservice.common.probe.schema;

import java.util.List;
import java.util.Map;

import org.hy.microservice.common.BaseData;





/**
 * 数据层：数据探针的结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-11
 * @version     v1.0
 */
public class ProbeSchema extends BaseData
{

    private static final long serialVersionUID = 772716015917509438L;
    
    
    
    /** 主键 */
    private String                       id;
    
    /** 归类 */
    private String                       type;
    
    /** 默认的逻辑名称 */
    private ProbeSchemaName              name;
    
    /** 所有逻辑名称。Map.key为本体结构串码metaCode */
    private Map<String ,ProbeSchemaName> allName;
    
    /** 是否创建本体结构。1创建，0不创建 */
    private Integer                      createMetas;
    
    
    
    public ProbeSchema()
    {
        this.name        = new ProbeSchemaName();
        this.createMetas = 0;
    }
    
    
    
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
     * 获取：归类
     */
    public String getType()
    {
        return type;
    }

    
    /**
     * 设置：归类
     * 
     * @param i_Type 归类
     */
    public void setType(String i_Type)
    {
        this.type = i_Type;
    }

    
    /**
     * 获取：默认的逻辑名称ID
     */
    public String getNameID()
    {
        return this.name.getId();
    }

    
    /**
     * 设置：默认的逻辑名称ID
     * 
     * @param i_NameID 默认的逻辑名称ID
     */
    public void setNameID(String i_NameID)
    {
        this.name.setId(i_NameID);
    }

    
    /**
     * 获取：默认的逻辑名称
     */
    public ProbeSchemaName getName()
    {
        return name;
    }

    
    /**
     * 设置：默认的逻辑名称
     * 
     * @param i_Name 默认的逻辑名称
     */
    public void setName(ProbeSchemaName i_Name)
    {
        this.name = i_Name;
    }


    /**
     * 获取：本体结构
     */
    public List<ProbeSchemaMeta> getMetas()
    {
        return this.name.getMetas();
    }

    
    /**
     * 设置：本体结构
     * 
     * @param i_Metas 本体结构
     */
    public void setMetas(List<ProbeSchemaMeta> i_Metas)
    {
        this.name.setMetas(i_Metas);
    }

    
    /**
     * 获取：所有逻辑名称。Map.key为本体结构串码metaCode
     */
    public Map<String ,ProbeSchemaName> getAllName()
    {
        return allName;
    }

    
    /**
     * 设置：所有逻辑名称。Map.key为本体结构串码metaCode
     * 
     * @param i_AllName 所有逻辑名称。Map.key为本体结构串码metaCode
     */
    public void setAllName(Map<String ,ProbeSchemaName> i_AllName)
    {
        this.allName = i_AllName;
    }

    
    /**
     * 获取：是否创建本体结构。1创建，0不创建
     */
    public Integer getCreateMetas()
    {
        return createMetas;
    }

    
    /**
     * 设置：是否创建本体结构。1创建，0不创建
     * 
     * @param i_CreateMetas 是否创建本体结构。1创建，0不创建
     */
    public void setCreateMetas(Integer i_CreateMetas)
    {
        this.createMetas = i_CreateMetas;
    }
    
}
