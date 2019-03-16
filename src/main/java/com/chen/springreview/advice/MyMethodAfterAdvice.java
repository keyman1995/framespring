package com.chen.springreview.advice;

import java.lang.reflect.Method;

public class MyMethodAfterAdvice implements MethodAfterAdvice {


    @Override
    public void afterReturning(Method method, Object target, Object[] objectsd, Object returnValue) {
        System.out.println(this+"对"+target+"做了后置增强，得到的返回值为："+returnValue);
    }
}
