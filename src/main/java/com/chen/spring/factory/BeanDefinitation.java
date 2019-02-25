package com.chen.spring.factory;

/**
 * bean定义 告诉bean工厂如何去创建bean
 */
public interface BeanDefinitation {


    String SCOPE_SINGLETION="singleton";

    String SCOPE_PROTOTYPE="prototype";

    Class<?> getBeanClass();

    String getScope();

    boolean isSingleton();

    boolean isPrototype();

    //采用成员创建bean时，需要获得bean的名称
    String getFactoryBeanName();

    //该方法是对于采取静态工厂 创建bean时，需要的方法名
    String getFactoryMethodName();

    //初始化方法
    String getInitMethodName();

    //释放bean需要的方法
    String getDesttoryMethodName();

}
