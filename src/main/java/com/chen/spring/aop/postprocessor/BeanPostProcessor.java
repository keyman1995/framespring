package com.chen.spring.aop.postprocessor;

import com.chen.spring.factory.BeanFactory;

public interface BeanPostProcessor {

    /**
     * 初始化之前进行增强
     * @param beanName
     * @param bean
     * @return
     */
    Object postProcessBeforeInitialization(String beanName, Object bean);

    /**
     * 初始化之后进行增强
     * @param beanName
     * @param bean
     * @return
     */
    Object postProcessAfterInitialization(String beanName, Object bean, BeanFactory beanFactory);

}
