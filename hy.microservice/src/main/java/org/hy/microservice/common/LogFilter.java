package org.hy.microservice.common;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.TimeGroupTotal;
import org.hy.common.app.Param;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.XJava;
import org.hy.common.xml.event.XRequestListener;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.AppInterfaces;
import org.hy.common.xml.plugins.AppMessage;
import org.hy.common.xml.plugins.XSQLFilter;

import org.hy.microservice.common.ipSafe.IIPSafeConfigService;
import org.hy.microservice.common.ipSafe.IPSafeConfig;
import org.hy.microservice.common.openapi.OpenApiConfig;
import org.hy.microservice.common.operationLog.IOperationLogService;
import org.hy.microservice.common.operationLog.OperationLog;
import org.hy.microservice.common.operationLog.OperationLogApi;





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
 *              v6.0  2023-11-30  添加：接口的个性化 "访问量达到上限" 的限制
 *              v7.0  2025-09-09  添加：允许哪些国家、地区访问的限制
 *              v7.1  2025-10-29  修正：WebSocket协议本身没有HTTP中的ContentType字段。发现人：王雨墨
 *              v8.0  2026-01-27  添加：日志记录请求头中的信息
 *              v9.0  2026-01-29  添加：日志记录幂等请求ID与用户可定制化的数据
 */
@WebFilter(filterName="logFilter" ,urlPatterns="/*" ,initParams={
        @WebInitParam(name="exclusions" ,value="*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.swf,*.txt,*.log,*.xml,*.md")
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
    
    /** 属性名称：客户端IP地址 */
    public  static final String                      $ATTR_UserIP       = "MS_Common_UserIP";
    
    
    
    private IIPSafeConfigService       ipSafeConfigService;
    
    private IOperationLogService       operationLogService;
    
    /** 每个API接口每分钟最大允许的请求量（全体接口的默认值） */
    private long                       apiUseMaxCountMinute;
    
    /** 每个API接口每10分钟最大允许的请求量（全体接口的默认值） */
    private long                       apiUseMaxCountMinute10;
    
    /** 每个API接口每分钟最大允许的请求量（接口的个性化配置，当没有时用默认值） */
    private Map<String ,OpenApiConfig> apiUseMaxCountMinuteMap;
    
    /** 每个API接口每10分钟最大允许的请求量（接口的个性化配置，当没有时用默认值） */
    private Map<String ,OpenApiConfig> apiUseMaxCountMinute10Map;
    
    /** 减轻数据库日志压力，简单信息的配置 */
    private Map<String ,OpenApiConfig> apiSimpleMap;
    
    /** 服务名称，产品运维时使用 */
    private String                     systemCode;
    
    /** 页面资源后缀 */
    private String                     pageUrlMappings;
    
    /** IP地址与地区 */
    private IP2Region                  ip2Region;
    
    /** 过滤无须日志记录的Http请求头 */ 
    private Map<String ,Param>         logHttpHeads;
    
    
    
    @SuppressWarnings("unchecked")
    public LogFilter()
    {
        this.ipSafeConfigService       = (IIPSafeConfigService) XJava.getObject("IPSafeConfigService");
        this.operationLogService       = (IOperationLogService) XJava.getObject("OperationLogService");
        this.apiUseMaxCountMinute      = XJava.getParam("MS_Common_ApiUseMaxCountMinute").getValueInt();
        this.apiUseMaxCountMinute10    = XJava.getParam("MS_Common_ApiUseMaxCountMinute10").getValueInt();
        this.apiUseMaxCountMinuteMap   = (Map<String ,OpenApiConfig>) XJava.getObject("MS_Common_ApiUseMaxCountMinuteMap");
        this.apiUseMaxCountMinute10Map = (Map<String ,OpenApiConfig>) XJava.getObject("MS_Common_ApiUseMaxCountMinute10Map");
        this.apiSimpleMap              = (Map<String ,OpenApiConfig>) XJava.getObject("MS_Common_ApiSimpleMap");
        this.systemCode                = XJava.getParam("MS_Common_ServiceName").getValue();
        this.ip2Region                 = (IP2Region) XJava.getObject("IP2Region");
        this.pageUrlMappings           = StringHelp.replaceAll(XJava.getParam("MS_Common_Page_UrlMappings").getValue() ,"*" ,"");
        this.logHttpHeads              = (Map<String ,Param>) XJava.getObject("MS_Common_Log_Filter_HttpHeads");
        
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
                Long v_MaxCount = this.apiUseMaxCountMinute;
                if ( !Help.isNull(this.apiUseMaxCountMinuteMap) )
                {
                    OpenApiConfig v_OpenApiConfig = this.apiUseMaxCountMinuteMap.get(i_APIUrl);
                    if ( v_OpenApiConfig != null && v_OpenApiConfig.getMaxCountMinute() != null)
                    {
                        if ( v_OpenApiConfig.getMaxCountMinute() >= 0 )
                        {
                            v_MaxCount = v_OpenApiConfig.getMaxCountMinute();
                        }
                    }
                }
                
                if ( v_TimeCount > v_MaxCount && v_MaxCount > 0 )
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
            v_APITimeTotal = new TimeGroupTotal(10);
            v_APITimeTotal.setMaxSize(8);
            $APITotalMinute10.put(i_APIUrl ,v_APITimeTotal);
        }
        else
        {
            Long v_TimeCount = v_APITimeTotal.get(v_Now);
            if ( v_TimeCount != null )
            {
                Long v_MaxCount = this.apiUseMaxCountMinute10;
                if ( !Help.isNull(this.apiUseMaxCountMinute10Map) )
                {
                    OpenApiConfig v_OpenApiConfig = this.apiUseMaxCountMinute10Map.get(i_APIUrl);
                    if ( v_OpenApiConfig != null && v_OpenApiConfig.getMaxCountMinute10() != null)
                    {
                        if ( v_OpenApiConfig.getMaxCountMinute10() >= 0 )
                        {
                            v_MaxCount = v_OpenApiConfig.getMaxCountMinute10();
                        }
                    }
                }
                
                if ( v_TimeCount > v_MaxCount && v_MaxCount > 0 )
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
        
        if ( Help.isNull(io_OLog.getAttackType()) )
        {
            // API接口允许被哪些国家、地区访问
            if ( !this.ip2Region.isAllow(io_OLog.getUserIP()) )
            {
                io_OLog.setAttackType("IP2R");
                io_OLog.setUrlResponse("{\"code\": \"-890\", \"message\": \"Non-permitted access to countries and regions\"}");
                io_OLog.setResultCode("-890");
                io_OLog.setResponseTime(Date.getNowTime().getTime());
                io_OLog.setTimeLen(io_OLog.getResponseTime() - io_OLog.getRequestTime());
            }
        }
        
        return io_OLog;
    }
    
    
    
    /**
     * 获取请求头，并且转为类似于Json的格式，但不是Json格式的字符串。
     * 
     *   key1值:value1值 + 回车
     *   key2值:value2值 + 回车
     *   ... ...
     *   keyN值:valueN值 + 回车
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-01-26
     * @version     v1.0
     *
     * @param i_Request
     * @return
     */
    private String getUrlRequestHead(HttpServletRequest i_Request)
    {
        StringBuilder       v_Buffer    = new StringBuilder();
        Enumeration<String> v_HeadNames = i_Request.getHeaderNames();
        int                 v_Count     = 0;
        
        while ( v_HeadNames.hasMoreElements() )
        {
            String v_HeadName = v_HeadNames.nextElement();
            if ( this.logHttpHeads.containsKey(v_HeadName) )
            {
                continue;
            }
            
            String v_HeadData = i_Request.getHeader(v_HeadName);
            v_Buffer.append(v_HeadName).append(":").append(v_HeadData).append("\n");
            v_Count++;
        }
        
        if ( v_Count >= 1 )
        {
            return v_Buffer.toString();
        }
        else
        {
            return "";
        }
    }
    
    
    
    /**
     * 减轻数据库日志压力，简单信息 或是 正常信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-12-27
     * @version     v1.0
     * 
     * @param i_Url          接口URL
     * @param i_RequestBody  原先的请求消息Body的内容
     * @return
     */
    private String getUrlRequestBody(String i_Url ,String i_RequestBody)
    {
        if ( Help.isNull(this.apiSimpleMap) )
        {
            return i_RequestBody;
        }
        
        OpenApiConfig v_OpenApiConfig = this.apiSimpleMap.get(i_Url);
        if ( v_OpenApiConfig == null || Help.isNull(v_OpenApiConfig.getSimpleClassName()) )
        {
            return i_RequestBody;
        }
        
        try
        {
            XJSON          v_XJson  = new XJSON();
            BaseSimpleInfo v_Simple = (BaseSimpleInfo) v_XJson.toJava(i_RequestBody ,Help.forName(v_OpenApiConfig.getSimpleClassName()));
            
            return v_Simple.toSimpleInfo();
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return i_RequestBody;
    }
    
    
    
    @Override
    public void doFilter(ServletRequest i_ServletRequest ,ServletResponse i_ServletResponse ,FilterChain i_FilterChain) throws IOException ,ServletException
    {
        HttpServletRequest v_HttpServletRequest = (HttpServletRequest) i_ServletRequest;
        String             v_Url                = v_HttpServletRequest.getServletPath();
        String []          v_Urls               = v_Url.split("/");
        
        // 分析中心、静态资源，不记录访问日志
        // 上传文件时也不解析
        
        if ( v_Urls.length < 3 
          || StringHelp.isContains(Help.NVL(i_ServletRequest.getContentType()).toLowerCase() ,"multipart/form-data") )
        {
             i_ServletResponse.setCharacterEncoding($CharacterEncoding);
             i_FilterChain.doFilter(i_ServletRequest ,i_ServletResponse);
             return;
        }
        else if ( !Help.isNull(this.pageUrlMappings) && v_Url.endsWith(this.pageUrlMappings) )
        {
            // Nothing.
        }
        else if ( StringHelp.isContains(v_Url ,"analyse" ,".") )
        {
            i_ServletResponse.setCharacterEncoding($CharacterEncoding);
            i_FilterChain.doFilter(i_ServletRequest ,i_ServletResponse);
            return;
        }
        
        // 没有配置 @RequestMapping(name) 的方法不记录访问日志
        OperationLogApi v_LogConfig = ProjectStartBase.$RequestMappingMethods.getRow(v_Urls[1] ,StringHelp.replaceLast(v_Url ,this.pageUrlMappings ,""));
        if ( v_LogConfig == null )
        {
            if ( v_Url.endsWith(this.pageUrlMappings) )
            {
                i_FilterChain.doFilter(new LogHttpServletRequestWrapper(v_HttpServletRequest ,this.pageUrlMappings) ,i_ServletResponse);
            }
            else
            {
                i_FilterChain.doFilter(i_ServletRequest ,i_ServletResponse);
            }
            return;
        }
        
        LogHttpServletRequestWrapper v_Request = new LogHttpServletRequestWrapper(v_HttpServletRequest ,this.pageUrlMappings);
        String                       v_UserIP  = this.getIpAddress(v_Request);
        OperationLog                 v_OLog    = new OperationLog();
        
        try
        {
            v_Request.setAttribute($ATTR_UserIP ,v_UserIP);
            // 解释用户账号信息。有Sesssion时，可直接从会话信息中取登录用户的信息
            if ( !Help.isNull(v_Request.getBodyString()) && StringHelp.isContains(Help.NVL(i_ServletRequest.getContentType()).toLowerCase() ,"json") )
            {
                XJSON        v_XJson = new XJSON();
                BaseViewMode v_BMode = (BaseViewMode) v_XJson.toJava(v_Request.getBodyString() ,BaseViewMode.class);
                v_OLog.setUserID(Help.NVL(v_BMode.getUpdateUserID() ,Help.NVL(v_BMode.getCreateUserID() ,Help.NVL(v_BMode.getUserID()))));
            }
            else
            {
                v_OLog.setUserID("");
            }
        }
        catch (Exception exce)
        {
            $Logger.error("Request's Json = " + v_Request.getBodyString() ,exce);
        }
        
        v_OLog.setLogName(v_LogConfig.getLogName());
        v_OLog.setCreateTime(new Date());
        v_OLog.setSystemCode(this.systemCode);
        v_OLog.setId(StringHelp.getUUID9n());
        v_OLog.setUrl(v_Url);
        v_OLog.setUrlRequest(v_Request.getQueryString());
        v_OLog.setUserIP(v_UserIP);
        v_OLog.setModuleCode(v_Urls[1]);
        v_OLog.setUrlRequestHead(this.getUrlRequestHead(v_HttpServletRequest));
        v_OLog.setUrlRequestBody(this.getUrlRequestBody(v_OLog.getUrl() ,v_Request.getBodyString()));
        
        this.backWhiteCheck(v_OLog);
        
        ServletOutputStream           v_Output   = null;
        LogHttpServletResponseWrapper v_Response = new LogHttpServletResponseWrapper((HttpServletResponse) i_ServletResponse ,v_OLog);
        
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
        v_OLog.setUrlRequestHead(this.getUrlRequestHead(i_Request));
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
