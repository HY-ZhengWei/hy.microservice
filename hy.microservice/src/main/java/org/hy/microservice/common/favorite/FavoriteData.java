package org.hy.microservice.common.favorite;

import org.hy.microservice.common.BaseData;





/**
 * 数据层：收藏夹
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-10-22
 * @version     v1.0
 */
public class FavoriteData extends BaseData
{

    private static final long serialVersionUID = -614626585904490856L;

    /** 主键 */
    private String  id;
    
    /** 逻辑ID */
    private String  dataID;
    
    /** 收藏名称 */
    private String  favoriteName;
    
    
    
    /**
     * 获取：主键
     */
    public String getId()
    {
        return id;
    }

    
    
    /**
     * 设置：主键
     * 
     * @param i_Id 主键
     */
    public void setId(String i_Id)
    {
        this.id = i_Id;
    }


    
    /**
     * 获取：逻辑ID
     */
    public String getDataID()
    {
        return dataID;
    }


    
    /**
     * 设置：逻辑ID
     * 
     * @param i_DataID 逻辑ID
     */
    public void setDataID(String i_DataID)
    {
        this.dataID = i_DataID;
    }


    
    /**
     * 获取：收藏名称
     */
    public String getFavoriteName()
    {
        return favoriteName;
    }


    
    /**
     * 设置：收藏名称
     * 
     * @param i_FavoriteName 收藏名称
     */
    public void setFavoriteName(String i_FavoriteName)
    {
        this.favoriteName = i_FavoriteName;
    }

}
