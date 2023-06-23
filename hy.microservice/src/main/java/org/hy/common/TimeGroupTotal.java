package org.hy.common;

import java.util.LinkedHashMap;
import java.util.Map;





/**
 * 时间分组统计
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-06-21
 * @version     v1.0
 */
public class TimeGroupTotal extends LinkedHashMap<Date ,Long> implements Map<Date ,Long>
{

    private static final long serialVersionUID = -385135442173957943L;
    
    /** 时间分组的分钟大小。有效取值范围在：1~59，建议取值范围在：1~30 */
    private int splitMinuteSize;
    
    /** 保存时间分组数据的最大数量。超过最大数量时自动删除最早的数据。0表示无限制 */
    private int maxSize = 0;
    
    
    
    public TimeGroupTotal(int i_SplitMinuteSize)
    {
        super();
        this.splitMinuteSize = i_SplitMinuteSize;
    }
    
    
    
    public TimeGroupTotal(int i_SplitMinuteSize ,int i_InitialCapacity)
    {
        super(i_InitialCapacity);
        this.splitMinuteSize = i_SplitMinuteSize;
    }
    
    
    
    /**
     * 生成时间分组的Key
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_TimeData
     * @return
     */
    public Date getTimeKey(Date i_TimeData)
    {
        Date v_TimeGroup = i_TimeData.getTimeGroup(this.splitMinuteSize);
        return v_TimeGroup;
    }
    
    
    
    /**
     * 添加时间分组数据
     * 
     * 支持不同时间分组类型的分组转换
     *
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_TimeDatas
     *
     * @see java.util.HashMap#putAll(java.util.Map)
     */
    @Override
    public void putAll(Map<? extends Date ,? extends Long> i_TimeDatas)
    {
        for (Map.Entry<? extends Date ,? extends Long> v_Item : i_TimeDatas.entrySet())
        {
            this.put(v_Item.getKey() ,v_Item.getValue());
        }
    }
    
    
    
    /**
     * 添加时间分组数据
     *
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_TimeData    时间分组数据
     * @param i_TotalCount  时间分组的统计数据（将被累加）
     * @return
     *
     * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public synchronized Long put(Date i_TimeData ,Long i_TotalCount)
    {
        Date v_DataKey   = this.getTimeKey(i_TimeData);
        Long v_DataValue = super.get(v_DataKey);
        
        if ( v_DataValue == null )
        {
            v_DataValue = i_TotalCount;
        }
        else
        {
            v_DataValue += i_TotalCount.longValue();
        }
        
        super.put(v_DataKey ,v_DataValue);
        
        // 超过最大数量时自动删除最早的数据。0表示无限制
        if ( this.maxSize > 0 && super.size() > this.maxSize )
        {
            this.remove(super.keySet().iterator().next());
        }
        
        return v_DataValue;
    }



    /**
     * 添加时间分组数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_TimeData  时间分组数据
     * @return            返回当前时间分组的总数量
     */
    public Long put(Date i_TimeData)
    {
        return this.put(i_TimeData ,1L);
    }
    
    
    
    /**
     * 添加时间分组数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @return            返回当前时间分组的总数量
     */
    public Long put()
    {
        return this.put(new Date() ,1L);
    }
    
    
    
    /**
     * 获取时间分组的统计数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_Key
     * @return
     *
     */
    public Long get(Date i_Key)
    {
        return super.get(this.getTimeKey(i_Key));
    }
    
    
    
    /**
     * 获取时间分组的统计数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_Key
     * @return
     *
     * @see java.util.LinkedHashMap#get(java.lang.Object)
     */
    @Override
    public Long get(Object i_Key)
    {
        if ( i_Key == null )
        {
            return 0L;
        }
        else if ( i_Key instanceof Date )
        {
            return super.get(this.getTimeKey((Date)i_Key));
        }
        else if ( i_Key instanceof java.util.Date )
        {
            return super.get(this.getTimeKey(new Date((java.util.Date)i_Key)));
        }
        else if ( i_Key instanceof Long )
        {
            return super.get(this.getTimeKey(new Date((Long)i_Key)));
        }
        else
        {
            return 0L;
        }
    }



    /**
     * 删除某一个时间分组的统计数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_TimeData  时间分组数据
     * @return
     */
    public Long remove(Date i_TimeData)
    {
        Date v_DataKey   = this.getTimeKey(i_TimeData);
        Long v_DataValue = super.remove(v_DataKey);
        
        return v_DataValue;
    }
    
    
    
    /**
     * 删除某一个时间分组的统计数据
     *
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_Key
     * @return
     *
     * @see java.util.HashMap#remove(java.lang.Object)
     */
    @Override
    public Long remove(Object i_Key)
    {
        if ( i_Key == null )
        {
            return 0L;
        }
        else if ( i_Key instanceof Date )
        {
            return super.remove(this.getTimeKey((Date)i_Key));
        }
        else if ( i_Key instanceof java.util.Date )
        {
            return super.remove(this.getTimeKey(new Date((java.util.Date)i_Key)));
        }
        else if ( i_Key instanceof Long )
        {
            return super.remove(this.getTimeKey(new Date((Long)i_Key)));
        }
        else
        {
            return 0L;
        }
    }

    

    /**
     * 获取：时间分组的分钟大小。有效取值范围在：1~59，建议取值范围在：1~30
     */
    public int getSplitMinuteSize()
    {
        return splitMinuteSize;
    }


    
    /**
     * 获取：保存时间分组数据的最大数量。超过最大数量时自动删除最早的数据。0表示无限制
     */
    public int getMaxSize()
    {
        return maxSize;
    }


    
    /**
     * 设置：保存时间分组数据的最大数量。超过最大数量时自动删除最早的数据。0表示无限制
     * 
     * @param i_MaxSize 保存时间分组数据的最大数量。超过最大数量时自动删除最早的数据。0表示无限制
     */
    public void setMaxSize(int i_MaxSize)
    {
        this.maxSize = i_MaxSize;
    }
    
}
