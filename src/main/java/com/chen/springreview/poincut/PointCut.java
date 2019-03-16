package com.chen.springreview.poincut;

import java.lang.reflect.Method;

public interface PointCut {

    boolean matchClass(Class<?> targetClass);

    boolean matchMethod(Method method,Class<?> targetClass);

}
