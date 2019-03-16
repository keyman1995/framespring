package com.chen.springreview.proxy;

public interface AopProxy {


    Object getProxy();

    Object getProxy(ClassLoader classLoader);

}
