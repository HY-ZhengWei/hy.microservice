package org.hy.microservice.common;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;

import org.hy.common.xml.XJSON;





/**
 * Web控制层--基类
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-12-09
 * @version     v1.0
 */
public class BaseController
{
    
    /**
     * 页面响应Json格式的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @param io_Response  响应对象
     * @param i_Data       响应数据对象
     */
    public static void writerJson(HttpServletResponse io_Response, Object i_Data)
    {
        try
        {
            XJSON v_XJSON       = new XJSON();
            String v_JsonString = v_XJSON.toJson(i_Data).toJSONString();
                    
            writer(io_Response ,"application/json" ,v_JsonString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 页面响应Json格式的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @param io_Response   响应对象
     * @param i_JsonString  响应Json数据
     */
    public static void writerJson(HttpServletResponse io_Response, String i_JsonString)
    {
        writer(io_Response ,"application/json" ,i_JsonString);
    }
    
    
    
    /**
     * 页面响应Html格式的数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @param io_Response  响应对象
     * @param i_Html       响应Html数据
     */
    public static void writerHtml(HttpServletResponse io_Response, String i_Html)
    {
        writer(io_Response ,"text/html" ,i_Html);
    }
    
    
    
    /**
     * 页面响应传递数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-12-09
     * @version     v1.0
     *
     * @param io_Response    响应对象
     * @param i_ContentType  响应类型
     * @param i_Content      响应内容
     */
    public static void writer(HttpServletResponse io_Response ,String i_ContentType ,String i_Content)
    {
        PrintWriter v_Out= null;
        
        try
        {
            // 设置页面不缓存
            io_Response.setHeader("Pragma" ,"No-cache");
            io_Response.setHeader("Cache-Control" ,"no-cache");
            io_Response.setCharacterEncoding("UTF-8");
            io_Response.setContentType(i_ContentType);
            
            v_Out = io_Response.getWriter();
            v_Out.print(i_Content);
            v_Out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if ( v_Out != null )
            {
                try
                {
                    v_Out.close();
                }
                catch (Exception exce)
                {
                    exce.printStackTrace();
                }
            }
        }
    }
    
}
