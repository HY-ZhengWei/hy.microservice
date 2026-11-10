package org.hy.microservice.common.probe;

import java.util.List;
import java.util.Map;

import org.hy.microservice.common.BaseData;





/**
 * 数据层：数据探针的响应报文
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public class ProbeResponse extends BaseData
{

    private static final long serialVersionUID = 6629268818486050116L;
    
    
    
    /** 今天之前最后一天完备的数据资产编号，预防丢失最后一天中的最后一条 */
    private List<String>        lasts;
    
    /** 完备的数据资产编号，会重复回应 */
    private List<String>        completes;
    
    /** 丢失的数据资产编号，可能会多次回应 */
    private List<String>        redos;
    
    /** 非法数据。Map.key是数据资产编号，Map.value是错误编号和原因 */
    private Map<String ,String> errorDatas;

    
    
    /**
     * 获取：今天之前最后一天完备的数据资产编号，预防丢失最后一天中的最后一条
     */
    public List<String> getLasts()
    {
        return lasts;
    }

    
    /**
     * 设置：今天之前最后一天完备的数据资产编号，预防丢失最后一天中的最后一条
     * 
     * @param i_Lasts 今天之前最后一天完备的数据资产编号，预防丢失最后一天中的最后一条
     */
    public void setLasts(List<String> i_Lasts)
    {
        this.lasts = i_Lasts;
    }

    
    /**
     * 获取：完备的数据资产编号，会重复回应
     */
    public List<String> getCompletes()
    {
        return completes;
    }

    
    /**
     * 设置：完备的数据资产编号，会重复回应
     * 
     * @param i_Completes 完备的数据资产编号，会重复回应
     */
    public void setCompletes(List<String> i_Completes)
    {
        this.completes = i_Completes;
    }

    
    /**
     * 获取：丢失的数据资产编号，可能会多次回应
     */
    public List<String> getRedos()
    {
        return redos;
    }

    
    /**
     * 设置：丢失的数据资产编号，可能会多次回应
     * 
     * @param i_Redos 丢失的数据资产编号，可能会多次回应
     */
    public void setRedos(List<String> i_Redos)
    {
        this.redos = i_Redos;
    }

    
    /**
     * 获取：非法数据
     */
    public Map<String ,String> getErrorDatas()
    {
        return errorDatas;
    }

    
    /**
     * 设置：非法数据
     * 
     * @param i_ErrorDatas 非法数据
     */
    public void setErrorDatas(Map<String ,String> i_ErrorDatas)
    {
        this.errorDatas = i_ErrorDatas;
    }
    
}
