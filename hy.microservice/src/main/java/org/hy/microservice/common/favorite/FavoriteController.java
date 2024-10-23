package org.hy.microservice.common.favorite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.user.UserSSO;
import org.hy.microservice.common.user.UserService;
import org.hy.microservice.common.view.ViewModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;





/**
 * 控制层：收藏夹
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-10-22
 * @version     v1.0
 */
@Controller
@RequestMapping(value="favorite" ,name="收藏夹")
public class FavoriteController extends BaseController
{
    private static final Logger $Logger = new Logger(FavoriteController.class);
    
    @Autowired
    @Qualifier("FavoriteService")
    private IFavoriteService           favoriteService;
    
    @Autowired
    @Qualifier("UserService")
    private UserService                userService;
    
    @Autowired
    @Qualifier("MS_Common_IsCheckToken")
    private Param                      isCheckToken;
    
     
    
    /**
     * 查询收藏夹
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     *
     * @param i_Token
     * @param i_Favorite
     * @return
     */
    @RequestMapping(name="查询收藏夹" ,value="queryFavorite" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<FavoriteVO> queryFavorite(@RequestParam(value="token" ,required=false) String i_Token
                                                 ,@RequestBody FavoriteVO i_Favorite)
    {
        $Logger.info("queryFavorite Start: " + i_Token);
        
        BaseResponse<FavoriteVO> v_RetResp = new BaseResponse<FavoriteVO>();
        long                      v_Count   = 0L;
        long                      v_Total   = 0L;
        
        if ( i_Favorite == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            if ( Help.isNull(i_Favorite.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
            {
                // 验证票据及用户登录状态
                if ( Help.isNull(i_Token) )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
                
                UserSSO v_User = this.userService.getUser(i_Token);
                if ( v_User == null )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
            }
            
            List<FavoriteVO> v_DataList = null;
            if ( !Help.isNull(i_Favorite.getId()) )
            {
                v_DataList = new ArrayList<FavoriteVO>();
                FavoriteDomain v_Favorite = this.favoriteService.queryByID(i_Favorite.getId());
                if ( v_Favorite != null )
                {
                    v_DataList.add(new FavoriteVO(v_Favorite));
                }
                v_Total = v_DataList.size();
            }
            else
            {
                Map<String ,FavoriteDomain> v_Datas   = this.favoriteService.queryByPage(i_Favorite.gatDomain());
                Map<String ,FavoriteVO>     v_DatasVO = ViewModelUtil.toView(v_Datas ,FavoriteVO.class);
                v_DataList = Help.toList(v_DatasVO);
                v_Total    = this.favoriteService.queryCount(i_Favorite.gatDomain());
                
                v_Datas  .clear();
                v_DatasVO.clear();
                v_Datas   = null;
                v_DatasVO = null;
            }
            
            v_Count = v_DataList.size();
            return v_RetResp.setData(v_DataList).setDataCount(v_Count).setTotalCount(v_Total);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("queryFavorite End: "  + i_Token + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 保存收藏夹
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-10-22
     * @version     v1.0
     *
     * @param i_Token
     * @param i_Favorite
     * @return
     */
    @RequestMapping(name="保存收藏夹" ,value="saveFavorite" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<FavoriteVO> saveFavorite(@RequestParam(value="token" ,required=false) String i_Token
                                                ,@RequestBody FavoriteVO i_Favorite)
    {
        BaseResponse<FavoriteVO> v_RetResp = new BaseResponse<FavoriteVO>();
        
        if ( i_Favorite == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            FavoriteDomain v_Favorite = i_Favorite.gatDomain();
            FavoriteDomain v_Old       = null;
            $Logger.info("saveFavorite Start: " + i_Token + ":" + v_Favorite.toString());
            
            if ( Help.isNull(v_Favorite.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            // 新创建的验证
            if ( Help.isNull(v_Favorite.getId()) )
            {
                if ( Help.isNull(v_Favorite.getServiceType()) )
                {
                    return v_RetResp.setCode("-10").setMessage("收藏业务分类为空");
                }
                
                if ( Help.isNull(v_Favorite.getDataID()) )
                {
                    return v_RetResp.setCode("-11").setMessage("收藏数据ID为空");
                }
                
                if ( Help.isNull(v_Favorite.getFavoriteName()) )
                {
                    return v_RetResp.setCode("-12").setMessage("收藏数据名称为空");
                }
                
                // 禁止创建时就删除
                v_Favorite.setIsDel(null);
                v_Favorite.setIsValid(null);
            }
            else
            {
                v_Old = this.favoriteService.queryByID(v_Favorite.getId());
                if ( v_Old == null )
                {
                    return v_RetResp.setCode("-20").setMessage("ID不存在");
                }
                
                // 未修改请求参数时，填充原来的
                v_Favorite.setDataID(Help.NVL(v_Favorite.getDataID() ,v_Old.getDataID()));
            }
            
            if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
            {
                // 验证票据及用户登录状态
                if ( Help.isNull(i_Token) )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
                
                UserSSO v_User = this.userService.getUser(i_Token);
                if ( v_User == null )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
                
                if ( !v_User.getUserId().equals(v_Favorite.getUserID()) )
                {
                    return v_RetResp.setCode("-902").setMessage("操作用户与登录用户不一致");
                }
            }
            
            synchronized (this)
            {
                // 防止重复
                if ( Help.isNull(v_Favorite.getId()) )
                {
                    FavoriteDomain v_SameDataID = this.favoriteService.queryByDataID(v_Favorite.getUserID() ,v_Favorite.getDataID());
                    if ( v_SameDataID != null )
                    {
                        $Logger.warn("创建收藏数据ID[" + v_Favorite.getDataID() + "]时已存在，禁止重复创建");
                        return v_RetResp.setCode("-903").setMessage("收藏数据ID" + v_Favorite.getDataID() + "已存在，禁止重复创建");
                    }
                }
                
                FavoriteDomain v_SaveRet = this.favoriteService.save(v_Favorite);
                if ( v_SaveRet != null )
                {
                    $Logger.info("用户（" + v_Favorite.getCreateUserID() + "）创建成功");
                    return v_RetResp.setData(new FavoriteVO(v_SaveRet));
                }
                else
                {
                    $Logger.error("用户（" + Help.NVL(v_Favorite.getCreateUserID() ,v_Favorite.getUserID()) + "）创建，异常");
                    return v_RetResp.setCode("-998").setMessage("系统异常");
                }
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("saveFavorite End: " + i_Token + ":" + i_Favorite.toString());
        }
    }
    
}
