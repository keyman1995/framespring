package com.chen.springreview.factoryreview;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public interface BeanDefinitionBack {

    String SINGE_SCOPE = "single";

    String PROTORY_SCOPE="protory";

    Class<?> getBeanClass();

    //采用静态工厂创建bean
    String getFactoryMethodBean();

    //采用的静态工厂bean
    String getBeanByFactory();

    //采用工厂beanc创建实例
    String getFactoryBeanMethodName();

    boolean isSingle();

    String getInitMethod();

    List<?> getConstructorArgumentValues();

    Constructor<?> getConstructor();

    void setConstructor( Constructor<?> constructor);

    Method getFactoryMethod();

    void setFactoryMethod(Method method);


}
