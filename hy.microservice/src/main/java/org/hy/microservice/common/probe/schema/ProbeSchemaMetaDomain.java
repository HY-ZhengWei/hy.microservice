package org.hy.microservice.common.probe.schema;

import org.hy.common.Help;
import org.hy.common.xml.log.Logger;

import org.hy.microservice.common.BaseDomain;
import org.hy.microservice.common.probe.enums.MetaFormat;





/**
 * 领域模型：数据探针的本体结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-11
 * @version     v1.0
 */
public class ProbeSchemaMetaDomain extends BaseDomain<ProbeSchemaMeta>
{
    
    private static final long serialVersionUID = -3451589376125891580L;
    
    private static final Logger $Logger = new Logger(ProbeSchemaMetaDomain.class);
    
    
    
    /** 格式类型 */
    private MetaFormat mformat;
    
    

    public ProbeSchemaMetaDomain()
    {
        this(new ProbeSchemaMeta());
    }
    
    
    
    public ProbeSchemaMetaDomain(ProbeSchemaMeta i_Data)
    {
        super(i_Data);
        
        // 转换：格式类型
        if ( !Help.isNull(this.gatData().getMformat()) )
        {
            this.mformat = MetaFormat.get(this.gatData().getMformat());
            if ( this.mformat == null )
            {
                String v_ErrMsg = "MetaFormat[" + this.gatData().getMformat() + "] is not exists.";
                $Logger.error(v_ErrMsg + i_Data.toString());
                throw new RuntimeException(v_ErrMsg);
            }
        }
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
     * 获取：逻辑名称ID
     */
    public String getNameID()
    {
        return this.data.getNameID();
    }


    /**
     * 设置：逻辑名称ID
     * 
     * @param i_NameID 逻辑名称ID
     */
    public void setNameID(String i_NameID)
    {
        this.data.setNameID(i_NameID);
    }
    
    
    /**
     * 获取：名称
     */
    public String getMname()
    {
        return this.data.getMname();
    }

    
    /**
     * 设置：名称
     * 
     * @param i_Mname 名称
     */
    public void setMname(String i_Mname)
    {
        this.data.setMname(i_Mname);
    }
    
    
    /**
     * 获取：格式类型
     */
    public MetaFormat getMformat()
    {
        return this.mformat;
    }

    
    /**
     * 设置：格式类型
     * 
     * @param i_Mformat 格式类型
     */
    public void setMformat(MetaFormat i_Mformat)
    {
        this.mformat = i_Mformat;
        if ( this.mformat == null )
        {
            this.data.setMformat(MetaFormat.String.getValue());
        }
        else
        {
            this.data.setMformat(this.mformat.getValue());
        }
    }

    
    /**
     * 获取：总长度
     */
    public Integer getMlength()
    {
        return this.data.getMlength();
    }

    
    /**
     * 设置：总长度
     * 
     * @param i_Mlength 总长度
     */
    public void setMlength(Integer i_Mlength)
    {
        this.data.setMlength(i_Mlength);
    }

    
    /**
     * 获取：小数位数
     */
    public Integer getMdecimal()
    {
        return this.data.getMdecimal();
    }

    
    /**
     * 设置：小数位数
     * 
     * @param i_Mdecimal 小数位数
     */
    public void setMdecimal(Integer i_Mdecimal)
    {
        this.data.setMdecimal(i_Mdecimal);
    }

    
    /**
     * 获取：是否为主键，默认为false
     */
    public Boolean getPk()
    {
        return this.data.getPk() != null && this.data.getPk() >= 1;
    }

    
    /**
     * 设置：是否为主键，默认为false
     * 
     * @param i_Pk 是否为主键，默认为false
     */
    public void setPk(Boolean i_Pk)
    {
        this.data.setPk(i_Pk != null && i_Pk ? 1 : 0);
    }


    @Override
    public String toString()
    {
        return this.data.toString();
    }
    
}
