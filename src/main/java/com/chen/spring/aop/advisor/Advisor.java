package com.chen.spring.aop.advisor;

public interface Advisor {

    //根据advice 进行实例化相应的bean
    String getAdviceBeanName();

    //获取pointCut的expression表达式
    String getExpression();
}
