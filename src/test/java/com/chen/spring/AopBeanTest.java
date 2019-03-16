package com.chen.spring;

import com.chen.springreview.advice.MethodInterceptorImpl;
import com.chen.springreview.advice.MyMethodAfterAdvice;
import com.chen.springreview.advice.MyMethodBeforeAdvice;
import com.chen.springreview.advisor.AspectJPointcutAdvisor;
import com.chen.springreview.beanpost.AdvisorAutoProxyCreator;
import com.chen.springreview.factoryreview.BeanReference;
import com.chen.springreview.factoryreview.CommonBeanDefinition;
import com.chen.springreview.factoryreview.PreBuildFactoryBean;
import com.chen.springreview.testbean.AABean;
import com.chen.springreview.testbean.BBean;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AopBeanTest {

    private static PreBuildFactoryBean preBean = new PreBuildFactoryBean();

    @Test
    public void testAop() throws Exception {
        CommonBeanDefinition beanDefinition = new CommonBeanDefinition();
        beanDefinition.setBeanClass(AABean.class);
        List<Object> args = new ArrayList<>();
        args.add(new BeanReference("bBean"));
        beanDefinition.setConstructorArgumentValues(args);
        beanDefinition.setInitMethod("initMethod");
        preBean.setBeanDefitionBack("aAbean",beanDefinition);

        beanDefinition = new CommonBeanDefinition();
        beanDefinition.setBeanClass(BBean.class);
        preBean.setBeanDefitionBack("bBean",beanDefinition);

        beanDefinition = new CommonBeanDefinition();
        beanDefinition.setBeanClass(MyMethodBeforeAdvice.class);
        preBean.setBeanDefitionBack("myBeforeAdvice",beanDefinition);

        beanDefinition = new CommonBeanDefinition();
        beanDefinition.setBeanClass(MyMethodAfterAdvice.class);
        preBean.setBeanDefitionBack("myAfterAdvice",beanDefinition);

        beanDefinition = new CommonBeanDefinition();
        beanDefinition.setBeanClass(MethodInterceptorImpl.class);
        preBean.setBeanDefitionBack("myInterceptor",beanDefinition);

        AdvisorAutoProxyCreator advisorAutoProxyCreator = new AdvisorAutoProxyCreator();
        advisorAutoProxyCreator.registAdvisor(new AspectJPointcutAdvisor("execution(* com.chen.springreview.testbean.AABean.do*(..))","myBeforeAdvice"));
        advisorAutoProxyCreator.registAdvisor(new AspectJPointcutAdvisor("execution(* com.chen.springreview.testbean.AABean.say*(..))","myAfterAdvice"));
        advisorAutoProxyCreator.registAdvisor(new AspectJPointcutAdvisor("execution(* com.chen.springreview.testbean.AABean.to*(..))","myInterceptor"));
        preBean.registerBeanPostProcessor(advisorAutoProxyCreator);

        preBean.preInstanceBean();

        AABean aaBean = (AABean) preBean.getBean("aAbean");

        aaBean.doSomething();

        aaBean.saySomething("Hello World");

        aaBean.toDoSomething("THIS IS AAbean");

    }
}
