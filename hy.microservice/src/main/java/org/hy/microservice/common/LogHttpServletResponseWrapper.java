package org.hy.microservice.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.hy.microservice.common.operationLog.OperationLog;





/**
 * 响应体中的内容一旦读取就不不存在了，所以直接读取是不行的。
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-04-11
 * @version     v1.0
 *              v2.0  2026-01-29  添加：操作日志，允许最终用户的控制层Controller对操作日志的可读可控
 */
public class LogHttpServletResponseWrapper extends HttpServletResponseWrapper implements LogHttpServletResponse
{

    private ByteArrayOutputStream buffer = null;   // 输出到byte array

    private ServletOutputStream   out    = null;

    private PrintWriter           writer = null;
    
    /** 操作日志，允许最终用户的控制层Controller对操作日志的可读可控 */
    private OperationLog          log    = null;



    public LogHttpServletResponseWrapper(HttpServletResponse i_Response ,OperationLog i_Log) throws IOException
    {
        super(i_Response);
        this.log    = i_Log;
        this.buffer = new ByteArrayOutputStream();      // 真正存储数据的流
        this.out    = new WapperedOutputStream(this.buffer);
        this.writer = new PrintWriter(new OutputStreamWriter(this.buffer ,Charset.forName("UTF-8")));
    }


    
    /**
     * 获取：操作日志，允许最终用户的控制层Controller对操作日志的可读可控
     */
    public OperationLog getOperationLog()
    {
        return log;
    }



    /** 重载父类获取outputstream的方法 */
    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
        return out;
    }



    /** 重载父类获取writer的方法 */
    @Override
    public PrintWriter getWriter() throws UnsupportedEncodingException
    {
        return writer;
    }



    /** 重载父类获取flushBuffer的方法 */
    @Override
    public void flushBuffer() throws IOException
    {
        if ( out != null )
        {
            out.flush();
        }
        if ( writer != null )
        {
            writer.flush();
        }
    }



    @Override
    public void reset()
    {
        buffer.reset();
    }



    /** 将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据 */
    public byte [] getResponseData() throws IOException
    {
        flushBuffer();
        return buffer.toByteArray();
    }



    /** 内部类，对ServletOutputStream进行包装 */
    private class WapperedOutputStream extends ServletOutputStream
    {

        private ByteArrayOutputStream bos = null;



        public WapperedOutputStream(ByteArrayOutputStream stream) throws IOException
        {
            bos = stream;
        }



        @Override
        public void write(int b) throws IOException
        {
            bos.write(b);
        }



        @Override
        public void write(byte [] b) throws IOException
        {
            bos.write(b ,0 ,b.length);
        }



        @Override
        public boolean isReady()
        {
            return false;
        }



        @Override
        public void setWriteListener(WriteListener listener)
        {
        }
    }
}
