package com.chen.spring.testbean;

public class ABeanFactory {

    /**
     * 静态工厂
     * @return
     */
    public static ABean getABean(){
        return new ABean();
    }

    /**
     * 非静态工厂
     * @return
     */
    public ABean getABean2(){
        return new ABean();
    }
}
