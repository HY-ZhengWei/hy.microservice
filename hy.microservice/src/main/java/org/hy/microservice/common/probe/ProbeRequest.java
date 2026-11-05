package org.hy.microservice.common.probe;

import java.util.List;
import java.util.Map;

import org.hy.microservice.common.BaseData;
import org.hy.microservice.common.probe.schema.ProbeSchema;





/**
 * 数据层：数据探针的请求报文
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public class ProbeRequest extends BaseData
{

    private static final long serialVersionUID = -1994924681993899036L;
    
    
    
    /** 数据探针的结构 */
    private ProbeSchema                      schema;
    
    /** 数据资产编号中的来源 */
    private String                           source;
    
    /** 数据资产编号中的归类 */
    private String                           type;
    
    /** 保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据 */
    private Map<String ,Map<String ,Object>> saves;
    
    /** 删除数据 */
    private List<String>                     dels;
    
    
    
    /**
     * 获取：数据探针的结构
     */
    public ProbeSchema getSchema()
    {
        return schema;
    }

    
    /**
     * 设置：数据探针的结构
     * 
     * @param i_Schema 数据探针的结构
     */
    public void setSchema(ProbeSchema i_Schema)
    {
        this.schema = i_Schema;
    }

    
    /**
     * 获取：数据资产编号中的来源
     */
    public String getSource()
    {
        return source;
    }

    
    /**
     * 设置：数据资产编号中的来源
     * 
     * @param i_Source 数据资产编号中的来源
     */
    public void setSource(String i_Source)
    {
        this.source = i_Source;
    }

    
    /**
     * 获取：数据资产编号中的归类
     */
    public String getType()
    {
        return type;
    }

    
    /**
     * 设置：数据资产编号中的归类
     * 
     * @param i_Type 数据资产编号中的归类
     */
    public void setType(String i_Type)
    {
        this.type = i_Type;
    }

    
    /**
     * 获取：保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据
     */
    public Map<String ,Map<String ,Object>> getSaves()
    {
        return saves;
    }

    
    /**
     * 设置：保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据
     * 
     * @param i_Saves 保存数据。Map.key 为数据资产编号中的：日期 + 序号。Map.value 为KeyValue的数据
     */
    public void setSaves(Map<String ,Map<String ,Object>> i_Saves)
    {
        this.saves = i_Saves;
    }

    
    /**
     * 获取：删除数据
     */
    public List<String> getDels()
    {
        return dels;
    }

    
    /**
     * 设置：删除数据
     * 
     * @param i_Dels 删除数据
     */
    public void setDels(List<String> i_Dels)
    {
        this.dels = i_Dels;
    }
    
}
