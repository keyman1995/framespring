package com.chen.springreview.factoryreview;

public interface BeanDefinitionRegistry {

    //注册bean
    void setBeanDefitionBack(String beanName,BeanDefinitionBack beanDefitionBack);

    //根据beanName获取beanDefinition
    BeanDefinitionBack getBeanDefinitionBack(String beanName);

    //判断beanName是否存在beanDefinition
    boolean isContainBeanDefinitionBack(String beanName);

}
