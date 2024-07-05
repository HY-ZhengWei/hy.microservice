package org.hy.microservice.common.view;

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
import org.hy.microservice.common.BaseView;





/**
 * 交互层领域转换工具
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-01
 * @version     v1.0
 */
public class ViewModelUtil
{
    
    private static final Logger $Logger = new Logger(ViewModelUtil.class);
    
    
    
    /**
     * 将领域层的集合转为交互层的集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param <Data>       数据层的泛型
     * @param <Domain>     领域层的泛型
     * @param <View>       交互层的泛型
     * @param i_Domains    领域层的领域集合
     * @param i_ViewClass  交互层的元类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <Data extends BaseData ,Domain extends BaseDomain<Data> ,View extends BaseView<Domain>> List<View> toView(List<Domain> i_Domains ,Class<View> i_ViewClass)
    {
        if ( i_Domains == null )
        {
            return null;
        }
        else if ( i_Domains.isEmpty() )
        {
            return new ArrayList<View>();
        }
        else if ( i_ViewClass == null )
        {
            throw new NullPointerException("Domain.class is null");
        }
        
        Constructor<View> v_ViewConstructor = findConstructor((Class<Domain>) i_Domains.get(0).getClass() ,i_ViewClass);
        if ( v_ViewConstructor == null )
        {
            throw new RuntimeException("Can't find Doamin Constructor(<? extends BaseData>)");
        }
        
        List<View> v_Views = new ArrayList<View>();
        for (Domain v_Domain : i_Domains)
        {
            View v_View = newView(v_ViewConstructor ,v_Domain);
            if ( v_View != null )
            {
                v_Views.add(v_View);
            }
        }
        return v_Views;
    }
    
    
    
    /**
     * 将领域层的集合转为交互层的集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param <Data>       数据层的泛型
     * @param <Domain>     领域层的泛型
     * @param <View>       交互层的泛型
     * @param i_Domains    领域层的领域集合
     * @param i_ViewClass  交互层的元类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <Data extends BaseData ,Domain extends BaseDomain<Data> ,View extends BaseView<Domain>> Set<View> toView(Set<Domain> i_Domains ,Class<View> i_ViewClass)
    {
        if ( i_Domains == null )
        {
            return null;
        }
        else if ( i_Domains.isEmpty() )
        {
            return new LinkedHashSet<View>();
        }
        else if ( i_ViewClass == null )
        {
            throw new NullPointerException("View.class is null");
        }
        
        Constructor<View> v_ViewConstructor = findConstructor((Class<Domain>) i_Domains.iterator().next().getClass() ,i_ViewClass);
        if ( v_ViewConstructor == null )
        {
            throw new RuntimeException("Can't find View Constructor(<? extends BaseDomain>)");
        }
        
        Set<View> v_Views = new LinkedHashSet<View>();
        for (Domain v_Domain : i_Domains)
        {
            View v_View = newView(v_ViewConstructor ,v_Domain);
            if ( v_View != null )
            {
                v_Views.add(v_View);
            }
        }
        return v_Views;
    }
    
    
    
    /**
     * 将领域层的集合转为交互层的集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param <Data>       数据层的泛型
     * @param <Domain>     领域层的泛型
     * @param <View>       交互层的泛型
     * @param i_Domains    领域层的领域集合
     * @param i_ViewClass  交互层的元类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <Data extends BaseData ,Domain extends BaseDomain<Data> ,View extends BaseView<Domain>> Map<String ,View> toView(Map<String ,Domain> i_Domains ,Class<View> i_ViewClass)
    {
        if ( i_Domains == null )
        {
            return null;
        }
        else if ( i_Domains.isEmpty() )
        {
            return new LinkedHashMap<String ,View>();
        }
        else if ( i_ViewClass == null )
        {
            throw new NullPointerException("View.class is null");
        }
        
        Constructor<View> v_ViewConstructor = findConstructor((Class<Domain>) i_Domains.values().iterator().next().getClass() ,i_ViewClass);
        if ( v_ViewConstructor == null )
        {
            throw new RuntimeException("Can't find View Constructor(<? extends BaseDomain>)");
        }
        
        Map<String ,View> v_Views = new LinkedHashMap<String ,View>();
        for (Map.Entry<String ,Domain> v_Domain : i_Domains.entrySet())
        {
            View v_View = newView(v_ViewConstructor ,v_Domain.getValue());
            if ( v_View != null )
            {
                v_Views.put(v_Domain.getKey() ,v_View);
            }
        }
        return v_Views;
    }
    
    
    
    /**
     * 用领域层的数据构建交互层的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param <Data>             数据层的泛型
     * @param <Domain>           领域层的泛型
     * @param <View>             交互层的泛型
     * @param i_ViewConstructor  交互层的构造器
     * @param i_Domain           领域层的领域
     * @return
     */
    private static <Data extends BaseData ,Domain extends BaseDomain<Data> ,View extends BaseView<Domain>> View newView(Constructor<View> i_ViewConstructor ,Domain i_Domain)
    {
        try
        {
            return i_ViewConstructor.newInstance(i_Domain);
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
     * 查找交互层的构造器，此构造器的入参类型是：领域层的对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-01
     * @version     v1.0
     *
     * @param <Data>         数据层的泛型
     * @param <Domain>       领域层的泛型
     * @param <View>         交互层的泛型
     * @param i_DomainClass  领域层的元类型
     * @param i_ViewClass    交互层的元类型
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <Data extends BaseData ,Domain extends BaseDomain<Data> ,View extends BaseView<Domain>> Constructor<View> findConstructor(Class<Domain> i_DomainClass ,Class<View> i_ViewClass)
    {
        Constructor<View>[] v_Constructors = (Constructor<View> []) i_ViewClass.getConstructors();
        if ( Help.isNull(v_Constructors) )
        {
            return null;
        }
        
        for (Constructor<View> v_Item : v_Constructors)
        {
            if ( v_Item.getParameterCount() != 1 )
            {
                continue;
            }
            
            if ( v_Item.getParameters()[0].getType().equals(i_DomainClass) )
            {
                return v_Item;
            }
        }
        
        return null;
    }
    
    
    
    private ViewModelUtil()
    {
        // 本类不可被 new
    }
    
}
