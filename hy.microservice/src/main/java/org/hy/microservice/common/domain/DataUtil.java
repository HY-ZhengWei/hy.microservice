package org.hy.microservice.common.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hy.microservice.common.BaseData;
import org.hy.microservice.common.BaseDomain;





/**
 * 数据层转换工具
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-11
 * @version     v1.0
 */
public class DataUtil
{
    
    /**
     * 将领域层的集合转为数据层的集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-11
     * @version     v1.0
     *
     * @param <Data>     数据层的泛型
     * @param <Domain>   领域层的泛型
     * @param i_Domains  领域层的数据集合
     * @return
     */
    public static <Data extends BaseData ,Domain extends BaseDomain<Data>> List<Data> toData(List<Domain> i_Domains)
    {
        if ( i_Domains == null )
        {
            return null;
        }
        else if ( i_Domains.isEmpty() )
        {
            return new ArrayList<Data>();
        }
        
        List<Data> v_Datas = new ArrayList<Data>();
        for (Domain v_Domain : i_Domains)
        {
            Data v_Data = v_Domain.gatData();
            if ( v_Data != null )
            {
                v_Datas.add(v_Data);
            }
        }
        return v_Datas;
    }
    
    
    
    /**
     * 将领域层的集合转为数据层的集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-11
     * @version     v1.0
     *
     * @param <Data>     数据层的泛型
     * @param <Domain>   领域层的泛型
     * @param i_Domains  领域层的数据集合
     * @return
     */
    public static <Data extends BaseData ,Domain extends BaseDomain<Data>> Set<Data> toData(Set<Domain> i_Domains)
    {
        if ( i_Domains == null )
        {
            return null;
        }
        else if ( i_Domains.isEmpty() )
        {
            return new LinkedHashSet<Data>();
        }
        
        Set<Data> v_Domains = new LinkedHashSet<Data>();
        for (Domain v_Domain : i_Domains)
        {
            Data v_Data = v_Domain.gatData();
            if ( v_Data != null )
            {
                v_Domains.add(v_Data);
            }
        }
        return v_Domains;
    }
    
    
    
    /**
     * 将领域层的集合转为数据层的集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-10-11
     * @version     v1.0
     *
     * @param <Data>     数据层的泛型
     * @param <Domain>   领域层的泛型
     * @param i_Domains  领域层的数据集合
     * @return
     */
    public static <Data extends BaseData ,Domain extends BaseDomain<Data>> Map<String ,Data> toData(Map<String ,Domain> i_Domains)
    {
        if ( i_Domains == null )
        {
            return null;
        }
        else if ( i_Domains.isEmpty() )
        {
            return new LinkedHashMap<String ,Data>();
        }
        
        Map<String ,Data> v_Datas = new LinkedHashMap<String ,Data>();
        for (Map.Entry<String ,Domain> v_Domain : i_Domains.entrySet())
        {
            Data v_Data = v_Domain.getValue().gatData();
            if ( v_Data != null )
            {
                v_Datas.put(v_Domain.getKey() ,v_Data);
            }
        }
        return v_Datas;
    }
    
    
    
    private DataUtil()
    {
        // 本类不可被 new
    }
    
}
