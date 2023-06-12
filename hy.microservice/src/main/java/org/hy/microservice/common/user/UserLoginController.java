package org.hy.microservice.common.user;

import java.util.Map;

import org.hy.common.Help;
import org.hy.common.license.AppKey;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.BaseViewMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;





/**
 * 用户中心：登录的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-08
 * @version     v1.0
 */
@Controller
@RestController
@RequestMapping(value="userToken" ,name="用户登录")
public class UserLoginController extends BaseController
{
    private static final Logger $Logger = new Logger(UserLoginController.class);
    
    
    
    @Autowired
    @Qualifier("MS_Common_AppKeys")
    private Map<String ,AppKey> appKeys;
    
    @Autowired
    @Qualifier("UserService")
    private UserService         userService;
    
    
    
    /**
     * 获取登录临时Code
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-15
     * @version     v1.0
     * 
     * @param i_AppKey   应用编号
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(name="获取临时票据" ,value="code" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<String> code(@RequestBody BaseViewMode i_Datas)
    {
        BaseResponse<String> v_Ret = new BaseResponse<String>();
        
        if ( i_Datas == null || Help.isNull(i_Datas.getAppKey()) )
        {
            $Logger.info("登录临时Code：应用编号为空");
            return v_Ret.setCode("-1").setMessage("登录临时Code：应用编号为空");
        }
        
        if ( Help.isNull(i_Datas.getUserID()) )
        {
            return v_Ret.setCode("-2").setMessage("登录临时Code：用户编号为空");
        }
        
        AppKey v_AppKey = this.appKeys.get(i_Datas.getAppKey());
        if ( v_AppKey == null )
        {
            $Logger.info("登录临时Code：应用编号无效");
            return v_Ret.setCode("-3").setMessage("登录临时Code：应用编号无效");
        }
        
        TokenInfo v_Token = this.userService.getCode(v_AppKey);
        if ( v_Token == null )
        {
            $Logger.info("登录临时Code：服务异常");
            return v_Ret.setCode("-100").setMessage("登录临时Code：服务异常");
        }
        
        return v_Ret.setData(v_Token.getCode());
    }
    
}
