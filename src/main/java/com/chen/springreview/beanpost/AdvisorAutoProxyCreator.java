package com.chen.springreview.beanpost;

import com.chen.springreview.advisor.Advisor;
import com.chen.springreview.advisor.AdvisorRegistory;
import com.chen.springreview.advisor.PointcutAdvisor;
import com.chen.springreview.factoryreview.BeanFactory;
import com.chen.springreview.factoryreview.BeanFactoryAware;
import com.chen.springreview.poincut.PointCut;
import com.chen.springreview.proxy.AopProxyFactory;
import com.chen.springreview.proxy.DefaultAopProxyFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

//AOP增强的观察者实现
public class AdvisorAutoProxyCreator implements BeanPostProcessor, AdvisorRegistory, BeanFactoryAware {

    //所有注册进来的切面都要在这注册进来
    private List<Advisor> advisors;

    private BeanFactory beanFactory;

    public AdvisorAutoProxyCreator() {
        this.advisors = new ArrayList<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    //只实现初始化之后的方法
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        List<Advisor> matchAdvisor = getMatchAdvisor(bean,beanName);
        if(CollectionUtils.isNotEmpty(matchAdvisor)){
            bean = this.createProxy(bean,beanName,matchAdvisor);
        }
        return bean;
    }

    //在此通过代理工厂去创建代理对象
    private Object createProxy(Object bean, String beanName, List<Advisor> matchAdvisor) {
        AopProxyFactory proxyFactory = new DefaultAopProxyFactory();
        return proxyFactory.createAopProxy(bean,beanName,matchAdvisor,beanFactory).getProxy();
    }

    private List<Advisor> getMatchAdvisor(Object bean, String beanName) {
        if(CollectionUtils.isEmpty(advisors)){
            return null;
        }
        Class<?> classes = bean.getClass();
        List<Method> allMethod = this.getAllMethodForClass(classes);
        List<Advisor> matchAdvisor = new ArrayList<>();
        for(Advisor advisor :this.advisors){
            if(advisor instanceof PointcutAdvisor){
                if(isPointcutMatchBean((PointcutAdvisor)advisor,classes,allMethod)){
                    matchAdvisor.add(advisor);
                }
            }
        }
        return matchAdvisor;
    }

    private boolean isPointcutMatchBean(PointcutAdvisor advisor, Class<?> classes, List<Method> allMethod) {
        PointCut pointCut = advisor.getPointCut();
        if(!pointCut.matchClass(classes)){
            return false;
        }
        for(Method method :allMethod){
            if(pointCut.matchMethod(method,classes)){
                return true;
            }
        }
        return false;
    }

    private List<Method> getAllMethodForClass(Class<?> classes) {
        List<Method> allMethods =new LinkedList<>();
        Set<Class<?>> methodClasses = new LinkedHashSet<>(ClassUtils.getAllInterfacesForClassAsSet(classes));
        methodClasses.add(classes);
        for(Class<?> clazz : methodClasses){
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
            for(Method method :methods){
                allMethods.add(method);
            }
        }
        return allMethods;
    }

    @Override
    public void registAdvisor(Advisor advisor) {
        this.advisors.add(advisor);
    }

    @Override
    public List<Advisor> getAdvisors() {
        return this.advisors;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
