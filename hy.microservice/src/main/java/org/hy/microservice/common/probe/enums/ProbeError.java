package org.hy.microservice.common.probe.enums;

import org.hy.microservice.common.BaseEnum;





/**
 * 数据探针的接口异常类型的枚举
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-06
 * @version     v1.0
 */
public enum ProbeError implements BaseEnum<String>
{
    
    E001_DataNull   ("PDE001"  ,"数据为空"),
    E002_YMDNo      ("PDE002"  ,"日期+序号长度不合法"),
    E003_YMD        ("PDE003"  ,"日期不合法"),
    E004_No         ("PDE004"  ,"序号不合法"),
    E005_NameExists ("PDE005"  ,"字段名称未定义"),
    E006_Type       ("PDE006"  ,"字段类型不合法"),
    E007_Lenght     ("PDE007"  ,"字段长度不合法"),
    E008_PK         ("PDE008"  ,"主键不能为空"),
    E009_Nullable   ("PDE009"  ,"必填字段不能为空"),
    
    ;
    
    
    
    /** 值 */
    private String value;
    
    /** 描述 */
    private String comment;
    
    
    
    /**
     * 数值转为常量
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-29
     * @version     v1.0
     *
     * @param i_Value
     * @return
     */
    public static ProbeError get(String i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (ProbeError v_Enum : ProbeError.values())
        {
            if ( v_Enum.value.equalsIgnoreCase(i_Value.trim()) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    ProbeError(String i_Value ,String i_Comment)
    {
        this.value   = i_Value;
        this.comment = i_Comment;
    }

    
    
    @Override
    public String getValue()
    {
        return this.value;
    }
    
    
    
    @Override
    public String getComment()
    {
        return this.comment;
    }
    
    

    @Override
    public String toString()
    {
        return this.value;
    }
    
    
    public String toString(String i_FieldName)
    {
        return this.value + ":" + i_FieldName + this.comment;
    }
    
}
