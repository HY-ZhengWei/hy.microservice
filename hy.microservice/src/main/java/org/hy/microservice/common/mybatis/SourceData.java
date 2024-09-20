package org.hy.microservice.common.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;





/**
 * 定义 @DataSource 注解，标记当前使用的数据源
 * 
 * @author      ZDF
 * @createDate  2022-01-06
 * @version     v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SourceData
{
    
    /**
     * 标记数据源XID的值
     * 
     * @author      ZDF
     * @createDate  2022-01-06
     * @version     v1.0
     *
     * @return
     */
    String value();
    
}
