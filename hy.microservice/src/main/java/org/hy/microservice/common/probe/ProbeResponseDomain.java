package org.hy.microservice.common.probe;

import java.util.List;

import org.hy.microservice.common.BaseDomain;





/**
 * 领域模型：数据探针的响应报文
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
public class ProbeResponseDomain extends BaseDomain<ProbeResponse>
{

    private static final long serialVersionUID = -6316883386233959314L;
    
    
    
    public ProbeResponseDomain()
    {
        this(new ProbeResponse());
    }
    
    
    
    public ProbeResponseDomain(ProbeResponse i_Data)
    {
        super(i_Data);
    }
    
    
    
    /**
     * 获取：今天之前最后一天完备的数据资产编号，预防丢失最后一天中的最后一条
     */
    public List<String> getLasts()
    {
        return this.data.getLasts();
    }

    
    /**
     * 设置：今天之前最后一天完备的数据资产编号，预防丢失最后一天中的最后一条
     * 
     * @param i_Lasts 今天之前最后一天完备的数据资产编号，预防丢失最后一天中的最后一条
     */
    public void setLasts(List<String> i_Lasts)
    {
        this.data.setLasts(i_Lasts);
    }

    
    /**
     * 获取：完备的数据资产编号，会重复回应
     */
    public List<String> getCompletes()
    {
        return this.data.getCompletes();
    }

    
    /**
     * 设置：完备的数据资产编号，会重复回应
     * 
     * @param i_Completes 完备的数据资产编号，会重复回应
     */
    public void setCompletes(List<String> i_Completes)
    {
        this.data.setCompletes(i_Completes);
    }

    
    /**
     * 获取：丢失的数据资产编号，可能会多次回应
     */
    public List<String> getRedos()
    {
        return this.data.getRedos();
    }

    
    /**
     * 设置：丢失的数据资产编号，可能会多次回应
     * 
     * @param i_Redos 丢失的数据资产编号，可能会多次回应
     */
    public void setRedos(List<String> i_Redos)
    {
        this.data.setRedos(i_Redos);
    }
    
}
