package org.hy.microservice.common.probe.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;





/**
 * 本体的注解接口
 * 
 * @author      ZhengWei(HY)
 * @version     v1.0  
 * @createDate  2026-11-10
 */
@Documented
@Target({ElementType.TYPE ,ElementType.METHOD ,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ontology
{
    
    /**
     * 归类
     * 
     * 使用形式有两种：
     *   1. 命名注解,  如：Ontology(type="xxx")
     *   2. 无命名注解，如：Ontology
     *      使用这种方式，隐含type为Java的类名称 
     *      
     * 只用于类，即Target(ElementType.TYPE）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public String type() default "";
    
    
    
    /**
     * 总长度（简写别名）
     * 
     * 1.用于setter方法，即Target(ElementType.METHOD）
     * 2.用于属性，      即Target(ElementType.FIELD)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public int l() default 0;
    
    
    
    /**
     * 总长度
     * 
     * 1.用于setter方法，即Target(ElementType.METHOD）
     * 2.用于属性，      即Target(ElementType.FIELD)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public int length() default 0;
    
    
    
    /**
     * 小数位数（简写别名）
     * 
     * 1.用于setter方法，即Target(ElementType.METHOD）
     * 2.用于属性，      即Target(ElementType.FIELD)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public int d() default 0;
    
    
    
    /**
     * 小数位数
     * 
     * 1.用于setter方法，即Target(ElementType.METHOD）
     * 2.用于属性，      即Target(ElementType.FIELD)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public int decimal() default 0;
    
    
    
    /**
     * 是否为主键，默认为false
     * 
     * 1.用于setter方法，即Target(ElementType.METHOD）
     * 2.用于属性，      即Target(ElementType.FIELD)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public boolean pk() default false;
    
    
    
    /**
     * 是否允许空，默认为true（简写别名）
     * 
     * 1.用于setter方法，即Target(ElementType.METHOD）
     * 2.用于属性，      即Target(ElementType.FIELD)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public boolean n() default true;
    
    
    
    /**
     * 是否允许空，默认为true
     * 
     * 1.用于setter方法，即Target(ElementType.METHOD）
     * 2.用于属性，      即Target(ElementType.FIELD)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public boolean nullable() default true;
    
    
    
    /**
     * 描述（简写别名）
     * 
     * 1.用于setter方法，即Target(ElementType.METHOD）
     * 2.用于属性，      即Target(ElementType.FIELD)
     * 3.用于类的定时，   即Target(ElementType.TYPE）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public String c() default "";
    
    
    
    /**
     * 描述
     * 
     * 1.用于setter方法，即Target(ElementType.METHOD）
     * 2.用于属性，      即Target(ElementType.FIELD)
     * 3.用于类的定时，   即Target(ElementType.TYPE）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2026-11-10
     * @version     v1.0
     *
     * @return
     */
    public String comment() default "";
    
}
