package com.chen.springreview.proxy;



import com.chen.springreview.advisor.Advisor;
import com.chen.springreview.factoryreview.BeanDefinitionBack;
import com.chen.springreview.factoryreview.BeanFactory;
import com.chen.springreview.factoryreview.CommonBeanFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class CglibDynamicAopProxy implements AopProxy, MethodInterceptor {

    private static final Enhancer enhancer = new Enhancer();
    private String beanName;
    private Object target;
    private List<Advisor> advisorList;
    private BeanFactory beanFactory;

    public CglibDynamicAopProxy(String beanName, Object target, List<Advisor> advisorList, BeanFactory beanFactory) {
        this.beanName = beanName;
        this.target = target;
        this.advisorList = advisorList;
        this.beanFactory = beanFactory;
    }

    public CglibDynamicAopProxy() {
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return AopProxyUtils.applyAdvice(target,method,args,advisorList,proxy,beanFactory);
    }

    @Override
    public Object getProxy()  {
        Class<?> superClass = this.target.getClass();
        enhancer.setSuperclass(superClass);
        enhancer.setInterfaces(this.getClass().getInterfaces());
        enhancer.setCallback(this);
        Constructor<?> constructor = null;
        try {
            constructor = superClass.getConstructor(new Class<?>[]{});
        } catch (NoSuchMethodException e) {

        }
        if(constructor!=null){
            return enhancer.create();
        }else{
            BeanDefinitionBack beanDefinitionBack = ((CommonBeanFactory)beanFactory).getBeanDefinitionBack(beanName);
            try {
                return enhancer.create(beanDefinitionBack.getConstructor().getParameterTypes(),((CommonBeanFactory)beanFactory).getConstructorArgumentValues(beanDefinitionBack));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
