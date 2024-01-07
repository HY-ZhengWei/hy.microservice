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
public class JU_YonYou_DBDocument
{
    
    private static boolean $IsInit = false;
    
    
    
    public JU_YonYou_DBDocument()
    {
        synchronized ( this )
        {
            if ( !$IsInit )
            {
                $IsInit = true;
                
                new XJavaInit(false ,"C:\\Software\\apache-tomcat-9.0.44\\webapps\\fimeson.microservice.yonyou\\WEB-INF\\classes\\");
            }
        }
    }
    
    
    
    @Test
    public void makeDoc()
    {
        DBDocument.makeDatabaseDoc("DS_MS_YonYou_FinancialDataCenter" ,"V1.0" ,"TYY_" ,"OpenApi.数据库设计-YonYou数据同步");
    }
    
}
