package com.chen.springreview.advice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AopAdviceChainInvocation {

    private static Method invokeMethod;

    static {
        try {
            invokeMethod = AopAdviceChainInvocation.class.getMethod("invoke", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private Object proxy;

    private Object target;

    private Method method;

    private Object[] args;

    private List<Object> advices;

    public AopAdviceChainInvocation(Object proxy, Object target, Method method, Object[] args, List<Object> advices) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.args = args;
        this.advices = advices;
    }

    private int i = 0;

    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        if (i < this.advices.size()) {
            Object advice = this.advices.get(i++);
            if (advice instanceof MethodBeforeAdvice) {
                ((MethodBeforeAdvice) advice).beforeMethod(invokeMethod, target, args);
            } else if (advice instanceof MethodInterceptor) {
                return ((MethodInterceptor) advice).invoke(invokeMethod, this, null);
            } else if (advice instanceof MethodAfterAdvice) {
                Object returnValue = this.invoke();
                ((MethodAfterAdvice) advice).afterReturning(method, target, args, returnValue);
                return returnValue;
            }
            return this.invoke();
        } else {
            return method.invoke(target, args);
        }

    }


}
