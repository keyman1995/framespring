package com.chen.springreview.factoryreview;

import com.chen.springreview.beanpost.BeanPostProcessor;

public interface BeanFactory {

    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;

    void registerBeanPostProcessor(BeanPostProcessor bpp);

}
