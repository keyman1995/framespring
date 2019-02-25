package com.chen.springreview.testbean;

public class AABean {

    private BBean bBean;

    public AABean() {
    }

    public AABean(BBean bBean) {
        this.bBean = bBean;
    }

    public void doSomething(){
        System.out.println("AABean doSomething  "+this);
    }

    public void initMethod(){
        System.out.println("AABean initDoSomething");
    }

    public void destroy(){
        System.out.println("AABean destroyed");
    }
}



