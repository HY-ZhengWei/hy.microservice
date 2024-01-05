package org.hy.microservice;

import org.hy.microservice.common.BaseJunit;
import org.hy.microservice.common.openapi.DBDocument;
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
    public void makeDoc() throws InterruptedException
    {
        DBDocument.makeDatabaseDoc("DS_MS_Common" ,"2.0.0");
    }
    
}
