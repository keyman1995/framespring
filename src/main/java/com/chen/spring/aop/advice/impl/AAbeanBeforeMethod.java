package com.chen.spring.aop.advice.impl;

import com.chen.spring.aop.advice.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class AAbeanBeforeMethod implements MethodBeforeAdvice {


    @Override
    public void methodBefore(Method method, Object target, Object[] objects) {
        System.out.println("This is before Method"+objects.toString());
    }
}
