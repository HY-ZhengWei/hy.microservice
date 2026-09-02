package org.hy.microservice.common.mybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;





/**
 * 定义 @SourceData 注解，标记当前使用的数据源
 * 
 * @author      ZDF
 * @createDate  2022-01-06
 * @version     v1.0
 *              v2.0  2026-08-18  动态数据源的适配范围升级(写法1~3)。合作解决人：xuhui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SourceData
{
    
    /**
     * 标记数据源XID的值
     * 
     * 写法1：变量入参【正常生效】
     *      @SourceData("#dbName")
     *
     * 写法2：静态工具调用【正常生效】
     *      @SourceData("#T(org.hy.microservice.xxx.yyy.zzz).getDsKey()")
     *
     * 写法3：实体属性
     *      @SourceData("#tenant.dsKey")
     *
     * 写法4：固定字符串
     *      @SourceData("DSG_MS_Common")
     * 
     * @author      ZDF
     * @createDate  2022-01-06
     * @version     v1.0
     *              v2.0  2026-08-18  动态数据源的适配范围升级(写法1~3)。解决人：xuhui
     *
     * @return
     */
    String value();
    
}
