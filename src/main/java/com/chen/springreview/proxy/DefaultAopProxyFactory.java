package com.chen.springreview.proxy;

import com.chen.springreview.advisor.Advisor;
import com.chen.springreview.factoryreview.BeanFactory;

import java.util.List;

public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public  AopProxy createAopProxy(Object bean, String beanName, List<Advisor> matchAdvisor, BeanFactory beanFactory) {

        return new CglibDynamicAopProxy(beanName,bean,matchAdvisor,beanFactory);
    }

    @Override
    public AopProxyFactory getDefaultAopProxyFactory() {
        return new DefaultAopProxyFactory();
    }
}
