package org.hy.microservice.common.user;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.app.Param;
import org.hy.common.license.AppKey;
import org.hy.common.license.Signaturer;
import org.hy.common.license.sign.ISignaturer;
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
    private static final Logger $Logger      = Logger.getLogger(UserService.class);
	
    /** 登陆的Session会话ID标识，标识着是否登陆成功 */
    public  static final String $SessionID   = "$XSSO$";
    
    /** 全局会话票据的前缀 */
    public  static final String $USID        = "USID";
    
    /** 本地会话票据的前缀 */
    public  static final String $SID         = "SID";
    
    
    /** 当前的票据 */
    protected static TokenInfo  $Token       = null;
    
    /** 获取票据的时间 */
    protected static long       $TokenTime   = 0L;
    
    /** 获取票据的过期时长（单位：秒） */
    protected static int        $TokenExpire = 0;
    
    
    
    @Xjava(ref="XHTTP_MS_Common_GetLoginUser")
    protected XHttp          xhGetLoginUser;
	
	@Xjava(ref="XHTTP_MS_Common_GetAccessToken")
    protected XHttp          xhGetAccessToken;
    
    @Xjava(ref="XHTTP_MS_Common_SetLoginUser")
    protected XHttp          xhSetLoginUser;
	
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
        return sessionTimeOut.getValueLong();
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
    
    
    
    /**
     * 获取访问Token（带缓存、加同步锁）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-07-05
     * @version     v1.0
     * 
     * @param i_AppKey   应用编号
     * @param i_UserSSO  应用接口访问者的信息
     *
     * @return
     */
    public synchronized String getAccessToken(AppKey i_AppKey ,UserSSO i_UserSSO)
    {
        long v_Timestamp = Date.getNowTime().getTime();
        if ( $TokenTime + $TokenExpire * 1000 > v_Timestamp )
        {
            return $Token.getAccessToken();
        }
        
        TokenInfo v_Token = this.getCode(i_AppKey);
        if ( v_Token != null )
        {
            $TokenTime   = Date.getNowTime().getTime();
            $TokenExpire = v_Token.getExpire() - 10;    // 为容错而减10秒
            $Token       = v_Token;
            
            String v_Code = v_Token.getCode();
            v_Token.setCode(null);                      // 应用系统的接口级访问，仅登录一次即可
            
            this.loginUser(v_Code ,i_AppKey ,i_UserSSO);
            return v_Token.getAccessToken();
        }
        else
        {
            return null;
        }
    }
    
    
    
    /**
     * 获取登录临时Code
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-15
     * @version     v1.0
     *
     * @param i_AppKey  应用编号
     *
     * @return
     */
    public TokenInfo getCode(AppKey i_AppKey)
    {
        $Logger.debug("获取Token Starting...");
        
        try
        {
            long        v_Timestamp = Date.getNowTime().getTime();
            ISignaturer v_Sign      = new Signaturer(i_AppKey.getPrivateKey());
            String      v_Signature = v_Sign.sign("appKey" + i_AppKey.getAppKey() + "timestamp" + v_Timestamp);
            Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
            v_ReqParams.put("appKey"    ,i_AppKey.getAppKey());
            v_ReqParams.put("timestamp" ,v_Timestamp);
            v_ReqParams.put("signature" ,URLEncoder.encode(v_Signature ,"UTF-8"));
            
            // $Logger.error("集成认证登录：" + appKey.getAppKey() + " - " + v_Timestamp + " - " + v_Signature);
            
            Return<?> v_Ret = xhGetAccessToken.request(v_ReqParams);
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                XJSON v_XJson = new XJSON();
                
                TokenResponse v_Data = (TokenResponse)v_XJson.toJava(v_Ret.getParamStr() ,TokenResponse.class);
                
                if ( v_Data != null )
                {
                    if ( BaseResponse.$Succeed.equals(v_Data.getCode()) && v_Data.getData() != null && v_Data.getData().getData() != null )
                    {
                        TokenInfo v_Token = v_Data.getData().getData();
                        $Logger.info("获取Token：" + v_Data.getCode() + " : " + v_Token.getAccessToken() + " ,过期时长：" + v_Token.getExpire());
                        return v_Token;
                    }
                    else
                    {
                        $Logger.error("获取Token异常：" + v_Data.getCode() + " - " + v_Data.getMessage());
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
    
    
    
    /**
     * 用户登录
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-15
     * @version     v1.0
     *
     * @param i_Code    临时Code
     * @param i_AppKey  应用编号
     * @param i_UserSSO
     */
    public TokenInfo loginUser(String i_Code ,AppKey i_AppKey ,UserSSO i_UserSSO)
    {
        i_UserSSO.setAppKey(i_AppKey.getAppKey());
        
        Map<String ,Object> v_ReqParams = new HashMap<String ,Object>();
        v_ReqParams.put("code" ,i_Code);
        
        try
        {
            XJSON v_XJson = new XJSON();
            v_XJson.setReturnNVL(false);
            
            Return<?> v_Ret = xhSetLoginUser.request(v_ReqParams ,v_XJson.toJson(i_UserSSO).toJSONString());
            
            if ( v_Ret != null && v_Ret.booleanValue() && !Help.isNull(v_Ret.getParamStr()) )
            {
                TokenResponse v_Data = (TokenResponse)v_XJson.toJava(v_Ret.getParamStr() ,TokenResponse.class);
                
                if ( v_Data != null )
                {
                    if ( BaseResponse.$Succeed.equals(v_Data.getCode()) )
                    {
                        return v_Data.getData().getData();
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
        
        return null;
    }
    
}
