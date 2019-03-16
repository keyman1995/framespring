package com.chen.springreview.testbean;

public class AABeanFactory {





    public static AABean getAABean3(BBean bBean){
        return new AABean(bBean);
    }

    public AABean getAABean4(BBean bBean){
        return new AABean(bBean);
    }

}
