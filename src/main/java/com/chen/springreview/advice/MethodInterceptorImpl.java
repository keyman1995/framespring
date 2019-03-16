package com.chen.springreview.advice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInterceptorImpl implements MethodInterceptor {

    @Override
    public Object invoke(Method method, Object target, Object[] objects) throws InvocationTargetException, IllegalAccessException {
        System.out.println(this+"对"+target+"做了环绕增强");
        Object ret = method.invoke(target,objects);
        System.out.println(this + "对 " + target + "进行了环绕 --后增强。方法的返回值为：" + ret);
        return ret;
    }
}
