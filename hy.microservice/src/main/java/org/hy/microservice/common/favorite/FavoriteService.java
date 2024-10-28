package org.hy.microservice.common.favorite;

import java.io.Serializable;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.common.domain.DomainUtil;





/**
 * 业务层：收藏夹
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-10-22
 * @version     v1.0
 */
@Xjava
public class FavoriteService implements IFavoriteService ,Serializable
{
    
    private static final long serialVersionUID = -4681197031916484873L;
    
    

    @Xjava
    private IFavoriteDAO favoriteDAO;
    
    @Xjava(ref="MS_Common_PagePerCount")
    private Param        pagePerCount;
    


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
    @Override
    public Map<String ,FavoriteDomain> queryList(FavoriteDomain i_Favorite)
    {
        return DomainUtil.toDomain(this.favoriteDAO.queryList(i_Favorite.gatData()) ,FavoriteDomain.class);
    }
    
    
    
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
    @Override
    public Map<String ,FavoriteDomain> queryByPage(FavoriteDomain io_Favorite)
    {
        long v_DefPagePerCount = this.pagePerCount.getValueLong();
        
        io_Favorite.setPageIndex(Help.NVL(io_Favorite.getPageIndex() ,1L));
        io_Favorite.setPagePerCount(Help.min(Help.NVL(io_Favorite.getPagePerCount() ,v_DefPagePerCount) ,v_DefPagePerCount));
        return DomainUtil.toDomain(this.favoriteDAO.queryByPage(io_Favorite.gatData()) ,FavoriteDomain.class);
    }
    
    
    
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
    @Override
    public Long queryCount(FavoriteDomain i_Favorite)
    {
        return this.favoriteDAO.queryCount(i_Favorite.gatData());
    }
    
    
    
    /**
     * 按ID查询收藏夹
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     * 
     * @param i_ID  收藏夹ID
     * @return
     */
    @Override
    public FavoriteDomain queryByID(String i_ID)
    {
        if ( Help.isNull(i_ID) )
        {
            return null;
        }
        
        FavoriteData v_Favorite = this.favoriteDAO.queryByID(i_ID);
        return v_Favorite == null ? null : new FavoriteDomain(v_Favorite);
    }
    
    
    
    /**
     * 按用户及收藏数据ID查询收藏夹
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-23
     * @version     v1.0
     * 
     * @param i_UserID       收藏用户ID
     * @param i_ServiceType  业务类型编码
     * @param i_DataID       收藏数据ID
     * @return
     */
    public FavoriteDomain queryByDataID(String i_UserID ,String i_ServiceType ,String i_DataID)
    {
        if ( Help.isNull(i_UserID) || Help.isNull(i_DataID) )
        {
            return null;
        }
        
        FavoriteData v_Favorite = this.favoriteDAO.queryByDataID(i_UserID ,i_ServiceType ,i_DataID);
        return v_Favorite == null ? null : new FavoriteDomain(v_Favorite);
    }
    
    
    
    
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
    @Override
    public FavoriteDomain save(FavoriteDomain io_Favorite)
    {
        if ( io_Favorite == null )
        {
            return null;
        }
        
        io_Favorite.setCreateUserID(Help.NVL(io_Favorite.getCreateUserID() ,io_Favorite.getUserID()));
        io_Favorite.setUpdateUserID(Help.NVL(io_Favorite.getUpdateUserID() ,io_Favorite.getUserID()));
        io_Favorite.setIsDel(       Help.NVL(io_Favorite.getIsDel()        ,0));
        io_Favorite.setUpdateTime(new Date());
        
        FavoriteDomain v_Old = null;
        if ( Help.isNull(io_Favorite.getId()) )
        {
            io_Favorite.setId("XFav" + StringHelp.getUUID());
            io_Favorite.setOrderBy(Help.NVL(io_Favorite.getOrderBy() ,0));
            io_Favorite.setIsValid(Help.NVL(io_Favorite.getIsValid() ,1));
            io_Favorite.setCreateTime(new Date());
        }
        else
        {
            v_Old = this.queryByID(io_Favorite.getId());
            io_Favorite.setDataID(     v_Old.getDataID());
            io_Favorite.setServiceType(v_Old.getServiceType());
            io_Favorite.setFavoriteName(Help.NVL(io_Favorite.getFavoriteName() ,v_Old.getFavoriteName()));
            io_Favorite.setOrderBy(     Help.NVL(io_Favorite.getOrderBy()      ,v_Old.getOrderBy()));
            io_Favorite.setIsValid(     Help.NVL(io_Favorite.getIsValid()      ,v_Old.getIsValid()));
            io_Favorite.setComment(     Help.NVL(io_Favorite.getComment()      ,v_Old.getComment()));
        }
        
        boolean v_Ret = this.favoriteDAO.save(io_Favorite.gatData());
        return v_Ret ? io_Favorite : null;
    }
    
}
