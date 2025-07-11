package org.hy.microservice;

import org.hy.microservice.common.config.XJavaInit;
import org.junit.Test;





/**
 * 数据库设计文档的生成
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-06-28
 * @version     v1.0
 */
public class JU_CallFlow_DBDocument
{
    
    private static boolean $IsInit = false;
    
    
    
    public JU_CallFlow_DBDocument()
    {
        synchronized ( this )
        {
            if ( !$IsInit )
            {
                $IsInit = true;
                
                new XJavaInit(false ,"D:\\Software\\apache-tomcat-9.0.96\\webapps\\lps.microservice.mes\\WEB-INF\\classes\\");
            }
        }
    }
    
    
    
    @Test
    public void makeDoc()
    {
        DBDocument.makeDatabaseDoc("DS_MS_msMes" ,"V1.0" ,null ,"OpenApi.数据库设计-CallFlow指令编排");
    }
    
}
