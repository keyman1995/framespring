package com.chen.springreview.proxy;

import com.chen.springreview.advice.AopAdviceChainInvocation;
import com.chen.springreview.advisor.Advisor;
import com.chen.springreview.advisor.PointcutAdvisor;
import com.chen.springreview.factoryreview.BeanFactory;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AopProxyUtils {


   public static Object applyAdvice(Object target, Method method, Object[] args, List<Advisor> matchAdvisorss, Object proxy, BeanFactory beanFactory) throws Exception {
       List<Object> advices = AopProxyUtils.getShouldApplyAdvices(target.getClass(),method,matchAdvisorss,beanFactory);
       if(CollectionUtils.isEmpty(advices)){
           return method.invoke(target,args);
       }else{
           AopAdviceChainInvocation chain = new AopAdviceChainInvocation(proxy,target,method,args,advices);
           return  chain.invoke();
       }
    }


    public static List<Object> getShouldApplyAdvices(Class<?> beanClass,Method method,List<Advisor> matchAdvisors,BeanFactory beanFactory) throws Exception {
       if(CollectionUtils.isEmpty(matchAdvisors)){
           return null;
       }
       List<Object> advices = new ArrayList<>();
       for(Advisor advisor : matchAdvisors){
           if(((PointcutAdvisor)advisor).getPointCut().matchMethod(method,beanClass)){
               advices.add(beanFactory.getBean(advisor.getAdviceBeanName()));
           }
       }
       return advices;
    }

}
