package com.chen.spring.aop.proxy;

import com.chen.spring.aop.advisor.Advisor;
import com.chen.spring.factory.BeanFactory;

import java.util.List;

public class DefaultAopFactory implements AopProxyFactory {


    @Override
    public AopProxy createAopProxy(Object bean, String beanName, List<Advisor> advisorList, BeanFactory beanFactory) {
        return new CglibDynamicProxy(beanName,bean,advisorList,beanFactory);
    }
}
