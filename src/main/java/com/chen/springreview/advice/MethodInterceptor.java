package com.chen.springreview.advice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface MethodInterceptor extends Advice {

    /**
     *
     * @param method 需要增强的方法
     * @param target 需要增强的对象
     * @param objects 方法的参数
     * @return
     */
    Object invoke(Method method,Object target,Object[] objects) throws InvocationTargetException, IllegalAccessException;

}
