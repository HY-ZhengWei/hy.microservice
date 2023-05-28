package org.hy.microservice.common.operationLog;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.ProjectStart;
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
 * 系统操作日志的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-11
 * @version     v1.0
 */
@Controller
@RequestMapping(value="operationLog" ,name="操作日志")
public class OperationLogController extends BaseController
{
    
    private static final Logger $Logger = new Logger(OperationLogController.class);
    
    
    
    @Autowired
    @Qualifier("OperationLogService")
    private IOperationLogService       operationLogService;
    
    @Autowired
    @Qualifier("UserService")
    private UserService                userService;
    
    @Autowired
    @Qualifier("MS_Common_IsCheckToken")
    private Param                      isCheckToken;
    
    
    
    /**
     * 查询系统操作日志
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-04-11
     * @version     v1.0
     *
     * @param i_Token
     * @return
     */
    @RequestMapping(name="查询系统操作日志" ,value="queryOperationLog" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<OperationLog> queryOperationLog(@RequestParam(value="token" ,required=false) String i_Token
                                                       ,@RequestBody OperationLog i_OperationLog)
    {
        BaseResponse<OperationLog> v_RetResp = new BaseResponse<OperationLog>();
        int                        v_Count = 0;
        
        if ( i_OperationLog == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("queryOperationLog Start: " + i_Token + ":" + i_OperationLog.toString());
            
            if ( Help.isNull(i_OperationLog.getUserID()) )
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
            
            List<OperationLog> v_DataList = this.operationLogService.queryList(i_OperationLog);
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
            $Logger.info("queryOperationLog End: "  + i_Token + ":" + i_OperationLog.toString() + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 查询系统模块列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-04-12
     * @version     v1.0
     *
     * @param i_Token
     * @return
     */
    @RequestMapping(name="查询系统模块" ,value="queryModule" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<OperationLogModule> queryModule(@RequestParam(value="token" ,required=false) String i_Token
                                                       ,@RequestBody OperationLog i_OperationLog)
    {
        BaseResponse<OperationLogModule> v_RetResp = new BaseResponse<OperationLogModule>();
        int                              v_Count = 0;
        
        if ( i_OperationLog == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("queryModule Start: " + i_Token + ":" + i_OperationLog.toString());
            
            if ( Help.isNull(i_OperationLog.getUserID()) )
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
            
            v_Count = ProjectStart.$RequestMappingModules.size();
            return v_RetResp.setData(Help.toList(ProjectStart.$RequestMappingModules));
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("queryModule End: "  + i_Token + ":" + i_OperationLog.toString() + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 查询系统接口列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-04-12
     * @version     v1.0
     *
     * @param i_Token
     * @return
     */
    @RequestMapping(name="查询系统接口" ,value="queryApi" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<OperationLogApi> queryApi(@RequestParam(value="token" ,required=false) String i_Token
                                                 ,@RequestBody OperationLog i_OperationLog)
    {
        BaseResponse<OperationLogApi> v_RetResp = new BaseResponse<OperationLogApi>();
        int                           v_Count = 0;
        
        if ( i_OperationLog == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("queryApi Start: " + i_Token + ":" + i_OperationLog.toString());
            
            if ( Help.isNull(i_OperationLog.getUserID()) )
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
            
            if ( Help.isNull(i_OperationLog.getModuleCode()) )
            {
                v_Count = ProjectStart.$RequestMappingMethods.size();
                return v_RetResp.setData(Help.toList(ProjectStart.$RequestMappingMethods));
            }
            else
            {
                List<OperationLogApi> v_Apis = ProjectStart.$RequestMappingMethods.get(i_OperationLog.getModuleCode());
                if ( !Help.isNull(v_Apis) )
                {
                    v_Count = ProjectStart.$RequestMappingMethods.get(i_OperationLog.getModuleCode()).size();
                    return v_RetResp.setData(ProjectStart.$RequestMappingMethods.get(i_OperationLog.getModuleCode()));
                }
                else
                {
                    return v_RetResp.setCode("-902").setMessage("未知的模块类型：" + i_OperationLog.getModuleCode());
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
            $Logger.info("queryApi End: "  + i_Token + ":" + i_OperationLog.toString() + " 返回: " + v_Count);
        }
    }
    
}
