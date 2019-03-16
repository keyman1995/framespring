package com.chen.spring.aop.proxy;

import com.chen.spring.aop.advice.AopAdviceInvocation;
import com.chen.spring.aop.advisor.Advisor;
import com.chen.spring.aop.advisor.PointCutAdvisor;
import com.chen.spring.factory.BeanFactory;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AopUtilsProxy {

    public static Object applyAdvice(Object target, Method method, Object[] args, List<Advisor> advisors, Object proxy, BeanFactory beanFactory){
        try {
            List<Object> adviceObject = AopUtilsProxy.shoudApplyAdvisors(advisors,method,target,beanFactory);
            if(CollectionUtils.isEmpty(adviceObject)){
                method.invoke(target,args);
            }else{
                AopAdviceInvocation aopAdviceInvocation = new AopAdviceInvocation(target,proxy,method,args,adviceObject);
                aopAdviceInvocation.invoke();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Object> shoudApplyAdvisors(List<Advisor> advisors,Method method,Object target,BeanFactory beanFactory) throws Exception {

        List<Object> advisorList = new ArrayList<>();
        for(Advisor advisor : advisors){
            if(advisor instanceof PointCutAdvisor){
                if(((PointCutAdvisor) advisor).getPointCut().matchMethod(method)){
                    advisorList.add(beanFactory.getBean(advisor.getAdviceBeanName()));
                }
            }
        }
        return advisorList;
    }
}
