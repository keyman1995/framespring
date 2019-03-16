package com.chen.spring.aop.proxy;

public interface AopProxy {

    Object getProxy() throws Exception;

    Object getProxy(ClassLoader classLoader);

}
