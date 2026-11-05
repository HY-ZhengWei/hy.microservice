package org.hy.microservice.common.probe.enums;

import org.hy.microservice.common.BaseEnum;





/**
 * 格式类型的枚举
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-11
 * @version     v1.0
 */
public enum MetaFormat implements BaseEnum<String>
{
    
    String ("STRING"  ,"文字"),
    
    Number ("NUMBER"  ,"数字"),
    
    Boolean("BOOLEAN" ,"逻辑"),
    
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
    public static MetaFormat get(String i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (MetaFormat v_Enum : MetaFormat.values())
        {
            if ( v_Enum.value.equalsIgnoreCase(i_Value.trim()) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    MetaFormat(String i_Value ,String i_Comment)
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
    
}
