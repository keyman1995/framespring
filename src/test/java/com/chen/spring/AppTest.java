package com.chen.spring;

import com.chen.spring.factory.BeanDefinitation;
import com.chen.spring.factory.DefaultBeafactory;
import com.chen.spring.factory.GenericBeanDefinition;
import com.chen.spring.testbean.ABean;
import com.chen.spring.testbean.ABeanFactory;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static DefaultBeafactory df = new DefaultBeafactory();

    @Test
    public void testRegist(){
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ABean.class);
        beanDefinition.setScope(BeanDefinitation.SCOPE_SINGLETION);
        beanDefinition.setInitMethodName("init");
        df.registerBeanDefinition("aBean",beanDefinition);
    }


    /**
     * 静态工厂闯创建实例
     */
    @Test
    public void testStaticFactoryRegist(){
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ABeanFactory.class);
        beanDefinition.setScope(BeanDefinitation.SCOPE_SINGLETION);
        beanDefinition.setFactoryMethodName("getABean");
        beanDefinition.setDetroyMethodName("destroy");
        df.registerBeanDefinition("staticBean",beanDefinition);
    }

    /**
     * 采用非静态工厂方法创建bean实例
     */
    @Test
    public void testFactoryRegist(){
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        //首先需要将工厂交给容器进行管理，先创建工厂实例，在创建bean实例
        beanDefinition.setBeanClass(ABeanFactory.class);
        String fbname = "factory";
        df.registerBeanDefinition(fbname,beanDefinition);


        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setFactoryBeanName(fbname);
        beanDefinition.setFactoryMethodName("getABean2");
        beanDefinition.setScope(BeanDefinitation.SCOPE_SINGLETION);
        df.registerBeanDefinition("factoryAbean",beanDefinition);
    }

    @AfterClass
    public static void testGetBean() throws Exception {
        System.out.println("构造方法方式------------------");
        ABean aBean = (ABean)df.getBean("aBean");
        aBean.doSomething();

        //采用静态工厂来创建的bean实例
        ABean aBean1 = (ABean)df.getBean("staticBean");
        aBean1.doSomething();

        //采用非静态方法来创建bean实例时，首先需要将创建bean的工厂进行实例化，在调用创建bean的实例操作
        ABean aBean2 = (ABean)df.getBean("factoryAbean");
        aBean2.doSomething();

        df.close();


    }
}
