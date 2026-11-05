package org.hy.microservice.common.probe;

import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.probe.schema.ProbeSchemaDomain;
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
 * 数据探针的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-10-12
 * @version     v1.0
 */
@Controller
@RequestMapping(value="probe" ,name="数据探针")
public class ProbeController extends BaseController
{
    
    private static final Logger $Logger = new Logger(ProbeController.class);
    
    
    
    @Autowired
    @Qualifier("ProbeService")
    private IProbeService              probeService;
    
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
    @RequestMapping(name="数据探针" ,value="data" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<ProbeResponseVO> data(@RequestParam(value="token" ,required=false) String i_Token
                                             ,@RequestBody ProbeRequestVO i_ProbeRequest)
    {
        BaseResponse<ProbeResponseVO> v_RetResp = new BaseResponse<ProbeResponseVO>();
        long                          v_Count   = 0L;
        long                          v_Total   = 0L;
        
        if ( i_ProbeRequest == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("data Start: " + i_Token + ":" + i_ProbeRequest.toString());
            
            if ( Help.isNull(i_ProbeRequest.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            ProbeRequestDomain v_ProbeRequest = i_ProbeRequest.gatDomain();
            if ( Help.isNull(v_ProbeRequest.getType()) )
            {
                return v_RetResp.setCode("-11").setMessage("数据探针的本体结构的归类为空");
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
            
            v_ProbeRequest.setType(v_ProbeRequest.getType().trim());
            if ( v_ProbeRequest.getSchema() != null )
            {
                v_ProbeRequest.getSchema().setType(  v_ProbeRequest.getType());
                v_ProbeRequest.getSchema().setUserID(v_ProbeRequest.getUserID());
                
                Return<ProbeSchemaDomain> v_SaveSchemaRet = this.probeService.saveSchema(v_ProbeRequest.getSchema());
                if ( v_SaveSchemaRet.booleanValue() )
                {
                    $Logger.info("保存数据探针的本体结构成功：" + v_SaveSchemaRet.getParamObj().getComment());
                }
            }
            
            return v_RetResp.setData((ProbeResponseVO) null).setDataCount(v_Count).setTotalCount(v_Total);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("data End: "  + i_Token + ":" + i_ProbeRequest.toString() + " 返回: " + v_Count);
        }
    }
    
}
