package org.hy.microservice;

import org.hy.microservice.common.BaseJunit;
import org.junit.Test;





/**
 * 数据库设计文档的生成
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-05
 * @version     v1.0
 */
public class JU_DBDocument extends BaseJunit
{
    
    @Test
    public void makeDoc()
    {
        DBDocument.makeDatabaseDoc("DS_MS_Common" ,"V2.0" ,null ,"OpenApi.数据库设计-公共");
    }
    
}
