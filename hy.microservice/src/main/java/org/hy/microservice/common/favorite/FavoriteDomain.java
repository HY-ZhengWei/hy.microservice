package org.hy.microservice.common.favorite;

import org.hy.microservice.common.BaseDomain;





/**
 * 领域模型：收藏夹
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-10-22
 * @version     v1.0
 */
public class FavoriteDomain extends BaseDomain<FavoriteData>
{

    private static final long serialVersionUID = 3396278444232814795L;
    
    
    
    public FavoriteDomain()
    {
        this(new FavoriteData());
    }
    
    
    
    public FavoriteDomain(FavoriteData i_Data)
    {
        super(i_Data);
    }
    
    
    
    /**
     * 获取：主键
     */
    public String getId()
    {
        return this.data.getId();
    }

    
    
    /**
     * 设置：主键
     * 
     * @param i_Id 主键
     */
    public void setId(String i_Id)
    {
        this.data.setId(i_Id);
    }

    
    
    /**
     * 获取：逻辑ID
     */
    public String getDataID()
    {
        return this.data.getDataID();
    }


    
    /**
     * 设置：逻辑ID
     * 
     * @param i_DataID 逻辑ID
     */
    public void setDataID(String i_DataID)
    {
        this.data.setDataID(i_DataID);
    }


    
    /**
     * 获取：收藏名称
     */
    public String getFavoriteName()
    {
        return this.data.getFavoriteName();
    }


    
    /**
     * 设置：收藏名称
     * 
     * @param i_FavoriteName 收藏名称
     */
    public void setFavoriteName(String i_FavoriteName)
    {
        this.data.setFavoriteName(i_FavoriteName);
    }
    
}
