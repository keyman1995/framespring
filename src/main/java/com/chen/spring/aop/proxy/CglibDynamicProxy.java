package com.chen.spring.aop.proxy;

import com.chen.spring.aop.advisor.Advisor;
import com.chen.spring.factory.BeanDefinitation;
import com.chen.spring.factory.BeanFactory;
import com.chen.spring.factory.DefaultBeafactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class CglibDynamicProxy implements AopProxy, MethodInterceptor {

    private static final Enhancer enhancer = new Enhancer();
    private String beanName;
    private Object target;
    private List<Advisor> advisors;
    private BeanFactory beanFactory;


    public CglibDynamicProxy(String beanName, Object target, List<Advisor> advisors, BeanFactory beanFactory) {
        this.beanName = beanName;
        this.target = target;
        this.advisors = advisors;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getProxy() throws Exception {
        Class<?> superClass = this.target.getClass();
        enhancer.setSuperclass(superClass);
        //TODO 这样写对吗 不应该是 this.target.getClass().getInterfaces()吗？
        enhancer.setInterfaces(superClass.getInterfaces());
        enhancer.setCallback(this);
        Constructor<?> constructor = null;
        try{
            constructor = superClass.getConstructor(new Class<?>[]{});
        }catch (NoSuchMethodException e){

        }
        if(constructor==null){
            return enhancer.create();
        }else{
            BeanDefinitation definition = ((DefaultBeafactory)beanFactory).getBeanDefinition(this.beanName);
            return enhancer.create(definition.getConstructor().getParameterTypes(),((DefaultBeafactory)beanFactory).getRealValues(definition.getConstructValue()));
        }
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return AopUtilsProxy.applyAdvice(target,method,objects,advisors,o,beanFactory);
    }
}
