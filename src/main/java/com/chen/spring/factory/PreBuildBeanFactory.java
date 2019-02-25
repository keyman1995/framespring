package com.chen.spring.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * 提前创建单列bean
 */
public class PreBuildBeanFactory extends DefaultBeafactory {


    private List<String> beanNames = new ArrayList<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinitation beanDefinitation) {
        super.registerBeanDefinition(beanName, beanDefinitation);
        //同步控制添加到beaNames;
        synchronized (beanNames){
            beanNames.add(beanName);
        }
    }

    /**
     * 单例模式 提前调用
     * @throws Exception
     */
    public void preInstantiateSingletons() throws Exception {
        synchronized (beanNames){
            for(String name :beanNames){
                BeanDefinitation beanDefinitation = this.getBeanDefinition(name);
                if(beanDefinitation.isSingleton()){
                    this.doGetBean(name);
                }
            }
        }
    }

}
