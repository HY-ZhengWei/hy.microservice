package org.hy.microservice.common.favorite;

import java.util.Map;





/**
 * 业务层：收藏夹
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-10-22
 * @version     v1.0
 */
public interface IFavoriteService
{
    
    /**
     * 查询任务收藏夹列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     * 
     * @param i_Favorite  收藏夹
     * @return
     */
    public Map<String ,FavoriteDomain> queryList(FavoriteDomain i_Favorite);
    
    
    
    /**
     * 查询任务收藏夹列表（分页查询）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     * 
     * @param io_Favorite  收藏夹
     * @return
     */
    public Map<String ,FavoriteDomain> queryByPage(FavoriteDomain io_Favorite);
    
    
    
    /**
     * 查询收藏夹列表的总记录数
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     *
     * @param i_Favorite  收藏夹
     * @return
     */
    public Long queryCount(FavoriteDomain i_Favorite);
    
    
    
    /**
     * 按ID查询收藏夹
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     * 
     * @param i_FavoriteID  收藏夹ID
     * @return
     */
    public FavoriteDomain queryByID(String i_FavoriteID);
    
    
    
    /**
     * 按用户及收藏数据ID查询收藏夹
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-23
     * @version     v1.0
     * 
     * @param i_UserID  收藏用户ID
     * @param i_DataID  收藏数据ID
     * @return
     */
    public FavoriteDomain queryByDataID(String i_UserID ,String i_DataID);
    
    
    
    /**
     * 新增、修改、逻辑删除收藏夹
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     *
     * @param io_Favorite  收藏夹
     * @return
     */
    public FavoriteDomain save(FavoriteDomain io_Favorite);
    
}
