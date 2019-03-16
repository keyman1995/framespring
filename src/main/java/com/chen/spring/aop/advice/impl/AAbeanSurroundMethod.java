package com.chen.spring.aop.advice.impl;

import com.chen.spring.aop.advice.MethodSurrournAdvice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AAbeanSurroundMethod implements MethodSurrournAdvice {
    @Override
    public Object invoke(Method method, Object target, Object[] arge) {
        System.out.println("执行方法前做了什么");
        try {
            Object returnValue = method.invoke(target,null);
            System.out.println("执行方法之后又在干什么"+returnValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
