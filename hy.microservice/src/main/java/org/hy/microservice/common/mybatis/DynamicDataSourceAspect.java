package org.hy.microservice.common.mybatis;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hy.common.Help;
import org.hy.common.xml.log.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeLocator;
import org.springframework.stereotype.Component;





/**
 * 动态多数据源的AOP切面的注册与拦截
 * 
 * 参考文献：https://blog.csdn.net/weixin_50348837/article/details/138130597
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-09-20
 * @version     v1.0
 *              v2.0  2026-08-18  添加：增加SpEL表达式支持，支持类注解、最高执行优先级，增加表达式缓存。合作解决人：xuhui
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DynamicDataSourceAspect
{
    
    private static final Logger                  $Logger          = new Logger(DynamicDataSourceAspect.class);
    
    /**
     * SpEL表达式解析器实例
     *
     * SpelExpressionParser是Spring提供的标准SpEL表达式解析实现，线程安全，全局单例复用即可，
     * 不要每次调用parseSpel都new新对象，避免频繁创建对象带来性能损耗。
     * 负责将字符串表达式编译为可执行的Expression对象。
     *
     */
    private static final ExpressionParser        $SpelParser      = new SpelExpressionParser();
    
    /**
     * SpEL表达式编译缓存
     *
     * key：注解上拿到的原始表达式字符串（带#，例如"#dbName"、"#T(com.xxx.Class).getDsKey()"），
     *      使用原始字符串作为key，避免#T场景下剔除#之后发生缓存key冲突；
     * value：经过SPEL_PARSER编译生成的Expression可执行对象。
     *
     * SpEL编译比较消耗性能，相同表达式重复调用时，直接复用缓存中已经编译好的Expression对象，
     * 不需要重复编译，提升高并发场景下切面执行性能。
     * 使用ConcurrentHashMap保证多线程AOP切面并发拦截时线程安全；初始容量64适配常规业务表达式数量。
     *
     * 注意：Expression对象本身是线程安全，可以多线程并发调用 {@link Expression#getValue(EvaluationContext)}。
     */
    private static final Map<String, Expression> $ExpressionCache = new ConcurrentHashMap<>(64);
    
    
    
    /**
     * 用于定义切点，标识哪些连接点（方法执行）需要被拦截。
     * 通过定义切点，后续通知@Before、@After、@Around 等操作。
     * 
     * 拦截目标类上有没有写注解：@within
     * 拦截目标方法上有没有写注解：@annotation
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-09-20
     * @version     v1.0
     *              v2.0  2026-08-18  添加：拦截目标类。xuhui
     */
    @Pointcut("@annotation(org.hy.microservice.common.mybatis.SourceData) || @within(org.hy.microservice.common.mybatis.SourceData)")
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
     *              v2.0  2026-08-18  添加：解析SpEL表达式。xuhui
     *
     * @param i_Point  AOP 环绕通知专用对象，允许你“包裹目标方法，手动控制什么时候执行业务代码”，适合做数据源切换、耗时统计、权限拦截、日志埋点。
     * @return
     * @throws Throwable
     */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint i_Point) throws Throwable
    {
        MethodSignature v_Signature  = (MethodSignature) i_Point.getSignature();                    // 获取被拦截方法的签名,该签名包含了方法的名称、参数类型等信息
        Method          v_Method     = v_Signature.getMethod();                                     // 从签名中获取了实际的方法对象
        Object          v_Target     = i_Point.getTarget();
        Object []       v_Args       = i_Point.getArgs();
        SourceData      v_SourceData = AnnotationUtils.findAnnotation(v_Method, SourceData.class);  // 获取名为SourceData的注解。
        
        if ( v_SourceData == null )
        {
            $Logger.debug("默认数据源：DSG_MS_Common");
            DynamicDataSource.setDataSource("DSG_MS_Common");
        }
        else 
        {
            String v_DSKey    = v_SourceData.value().trim();
            String v_RawValue = v_DSKey;
            if ( v_RawValue.startsWith("#") ) 
            {
                v_DSKey = this.parseSpel(v_RawValue, v_Signature, v_Args, v_Target);
                $Logger.debug("SpEL解析数据源表达式[{}] → 结果:{}", v_RawValue, v_DSKey);
            } 
            
            if ( DynamicDataSource.exists(v_DSKey) )
            {
                $Logger.debug("切换数据源：" + v_SourceData.value());
                DynamicDataSource.setDataSource(v_SourceData.value());
            }
            else
            {
                throw $Logger.error(new RuntimeException("无效数据源：" + v_SourceData.value() + " of " + v_Method.toString()));
            }
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
            $Logger.debug("切换数据源完毕，恢复默认数据源");
        }
    }
    
    
    
    /**
     * 解析SpEL表达式，支持两类表达式：
     *   1) 普通变量表达式：#dbName、#dto.dsKey，用于直接取方法入参变量;
     *   2) 静态类调用表达式：#T(com.xxx.ClassName).getDsKey()，底层自动剔除#转为合法T()语法;
     * 
     * @author      xuhui
     * @createDate  2026-08-18
     * @version     v1.0
     *
     * @param i_SpelStr          原始SpEL表达式字符串，来自@SourceData注解value，以#开头,
     *                             例如 `"#dbName"`
     *                             例如 `"#T(org.hy.microservice.workorder.config.routingdatasource.TenantContextHolder).getDsKey()"`
     * @param i_MethodSignature  方法签名对象，用于获取方法参数名称、方法元信息
     * @param i_Args             被AOP拦截的目标方法实际入参数组，与paramNames一一对应
     * @param i_Target           AOP拦截的目标对象（被切面代理的Bean实例）
     * @return                   解析完成后的数据源key字符串，非空
     * @throws RuntimeException  SpEL编译失败、执行异常、解析结果为空/空白字符串时抛出
     */
    private String parseSpel(String i_SpelStr, MethodSignature i_MethodSignature, Object[] i_Args, Object i_Target)
    {
        StandardEvaluationContext v_Context = new StandardEvaluationContext(i_Target);
        // 支持SpEL T(xxx.类名) 静态调用语法，解决 Function 'T' could not be found
        v_Context.setTypeLocator(new StandardTypeLocator());

        String[] v_ParamNames;
        try
        {
            v_ParamNames = i_MethodSignature.getParameterNames();
        }
        catch (Exception e)
        {
            $Logger.warn("无法获取方法参数名称，缺少编译参数-parameters，#变量方式不可用，仅支持#T()静态调用" ,e);
            v_ParamNames = new String[0];
        }

        if ( v_ParamNames.length > 0 )
        {
            for (int i = 0; i < v_ParamNames.length; i++)
            {
                v_Context.setVariable(v_ParamNames[i] ,i_Args[i]);
                // 兜底arg0、arg1，参数名获取失败时兼容
                v_Context.setVariable("arg" + i ,i_Args[i]);
            }
        }
        
        // ==========关键逻辑：智能处理#T冲突==========
        String v_RealExpression;
        if ( i_SpelStr.startsWith("#T(") )
        {
            // #T(xxx) 剔除首字符#，转为合法T表达式
            v_RealExpression = i_SpelStr.substring(1);
        }
        else
        {
            // #dbName、#dto.key 普通变量表达式，保留#
            v_RealExpression = i_SpelStr;
        }
        
        Object v_Value = null;
        try
        {
            Expression v_Expression = $ExpressionCache.computeIfAbsent(i_SpelStr ,k -> $SpelParser.parseExpression(v_RealExpression));
            v_Value = v_Expression.getValue(v_Context);
        }
        catch (Exception error)
        {
            throw $Logger.error(new RuntimeException("SpEL表达式执行异常，表达式:" + i_SpelStr ,error));
        }

        // 兼容第三方工具类+原生双重判空
        if ( Help.isNull(v_Value) )
        {
            throw $Logger.error(new RuntimeException("SpEL表达式解析结果为空：" + i_SpelStr));
        }
        String v_DSKey = String.valueOf(v_Value).trim();
        if ( Help.isNull(v_DSKey) )
        {
            throw $Logger.error(new RuntimeException("SpEL解析得到数据源key为空！表达式：" + i_SpelStr));
        }
        return v_DSKey;
    }
    
}
