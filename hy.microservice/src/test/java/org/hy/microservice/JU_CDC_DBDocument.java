package org.hy.microservice;

import org.hy.microservice.common.config.XJavaInit;
import org.junit.Test;





/**
 * 数据库设计文档的生成
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-05
 * @version     v1.0
 */
public class JU_CDC_DBDocument
{
    
    private static boolean $IsInit = false;
    
    
    
    public JU_CDC_DBDocument()
    {
        synchronized ( this )
        {
            if ( !$IsInit )
            {
                $IsInit = true;
                
                new XJavaInit(false ,"C:\\Software\\apache-tomcat-9.0.44\\webapps\\hy.microservice.cdc\\WEB-INF\\classes\\");
            }
        }
    }
    
    
    
    @Test
    public void makeDoc()
    {
        DBDocument.makeDatabaseDoc("DS_MS_CDC_FMS" ,"V2.2" ,null ,"OpenApi.数据库设计-CDC数据同步");
    }
    
}
