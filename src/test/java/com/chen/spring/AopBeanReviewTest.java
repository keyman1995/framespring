package com.chen.spring;

import com.chen.spring.aop.advice.MethodSurrournAdvice;
import com.chen.spring.aop.advice.impl.AAbeanAfterMethod;
import com.chen.spring.aop.advice.impl.AAbeanBeforeMethod;
import com.chen.spring.aop.advice.impl.AAbeanSurroundMethod;
import com.chen.spring.aop.advisor.AdvisorAutoProxyCreator;
import com.chen.spring.aop.advisor.AspectJPointcutAdvisor;
import com.chen.spring.factory.BeanReference;
import com.chen.spring.factory.DefaultBeafactory;
import com.chen.spring.factory.GenericBeanDefinition;
import com.chen.spring.testbean.ABean;
import com.chen.spring.testbean.BBean;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AopBeanReviewTest {


    private static DefaultBeafactory defaultBeafactory = new DefaultBeafactory();

    @Test
    public void testReview() throws Exception {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ABean.class);
        List<Object> args = new ArrayList<>();
        args.add(new BeanReference("bBean"));
        args.add("Hello");
        beanDefinition.setConstructValues(args);
        defaultBeafactory.registerBeanDefinition("aAbean", beanDefinition);

        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(BBean.class);
        defaultBeafactory.registerBeanDefinition("bBean",beanDefinition);

        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(AAbeanBeforeMethod.class);
        defaultBeafactory.registerBeanDefinition("aAbeanBeforeMethod", beanDefinition);
        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(AAbeanAfterMethod.class);
        defaultBeafactory.registerBeanDefinition("aAbeanAfterMethod",beanDefinition);
        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(MethodSurrournAdvice.class);
        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(AAbeanSurroundMethod.class);
        defaultBeafactory.registerBeanDefinition("aAbeanSurroundMethod",beanDefinition);


        AdvisorAutoProxyCreator advisorAutoProxyCreator = new AdvisorAutoProxyCreator();
        advisorAutoProxyCreator.registAdvisor(new AspectJPointcutAdvisor("aAbeanBeforeMethod", "execution (* com.chen.spring.testbean.ABean.do*(..))"));
        advisorAutoProxyCreator.registAdvisor(new AspectJPointcutAdvisor("aAbeanAfterMethod","execution (* com.chen.spring.testbean.ABean.say*(..))"));
        advisorAutoProxyCreator.registAdvisor(new AspectJPointcutAdvisor("aAbeanSurroundMethod","execution (* com.chen.spring.testbean.ABean.show*(..))"));
        defaultBeafactory.registerBeanPostProcessor(advisorAutoProxyCreator);

        ABean aBean = (ABean) defaultBeafactory.getBean("aAbean");
        aBean.doSomething();
        aBean.doAnyThing("ChenJie");
        aBean.sayAfter("ZhanSan");
        aBean.showSomething("WangWu");
    }
}
