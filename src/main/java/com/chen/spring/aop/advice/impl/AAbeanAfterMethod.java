package com.chen.spring.aop.advice.impl;

import com.chen.spring.aop.advice.MethodAfterAdvice;

import java.lang.reflect.Method;

public class AAbeanAfterMethod implements MethodAfterAdvice {

    @Override
    public void methodAfter(Method method, Object target, Object[] objects, Object returnValue) {
        System.out.println("This is AfterMethod and the return Value"+returnValue);
    }
}
