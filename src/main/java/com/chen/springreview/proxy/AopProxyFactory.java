package com.chen.springreview.proxy;

import com.chen.springreview.advisor.Advisor;
import com.chen.springreview.factoryreview.BeanFactory;

import java.util.List;

public interface AopProxyFactory {

    AopProxy createAopProxy(Object bean, String beanName, List<Advisor> matchAdvisor, BeanFactory beanFactory);

    AopProxyFactory getDefaultAopProxyFactory();
}
