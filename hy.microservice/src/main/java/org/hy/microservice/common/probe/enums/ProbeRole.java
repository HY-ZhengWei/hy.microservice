package org.hy.microservice.common.probe.enums;

import org.hy.microservice.common.BaseEnum;





/**
 * 角色类型的枚举
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-23
 * @version     v1.0
 */
public enum ProbeRole implements BaseEnum<String>
{
    
    Client("C" ,"客户端"),
    
    Server("S" ,"服务端"),
    
    ;
    
    
    
    /** 值 */
    private String value;
    
    /** 描述 */
    private String comment;
    
    
    
    /**
     * 数值转为常量
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-23
     * @version     v1.0
     *
     * @param i_Value
     * @return
     */
    public static ProbeRole get(String i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (ProbeRole v_Enum : ProbeRole.values())
        {
            if ( v_Enum.value.equalsIgnoreCase(i_Value.trim()) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    ProbeRole(String i_Value ,String i_Comment)
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
