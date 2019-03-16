package com.chen.spring.aop.advice;

import java.lang.reflect.Method;

//前置增强
public interface MethodBeforeAdvice extends Advice {

    /**
     * 前置增强需要知道方法本身，方法所属的对象，方法所在的参数
     * @param method
     * @param target
     * @param objects
     */
    void methodBefore(Method method,Object target,Object[] objects);

}
