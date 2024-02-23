package org.hy.microservice.common;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;





/**
 * Spring MVC 5.3之后支持 *.do 后缀请求的方法
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-23
 * @version     v1.0
 * @see https://blog.csdn.net/a272265225/article/details/132033609
 */
// 启用 *.page 后缀请开启
// @Component
public class SuffixRequestMappingHandlerMapping extends RequestMappingHandlerMapping implements Ordered
{

    @SuppressWarnings("deprecation")
    public SuffixRequestMappingHandlerMapping()
    {
        // 该方法也是被弃用的，后续可能需要自定义匹配规则。
        super.setUseSuffixPatternMatch(true);
    }

    
    
    /**
     * 在 Spring 中，Ordered 接口是一个标记接口，用于表示实现了该接口的对象具有顺序性。
     * 实现了 Ordered 接口的对象可以通过 getOrder() 方法返回一个整数值，表示对象的顺序，数值越小的对象优先级越高。
     * 
     * 在 Spring 中，许多组件都实现了 Ordered 接口，比如 Interceptor、Filter、HandlerInterceptor、ApplicationListener 等。
     * 通过设置这些组件的顺序，可以控制它们的执行顺序。
     * 如果一个组件没有实现 Ordered 接口，Spring 会将其视为具有默认顺序，具体规则取决于组件类型。
     * 一般来说，如果没有显式设置顺序，Spring 将按照一定的规则自动确定执行顺序。
     *
     * @author      ZhengWei(HY)
     * @createDate  2024-02-23
     * @version     v1.0
     *
     * @return
     *
     * @see org.springframework.web.servlet.handler.AbstractHandlerMapping#getOrder()
     */
    @Override
    public int getOrder()
    {
        // 这里很重要
        return super.getOrder() - 1;
    }
    
}
