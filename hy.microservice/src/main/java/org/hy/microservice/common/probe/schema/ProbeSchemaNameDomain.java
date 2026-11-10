package org.hy.microservice.common.probe.schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;

import org.hy.microservice.common.BaseDomain;
import org.hy.microservice.common.domain.DataUtil;
import org.hy.microservice.common.domain.DomainUtil;





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

    
    
    /** 通过字段名称或名称代号快速定位字段信息的缓存（仅内部使用） */
    private Map<String ,ProbeSchemaMetaDomain> metasNames;
    
    

    public ProbeSchemaNameDomain()
    {
        this(new ProbeSchemaName());
    }
    
    
    
    public ProbeSchemaNameDomain(ProbeSchemaName i_Data)
    {
        super(i_Data);
        this.metasNames = new HashMap<>();
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
    public List<ProbeSchemaMetaDomain> getMetas()
    {
        return DomainUtil.toDomain(this.data.getMetas() ,ProbeSchemaMetaDomain.class);
    }

    
    /**
     * 设置：本体结构
     * 
     * @param i_Metas 本体结构
     */
    public synchronized void setMetas(List<ProbeSchemaMetaDomain> i_Metas)
    {
        this.data.setMetas(DataUtil.toData(i_Metas));
        this.metasNames.clear();
    }
    
    
    /**
     * 获取：通过字段名称或名称代号快速定位字段信息的缓存
     */
    public synchronized Map<String ,ProbeSchemaMetaDomain> getMetasNames()
    {
        if ( Help.isNull(this.metasNames) && !Help.isNull(this.data.getMetas()) )
        {
            for (ProbeSchemaMetaDomain v_Item : this.getMetas())
            {
                this.metasNames.put(v_Item.getMname()    ,v_Item);
                this.metasNames.put(v_Item.getMnameABC() ,v_Item);
            }
        }
        
        return this.metasNames;
    }
}
