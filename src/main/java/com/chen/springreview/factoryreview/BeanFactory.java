package com.chen.springreview.factoryreview;

public interface BeanFactory {

    Object getBean(String beanName) throws Exception;

}
