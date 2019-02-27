package com.chen.springreview.factoryreview;

public interface BeanFactory {

    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;

}
