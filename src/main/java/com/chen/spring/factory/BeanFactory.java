package com.chen.spring.factory;


import com.chen.spring.aop.postprocessor.BeanPostProcessor;

public interface BeanFactory {

    Object getBean(String name) throws Exception;

    void close();

    /**
     *  将advisor注入进来
     * @param beanPostProcessor
     */
    void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
