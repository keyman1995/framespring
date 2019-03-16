package com.chen.spring.aop.advice;

import java.lang.reflect.Method;

public interface MethodAfterAdvice extends Advice {

    /**
     * 后置增强就是在方法运行之后进行增强
     * @param method 方法本身
     * @param target 方法所属对象
     * @param objects 参数
     * @param returnValue 范沪指
     */
    void methodAfter(Method method,Object target,Object[] objects,Object returnValue);

}
