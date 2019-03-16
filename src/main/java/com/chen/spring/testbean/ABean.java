package com.chen.spring.testbean;

public class ABean {

    private BBean bBean;

    private String name;

    public ABean() {
    }

    public ABean(BBean bBean) {
        this.bBean = bBean;
    }

    public ABean(BBean bBean, String name) {
        this.bBean = bBean;
        this.name = name;
    }

    public void doSomething(){
        System.out.println(System.currentTimeMillis()+" "+this);
    }

    public void doAnyThing(String str){
        System.out.println("This is "+str);
    }

    public String sayAfter(String name){
        System.out.println("This is After returnValue"+name);
        return name;
    }

    public String showSomething(String name){
        System.out.println("This is ShowMethod"+ name);
        return name;
    }

    public void init(){
        System.out.println("ABean.init()执行了");
    }

    public void destroy(){
        System.out.println("ABean.destroy()执行了");
    }
}
