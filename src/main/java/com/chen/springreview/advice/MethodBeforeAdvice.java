package com.chen.springreview.advice;

import java.lang.reflect.Method;

public interface MethodBeforeAdvice extends Advice{

    /**
     *
     * @param method 方法本身
     * @param target 需要增强的对象
     * @param paramValues 参数值
     */
    void beforeMethod(Method method,Object target,Object[] paramValues);
}
