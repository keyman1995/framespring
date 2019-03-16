package com.chen.spring.aop.advice;

import java.lang.reflect.Method;

public interface MethodSurrournAdvice extends Advice {

    /**
     * 换绕增强 在方法执行过程中进行增强(其中就包括如果抛了异常也需要处理)
     * @param method 方法本身
     * @param target 方法的类
     * @param arge 方法参数
     * @return
     */
    Object invoke(Method method,Object target,Object[] arge);
}
