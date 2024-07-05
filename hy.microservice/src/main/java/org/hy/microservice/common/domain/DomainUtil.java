package org.hy.microservice.common.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hy.common.Help;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseData;
import org.hy.microservice.common.BaseDomain;





/**
 * 领域层数据转换工具
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-06-27
 * @version     v1.0
 */
public class DomainUtil
{
    
    private static final Logger $Logger = new Logger(DomainUtil.class);
    
    
    
    /**
     * 将数据层的集合转为领域层的集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-27
     * @version     v1.0
     *
     * @param <Data>         数据层的泛型
     * @param <Domain>       领域层的泛型
     * @param i_Datas        数据层的数据集合
     * @param i_DomainClass  领域层的元类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <Data extends BaseData ,Domain extends BaseDomain<Data>> List<Domain> toDomain(List<Data> i_Datas ,Class<Domain> i_DomainClass)
    {
        if ( i_Datas == null )
        {
            return null;
        }
        else if ( i_Datas.isEmpty() )
        {
            return new ArrayList<Domain>();
        }
        else if ( i_DomainClass == null )
        {
            throw new NullPointerException("Domain.class is null");
        }
        
        Constructor<Domain> v_DomainConstructor = findConstructor((Class<Data>) i_Datas.get(0).getClass() ,i_DomainClass);
        if ( v_DomainConstructor == null )
        {
            throw new RuntimeException("Can't find Doamin Constructor(<? extends BaseData>)");
        }
        
        List<Domain> v_Domains = new ArrayList<Domain>();
        for (Data v_Data : i_Datas)
        {
            Domain v_Domain = newDomain(v_DomainConstructor ,v_Data);
            if ( v_Domain != null )
            {
                v_Domains.add(v_Domain);
            }
        }
        return v_Domains;
    }
    
    
    
    /**
     * 将数据层的集合转为领域层的集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-27
     * @version     v1.0
     *
     * @param <Data>         数据层的泛型
     * @param <Domain>       领域层的泛型
     * @param i_Datas        数据层的数据集合
     * @param i_DomainClass  领域层的元类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <Data extends BaseData ,Domain extends BaseDomain<Data>> Set<Domain> toDomain(Set<Data> i_Datas ,Class<Domain> i_DomainClass)
    {
        if ( i_Datas == null )
        {
            return null;
        }
        else if ( i_Datas.isEmpty() )
        {
            return new LinkedHashSet<Domain>();
        }
        else if ( i_DomainClass == null )
        {
            throw new NullPointerException("Domain.class is null");
        }
        
        Constructor<Domain> v_DomainConstructor = findConstructor((Class<Data>) i_Datas.iterator().next().getClass() ,i_DomainClass);
        if ( v_DomainConstructor == null )
        {
            throw new RuntimeException("Can't find Doamin Constructor(<? extends BaseData>)");
        }
        
        Set<Domain> v_Domains = new LinkedHashSet<Domain>();
        for (Data v_Data : i_Datas)
        {
            Domain v_Domain = newDomain(v_DomainConstructor ,v_Data);
            if ( v_Domain != null )
            {
                v_Domains.add(v_Domain);
            }
        }
        return v_Domains;
    }
    
    
    
    /**
     * 将数据层的集合转为领域层的集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-27
     * @version     v1.0
     *
     * @param <Data>         数据层的泛型
     * @param <Domain>       领域层的泛型
     * @param i_Datas        数据层的数据集合
     * @param i_DomainClass  领域层的元类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <Data extends BaseData ,Domain extends BaseDomain<Data>> Map<String ,Domain> toDomain(Map<String ,Data> i_Datas ,Class<Domain> i_DomainClass)
    {
        if ( i_Datas == null )
        {
            return null;
        }
        else if ( i_Datas.isEmpty() )
        {
            return new LinkedHashMap<String ,Domain>();
        }
        else if ( i_DomainClass == null )
        {
            throw new NullPointerException("Domain.class is null");
        }
        
        Constructor<Domain> v_DomainConstructor = findConstructor((Class<Data>) i_Datas.values().iterator().next().getClass() ,i_DomainClass);
        if ( v_DomainConstructor == null )
        {
            throw new RuntimeException("Can't find Doamin Constructor(<? extends BaseData>)");
        }
        
        Map<String ,Domain> v_Domains = new LinkedHashMap<String ,Domain>();
        for (Map.Entry<String ,Data> v_Data : i_Datas.entrySet())
        {
            Domain v_Domain = newDomain(v_DomainConstructor ,v_Data.getValue());
            if ( v_Domain != null )
            {
                v_Domains.put(v_Data.getKey() ,v_Domain);
            }
        }
        return v_Domains;
    }
    
    
    
    /**
     * 用数据层的数据构建领域层的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-27
     * @version     v1.0
     *
     * @param <Data>               数据层的泛型
     * @param <Domain>             领域层的泛型
     * @param i_DomainConstructor  领域层的构造器
     * @param i_Data               数据层的数据
     * @return
     */
    private static <Data extends BaseData ,Domain extends BaseDomain<Data>> Domain newDomain(Constructor<Domain> i_DomainConstructor ,Data i_Data)
    {
        try
        {
            return i_DomainConstructor.newInstance(i_Data);
        }
        catch (InstantiationException e)
        {
            $Logger.error(e);
        }
        catch (IllegalAccessException e)
        {
            $Logger.error(e);
        }
        catch (IllegalArgumentException e)
        {
            $Logger.error(e);
        }
        catch (InvocationTargetException e)
        {
            $Logger.error(e);
        }
        return null;
    }
    
    
    
    /**
     * 查找领域层的构造器，此构造器的入参类型是：数据层的对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-06-27
     * @version     v1.0
     *
     * @param <Data>         数据层的泛型
     * @param <Domain>       领域层的泛型
     * @param i_DataClass    数据层的元类型
     * @param i_DomainClass  领域层的元类型
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <Data extends BaseData ,Domain extends BaseDomain<Data>> Constructor<Domain> findConstructor(Class<Data> i_DataClass ,Class<Domain> i_DomainClass)
    {
        Constructor<Domain>[] v_Constructors = (Constructor<Domain> []) i_DomainClass.getConstructors();
        if ( Help.isNull(v_Constructors) )
        {
            return null;
        }
        
        for (Constructor<Domain> v_Item : v_Constructors)
        {
            if ( v_Item.getParameterCount() != 1 )
            {
                continue;
            }
            
            if ( v_Item.getParameters()[0].getType().equals(i_DataClass) )
            {
                return v_Item;
            }
        }
        
        return null;
    }
    
    
    
    private DomainUtil()
    {
        // 本类不可被 new
    }
    
}
