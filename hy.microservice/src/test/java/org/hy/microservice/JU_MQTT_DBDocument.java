package org.hy.microservice;

import org.hy.microservice.common.config.XJavaInit;
import org.junit.Test;





/**
 * 数据库设计文档的生成
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-06-03
 * @version     v1.0
 */
public class JU_MQTT_DBDocument
{
    
    private static boolean $IsInit = false;
    
    
    
    public JU_MQTT_DBDocument()
    {
        synchronized ( this )
        {
            if ( !$IsInit )
            {
                $IsInit = true;
                
                new XJavaInit(false ,"C:\\Software\\apache-tomcat-9.0.44\\webapps\\hy.microservice.mqtt\\WEB-INF\\classes\\");
            }
        }
    }
    
    
    
    @Test
    public void makeDoc()
    {
        DBDocument.makeDatabaseDoc("DS_MS_MQTT" ,"V3.0" ,null ,"OpenApi.数据库设计-MQTT数据物联");
    }
    
}
