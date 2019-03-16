package com.chen.springreview.proxy;

import com.chen.springreview.advisor.Advisor;
import com.chen.springreview.factoryreview.BeanFactory;
import com.sun.deploy.net.proxy.ProxyUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkDynamicAopProxy implements InvocationHandler,AopProxy {

    private Object target;

    private String beanName;

    private List<Advisor> matchAdvisor;

    private BeanFactory beanFactory;

    public JdkDynamicAopProxy(Object target, String beanName, List<Advisor> matchAdvisor, BeanFactory beanFactory) {
        this.target = target;
        this.beanName = beanName;
        this.matchAdvisor = matchAdvisor;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getProxy() {
        return this.getProxy(this.getClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,target.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return AopProxyUtils.applyAdvice(target,method,args,matchAdvisor,proxy,beanFactory);
    }
}
