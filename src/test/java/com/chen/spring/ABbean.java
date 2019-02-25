package com.chen.spring;

import com.chen.springreview.factoryreview.BeanReference;
import com.chen.springreview.factoryreview.CommonBeanDefinition;
import com.chen.springreview.factoryreview.CommonBeanFactory;
import com.chen.springreview.testbean.AABean;
import com.chen.springreview.testbean.AABeanFactory;
import com.chen.springreview.testbean.BBean;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ABbean {

   static CommonBeanFactory commonBeanFactory = new CommonBeanFactory();

    @Test
    public void testConstructor() throws Exception {
        CommonBeanDefinition definition = new CommonBeanDefinition();
        definition.setBeanClass(AABean.class);
        List<Object> args = new ArrayList<>();
        args.add(new BeanReference("cbean"));
        definition.setConstructorArgumentValues(args);
        commonBeanFactory.setBeanDefitionBack("aAbean",definition);

        definition = new CommonBeanDefinition();
        definition.setBeanClass(BBean.class);
        definition.setConstructorArgumentValues(null);
        commonBeanFactory.setBeanDefitionBack("cbean",definition);

        AABean aaBean = (AABean)commonBeanFactory.getBean("aAbean");
        aaBean.doSomething();
    }

    @Test
    public void testByStaticMethod() throws Exception {
        CommonBeanDefinition definition = new CommonBeanDefinition();
        definition.setBeanClass(AABeanFactory.class);
        List<Object> args = new ArrayList<>();
        args.add(new BeanReference("cbean"));
        definition.setConstructorArgumentValues(args);
        definition.setStaticMethodName("getAABean3");
        commonBeanFactory.setBeanDefitionBack("aAbean",definition);

        definition = new CommonBeanDefinition();
        definition.setBeanClass(BBean.class);
        definition.setConstructorArgumentValues(null);
        commonBeanFactory.setBeanDefitionBack("cbean",definition);

        AABean aaBean = (AABean) commonBeanFactory.getBean("aAbean");
        aaBean.doSomething();
    }

    @Test
    public void testFactoryBeanMethod() throws Exception {
        CommonBeanDefinition definition = new CommonBeanDefinition();
        definition.setBeanNameFactory("aAbeanFactory");
        definition.setStaticMethodName("getAABean4");
        List<Object> args = new ArrayList<>();
        args.add(new BeanReference("cbean"));
        definition.setConstructorArgumentValues(args);
        commonBeanFactory.setBeanDefitionBack("aAbean",definition);

        definition = new CommonBeanDefinition();
        definition.setBeanClass(BBean.class);
        definition.setConstructorArgumentValues(null);
        commonBeanFactory.setBeanDefitionBack("cbean",definition);

        definition = new CommonBeanDefinition();
        definition.setBeanClass(AABeanFactory.class);
        commonBeanFactory.setBeanDefitionBack("aAbeanFactory",definition);

        AABean aaBean = (AABean) commonBeanFactory.getBean("aAbean");
        aaBean.doSomething();
    }





    @AfterClass
    public static void testConstructorBy(){

    }
}
