package org.hy.microservice.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import org.hy.common.StringHelp;





/**
 * Request Body只能读取一次
 *     原因很简单：因为是流。想想看，java中的流也是只能读一次，因为读完之后，position就到末尾了。
 * 
 * 思路：第一次读的时候，把流数据暂存起来。后面需要的时候，直接把暂存的数据返回出去。
 * 
 * 实现逻辑：
 *     1. 自定义一个HttpServletRequestWrapper，覆写getInputStream()和getReader()方法。
 *     2. 增加一个Filter，在doFilter()中启用自定义的Wrapper
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-10
 * @version     v1.0
 */
public class LogHttpServletRequestWrapper extends HttpServletRequestWrapper
{

    private final byte [] body;
    
    private final String  bodyString;
    
    private final String  pageUrlMappings;



    public LogHttpServletRequestWrapper(HttpServletRequest i_Request ,final String i_PageUrlMappings) throws IOException
    {
        super(i_Request);
        this.bodyString      = this.getBodyString(i_Request);
        this.body            = this.bodyString.getBytes(Charset.forName("UTF-8"));
        this.pageUrlMappings = i_PageUrlMappings;
    }
    
    
    
    /**
     * 为了使Spring能识别定制化的后缀 *.page
     *
     * @author      ZhengWei(HY)
     * @createDate  2026-01-27
     * @version     v1.0
     *
     * @return
     *
     * @see javax.servlet.http.HttpServletRequestWrapper#getRequestURI()
     */
    @Override
    public String getRequestURI()
    {
        return StringHelp.replaceLast(super.getRequestURI() ,this.pageUrlMappings ,"");
    }
    
    
    
    /**
     * 获取：请求体的数据
     */
    public String getBodyString()
    {
        return bodyString;
    }



    private String getBodyString(ServletRequest i_Request) throws IOException
    {
        StringBuilder v_Buffer = new StringBuilder();
        InputStream   v_Input  = i_Request.getInputStream();
        try ( BufferedReader v_Reader = new BufferedReader(new InputStreamReader(v_Input ,Charset.forName("UTF-8"))); )
        {
            String v_Line = "";
            while ( (v_Line = v_Reader.readLine()) != null )
            {
                v_Buffer.append(v_Line);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if ( v_Input != null )
            {
                v_Input.close();
            }
        }
        return v_Buffer.toString();
    }



    @Override
    public BufferedReader getReader() throws IOException
    {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }



    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        final ByteArrayInputStream v_BodyByteIO = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException
            {
                return v_BodyByteIO.read();
            }



            @Override
            public boolean isFinished()
            {
                return v_BodyByteIO.available() == 0;
            }



            @Override
            public boolean isReady()
            {
                return true;
            }



            @Override
            public void setReadListener(ReadListener readListener)
            {
            }
        };
    }
}
