package org.hy.microservice;

import org.hy.microservice.common.config.XJavaInit;
import org.junit.Test;





/**
 * 数据库设计文档的生成
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-01-07
 * @version     v1.0
 */
public class JU_OpenApis_DBDocument
{
    
    private static boolean $IsInit = false;
    
    
    
    public JU_OpenApis_DBDocument()
    {
        synchronized ( this )
        {
            if ( !$IsInit )
            {
                $IsInit = true;
                
                new XJavaInit(false ,"D:\\Software\\apache-tomcat-9.0.96\\webapps\\lps.microservice.openapis\\WEB-INF\\classes\\");
            }
        }
    }
    
    
    
    @Test
    public void makeDoc()
    {
        DBDocument.makeDatabaseDoc("DS_MS_OpenApis" ,"V1.0" ,null ,"OpenApi.数据库设计-ApiGateway接口网关");
    }
    
}
