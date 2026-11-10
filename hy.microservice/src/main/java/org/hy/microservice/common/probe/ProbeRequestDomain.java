package org.hy.microservice.common.probe;

import java.util.List;
import java.util.Map;

import org.hy.microservice.common.BaseDomain;
import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;





/**
 * 领域模型：数据探针的请求报文
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public class ProbeRequestDomain extends BaseDomain<ProbeRequest>
{

    private static final long serialVersionUID = 4071690453337186166L;

    

    public ProbeRequestDomain()
    {
        this(new ProbeRequest());
    }
    
    
    
    public ProbeRequestDomain(ProbeRequest i_Data)
    {
        super(i_Data);
    }
    
    
    
    /**
     * 获取：数据探针的结构
     */
    public ProbeSchemaDomain getSchema()
    {
        if ( this.data.getSchema() == null )
        {
            return null;
        }
        else
        {
            return new ProbeSchemaDomain(this.data.getSchema());
        }
    }

    
    /**
     * 设置：数据探针的结构
     * 
     * @param i_Schema 数据探针的结构
     */
    public void setSchema(ProbeSchemaDomain i_Schema)
    {
        if ( i_Schema != null )
        {
            this.data.setSchema(i_Schema.gatData());
        }
        else
        {
            this.data.setSchema(null);
        }
    }
    
    
    /**
     * 获取：数据资产编号中的归类
     */
    public String getType()
    {
        return this.data.getType();
    }

    
    /**
     * 设置：数据资产编号中的归类
     * 
     * @param i_Type 数据资产编号中的归类
     */
    public void setType(String i_Type)
    {
        this.data.setType(i_Type);
    }

    
    /**
     * 获取：数据资产编号中的来源
     */
    public String getSource()
    {
        return this.data.getSource();
    }

    
    /**
     * 设置：数据资产编号中的来源
     * 
     * @param i_Source 数据资产编号中的来源
     */
    public void setSource(String i_Source)
    {
        this.data.setSource(i_Source);
    }

    
    /**
     * 获取：保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据
     */
    public Map<String ,Map<String ,Object>> getSaves()
    {
        return this.data.getSaves();
    }

    
    /**
     * 设置：保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据
     * 
     * @param i_Saves 保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据
     */
    public void setSaves(Map<String ,Map<String ,Object>> i_Saves)
    {
        this.data.setSaves(i_Saves);
    }

    
    /**
     * 获取：删除数据
     */
    public List<String> getDels()
    {
        return this.data.getDels();
    }

    
    /**
     * 设置：删除数据
     * 
     * @param i_Dels 删除数据
     */
    public void setDels(List<String> i_Dels)
    {
        this.data.setDels(i_Dels);
    }
    
}
