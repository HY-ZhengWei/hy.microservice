package org.hy.microservice.common.favorite;

import org.hy.microservice.common.BaseView;





/**
 * 交互模型：收藏夹
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-10-22
 * @version     v1.0
 */
public class FavoriteVO extends BaseView<FavoriteDomain>
{

    private static final long serialVersionUID = -6378319972961617075L;



    public FavoriteVO()
    {
        this(new FavoriteDomain());
    }
    
    
    
    public FavoriteVO(FavoriteDomain i_Domain)
    {
        super(i_Domain);
    }
    
    
    
    /**
     * 获取：主键
     */
    public String getId()
    {
        return this.domain.getId();
    }

    
    
    /**
     * 设置：主键
     * 
     * @param i_Id 主键
     */
    public void setId(String i_Id)
    {
        this.domain.setId(i_Id);
    }

    
    
    /**
     * 获取：逻辑ID
     */
    public String getDataID()
    {
        return this.domain.getDataID();
    }


    
    /**
     * 设置：逻辑ID
     * 
     * @param i_DataID 逻辑ID
     */
    public void setDataID(String i_DataID)
    {
        this.domain.setDataID(i_DataID);
    }


    
    /**
     * 获取：收藏名称
     */
    public String getFavoriteName()
    {
        return this.domain.getFavoriteName();
    }


    
    /**
     * 设置：收藏名称
     * 
     * @param i_FavoriteName 收藏名称
     */
    public void setFavoriteName(String i_FavoriteName)
    {
        this.domain.setFavoriteName(i_FavoriteName);
    }
    
}
