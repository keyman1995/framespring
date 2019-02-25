package com.chen.spring.factory;

public class BeanReference {

    private String beanName;

    public BeanReference(String beanName) {
        super();
        this.beanName = beanName;
    }

    public String getBeanName(){
        return this.beanName;
    }
}
