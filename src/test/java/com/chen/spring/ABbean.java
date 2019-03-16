package com.chen.spring;

import com.chen.springreview.factoryreview.*;
import com.chen.springreview.testbean.AABean;
import com.chen.springreview.testbean.AABeanFactory;
import com.chen.springreview.testbean.BBean;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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


    @Test
    public void testABbean() throws Exception {
        CommonBeanDefinition definition = new CommonBeanDefinition();
        definition.setBeanClass(BBean.class);
        definition.setConstructorArgumentValues(null);
        PropertyValue propertyValue = new PropertyValue("name","zhangsan");
        PropertyValue propertyValue1 = new PropertyValue("aaBean",new BeanReference("aaBean"));
        List<PropertyValue> propertyValues = new ArrayList<>();
        propertyValues.add(propertyValue);
        propertyValues.add(propertyValue1);
        definition.setPropertyValues(propertyValues);
        commonBeanFactory.setBeanDefitionBack("bbean",definition);

        definition = new CommonBeanDefinition();
        definition.setBeanClass(AABean.class);
        definition.setConstructorArgumentValues(null);
        definition.setScope(BeanDefinitionBack.SINGE_SCOPE);
        commonBeanFactory.setBeanDefitionBack("aaBean",definition);

        BBean bBean = (BBean) commonBeanFactory.getBean("bbean");
        bBean.getName();
        bBean.getAaBean();

    }

    @Test
    public void testProperties() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getResourceAsStream("/bean.properties");
        properties.load(inputStream);
        CommonBeanDefinition definition = new CommonBeanDefinition();
        definition.setBeanClass(BBean.class);
        definition.setConstructorArgumentValues(null);
        List<PropertyValue> propertyValues = new ArrayList<>();
        PropertyValue propertyValue = new PropertyValue("name",properties);
        propertyValues.add(propertyValue);
        definition.setPropertyValues(propertyValues);
        commonBeanFactory.setBeanDefitionBack("bbean",definition);

        BBean bBean = (BBean)commonBeanFactory.getBean(BBean.class);
        bBean.getName();
    }





    @AfterClass
    public static void testConstructorBy(){

    }
}
