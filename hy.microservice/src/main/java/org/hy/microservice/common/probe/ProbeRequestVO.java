package org.hy.microservice.common.probe;

import java.util.List;
import java.util.Map;

import org.hy.microservice.common.BaseView;
import org.hy.microservice.common.probe.schema.ProbeSchemaVO;





/**
 * 交互模型：数据探针的请求报文
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public class ProbeRequestVO extends BaseView<ProbeRequestDomain>
{

    private static final long serialVersionUID = -1268295363127158204L;

    

    public ProbeRequestVO()
    {
        this(new ProbeRequestDomain());
    }
    
    
    
    public ProbeRequestVO(ProbeRequestDomain i_Domain)
    {
        super(i_Domain);
    }
    
    
    /**
     * 获取：数据探针的结构
     */
    public ProbeSchemaVO getSchema()
    {
        if ( this.domain.getSchema() == null )
        {
            return null;
        }
        else
        {
            return new ProbeSchemaVO(this.domain.getSchema());
        }
    }

    
    /**
     * 设置：数据探针的结构
     * 
     * @param i_Schema 数据探针的结构
     */
    public void setSchema(ProbeSchemaVO i_Schema)
    {
        if ( i_Schema == null )
        {
            this.domain.setSchema(null);
        }
        else
        {
            this.domain.setSchema(i_Schema.gatDomain());
        }
    }

    
    /**
     * 获取：数据资产编号中的来源
     */
    public String getSource()
    {
        return this.domain.getSource();
    }

    
    /**
     * 设置：数据资产编号中的来源
     * 
     * @param i_Source 数据资产编号中的来源
     */
    public void setSource(String i_Source)
    {
        this.domain.setSource(i_Source);
    }

    
    /**
     * 获取：数据资产编号中的归类
     */
    public String getType()
    {
        return this.domain.getType();
    }

    
    /**
     * 设置：数据资产编号中的归类
     * 
     * @param i_Type 数据资产编号中的归类
     */
    public void setType(String i_Type)
    {
        this.domain.setType(i_Type);
    }

    
    /**
     * 获取：保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据
     */
    public Map<String ,Map<String ,Object>> getSaves()
    {
        return this.domain.getSaves();
    }

    
    /**
     * 设置：保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据
     * 
     * @param i_Saves 保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据
     */
    public void setSaves(Map<String ,Map<String ,Object>> i_Saves)
    {
        this.domain.setSaves(i_Saves);
    }

    
    /**
     * 获取：删除数据
     */
    public List<String> getDels()
    {
        return this.domain.getDels();
    }

    
    /**
     * 设置：删除数据
     * 
     * @param i_Dels 删除数据
     */
    public void setDels(List<String> i_Dels)
    {
        this.domain.setDels(i_Dels);
    }
    
}
