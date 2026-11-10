package org.hy.microservice.common.probe.enums;

import org.hy.microservice.common.BaseEnum;





/**
 * 数据探针的关键字的枚举
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-06
 * @version     v1.0
 */
public enum ProbeKeyword implements BaseEnum<String>
{
    
    AssetID     ("assetID"      ,"数据资产编号"),
    IsDel       ("isDel"        ,"删除标记"),
    CreateUserID("createUserID" ,"数据创建人ID"),
    UpdateUserID("updateUserID" ,"数据修改人ID"),
    CreateTime  ("createTime"   ,"创建时间"),
    UpdateTime  ("updateTime"   ,"修改时间"),
    TableName   ("tableName"    ,"表名称"),
    
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
    public static ProbeKeyword get(String i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (ProbeKeyword v_Enum : ProbeKeyword.values())
        {
            if ( v_Enum.value.equalsIgnoreCase(i_Value.trim()) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    ProbeKeyword(String i_Value ,String i_Comment)
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
