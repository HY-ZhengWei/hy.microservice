package org.hy.microservice.common.ipSafe;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.operationLog.OperationLogController;
import org.hy.microservice.common.user.UserSSO;
import org.hy.microservice.common.user.UserService;
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
 * 系统安全访问IP黑白名单的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-05-26
 * @version     v1.0
 */
@Controller
@RequestMapping(value="ipSafeConfig" ,name="访问安全")
public class IPSafeConfigController extends BaseController
{
    
    private static final Logger $Logger = new Logger(OperationLogController.class);
    
    
    
    @Autowired
    @Qualifier("IPSafeConfigService")
    private IIPSafeConfigService       ipSafeConfigService;
    
    @Autowired
    @Qualifier("UserService")
    private UserService                userService;
    
    @Autowired
    @Qualifier("MS_Common_IsCheckToken")
    private Param                      isCheckToken;
    
    
    
    /**
     * 查询系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_Token
     * @return
     */
    @RequestMapping(name="查询安全配置" ,value="queryIPSafeConfig" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<IPSafeConfig> queryIPSafeConfig(@RequestParam(value="token" ,required=false) String i_Token
                                                       ,@RequestBody IPSafeConfig i_IPSafeConfig)
    {
        BaseResponse<IPSafeConfig> v_RetResp = new BaseResponse<IPSafeConfig>();
        int                        v_Count = 0;
        
        if ( i_IPSafeConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("queryIPSafeConfig Start: " + i_Token + ":" + i_IPSafeConfig.toString());
            
            if ( Help.isNull(i_IPSafeConfig.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            if ( Help.isNull(i_IPSafeConfig.getIpType()) )
            {
                return v_RetResp.setCode("-3").setMessage("IP类型为空");
            }
            
            if ( !StringHelp.isContains(i_IPSafeConfig.getIpType() ,IPSafeConfig.$Type_BackList ,IPSafeConfig.$Type_WhiteList) )
            {
                return v_RetResp.setCode("-4").setMessage("非法IP类型");
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
            
            List<IPSafeConfig> v_DataList = this.ipSafeConfigService.queryByIPType(i_IPSafeConfig);
            v_Count = v_DataList.size();
            return v_RetResp.setData(v_DataList);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("queryIPSafeConfig End: "  + i_Token + ":" + i_IPSafeConfig.toString() + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 保存系统安全访问IP黑白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_Token
     * @param i_IPSafeConfig
     * @return
     */
    @RequestMapping(name="保存安全配置" ,value="saveIPSafeConfig" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<IPSafeConfig> saveIPSafeConfig(@RequestParam(value="token" ,required=false) String i_Token
                                                      ,@RequestBody IPSafeConfig i_IPSafeConfig)
    {
        BaseResponse<IPSafeConfig> v_RetResp = new BaseResponse<IPSafeConfig>();
        
        if ( i_IPSafeConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("saveIPSafeConfig Start: " + i_Token + ":" + i_IPSafeConfig.toString());
            
            if ( Help.isNull(i_IPSafeConfig.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            // 新创建的验证
            if ( Help.isNull(i_IPSafeConfig.getId()) )
            {
                if ( Help.isNull(i_IPSafeConfig.getIpType()) )
                {
                    return v_RetResp.setCode("-3").setMessage("IP类型为空");
                }
                
                if ( !StringHelp.isContains(i_IPSafeConfig.getIpType() ,IPSafeConfig.$Type_BackList ,IPSafeConfig.$Type_WhiteList) )
                {
                    return v_RetResp.setCode("-4").setMessage("非法IP类型");
                }
                
                if ( Help.isNull(i_IPSafeConfig.getIp()) )
                {
                    return v_RetResp.setCode("-5").setMessage("IP为空");
                }
                
                // 防止重复
                if ( this.ipSafeConfigService.queryAll().getRow(i_IPSafeConfig.getIpType() ,i_IPSafeConfig.getIp()) != null )
                {
                    return v_RetResp.setCode("-6").setMessage("相同安全配置已存在，请勿重复添加");
                }
            }
            // 更新的验证
            else
            {
                if ( Help.isNull(i_IPSafeConfig.getIpType())
                  && Help.isNull(i_IPSafeConfig.getIp())
                  && Help.isNull(i_IPSafeConfig.getComment())
                  && Help.isNull(i_IPSafeConfig.getIsDel()) )
                {
                    return v_RetResp.setCode("-7").setMessage("没有要更新的内容");
                }
                
                IPSafeConfig v_OldConfig = this.ipSafeConfigService.queryByID(i_IPSafeConfig);
                if ( v_OldConfig == null )
                {
                    return v_RetResp.setCode("-102").setMessage("更新主键不存在");
                }
                
                if ( i_IPSafeConfig.getIsDel() != null && i_IPSafeConfig.getIsDel() != 0 )
                {
                    // Nothing. 删除时不验证是否重复
                }
                else if ( !Help.isNull(i_IPSafeConfig.getIpType()) && !Help.isNull(i_IPSafeConfig.getIp()) )
                {
                    // 防止重复：新IP + IPType的重复
                    if ( this.ipSafeConfigService.queryAll().getRow(i_IPSafeConfig.getIpType() ,i_IPSafeConfig.getIp()) != null )
                    {
                        return v_RetResp.setCode("-6").setMessage("相同安全配置已存在，请勿重复添加");
                    }
                }
                else if ( !Help.isNull(i_IPSafeConfig.getIp()) )
                {
                    // 防止重复：新IP重复
                    if ( this.ipSafeConfigService.queryAll().getRow(v_OldConfig.getIpType() ,i_IPSafeConfig.getIp()) != null )
                    {
                        return v_RetResp.setCode("-6").setMessage("相同安全配置已存在，请勿重复添加");
                    }
                }
                else if ( !Help.isNull(i_IPSafeConfig.getIpType()) )
                {
                    // 防止重复：新IPType重复
                    if ( this.ipSafeConfigService.queryAll().getRow(i_IPSafeConfig.getIpType() ,v_OldConfig.getIp()) != null )
                    {
                        return v_RetResp.setCode("-6").setMessage("相同安全配置已存在，请勿重复添加");
                    }
                }
            }
            
            if ( Help.isNull(i_IPSafeConfig.getUserID()) && Help.isNull(i_IPSafeConfig.getCreateUserID()) )
            {
                return v_RetResp.setCode("-101").setMessage("操作用户编号为空");
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
                
                if ( !v_User.getUserId().equals(i_IPSafeConfig.getUserID()) )
                {
                    return v_RetResp.setCode("-902").setMessage("操作用户与登录用户不一致");
                }
            }

            
            IPSafeConfig v_SaveRet = null;
            if ( Help.isNull(i_IPSafeConfig.getId()) )
            {
                v_SaveRet = this.ipSafeConfigService.insert(i_IPSafeConfig);
            }
            else
            {
                v_SaveRet = this.ipSafeConfigService.update(i_IPSafeConfig);
            }
            
            if ( v_SaveRet != null )
            {
                $Logger.info("用户（" + v_SaveRet.getCreateUserID() + "）创建" + v_SaveRet.toString() + "，成功");
                return v_RetResp.setData(v_SaveRet);
            }
            else
            {
                $Logger.error("用户（" + Help.NVL(i_IPSafeConfig.getCreateUserID() ,i_IPSafeConfig.getUserID()) + "）创建" + i_IPSafeConfig.toString() + "，异常");
                return v_RetResp.setCode("-998").setMessage("系统异常");
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("saveIPSafeConfig End: " + i_Token + ":" + i_IPSafeConfig.toString());
        }
    }
    
    
    
    /**
     * 刷新系统安全访问IP黑白名单的缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-26
     * @version     v1.0
     *
     * @param i_Token
     * @return
     */
    @RequestMapping(name="刷新安全配置" ,value="refurbishIPSafeConfig" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<IPSafeConfig> refurbishIPSafeConfig(@RequestParam(value="token" ,required=false) String i_Token
                                                           ,@RequestBody IPSafeConfig i_IPSafeConfig)
    {
        BaseResponse<IPSafeConfig> v_RetResp = new BaseResponse<IPSafeConfig>();
        int                        v_Count = 0;
        
        if ( i_IPSafeConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("refurbishIPSafeConfig Start: " + i_Token + ":" + i_IPSafeConfig.toString());
            
            if ( Help.isNull(i_IPSafeConfig.getUserID()) )
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
            
            this.ipSafeConfigService.cacheIPSafesRefurbish();
            return v_RetResp.setData(i_IPSafeConfig);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("refurbishIPSafeConfig End: "  + i_Token + ":" + i_IPSafeConfig.toString() + " 返回: " + v_Count);
        }
    }
    
}
