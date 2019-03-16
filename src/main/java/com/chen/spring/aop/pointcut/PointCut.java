package com.chen.spring.aop.pointcut;

import java.lang.reflect.Method;

/**
 * 切点的主要作用是用来匹配类和匹配方法的
 */
public interface PointCut {

    /**
     * 需要匹配的类
     * @param targetClass
     * @return
     */
    boolean matchClass(Class<?> targetClass);


    /**
     * 需要匹配的方法
     * @param method
     * @return
     */
    boolean matchMethod(Method method);

}
