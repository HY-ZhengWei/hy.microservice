package org.hy.microservice.common.operationLog;

import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.ProjectStartBase;
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
 *              v2.0  2025-09-02  添加：分页查询
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
    @RequestMapping(name="查询系统操作日志" ,value="queryOperationLog" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<OperationLog> queryOperationLog(@RequestParam(value="token" ,required=false) String i_Token
                                                       ,@RequestBody OperationLog i_OperationLog)
    {
        BaseResponse<OperationLog> v_RetResp = new BaseResponse<OperationLog>();
        long                       v_Count   = 0L;
        long                       v_Total   = 0L;
        
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
            
            List<OperationLog> v_DataList = this.operationLogService.queryListByPage(i_OperationLog);
            v_Count = v_DataList.size();
            v_Total = this.operationLogService.queryCount(i_OperationLog);
            return v_RetResp.setData(v_DataList).setDataCount(v_Count).setTotalCount(v_Total);
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
    @RequestMapping(name="查询系统模块" ,value="queryModule" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<OperationLogModule> queryModule(@RequestParam(value="token" ,required=false) String i_Token
                                                       ,@RequestBody OperationLog i_OperationLog)
    {
        BaseResponse<OperationLogModule> v_RetResp = new BaseResponse<OperationLogModule>();
        long                             v_Count   = 0L;
        
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
            
            v_Count = ProjectStartBase.$RequestMappingModules.size();
            return v_RetResp.setData(Help.toList(ProjectStartBase.$RequestMappingModules)).setDataCount(v_Count);
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
    @RequestMapping(name="查询系统接口" ,value="queryApi" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<OperationLogApi> queryApi(@RequestParam(value="token" ,required=false) String i_Token
                                                 ,@RequestBody OperationLog i_OperationLog)
    {
        BaseResponse<OperationLogApi> v_RetResp = new BaseResponse<OperationLogApi>();
        long                          v_Count   = 0;
        
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
                v_Count = ProjectStartBase.$RequestMappingMethods.size();
                return v_RetResp.setData(Help.toList(ProjectStartBase.$RequestMappingMethods));
            }
            else
            {
                Map<String ,OperationLogApi> v_Apis = ProjectStartBase.$RequestMappingMethods.get(i_OperationLog.getModuleCode());
                if ( !Help.isNull(v_Apis) )
                {
                    v_Count = ProjectStartBase.$RequestMappingMethods.get(i_OperationLog.getModuleCode()).size();
                    return v_RetResp.setData(Help.toList(ProjectStartBase.$RequestMappingMethods.get(i_OperationLog.getModuleCode()))).setDataCount(v_Count);
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
