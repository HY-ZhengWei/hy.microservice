package org.hy.microservice.common;





/**
 * 通用的领域模型
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-06-12
 * @version     v1.0
 * @param <Data>  数据对象的类型
 */
public class BaseDomain<Data>
{
 
    /** 数据对象 */
    protected Data data;
    
    
    
    /**
     * 获取：Data 类型的数据
     */
    public Data gatData()
    {
        return data;
    }

    
    /**
     * 设置：Data 类型的数据
     * 
     * @param i_Data 类型的数据
     */
    public void satData(Data i_Data)
    {
        this.data = i_Data;
    }
    
}
