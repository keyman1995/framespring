package com.chen.springreview.advice;

import java.lang.reflect.Method;

public class MyMethodBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void beforeMethod(Method method, Object target, Object[] paramValues) {
        System.out.println(this+"d对"+target+"做了前置增强");
    }
}
