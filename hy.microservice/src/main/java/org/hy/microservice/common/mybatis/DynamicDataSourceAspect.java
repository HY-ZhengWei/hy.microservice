package org.hy.microservice.common.mybatis;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hy.common.xml.log.Logger;
import org.springframework.stereotype.Component;





/**
 * 动态多数据源的AOP切面的注册与拦截
 * 
 * 参考文献：https://blog.csdn.net/weixin_50348837/article/details/138130597
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-09-20
 * @version     v1.0
 */
@Aspect
@Component
public class DynamicDataSourceAspect
{
    
    private static final Logger $Logger = new Logger(DynamicDataSourceAspect.class);
    
    
    
    /**
     * 用于定义切点，标识哪些连接点（方法执行）需要被拦截。
     * 通过定义切点，后续通知@Before、@After、@Around 等操作。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     */
    @Pointcut("@annotation(org.hy.microservice.common.mybatis.SourceData)")
    public void dataSourcePointCut()
    {
        // 前置通知逻辑
    }



    /**
     * 环绕通知，可以控制目标方法的执行，既可以在方法执行前后添加逻辑，也可以决定是否执行目标方法，
     * 适合更复杂的逻辑处理，如事务管理。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *
     * @param i_Point
     * @return
     * @throws Throwable
     */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint i_Point) throws Throwable
    {
        MethodSignature v_Signature  = (MethodSignature) i_Point.getSignature(); // 获取被拦截方法的签名,该签名包含了方法的名称、参数类型等信息
        Method          v_Method     = v_Signature.getMethod();                  // 从签名中获取了实际的方法对象
        SourceData      v_SourceData = v_Method.getAnnotation(SourceData.class); // 获取名为SourceData的注解。
        
        if ( v_SourceData == null )
        {
            $Logger.debug("默认数据源：DSG_MS_Common");
            DynamicDataSource.setDataSource("DSG_MS_Common");
        }
        else if ( DynamicDataSource.exists(v_SourceData.value()) )
        {
            $Logger.debug("切换数据源：" + v_SourceData.value());
            DynamicDataSource.setDataSource(v_SourceData.value());
        }
        else
        {
            String v_Error = "无效数据源：" + v_SourceData.value() + " of " + v_Method.toString();
            $Logger.error(v_Error);
            throw new RuntimeException(v_Error);
        }
        
        try
        {
            return i_Point.proceed();
        }
        finally
        {
            // 最后一定要清除，这里使用的ThreadLocal来存储的数据源key，所以为了防止内存泄露一定要清除
            // 而且该清除操作也是为了防止该切换操作对后续的切换操作造成影响
            DynamicDataSource.clearDataSource();
        }
    }
    
}
