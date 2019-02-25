package com.chen.spring;

import com.chen.springreview.factoryreview.BeanDefinitionBack;
import com.chen.springreview.factoryreview.CommonBeanDefinition;
import com.chen.springreview.factoryreview.PreBuildFactoryBean;
import com.chen.springreview.testbean.AABean;
import com.chen.springreview.testbean.AABeanFactory;
import org.junit.AfterClass;
import org.junit.Test;

public class BppTest {

    private static PreBuildFactoryBean commonBeanFactory = new PreBuildFactoryBean();

    @Test
    public void AABeanRegistion() throws Exception {
        CommonBeanDefinition commonBeanDefinition = new CommonBeanDefinition();
        commonBeanDefinition.setBeanClass(AABean.class);
        commonBeanDefinition.setScope(BeanDefinitionBack.SINGE_SCOPE);
        commonBeanDefinition.setInitMethod("initMethod");
        commonBeanFactory.setBeanDefitionBack("aABean", commonBeanDefinition);

        commonBeanFactory.preInstanceBean();
    }


    @Test
    public void AABeanFactoryMethod() throws Exception {
        CommonBeanDefinition beanDefinition = new CommonBeanDefinition();
        beanDefinition.setBeanClass(AABeanFactory.class);
        beanDefinition.setScope(BeanDefinitionBack.SINGE_SCOPE);
        beanDefinition.setInitMethod("initMethod");
        beanDefinition.setStaticMethodName("getAABean");
        commonBeanFactory.setBeanDefitionBack("aABean1", beanDefinition);

        commonBeanFactory.preInstanceBean();
    }

    @Test
    public void aABeanFactroy() throws Exception {
        CommonBeanDefinition definition = new CommonBeanDefinition();
        definition.setBeanClass(AABeanFactory.class);
        definition.setScope(BeanDefinitionBack.SINGE_SCOPE);
        commonBeanFactory.setBeanDefitionBack("beanFactoy", definition);

        definition = new CommonBeanDefinition();
        definition.setBeanNameFactory("beanFactoy");
        definition.setScope(BeanDefinitionBack.SINGE_SCOPE);
        definition.setInitMethod("initMethod");
        definition.setFactoyBeanMethod("getAABean2");
        commonBeanFactory.setBeanDefitionBack("aAbean2", definition);

        commonBeanFactory.preInstanceBean();
    }

    @AfterClass
    public static void afterRegister() throws Exception {
        AABean aaBean = (AABean) commonBeanFactory.getBean("aABean");
        aaBean.doSomething();

        AABean aaBean1 = (AABean) commonBeanFactory.getBean("aABean1");
        aaBean1.doSomething();

        AABean aaBean2 = (AABean) commonBeanFactory.getBean("aAbean2");
        aaBean2.doSomething();
    }
}
