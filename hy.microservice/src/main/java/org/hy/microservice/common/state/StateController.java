package org.hy.microservice.common.state;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.BaseViewMode;
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
 * 状态机的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-07-15
 * @version     v1.0
 */
@Controller
@RequestMapping(value="state" ,name="状态模型")
public class StateController extends BaseController
{
    
    private static final Logger $Logger = new Logger(StateController.class);
    
    
    
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
     * @createDate  2024-07-15
     * @version     v1.0
     *
     * @param i_Token
     * @param i_ServiceType
     * @return
     */
    @RequestMapping(name="查询状态机" ,value="queryStateMachine" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<StateMachineCellVO> queryStateMachine(@RequestParam(value="token" ,required=false) String i_Token
                                                             ,@RequestBody BaseViewMode i_ServiceType)
    {
        BaseResponse<StateMachineCellVO> v_RetResp = new BaseResponse<StateMachineCellVO>();
        long                             v_Count   = 0L;
        
        if ( i_ServiceType == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("queryStateMachine Start: " + i_Token + ":" + i_ServiceType.toString());
            
            if ( Help.isNull(i_ServiceType.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            if ( Help.isNull(i_ServiceType.getServiceType()) )
            {
                return v_RetResp.setCode("-3").setMessage("业务类型为空");
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
            
            StateMachineCell v_Data = StateMachineManager.getStateMachineCell(i_ServiceType.getServiceType());
            v_Count = 1;
            return v_RetResp.setData(new StateMachineCellVO(v_Data)).setDataCount(v_Count);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("queryStateMachine End: "  + i_Token + ":" + i_ServiceType.toString() + " 返回: " + v_Count);
        }
    }
    
    
}
