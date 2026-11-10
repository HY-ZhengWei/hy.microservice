package org.hy.microservice.common.probe.schema;

import org.hy.microservice.common.BaseData;
import org.hy.microservice.common.probe.enums.MetaFormat;





/**
 * 数据层：数据探针的本体结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-11
 * @version     v1.0
 */
public class ProbeSchemaMeta extends BaseData
{
    
    private static final long serialVersionUID = -6189984283543611605L;
    
    
    
    /** 主键 */
    private String  id;
    
    /** 归类ID */
    private String  schemaID;
    
    /** 逻辑名称ID */
    private String  nameID;
    
    /** 名称 */
    private String  mname;
    
    /** 格式类型 */
    private String  mformat;
    
    /** 总长度 */
    private Integer mlength;
    
    /** 小数位数 */
    private Integer mdecimal;
    
    /** 是否为主键，默认为0，即非主键 */
    private Integer pk;
    
    /** 是否允许空，默认为1，即允许空 */
    private Integer nullable;
    
    
    
    public ProbeSchemaMeta()
    {
        this.mformat  = MetaFormat.String.getValue();
        this.mlength  = 1;
        this.mdecimal = 0;
        this.pk       = 0;
        this.nullable = 1;
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
     * 获取：逻辑名称ID
     */
    public String getNameID()
    {
        return nameID;
    }


    /**
     * 设置：逻辑名称ID
     * 
     * @param i_NameID 逻辑名称ID
     */
    public void setNameID(String i_NameID)
    {
        this.nameID = i_NameID;
    }


    /**
     * 获取：名称
     */
    public String getMname()
    {
        return mname;
    }

    
    /**
     * 设置：名称
     * 
     * @param i_Mname 名称
     */
    public void setMname(String i_Mname)
    {
        this.mname = i_Mname;
    }

    
    /**
     * 获取：格式类型
     */
    public String getMformat()
    {
        return mformat;
    }

    
    /**
     * 设置：格式类型
     * 
     * @param i_Mformat 格式类型
     */
    public void setMformat(String i_Mformat)
    {
        this.mformat = i_Mformat;
    }

    
    /**
     * 获取：总长度
     */
    public Integer getMlength()
    {
        return mlength;
    }

    
    /**
     * 设置：总长度
     * 
     * @param i_Mlength 总长度
     */
    public void setMlength(Integer i_Mlength)
    {
        this.mlength = i_Mlength;
    }

    
    /**
     * 获取：小数位数
     */
    public Integer getMdecimal()
    {
        return mdecimal;
    }

    
    /**
     * 设置：小数位数
     * 
     * @param i_Mdecimal 小数位数
     */
    public void setMdecimal(Integer i_Mdecimal)
    {
        this.mdecimal = i_Mdecimal;
    }

    
    /**
     * 获取：是否为主键，默认为0
     */
    public Integer getPk()
    {
        return pk;
    }

    
    /**
     * 设置：是否为主键，默认为0
     * 
     * @param i_Pk 是否为主键，默认为0
     */
    public void setPk(Integer i_Pk)
    {
        this.pk = i_Pk;
    }
    
    
    /**
     * 获取：是否允许空，默认为1，即允许空
     */
    public Integer getNullable()
    {
        return nullable;
    }

    
    /**
     * 设置：是否允许空，默认为1，即允许空
     * 
     * @param i_Nullable 是否允许空，默认为1，即允许空
     */
    public void setNullable(Integer i_Nullable)
    {
        this.nullable = i_Nullable;
    }



    @Override
    public String toString()
    {
        StringBuilder v_Buffer = new StringBuilder();
        
        v_Buffer.append(this.mname)   .append(";");
        v_Buffer.append(this.mformat) .append(";");
        v_Buffer.append(this.mlength) .append(";");
        v_Buffer.append(this.mdecimal).append(";");
        v_Buffer.append(this.pk);
        v_Buffer.append(this.nullable);
        
        return v_Buffer.toString();
    }
    
}
