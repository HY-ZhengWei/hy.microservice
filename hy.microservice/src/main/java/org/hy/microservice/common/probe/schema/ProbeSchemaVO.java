package org.hy.microservice.common.probe.schema;

import java.util.List;
import java.util.Map;

import org.hy.microservice.common.BaseView;
import org.hy.microservice.common.domain.DomainUtil;
import org.hy.microservice.common.view.ViewModelUtil;





/**
 * 交互模型：数据探针的结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public class ProbeSchemaVO extends BaseView<ProbeSchemaDomain>
{

    private static final long serialVersionUID = -8618997227245035688L;



    public ProbeSchemaVO()
    {
        this(new ProbeSchemaDomain());
    }
    
    
    
    public ProbeSchemaVO(ProbeSchemaDomain i_Domain)
    {
        super(i_Domain);
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
     * 获取：归类
     */
    public String getType()
    {
        return this.domain.getType();
    }

    
    /**
     * 设置：归类
     * 
     * @param i_Type 归类
     */
    public void setType(String i_Type)
    {
        this.domain.setType(i_Type);
    }

    
    /**
     * 获取：默认的逻辑名称ID
     */
    public String getNameID()
    {
        return this.domain.getNameID();
    }

    
    /**
     * 设置：默认的逻辑名称ID
     * 
     * @param i_NameID  默认的逻辑名称ID
     */
    public void setNameID(String i_NameID)
    {
        this.domain.setNameID(i_NameID);
    }
    
    
    /**
     * 获取：默认的逻辑名称
     */
    public ProbeSchemaNameVO getName()
    {
        return new ProbeSchemaNameVO(this.domain.getName());
    }

    
    /**
     * 设置：默认的逻辑名称
     * 
     * @param i_Name 默认的逻辑名称
     */
    public void setName(ProbeSchemaNameVO i_Name)
    {
        this.domain.setName(i_Name.gatDomain());
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
    
    
    /**
     * 获取：所有逻辑名称。Map.key为本体结构串码metaCode
     */
    public Map<String ,ProbeSchemaNameVO> getAllName()
    {
        return ViewModelUtil.toView(this.domain.getAllName() ,ProbeSchemaNameVO.class);
    }

    
    /**
     * 设置：所有逻辑名称。Map.key为本体结构串码metaCode
     * 
     * @param i_AllName 所有逻辑名称。Map.key为本体结构串码metaCode
     */
    public void setAllName(Map<String ,ProbeSchemaNameVO> i_AllName)
    {
        this.domain.setAllName(DomainUtil.toDomain(i_AllName));
    }
    
}
