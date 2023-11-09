package org.hy.microservice.common;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.TimeGroupTotal;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.XJava;
import org.hy.common.xml.event.XRequestListener;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.AppInterfaces;
import org.hy.common.xml.plugins.AppMessage;
import org.hy.common.xml.plugins.XSQLFilter;
import org.hy.microservice.common.ipSafe.IIPSafeConfigService;
import org.hy.microservice.common.ipSafe.IPSafeConfig;
import org.hy.microservice.common.operationLog.IOperationLogService;
import org.hy.microservice.common.operationLog.OperationLog;





/**
 * 启用Filter需要在Springboot的启动类加@ServletComponentScan
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-10
 * @version     v1.0
 *              v2.0  2023-08-09  添加：接口模块编号和URL地址，允许更细粒度的控制黑白名单
 *                                添加：黑白名单的命中率功能，提高黑白名单的判定性能
 *              v3.0  2023-08-11  添加：延时单线程队列周期性的处理日志的持久化
 *              v4.0  2023-08-16  添加：Web请求接口 @XRequest 的事件监监听器接口，支持它接口的日志、黑白名单等功能
 *                                修正：小概率未记录 "访问量达到上限" 的日志
 *              v5.0  2023-09-01  添加：没有配置 @RequestMapping(name) 的方法不记录访问日志
 */
@WebFilter(filterName="logFilter" ,urlPatterns="/*" ,initParams={
        @WebInitParam(name="exclusions" ,value="*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.swf")
       ,@WebInitParam(name="cachesize"  ,value="1000")
       ,@WebInitParam(name="timeout"    ,value="60")
})
public class LogFilter extends XSQLFilter implements XRequestListener
{
    private static final Logger                      $Logger            = new Logger(LogFilter.class);
    
    /** 接口的时间分组统计（每分钟） */
    private static final Map<String ,TimeGroupTotal> $APITotalMinute    = new HashMap<String ,TimeGroupTotal>();
    
    /** 接口的时间分组统计（每10分钟） */
    private static final Map<String ,TimeGroupTotal> $APITotalMinute10  = new HashMap<String ,TimeGroupTotal>();
    
    /** 字符集 */
    public  static       String                      $CharacterEncoding = "UTF-8";
    
    
    
    private IIPSafeConfigService ipSafeConfigService;
    
    private IOperationLogService operationLogService;
    
    private long                 apiUseMaxCountMinute;
    
    private long                 apiUseMaxCountMinute10;
    
    private String               systemCode;
    
    
    
    public LogFilter()
    {
        this.ipSafeConfigService    = (IIPSafeConfigService) XJava.getObject("IPSafeConfigService");
        this.operationLogService    = (IOperationLogService) XJava.getObject("OperationLogService");
        this.apiUseMaxCountMinute   = Long.valueOf(XJava.getParam("MS_Common_ApiUseMaxCountMinute").getValue());
        this.apiUseMaxCountMinute10 = Long.valueOf(XJava.getParam("MS_Common_ApiUseMaxCountMinute10").getValue());
        this.systemCode             = XJava.getParam("MS_Common_ServiceName").getValue();
        
        AppInterfaces.setListener(this);
    }
    
    
    
    /**
     * 统计接口访问的分时数量，及判定接口访问量是否超过阈值（每分钟）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_APIUrl
     * @return
     */
    private synchronized boolean allowUseAPIMinute(String i_APIUrl)
    {
        TimeGroupTotal v_APITimeTotal = $APITotalMinute.get(i_APIUrl);
        Date           v_Now          = Date.getNowTime();
        
        if ( v_APITimeTotal == null )
        {
            v_APITimeTotal = new TimeGroupTotal(1);
            v_APITimeTotal.setMaxSize(60);
            $APITotalMinute.put(i_APIUrl ,v_APITimeTotal);
        }
        else
        {
            Long v_TimeCount = v_APITimeTotal.get(v_Now);
            if ( v_TimeCount != null )
            {
                if ( v_TimeCount > this.apiUseMaxCountMinute )
                {
                    return false;
                }
            }
        }
        
        v_APITimeTotal.put(v_Now);
        return true;
    }
    
    
    
    /**
     * 统计接口访问的分时数量，及判定接口访问量是否超过阈值（每10分钟）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-23
     * @version     v1.0
     *
     * @param i_APIUrl
     * @return
     */
    private synchronized boolean allowUseAPIMinute10(String i_APIUrl)
    {
        TimeGroupTotal v_APITimeTotal = $APITotalMinute10.get(i_APIUrl);
        Date           v_Now          = Date.getNowTime();
        
        if ( v_APITimeTotal == null )
        {
            v_APITimeTotal = new TimeGroupTotal(15);
            v_APITimeTotal.setMaxSize(8);
            $APITotalMinute10.put(i_APIUrl ,v_APITimeTotal);
        }
        else
        {
            Long v_TimeCount = v_APITimeTotal.get(v_Now);
            if ( v_TimeCount != null )
            {
                if ( v_TimeCount > this.apiUseMaxCountMinute10 )
                {
                    return false;
                }
            }
        }
        
        v_APITimeTotal.put(v_Now);
        return true;
    }
    
    
    
    /**
     * 是否存在白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-27
     * @version     v1.0
     *
     * @return
     */
    private boolean haveWhiteList()
    {
        Map<String ,IPSafeConfig> v_IPMap = this.ipSafeConfigService.queryAll().get(IPSafeConfig.$Type_WhiteList);
        
        return !Help.isNull(v_IPMap);
    }
    
    
    
    /**
     * 指定IP是否为黑名单或白名单
     * 
     * IP地址支持：IP段
     * 黑白名单判定优先级：黑名单 > 白名单 > 接口URL > 接口模块 > IP地址
     * 
     *   举例1：当配置IP 127.0.0.1 的 "接口URL /A/B/C" 为配置黑名单时，无论是否有白名单，均拒绝IP 127.0.0.1的访问 "接口/A/B/C
     *   举例2：当配置IP 127.0.0.1 的 "接口URL /A/B/C" 为白名单，且无其它黑名单时，"接口/A/B/C" 仅允许此IP 127.0.0.1访问，其它IP无权访问
     *   举例3：当 "接口URL /A/B/C" 未配置白名单，且无黑名单时，允许任何系统访问
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-27
     * @version     v1.0
     *
     * @param i_IPType  IP类型
     * @param io_OLog   操作日志
     * @return
     */
    private boolean isbackWhiteList(String i_IPType ,OperationLog io_OLog)
    {
        Map<String ,IPSafeConfig> v_IPMap = this.ipSafeConfigService.queryAll().get(i_IPType);
        
        if ( Help.isNull(v_IPMap) )
        {
            return false;
        }
        
        List<String> v_IPs   = new ArrayList<String>();
        String []    v_IPArr = io_OLog.getUserIP().split("\\.");   // IP地址支持：IP段
        
        v_IPs.add(io_OLog.getUserIP());
        if ( v_IPArr.length >= 4 )
        {
            v_IPs.add(v_IPArr[0] + "." + v_IPArr[1] + "." + v_IPArr[2] + ".");
            v_IPs.add(v_IPArr[0] + "." + v_IPArr[1] + ".");
            v_IPs.add(v_IPArr[0] + ".");
        }
        
        String       v_FindKey      = "";
        IPSafeConfig v_IPSafeConfig = null;
        for (String v_IP : v_IPs)
        {
            // 接口URL的判定
            v_FindKey      = v_IP + "@" + io_OLog.getModuleCode() + "@" + io_OLog.getUrl();
            v_IPSafeConfig = v_IPMap.get(v_FindKey);
            if ( v_IPSafeConfig != null )
            {
                this.ipSafeConfigService.putIPSafeHit(io_OLog.getIpSafeKey() ,i_IPType);
                return true;
            }
            
            // 接口模块的判定
            v_FindKey      = v_IP + "@" + io_OLog.getModuleCode();
            v_IPSafeConfig = v_IPMap.get(v_FindKey);
            if ( v_IPSafeConfig != null )
            {
                this.ipSafeConfigService.putIPSafeHit(io_OLog.getIpSafeKey() ,i_IPType);
                return true;
            }
            
            // IP地址的判定
            v_FindKey      = v_IP;
            v_IPSafeConfig = v_IPMap.get(v_FindKey);
            if ( v_IPSafeConfig != null )
            {
                this.ipSafeConfigService.putIPSafeHit(io_OLog.getIpSafeKey() ,i_IPType);
                return true;
            }
        }
        
        return false;
    }
    
    
    
    /**
     * 黑白名单检查（支持前缀模糊匹配）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-27
     * @version     v1.0
     *
     * @param io_OLog
     */
    private OperationLog backWhiteCheck(OperationLog io_OLog)
    {
        // 内存命中
        String v_IPSafeHit = this.ipSafeConfigService.getIPSafeHit(io_OLog.getIpSafeKey());
        
        // 黑名单查询
        if ( IPSafeConfig.$Type_BackList.equals(v_IPSafeHit)
          || isbackWhiteList(IPSafeConfig.$Type_BackList ,io_OLog) )
        {
            io_OLog.setAttackType(IPSafeConfig.$Type_BackList);
            io_OLog.setUrlResponse("{\"code\": \"-891\", \"message\": \"" + IPSafeConfig.$Type_BackList + "\"}");
            io_OLog.setResultCode("-891");
            io_OLog.setResponseTime(Date.getNowTime().getTime());
            io_OLog.setTimeLen(io_OLog.getResponseTime() - io_OLog.getRequestTime());
        }
        else
        {
            // 有白名单时，才启用
            if ( this.haveWhiteList() )
            {
                // 白名单查询
                if ( IPSafeConfig.$Type_WhiteList.equals(v_IPSafeHit)
                  || isbackWhiteList(IPSafeConfig.$Type_WhiteList ,io_OLog) )
                {
                    io_OLog.setAttackType(IPSafeConfig.$Type_WhiteList);
                }
                else
                {
                    io_OLog.setAttackType("Not on the " + IPSafeConfig.$Type_WhiteList);
                    io_OLog.setUrlResponse("{\"code\": \"-892\", \"message\": \"Not on the " + IPSafeConfig.$Type_WhiteList + "\"}");
                    io_OLog.setResultCode("-892");
                    io_OLog.setResponseTime(Date.getNowTime().getTime());
                    io_OLog.setTimeLen(io_OLog.getResponseTime() - io_OLog.getRequestTime());
                }
            }
        }
        
        return io_OLog;
    }
    
    

    @Override
    public void doFilter(ServletRequest i_ServletRequest ,ServletResponse i_ServletResponse ,FilterChain i_FilterChain) throws IOException ,ServletException
    {
        String    v_Url  = ((HttpServletRequest) i_ServletRequest).getServletPath();
        String [] v_Urls = v_Url.split("/");
        
        // 分析中心、静态资源，不记录访问日志
        if ( StringHelp.isContains(v_Url ,"analyse" ,".") || v_Urls.length < 3 )
        {
            i_ServletResponse.setCharacterEncoding($CharacterEncoding);
            i_FilterChain.doFilter(i_ServletRequest ,i_ServletResponse);
            return;
        }
        
        // 没有配置 @RequestMapping(name) 的方法不记录访问日志
        if ( ProjectStartBase.$RequestMappingMethods.getRow(v_Urls[1] ,v_Url) == null )
        {
            i_FilterChain.doFilter(i_ServletRequest ,i_ServletResponse);
            return;
        }
        
        LogHttpServletRequestWrapper v_Request = new LogHttpServletRequestWrapper((HttpServletRequest) i_ServletRequest);
        OperationLog                 v_OLog    = new OperationLog();
        
        try
        {
            // 解释用户账号信息。有Sesssion时，可直接从会话信息中取登录用户的信息
            if ( !Help.isNull(v_Request.getBodyString()) )
            {
                XJSON        v_XJson = new XJSON();
                BaseViewMode v_BMode = (BaseViewMode) v_XJson.toJava(v_Request.getBodyString() ,BaseViewMode.class);
                v_OLog.setUserID(Help.NVL(v_BMode.getCreateUserID() ,v_BMode.getUserID()));
            }
            else
            {
                v_OLog.setUserID("");
            }
            
            v_OLog.setCreateTime(new Date());
            v_OLog.setId(StringHelp.getUUID());
            v_OLog.setUrl(v_Url);
            v_OLog.setUrlRequest(v_Request.getQueryString());
            v_OLog.setUrlRequestBody(v_Request.getBodyString());
            v_OLog.setUserIP(getIpAddress(v_Request));
            v_OLog.setSystemCode(this.systemCode);
            v_OLog.setModuleCode(v_Urls[1]);
            
            this.backWhiteCheck(v_OLog);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        ServletOutputStream           v_Output   = null;
        LogHttpServletResponseWrapper v_Response = new LogHttpServletResponseWrapper((HttpServletResponse) i_ServletResponse);
        
        // 阻止访问
        if ( !Help.isNull(v_OLog.getUrlResponse()) )
        {
            i_ServletResponse.setCharacterEncoding($CharacterEncoding);
            i_ServletResponse.setContentType("application/json");
            v_Output = i_ServletResponse.getOutputStream();
            v_Output.write(v_OLog.getUrlResponse().getBytes());
            this.operationLogService.insert(v_OLog);
            return;
        }
        // 访问量达到上限
        else if ( !this.allowUseAPIMinute(v_OLog.getUrl()) )
        {
            v_OLog.setUrlResponse("访问量达到上限");
            v_OLog.setAttackType("ApiUseMaxCountMinute");
            i_ServletResponse.setCharacterEncoding($CharacterEncoding);
            i_ServletResponse.setContentType("application/json");
            v_Output = i_ServletResponse.getOutputStream();
            v_Output.write(v_OLog.getUrlResponse().getBytes());
            this.operationLogService.insert(v_OLog);
            return;
        }
        // 访问量达到上限（10分钟）
        else if ( !this.allowUseAPIMinute10(v_OLog.getUrl()) )
        {
            v_OLog.setUrlResponse("访问量达到10分钟上限");
            v_OLog.setAttackType("ApiUseMaxCountMinute10");
            i_ServletResponse.setCharacterEncoding($CharacterEncoding);
            i_ServletResponse.setContentType("application/json");
            v_Output = i_ServletResponse.getOutputStream();
            v_Output.write(v_OLog.getUrlResponse().getBytes());
            this.operationLogService.insert(v_OLog);
            return;
        }
        
        this.operationLogService.insert(v_OLog);
        
        super.doFilter(v_Request ,v_Response ,i_FilterChain);
        // 原始的方式 i_FilterChain.doFilter(v_Request ,v_Response);
        
        try
        {
            byte [] v_ResponseBody = v_Response.getResponseData();
            v_OLog.setUrlResponse(new String(v_ResponseBody ,$CharacterEncoding));
            v_OLog.setResponseTime(Date.getNowTime().getTime());
            v_OLog.setTimeLen(v_OLog.getResponseTime() - v_OLog.getRequestTime());
            
            if ( !Help.isNull(v_OLog.getUrlResponse()) )
            {
                try
                {
                    XJSON           v_XJson = new XJSON();
                    BaseResponse<?> v_BResp = (BaseResponse<?>) v_XJson.toJava(v_OLog.getUrlResponse() ,BaseResponse.class);
                    v_OLog.setResultCode(v_BResp.getCode());
                }
                catch (Exception exce)
                {
                    // 非Json格式的响应报文，默认为成功
                    v_OLog.setResultCode(BaseResponse.$Succeed);
                }
                
                if ( v_OLog.getUrlResponse().length() >= 4000 )
                {
                    v_OLog.setUrlResponse(v_OLog.getUrlResponse().substring(0 ,3999));
                }
            }
            
            this.operationLogService.update(v_OLog);
            
            v_Output = i_ServletResponse.getOutputStream();
            v_Output.write(v_ResponseBody);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        finally
        {
            if ( v_Output != null )
            {
                v_Output.flush();
                v_Output.close();
            }
            v_Output = null;
        }
    }
    

	
	/**
     * 在执行前一刻被触发。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-16
     * @version     v1.0
     *
     * @param i_Request      请求对象
     * @param i_Response     响应对象
     * @param io_RequestData 请求数据
     * @param i_Message      请求数据的原始报文
     * @return               当返回 false 时，中断 "执行"。
     *                       当返回 true  时，执行接口调用
     */
    @Override
    public Return<Object> before(HttpServletRequest  i_Request
                                ,HttpServletResponse i_Response
                                ,AppMessage<?>       io_RequestData
                                ,String              i_Message)
    {
        Return<Object> v_Ret    = new Return<Object>(true);
        OperationLog   v_OLog   = new OperationLog();
        String         v_UserID = Help.NVL(i_Request.getParameter("userID") ,i_Request.getParameter("createUserID"));
        
        v_OLog.setCreateTime(new Date());
        v_OLog.setId(StringHelp.getUUID());
        v_OLog.setUrl("/app/" + io_RequestData.getSid());
        v_OLog.setUrlRequest(i_Request.getQueryString());
        v_OLog.setUrlRequestBody(i_Message);
        v_OLog.setUserIP(getIpAddress(i_Request));
        v_OLog.setSystemCode(this.systemCode);
        v_OLog.setModuleCode("/app");
        v_OLog.setUserID(Help.NVL(v_UserID ,Help.NVL(io_RequestData.getSession())));
        
        this.backWhiteCheck(v_OLog);
        
        // 阻止访问
        if ( !Help.isNull(v_OLog.getUrlResponse()) )
        {
            v_Ret.set(false).setParamStr(v_OLog.getUrlResponse());
        }
        // 访问量达到上限
        else if ( !this.allowUseAPIMinute(v_OLog.getUrl()) )
        {
            v_OLog.setUrlResponse("访问量达到上限");
            v_OLog.setAttackType("ApiUseMaxCountMinute");
            v_Ret.set(false).setParamStr(v_OLog.getUrlResponse());
        }
        // 访问量达到上限（10分钟）
        else if ( !this.allowUseAPIMinute10(v_OLog.getUrl()) )
        {
            v_OLog.setUrlResponse("访问量达到10分钟上限");
            v_OLog.setAttackType("ApiUseMaxCountMinute10");
            v_Ret.set(false).setParamStr(v_OLog.getUrlResponse());
        }
        
        this.operationLogService.insert(v_OLog);
        return v_Ret.setParamObj(v_OLog);
    }
    
    
    
    /**
     * 执行成功后被触发。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-16
     * @version     v1.0
     *
     * @param i_RequestData   请求数据
     * @param i_ResponseData  响应数据
     * @param i_Other         用户自定义的数据
     */
    @Override
    public void succeed(AppMessage<?> i_RequestData ,AppMessage<?> i_ResponseData ,Object i_Other)
    {
        OperationLog v_OLog = (OperationLog)i_Other;
        
        if ( i_ResponseData != null )
        {
            try
            {
                v_OLog.setUrlResponse(i_ResponseData.toString());
            }
            catch (Exception exce)
            {
                v_OLog.setUrlResponse(exce.toString());
                $Logger.warn(exce);
            }
        }
        else
        {
            v_OLog.setUrlResponse("");
        }
        v_OLog.setResponseTime(Date.getNowTime().getTime());
        v_OLog.setTimeLen(v_OLog.getResponseTime() - v_OLog.getRequestTime());
        v_OLog.setResultCode(BaseResponse.$Succeed);
        this.operationLogService.update(v_OLog);
    }
    
    
    
    /**
     * 执行异常后被触发。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-16
     * @version     v1.0
     *
     * @param i_RequestData   请求数据
     * @param i_Exception     异常数据
     * @param i_Other         用户自定义的数据
     */
    @Override
    public void fail(AppMessage<?> i_RequestData ,Exception i_Exception ,Object i_Other)
    {
        OperationLog v_OLog = (OperationLog)i_Other;
        
        if ( i_Exception != null )
        {
            v_OLog.setUrlResponse(i_Exception.toString());
        }
        else
        {
            v_OLog.setUrlResponse("");
        }
        v_OLog.setResponseTime(Date.getNowTime().getTime());
        v_OLog.setTimeLen(v_OLog.getResponseTime() - v_OLog.getRequestTime());
        v_OLog.setResultCode(i_RequestData.getRc());
        this.operationLogService.update(v_OLog);
    }



    /**
     * 获取用户请求的公网IP
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-04-10
     * @version     v1.0
     *
     * @param i_Request
     * @return
     */
    private String getIpAddress(HttpServletRequest i_Request)
    {
        String ip = i_Request.getHeader("x-forwarded-for");
        if ( ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip) )
        {
            ip = i_Request.getHeader("Proxy-Client-IP");
        }
        if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) )
        {
            ip = i_Request.getHeader("WL-Proxy-Client-IP");
        }
        if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) )
        {
            ip = i_Request.getRemoteAddr();
            if ( ip.equals("127.0.0.1") )
            {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try
                {
                    inet = InetAddress.getLocalHost();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if ( ip != null && ip.length() > 15 )
        {
            if ( ip.indexOf(",") > 0 )
            {
                ip = ip.substring(0 ,ip.indexOf(","));
            }
        }
        return ip;
    }
    
}
