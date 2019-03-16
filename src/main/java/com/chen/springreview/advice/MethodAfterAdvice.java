package com.chen.springreview.advice;

import java.lang.reflect.Method;

public interface MethodAfterAdvice extends Advice{

    /**
     *
     * @param method 需要增强的方法
     * @param target 目标函数
     * @param objectsd 参数值
     * @param returnValue 返回值
     */
    void afterReturning(Method method,Object target,Object[] objectsd,Object returnValue);
}
