package org.hy.microservice.common.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.app.Param;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;





/**
 * 用户业务
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-02-04
 * @version     v1.0
 */
@Xjava
public class UserService
{
    private static final Logger $Logger    = Logger.getLogger(UserService.class);
	
    /** 登陆的Session会话ID标识，标识着是否登陆成功 */
    public  static final String $SessionID = "$XSSO$";
    
    /** 全局会话票据的前缀 */
    public  static final String $USID      = "USID";
    
    /** 本地会话票据的前缀 */
    public  static final String $SID       = "SID";
    
    
    
    @Xjava(ref="XHTTP_MS_Common_GetLoginUser")
    protected XHttp          xhGetLoginUser;
    
    /**
     * 票据有效时长（单位：秒）
     */
    @Xjava(ref="MS_Common_SessionTimeOut")
    protected Param          sessionTimeOut;
    
    
    
    /**
     * 全局会话 & 本地会话：获取默认会话最大有效时长（单位：秒）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-03-03
     * @version     v1.0
     *
     * @return
     */
    public long getMaxExpireTimeLen()
    {
        return Long.parseLong(sessionTimeOut.getValue());
    }
    
    
    
    /**
     * 本地会话：获取会话ID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-06-05
     * @version     v1.0
     *
     * @param i_Session
     * @return
     */
    public String sessionGetID(final HttpSession i_Session)
    {
        return $SID + i_Session.getId();
    }
    
    
    
    /**
     * 本地会话：获取用户数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-06-05
     * @version     v1.0
     * 
     * @param i_Session
     * @return
     */
    public UserSSO sessionGetUser(final HttpSession i_Session)
    {
        return (UserSSO)i_Session.getAttribute($SessionID);
    }
    
    
    
    /**
     * 本地会话：删除用户数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-06-05
     * @version     v1.0
     * 
     * @param i_Session
     * @return
     */
    public void sessionRemove(final HttpSession i_Session)
    {
        i_Session.removeAttribute($SessionID);
        i_Session.invalidate();
    }
    
    
    
    /**
     * 获取已登录的用户信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-02-04
     * @version     v1.0
     *
     * @param i_Token
     * @return
     */
    public UserSSO getUser(String i_Token)
    {
        Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
        v_ReqParams.put("token" ,i_Token);
        
        try
        {
            XJSON v_XJson = new XJSON();
            v_XJson.setReturnNVL(false);
            
            Return<?> v_Ret = xhGetLoginUser.request(v_ReqParams);
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                GetLoginUserRequest v_Data = (GetLoginUserRequest)v_XJson.toJava(v_Ret.getParamStr() ,GetLoginUserRequest.class);
                
                if ( v_Data != null )
                {
                    if ( BaseResponse.$Succeed.equals(v_Data.getCode()) && v_Data.getData() != null )
                    {
                        return v_Data.getData().getData();
                    }
                }
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
}
