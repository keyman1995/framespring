package com.chen.spring;

import com.chen.spring.factory.BeanReference;
import com.chen.spring.factory.DefaultBeafactory;
import com.chen.spring.factory.GenericBeanDefinition;
import com.chen.spring.testbean.ABean;
import com.chen.spring.testbean.BBean;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SpringBeanTest {

    private static DefaultBeafactory df = new DefaultBeafactory();

    @Test
    public void testConstruct() throws Exception {
        DefaultBeafactory defaultBeafactory = new DefaultBeafactory();
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ABean.class);
        List<Object> construtValue = new ArrayList<>();
        construtValue.add(new BeanReference("bBean"));
        construtValue.add("hello");
        beanDefinition.setConstructValues(construtValue);
        defaultBeafactory.registerBeanDefinition("aBean",beanDefinition);

        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(BBean.class);
        defaultBeafactory.registerBeanDefinition("bBean",beanDefinition);

        ABean aBean = (ABean) defaultBeafactory.getBean("aBean");


    }

}
