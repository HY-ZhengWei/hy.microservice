package org.hy.microservice.common;





/**
 * 与页面交互的基础类的类型、参数、状态类的
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-05-14
 * @version     v1.0
 */
public class BaseViewModeType extends BaseViewMode
{

    private static final long serialVersionUID = 3580003804899041130L;
    
    
    
    /** 类型ID */
    private String  typeID;
    
    /** 类型名称 */
    private String  typeName;
    
    /** 类型编码 */
    private String  typeCode;

    
    
    /**
     * 获取：类型ID
     */
    public String getTypeID()
    {
        return typeID;
    }

    
    /**
     * 设置：类型ID
     * 
     * @param i_TypeID 类型ID
     */
    public void setTypeID(String i_TypeID)
    {
        this.typeID = i_TypeID;
    }

    
    /**
     * 获取：类型名称
     */
    public String getTypeName()
    {
        return typeName;
    }

    
    /**
     * 设置：类型名称
     * 
     * @param i_TypeName 类型名称
     */
    public void setTypeName(String i_TypeName)
    {
        this.typeName = i_TypeName;
    }

    
    /**
     * 获取：类型编码
     */
    public String getTypeCode()
    {
        return typeCode;
    }

    
    /**
     * 设置：类型编码
     * 
     * @param i_TypeCode 类型编码
     */
    public void setTypeCode(String i_TypeCode)
    {
        this.typeCode = i_TypeCode;
    }
    
}
