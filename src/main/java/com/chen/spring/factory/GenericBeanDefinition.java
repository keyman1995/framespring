package com.chen.spring.factory;

import org.apache.commons.lang3.StringUtils;

public class GenericBeanDefinition implements BeanDefinitation {

    private Class<?> beanClass;

    private String factoryBeanName;

    private String factoryMethodName;

    private String initMethodName;

    private String detroyMethodName;

    private String scope = BeanDefinitation.SCOPE_PROTOTYPE;

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public boolean isSingleton() {
        return this.scope.equals(BeanDefinitation.SCOPE_SINGLETION);
    }

    @Override
    public boolean isPrototype() {
        return this.scope.equals(BeanDefinitation.SCOPE_PROTOTYPE);
    }

    @Override
    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }

    @Override
    public String getFactoryMethodName() {
        return this.factoryMethodName;
    }

    @Override
    public String getInitMethodName() {
        return this.initMethodName;
    }

    @Override
    public String getDesttoryMethodName() {
        return this.detroyMethodName;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public void setDetroyMethodName(String detroyMethodName) {
        this.detroyMethodName = detroyMethodName;
    }

    public void setScope(String scope) {
        if(StringUtils.isNoneEmpty(scope)){
            this.scope=scope;
        }
    }
}
