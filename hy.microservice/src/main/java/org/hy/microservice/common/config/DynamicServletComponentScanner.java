package org.hy.microservice.common.config;

import org.hy.common.app.Param;
import org.hy.common.xml.XJava;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;





/**
* 配置多包扫描路径
*
* @author      ZhengWei(HY)
* @createDate  2024-06-25
* @version     v1.0
*/
@Configuration
public class DynamicServletComponentScanner
{

    @Bean
    @Profile("prod")
    public static BeanDefinitionRegistryPostProcessor i_BeanDefinitionRegistryPostProcessor()
    {
        return new BeanDefinitionRegistryPostProcessor()
        {
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry i_Registry) throws BeansException
            {
                // 扫描多个包路径
                String    v_RootPackageNames   = ((Param)XJava.getObject("MS_Common_RootPackageName")).getValue();
                String [] v_RootPackageNameArr = v_RootPackageNames.split(",");
                ClassPathBeanDefinitionScanner v_Scanner = new ClassPathBeanDefinitionScanner(i_Registry);
                v_Scanner.scan(v_RootPackageNameArr);
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                // 这里可以添加额外的逻辑处理，也可什么都不做
            }
        };
    }
    
}