package org.hy.microservice.common.favorite;

import java.util.Map;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * DAO层：收藏夹
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-10-22
 * @version     v1.0
 */
@Xjava(id="FavoriteDAO" ,value=XType.XSQL)
public interface IFavoriteDAO
{
    
    /**
     * 查询收藏夹列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     * 
     * @param i_Favorite  收藏夹
     * @return
     */
    @Xsql(id="XSQL_Common_FavoriteData_Query")
    public Map<String ,FavoriteData> queryList(FavoriteData i_Favorite);
    
    
    
    /**
     * 查询收藏夹列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     * 
     * @param i_Favorite  收藏夹
     * @return
     */
    @Xsql(id="XSQL_Common_FavoriteData_Query_ByPage" ,paging=true)
    public Map<String ,FavoriteData> queryByPage(FavoriteData i_Favorite);
    
    
    
    /**
     * 查询收藏夹列表的总记录数
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     *
     * @return
     */
    @Xsql(id="XSQL_Common_FavoriteData_Count")
    public Long queryCount(FavoriteData i_Favorite);
    
    
    
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
    @Xsql(id="XSQL_Common_FavoriteData_Query" ,returnOne=true)
    public FavoriteData queryByID(@Xparam("id") String i_ID);
    
    
    
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
    @Xsql(id="XSQL_Common_FavoriteData_Query" ,returnOne=true)
    public FavoriteData queryByDataID(@Xparam("userID") String i_UserID ,@Xparam("dataID") String i_DataID);
    
    
    
    /**
     * 新增、修改、逻辑删除收藏夹
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     *
     * @param i_Favorite  收藏夹
     * @return
     */
    @Xsql("GXSQL_Common_FavoriteData_Save")
    public boolean save(FavoriteData i_Favorite);
    
}
