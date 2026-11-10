package org.hy.microservice.common.probe.schema;

import org.hy.microservice.common.BaseView;
import org.hy.microservice.common.probe.enums.MetaFormat;





/**
 * 交互模型：数据探针的本体结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public class ProbeSchemaMetaVO extends BaseView<ProbeSchemaMetaDomain>
{
    
    private static final long serialVersionUID = 6018771442624425105L;

    

    public ProbeSchemaMetaVO()
    {
        this(new ProbeSchemaMetaDomain());
    }
    
    
    
    public ProbeSchemaMetaVO(ProbeSchemaMetaDomain i_Data)
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
     * 获取：逻辑名称ID
     */
    public String getNameID()
    {
        return this.domain.getNameID();
    }


    /**
     * 设置：逻辑名称ID
     * 
     * @param i_NameID 逻辑名称ID
     */
    public void setNameID(String i_NameID)
    {
        this.domain.setNameID(i_NameID);
    }
    
    
    /**
     * 获取：名称
     */
    public String getMname()
    {
        return this.domain.getMname();
    }

    
    /**
     * 设置：名称
     * 
     * @param i_Mname 名称
     */
    public void setMname(String i_Mname)
    {
        this.domain.setMname(i_Mname);
    }
    
    
    /**
     * 获取：名称的ABC代号
     */
    public String getMnameABC()
    {
        return this.domain.getMnameABC();
    }
    
    
    /**
     * 获取：格式类型
     */
    public String getMformat()
    {
        return this.domain.getMformat() == null ? MetaFormat.String.getValue() : this.domain.getMformat().getValue();
    }

    
    /**
     * 设置：格式类型
     * 
     * @param i_Mformat 格式类型
     */
    public void setMformat(String i_Mformat)
    {
        MetaFormat v_MetaFormat = MetaFormat.get(i_Mformat);
        if ( v_MetaFormat != null )
        {
            this.domain.setMformat(v_MetaFormat);
        }
        else
        {
            this.domain.setMformat(MetaFormat.String);
        }
    }

    
    /**
     * 获取：总长度
     */
    public Integer getMlength()
    {
        return this.domain.getMlength();
    }

    
    /**
     * 设置：总长度
     * 
     * @param i_Mlength 总长度
     */
    public void setMlength(Integer i_Mlength)
    {
        this.domain.setMlength(i_Mlength);
    }

    
    /**
     * 获取：小数位数
     */
    public Integer getMdecimal()
    {
        return this.domain.getMdecimal();
    }

    
    /**
     * 设置：小数位数
     * 
     * @param i_Mdecimal 小数位数
     */
    public void setMdecimal(Integer i_Mdecimal)
    {
        this.domain.setMdecimal(i_Mdecimal);
    }

    
    /**
     * 获取：是否为主键，默认为false
     */
    public Boolean getPk()
    {
        return this.domain.getPk();
    }

    
    /**
     * 设置：是否为主键，默认为false
     * 
     * @param i_Pk 是否为主键，默认为false
     */
    public void setPk(Boolean i_Pk)
    {
        this.domain.setPk(i_Pk);
    }
    
    
    /**
     * 获取：是否允许空，默认为1，即允许空
     */
    public Boolean getNullable()
    {
        return this.domain.getNullable();
    }

    
    /**
     * 设置：是否允许空，默认为1，即允许空
     * 
     * @param i_Nullable 是否允许空，默认为1，即允许空
     */
    public void setNullable(Boolean i_Nullable)
    {
        this.domain.setNullable(i_Nullable);
    }
    
}
