package com.chen.spring.aop.advice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AopAdviceInvocation {

    private Object target;
    private Object proxy;
    private Method method;
    private Object[] args;
    private List<Object> adviceList;

    private static Method invokeMethod;

    static {
        try {
            invokeMethod = AopAdviceInvocation.class.getMethod("invoke", null);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }


    public AopAdviceInvocation(Object target, Object proxy, Method method, Object[] args, List<Object> adviceList) {
        this.target = target;
        this.proxy = proxy;
        this.method = method;
        this.args = args;
        this.adviceList = adviceList;
    }

    private int i = 0;

    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        if (i < this.adviceList.size()) {
            Object advice = this.adviceList.get(i++);
            if (advice instanceof MethodBeforeAdvice) {
                ((MethodBeforeAdvice) advice).methodBefore(method, target, args);
            } else if (advice instanceof MethodAfterAdvice) {
                Object returnValue = this.invoke();
                ((MethodAfterAdvice) advice).methodAfter(method, target, args, returnValue);
                return returnValue;
            } else if (advice instanceof MethodSurrournAdvice) {
                return ((MethodSurrournAdvice) advice).invoke(invokeMethod,this,args);
            }
            return this.invoke();
        }else{
            return method.invoke(target,args);
        }
    }

}
