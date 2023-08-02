package org.hy.microservice.common;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
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
import org.hy.common.StringHelp;
import org.hy.common.TimeGroupTotal;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.XSQLFilter;
import org.hy.microservice.common.ipSafe.IIPSafeConfigService;
import org.hy.microservice.common.ipSafe.IPSafeConfig;
import org.hy.microservice.common.operationLog.IOperationLogDAO;
import org.hy.microservice.common.operationLog.OperationLog;





/**
 * 启用Filter需要在Springboot的启动类加@ServletComponentScan
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-10
 * @version     v1.0
 */
@WebFilter(filterName="logFilter" ,urlPatterns="/*" ,initParams={
        @WebInitParam(name="exclusions" ,value="*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.swf")
       ,@WebInitParam(name="cachesize"  ,value="1000")
       ,@WebInitParam(name="timeout"    ,value="60")
})
public class LogFilter extends XSQLFilter
{
    private static final Logger                      $Logger           = new Logger(LogFilter.class);
    
    /** 接口的时间分组统计（每分钟） */
    private static final Map<String ,TimeGroupTotal> $APITotalMinute   = new HashMap<String ,TimeGroupTotal>();
    
    /** 接口的时间分组统计（每10分钟） */
    private static final Map<String ,TimeGroupTotal> $APITotalMinute10 = new HashMap<String ,TimeGroupTotal>();
    
    
    
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
                long v_MaxCount = Long.valueOf(XJava.getParam("MS_Common_ApiUseMaxCountMinute").getValue());
                if ( v_TimeCount > v_MaxCount )
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
                long v_MaxCount = Long.valueOf(XJava.getParam("MS_Common_ApiUseMaxCountMinute10").getValue());
                if ( v_TimeCount > v_MaxCount )
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
        IIPSafeConfigService      v_IPSafeConfigService = (IIPSafeConfigService) XJava.getObject("IPSafeConfigService");
        Map<String ,IPSafeConfig> v_IPMap               = v_IPSafeConfigService.queryAll().get(IPSafeConfig.$Type_WhiteList);
        
        return !Help.isNull(v_IPMap);
    }
    
    
    
    /**
     * 指定IP是否为黑名单或白名单
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-05-27
     * @version     v1.0
     *
     * @param i_IPType  IP类型
     * @param i_IP      IP地址
     * @return
     */
    private boolean isbackWhiteList(String i_IPType ,String i_IP)
    {
        IIPSafeConfigService      v_IPSafeConfigService = (IIPSafeConfigService) XJava.getObject("IPSafeConfigService");
        Map<String ,IPSafeConfig> v_IPMap               = v_IPSafeConfigService.queryAll().get(i_IPType);
        
        if ( Help.isNull(v_IPMap) )
        {
            return false;
        }
        
        IPSafeConfig v_IPSafeConfig = v_IPMap.get(i_IP);
        if ( v_IPSafeConfig != null )
        {
            return true;
        }
        
        String [] v_IPArr = i_IP.split("\\.");
        if ( v_IPArr.length >= 4 )
        {
            String v_IP = v_IPArr[0] + "." + v_IPArr[1] + "." + v_IPArr[2] + ".";
            
            v_IPSafeConfig = v_IPMap.get(v_IP);
            if ( v_IPSafeConfig != null )
            {
                return true;
            }
            
            v_IP           = v_IPArr[0] + "." + v_IPArr[1] + ".";
            v_IPSafeConfig = v_IPMap.get(v_IP);
            if ( v_IPSafeConfig != null )
            {
                return true;
            }
            
            v_IP           = v_IPArr[0] + ".";
            v_IPSafeConfig = v_IPMap.get(v_IP);
            if ( v_IPSafeConfig != null )
            {
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
        // 黑名单查询
        if ( isbackWhiteList(IPSafeConfig.$Type_BackList ,io_OLog.getUserIP()) )
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
                if ( isbackWhiteList(IPSafeConfig.$Type_WhiteList ,io_OLog.getUserIP()) )
                {
                    io_OLog.setAttackType(IPSafeConfig.$Type_WhiteList);
                }
                else
                {
                    io_OLog.setAttackType("not " + IPSafeConfig.$Type_WhiteList);
                    io_OLog.setUrlResponse("{\"code\": \"-892\", \"message\": \"not " + IPSafeConfig.$Type_WhiteList + "\"}");
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
        if ( StringHelp.isContains(v_Url ,"analyse" ,".") || v_Urls.length < 3 )
        {
            i_FilterChain.doFilter(i_ServletRequest ,i_ServletResponse);
            return;
        }
        
        LogHttpServletRequestWrapper v_Request = new LogHttpServletRequestWrapper((HttpServletRequest) i_ServletRequest);
        OperationLog                 v_OLog    = new OperationLog();
        IOperationLogDAO             v_OLogDAO = (IOperationLogDAO) XJava.getObject("OperationLogDAO");
        
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
            v_OLog.setSystemCode(XJava.getParam("MS_Common_ServiceName").getValue());
            v_OLog.setModuleCode(v_Urls[1]);
            
            v_OLogDAO.insert(this.backWhiteCheck(v_OLog));
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
            i_ServletResponse.setCharacterEncoding("UTF-8");
            i_ServletResponse.setContentType("application/json");
            v_Output = i_ServletResponse.getOutputStream();
            v_Output.write(v_OLog.getUrlResponse().getBytes());
            return;
        }
        // 访问量达到上限
        if ( !this.allowUseAPIMinute(v_OLog.getUrl()) )
        {
            v_OLog.setUrlResponse("访问量达到上限");
            i_ServletResponse.setCharacterEncoding("UTF-8");
            i_ServletResponse.setContentType("application/json");
            v_Output = i_ServletResponse.getOutputStream();
            v_Output.write(v_OLog.getUrlResponse().getBytes());
            return;
        }
        // 访问量达到上限（10分钟）
        if ( !this.allowUseAPIMinute10(v_OLog.getUrl()) )
        {
            v_OLog.setUrlResponse("访问量达到10分钟上限");
            i_ServletResponse.setCharacterEncoding("UTF-8");
            i_ServletResponse.setContentType("application/json");
            v_Output = i_ServletResponse.getOutputStream();
            v_Output.write(v_OLog.getUrlResponse().getBytes());
            return;
        }
        
        super.doFilter(v_Request ,v_Response ,i_FilterChain);
        //i_FilterChain.doFilter(v_Request ,v_Response);
        
        try
        {
            byte [] v_ResponseBody = v_Response.getResponseData();
            v_OLog.setUrlResponse(new String(v_ResponseBody));
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
            
            v_OLogDAO.update(v_OLog);
            
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
