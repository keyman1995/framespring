package com.chen.springreview.testbean;

public class AABean {

    private BBean bBean;

    public AABean(BBean bBean) {
        this.bBean = bBean;
    }

    public void doSomething(){
        System.out.println("AABean doSomething  "+this);
    }

    public String saySomething(String word){
        System.out.println("AABean saySomething and return");
        return word;
    }

    public String toDoSomething(String someThing){
        System.out.println("AABean toDoSomething");
        return someThing;
    }

    public void initMethod(){
        System.out.println("AABean initDoSomething");
    }

    public void destroy(){
        System.out.println("AABean destroyed");
    }
}



