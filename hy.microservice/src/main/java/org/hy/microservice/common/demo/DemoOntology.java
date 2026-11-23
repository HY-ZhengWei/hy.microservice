package org.hy.microservice.common.demo;

import org.hy.common.xml.SerializableDef;

import org.hy.microservice.common.probe.annotation.Ontology;





/**
 * 数据探针本体的定义（客户端）
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-10
 * @version     v1.0
 */
@Ontology(c="Demo：数据探针本体的定义（客户端）")
public class DemoOntology extends SerializableDef
{
    
    private static final long serialVersionUID = 3012075379541648084L;

    
    
    /** 主键 */
    @Ontology(l=32 ,pk=true)
    private String  id;
    
    /** 名称 */
    @Ontology(l=100 ,c="名称" ,n=false)
    private String  name;
    
    /** 年龄 */
    @Ontology(l=10)
    private Integer age;
    
    /** 小时数 */
    @Ontology(l=10)
    private Long    hours;
    
    /** 体重 */
    @Ontology(l=5 ,d=2 ,c="体重")
    private Double  weight;
    
    /** 成功标记 */
    @Ontology(c="成功标记")
    private Boolean succeed;
    
    /** 通过标记 */
    @Ontology
    private Boolean pass;
    
    
    
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
     * 获取：名称
     */
    public String getName()
    {
        return name;
    }

    
    /**
     * 设置：名称
     * 
     * @param i_Name 名称
     */
    public void setName(String i_Name)
    {
        this.name = i_Name;
    }

    
    /**
     * 获取：年龄
     */
    public Integer getAge()
    {
        return age;
    }

    
    /**
     * 设置：年龄
     * 
     * @param i_Age 年龄
     */
    public void setAge(Integer i_Age)
    {
        this.age = i_Age;
    }

    
    /**
     * 获取：小时数
     */
    public Long getHours()
    {
        return hours;
    }

    
    /**
     * 设置：小时数
     * 
     * @param i_Hours 小时数
     */
    public void setHours(Long i_Hours)
    {
        this.hours = i_Hours;
    }

    
    /**
     * 获取：体重
     */
    public Double getWeight()
    {
        return weight;
    }

    
    /**
     * 设置：体重
     * 
     * @param i_Weight 体重
     */
    public void setWeight(Double i_Weight)
    {
        this.weight = i_Weight;
    }

    
    /**
     * 获取：成功标记
     */
    public Boolean getSucceed()
    {
        return succeed;
    }

    
    /**
     * 设置：成功标记
     * 
     * @param i_Succeed 成功标记
     */
    public void setSucceed(Boolean i_Succeed)
    {
        this.succeed = i_Succeed;
    }

    
    /**
     * 获取：通过标记
     */
    public Boolean getPass()
    {
        return pass;
    }

    
    /**
     * 设置：通过标记
     * 
     * @param i_Pass 通过标记
     */
    public void setPass(Boolean i_Pass)
    {
        this.pass = i_Pass;
    }
    
}
