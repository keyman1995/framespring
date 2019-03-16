package com.chen.springreview.beanpost;

//bean的观察者接口
public interface BeanPostProcessor {

    //初始化前观察
     Object postProcessBeforeInitialization(Object bean,String beanName);

    //初始化之后
    Object postProcessAfterInitialization(Object bean,String beanName);

}
