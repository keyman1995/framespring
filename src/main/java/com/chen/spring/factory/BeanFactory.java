package com.chen.spring.factory;

public interface BeanFactory {

    Object getBean(String name) throws Exception;

    void close();
}
