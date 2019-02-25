package com.chen.springreview.factoryreview;

import java.util.ArrayList;
import java.util.List;

public class PreBuildFactoryBean extends CommonBeanFactory {

    private List<String> beanNames = new ArrayList<>();

    @Override
    public void setBeanDefitionBack(String beanName, BeanDefinitionBack beanDefitionBack) {
        super.setBeanDefitionBack(beanName, beanDefitionBack);
        synchronized (beanNames){
            beanNames.add(beanName);
        }
    }

    public void preInstanceBean() throws Exception {
        synchronized (beanNames){
            for(String beanName:beanNames){
                BeanDefinitionBack definitionBack = this.getBeanDefinitionBack(beanName);
                if(definitionBack.isSingle()){
                    this.getBean(beanName);
                }
            }
        }
    }

}
