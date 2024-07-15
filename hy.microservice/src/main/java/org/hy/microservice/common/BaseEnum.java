package org.hy.microservice.common;





/**
 * 基础枚举类
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-11-05
 * @version     v1.0
 */
public interface BaseEnum<O>
{
    
    /**
     * 枚举值
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-05
     * @version     v1.0
     *
     * @return
     */
    public O getValue();
    
    
    
    /**
     * 备注说明
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-15
     * @version     v1.0
     *
     * @return
     */
    public String getComment();
    
}
