package org.hy.microservice.common.probe.schema;

import java.util.List;

import org.hy.microservice.common.BaseView;
import org.hy.microservice.common.domain.DomainUtil;
import org.hy.microservice.common.view.ViewModelUtil;





/**
 * 交互模型：数据探针的结构中本体逻辑名称
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-26
 * @version     v1.0
 */
public class ProbeSchemaNameVO extends BaseView<ProbeSchemaNameDomain>
{
    
    private static final long serialVersionUID = 2086813782285554419L;


    
    public ProbeSchemaNameVO()
    {
        this(new ProbeSchemaNameDomain());
    }
    
    
    
    public ProbeSchemaNameVO(ProbeSchemaNameDomain i_Data)
    {
        super(i_Data);
    }
    
    
    
    /**
     * 获取：主键
     */
    public String getId()
    {
        return this.domain.getId();
    }
    
    
    /**
     * 设置：主键
     * 
     * @param i_Id 主键
     */
    public void setId(String i_Id)
    {
        this.domain.setId(i_Id);
    }
    
    
    /**
     * 获取：归类ID
     */
    public String getSchemaID()
    {
        return this.domain.getSchemaID();
    }

    
    /**
     * 设置：归类ID
     * 
     * @param i_SchemaID 归类ID
     */
    public void setSchemaID(String i_SchemaID)
    {
        this.domain.setSchemaID(i_SchemaID);
    }
    
    
    /**
     * 获取：逻辑名称
     */
    public String getTableName()
    {
        return this.domain.getTableName();
    }

    
    /**
     * 设置：逻辑名称
     * 
     * @param i_TableName 逻辑名称
     */
    public void setTableName(String i_TableName)
    {
        this.domain.setTableName(i_TableName);
    }

    
    /**
     * 获取：本体结构串码
     */
    public String getMetaCode()
    {
        return this.domain.getMetaCode();
    }

    
    /**
     * 设置：本体结构串码
     * 
     * @param i_MetaCode 本体结构串码
     */
    public void setMetaCode(String i_MetaCode)
    {
        this.domain.setMetaCode(i_MetaCode);
    }

    
    /**
     * 获取：本体结构
     */
    public List<ProbeSchemaMetaVO> getMetas()
    {
        return ViewModelUtil.toView(this.domain.getMetas() ,ProbeSchemaMetaVO.class);
    }

    
    /**
     * 设置：本体结构
     * 
     * @param i_Metas 本体结构
     */
    public void setMetas(List<ProbeSchemaMetaVO> i_Metas)
    {
        this.domain.setMetas(DomainUtil.toDomain(i_Metas));
    }
    
}
