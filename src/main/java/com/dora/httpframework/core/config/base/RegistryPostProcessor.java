package com.dora.httpframework.core.config.base;

import com.dora.httpframework.core.annotation.HttpServer;
import com.dora.httpframework.exception.DoraException;
import com.dora.httpframework.proxy.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.Set;

/**
 * @Describe 手动注入
 * @Author dora 1.0.1
 **/
public class RegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private String[] scanning;

    public RegistryPostProcessor(String... scanning) {
        this.scanning = scanning;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //创建扫描类
        ScanningComponentProvider beanScanner = new ScanningComponentProvider(HttpServer.class);

        Set<BeanDefinition> beanDefinitions = beanScanner.findCandidateComponents(scanning);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class<?> clazz;
            try {
                clazz = Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                throw new DoraException("class not found:" + beanDefinition.getBeanClassName());
            }
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ProxyFactoryBean.class);
            String beanName = beanDefinition.getBeanClassName();
            beanDefinitionBuilder.addPropertyValue("clazz", clazz);

            BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinitionBuilder.getBeanDefinition(), beanName);
            BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
