package com.chen.spring.factory;

/**
 * bean定义注册到bean工厂里去 所以要先去定义好bean之后 就可以注册到bean 工厂中
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName,BeanDefinitation beanDefinitation);

    BeanDefinitation getBeanDefinition(String beanName);

    boolean containsBeanDefinition(String beanName);

}
