package org.hy.microservice.common.probe.schema;

import java.util.List;
import java.util.Map;

import org.hy.common.XJavaID;
import org.hy.microservice.common.BaseDomain;
import org.hy.microservice.common.domain.DataUtil;
import org.hy.microservice.common.domain.DomainUtil;





/**
 * 领域模型：数据探针的结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-11
 * @version     v1.0
 */
public class ProbeSchemaDomain extends BaseDomain<ProbeSchema> implements XJavaID
{

    private static final long serialVersionUID = 4622662203502051071L;
    
    
    
    /** 旧的逻辑ID */
    private String xidOld;
    
    
    
    public ProbeSchemaDomain()
    {
        this(new ProbeSchema());
    }
    
    
    
    public ProbeSchemaDomain(ProbeSchema i_Data)
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
     * 获取：归类
     */
    public String getType()
    {
        return this.data.getType();
    }

    
    /**
     * 设置：归类
     * 
     * @param i_Type 归类
     */
    public void setType(String i_Type)
    {
        this.data.setType(i_Type);
    }

    
    /**
     * 获取：默认的逻辑名称ID
     */
    public String getNameID()
    {
        return this.data.getNameID();
    }

    
    /**
     * 设置：默认的逻辑名称ID
     * 
     * @param i_NameID  默认的逻辑名称ID
     */
    public void setNameID(String i_NameID)
    {
        this.data.setNameID(i_NameID);
    }
    
    
    /**
     * 获取：默认的逻辑名称
     */
    public ProbeSchemaNameDomain getName()
    {
        return new ProbeSchemaNameDomain(this.data.getName());
    }

    
    /**
     * 设置：默认的逻辑名称
     * 
     * @param i_Name 默认的逻辑名称
     */
    public void setName(ProbeSchemaNameDomain i_Name)
    {
        this.data.setName(i_Name.gatData());
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
    public void setMetas(List<ProbeSchemaMetaDomain> i_Metas)
    {
        this.data.setMetas(DataUtil.toData(i_Metas));
    }
    
    
    /**
     * 获取：所有逻辑名称。Map.key为本体结构串码metaCode
     */
    public Map<String ,ProbeSchemaNameDomain> getAllName()
    {
        return DomainUtil.toDomain(this.data.getAllName() ,ProbeSchemaNameDomain.class);
    }

    
    /**
     * 设置：所有逻辑名称。Map.key为本体结构串码metaCode
     * 
     * @param i_AllName 所有逻辑名称。Map.key为本体结构串码metaCode
     */
    public void setAllName(Map<String ,ProbeSchemaNameDomain> i_AllName)
    {
        this.data.setAllName(DataUtil.toData(i_AllName));
    }
    
    
    /**
     * 获取：是否创建本体结构。1创建，0不创建
     */
    public Boolean getCreateMetas()
    {
        return this.data.getCreateMetas() == 1;
    }

    
    /**
     * 设置：是否创建本体结构。1创建，0不创建
     * 
     * @param i_CreateMetas 是否创建本体结构。1创建，0不创建
     */
    public void setCreateMetas(Boolean i_CreateMetas)
    {
        this.data.setCreateMetas(i_CreateMetas ? 1 : 0);
    }
    
    
    /**
     * 获取：逻辑ID
     */
    public String getXid()
    {
        return this.data.getType();
    }
    
    
    /**
     * 设置：逻辑ID
     * 
     * @param i_Xid 逻辑ID
     */
    public void setXid(String i_Xid)
    {
        this.data.setType(i_Xid);
    }
    
    
    /**
     * 获取：旧的逻辑ID
     */
    public String getXidOld()
    {
        return xidOld;
    }

    
    /**
     * 设置：旧的逻辑ID
     * 
     * @param i_XidOld 旧的逻辑ID
     */
    public void setXidOld(String i_XidOld)
    {
        this.xidOld = i_XidOld;
    }

    
    /**
     * 设置XJava池中对象的ID标识。此方法不用用户调用设置值，是自动的。
     * 
     * @param i_XJavaID
     */
    @Override
    public void setXJavaID(String i_XJavaID)
    {
        this.data.setType(i_XJavaID);
    }
    
    
    /**
     * 获取XJava池中对象的ID标识。
     * 
     * @return
     */
    @Override
    public String getXJavaID()
    {
        return this.data.getType();
    }
    
}
