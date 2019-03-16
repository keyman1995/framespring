package com.chen.spring.aop.advisor;

import com.chen.spring.aop.pointcut.PointCut;
import com.chen.spring.aop.postprocessor.BeanPostProcessor;
import com.chen.spring.aop.proxy.AopProxyFactory;
import com.chen.spring.aop.proxy.DefaultAopFactory;
import com.chen.spring.factory.BeanFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Aop 增强处理的观察者实现
public class AdvisorAutoProxyCreator implements BeanPostProcessor,AdvisorRegistry {


    private List<Advisor> advisors;

    private BeanFactory beanFactory;

    public AdvisorAutoProxyCreator() {
        this.advisors = new ArrayList<>();
    }

    @Override
    public void registAdvisor(Advisor advisor) {
        advisors.add(advisor);
    }

    @Override
    public List<Advisor> getAdvisors() {
        return this.advisors;
    }

    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean,BeanFactory beanFactory) {
        //判断该bean实例是否需要增强
        List<Advisor> matchedAdvisora = getMatchedAdvisor(bean);
        if(CollectionUtils.isEmpty(matchedAdvisora)){
            return bean;
        }else{
            try {
                this.beanFactory = beanFactory;
              return   this.createProxy(beanName,bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    private Object createProxy(String beanName, Object bean) throws Exception {
        AopProxyFactory aopProxyFactory = new DefaultAopFactory();
       return aopProxyFactory.createAopProxy(bean,beanName,this.advisors,beanFactory).getProxy();
    }

    private List<Advisor> getMatchedAdvisor(Object bean) {
        List<Advisor> matchedAdvisora = new ArrayList<>();
        List<Method> allMethods = this.getAllMethod(bean.getClass());
        for(Advisor advisor :this.advisors){
            if(advisor instanceof AspectJPointcutAdvisor){
               if(isAdvisorPointCut((PointCutAdvisor)advisor,allMethods,bean)){
                   matchedAdvisora.add(advisor);
               }
            }
        }


        return matchedAdvisora;
    }

    private boolean isAdvisorPointCut(PointCutAdvisor advisor, List<Method> allMethods, Object bean) {

        PointCut pointCut = advisor.getPointCut();

        if(!pointCut.matchClass(bean.getClass())){
            return false;
        }

        for(Method method : allMethods){
            if(pointCut.matchMethod(method)){
                return true;
            }else{
               continue;
            }
        }
        return false;
    }

    private List<Method> getAllMethod(Class<?> aClass) {
        List<Method> methods = new ArrayList<>();
        Set<Class<?>> classSet = new HashSet<>(ClassUtils.getAllInterfacesForClassAsSet(aClass));
        classSet.add(aClass);
        for(Class<?> fatherClass : classSet){
            Method[] fatherMethod = ReflectionUtils.getAllDeclaredMethods(fatherClass);
            for(Method method : fatherMethod){
                methods.add(method);
            }
        }

        return methods;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
