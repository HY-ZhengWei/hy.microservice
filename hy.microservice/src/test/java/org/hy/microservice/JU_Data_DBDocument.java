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
public class JU_Data_DBDocument
{
    
    private static boolean $IsInit = false;
    
    
    
    public JU_Data_DBDocument()
    {
        synchronized ( this )
        {
            if ( !$IsInit )
            {
                $IsInit = true;
                
                new XJavaInit(false ,"D:\\Software\\apache-tomcat-9.0.96\\webapps\\hy.microservice.data\\WEB-INF\\classes\\");
            }
        }
    }
    
    
    
    @Test
    public void makeDoc()
    {
        DBDocument.makeDatabaseDoc("DS_MS_Data" ,"V10.0" ,null ,"OpenApi.数据库设计-Data数据服务");
    }
    
}
