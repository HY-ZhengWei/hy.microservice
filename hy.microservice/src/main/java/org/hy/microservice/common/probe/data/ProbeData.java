package org.hy.microservice.common.probe.data;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.XJavaID;

import org.hy.microservice.common.BaseData;





/**
 * 数据层：数据探针的数据
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-11-09
 * @version     v1.0
 */
public class ProbeData extends BaseData implements XJavaID
{

    private static final long serialVersionUID = 5213722641211328232L;

    
    
    /** 数据资产编号 */
    private String              assetID;
    
    /** 归类 */
    private String              type;
    
    /** 本体结构串码 */
    private String              metaCode;
    
    /** 数据 */
    private Map<String ,Object> data;
    
    /** 数据由缓存到持久化的异常次数 */
    private int                 cacheToDBErrCount;
    
    
    
    public ProbeData()
    {
        this.data              = new HashMap<>();
        this.cacheToDBErrCount = 0;
    }
    
    
    /**
     * 获取：数据资产编号
     */
    public String getAssetID()
    {
        return assetID;
    }

    
    /**
     * 设置：数据资产编号
     * 
     * @param i_AssetID 数据资产编号
     */
    public void setAssetID(String i_AssetID)
    {
        this.assetID = i_AssetID;
    }


    /**
     * 获取：归类
     */
    public String getType()
    {
        return type;
    }

    
    /**
     * 设置：归类
     * 
     * @param i_Type 归类
     */
    public void setType(String i_Type)
    {
        this.type = i_Type;
    }

    
    /**
     * 获取：本体结构串码
     */
    public String getMetaCode()
    {
        return metaCode;
    }

    
    /**
     * 设置：本体结构串码
     * 
     * @param i_MetaCode 本体结构串码
     */
    public void setMetaCode(String i_MetaCode)
    {
        this.metaCode = i_MetaCode;
    }


    /**
     * 获取：数据
     */
    public Map<String ,Object> getData()
    {
        return data;
    }

    
    /**
     * 设置：数据
     * 
     * @param i_Data 数据
     */
    public void setData(Map<String ,Object> i_Data)
    {
        this.data = i_Data;
    }

    
    /**
     * 获取：数据由缓存到持久化的异常次数
     */
    public int getCacheToDBErrCount()
    {
        return cacheToDBErrCount;
    }

    
    /**
     * 设置：数据由缓存到持久化的异常次数
     * 
     * @param i_CacheToDBErrCount 数据由缓存到持久化的异常次数
     */
    public void setCacheToDBErrCount(int i_CacheToDBErrCount)
    {
        this.cacheToDBErrCount = i_CacheToDBErrCount;
    }


    @Override
    public void setXJavaID(String i_XJavaID)
    {
        this.assetID = i_XJavaID;
    }

    
    @Override
    public String getXJavaID()
    {
        return this.assetID;
    }

}
