package com.chen.springreview.factoryreview;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class CommonBeanDefinition implements BeanDefinitionBack {

    private Class<?> beanClass;

    private String staticMethodName;

    private String beanNameFactory;

    private String initMethod;

    private String factoyBeanMethod;

    //单例默认是单例bean
    private String scope = BeanDefinitionBack.SINGE_SCOPE;

    private List<?> constructorArgumentValues;

    private Constructor<?> constructor;

    private Method factroyMethod;

    private List<PropertyValue> propertyValues;

    private String aliasName;

    private List<String> limitBeanClass;

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setStaticMethodName(String staticMethodName) {
        this.staticMethodName = staticMethodName;
    }

    public void setBeanNameFactory(String beanNameFactory) {
        this.beanNameFactory = beanNameFactory;
    }

    public void setScope(String scope) {
        if(StringUtils.isEmpty(scope)){
            this.scope = BeanDefinitionBack.PROTORY_SCOPE;
        }
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    public void setFactoyBeanMethod(String factoyBeanMethod) {
        this.factoyBeanMethod = factoyBeanMethod;
    }

    public void setConstructorArgumentValues(List<?> constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public void setPropertyValues(List<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public void setLimitBeanClass(List<String> limitBeanClass) {
        this.limitBeanClass = limitBeanClass;
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return this.propertyValues;
    }

    @Override
    public void setFactoryMethod(Method method) {
        this.factroyMethod = method;
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public String getFactoryMethodBean() {
        return this.staticMethodName;
    }

    @Override
    public String getBeanByFactory() {
        return this.beanNameFactory;
    }

    @Override
    public boolean isSingle() {
        return StringUtils.equals(this.scope,BeanDefinitionBack.SINGE_SCOPE);
    }

    @Override
    public String getInitMethod() {
        return this.initMethod;
    }

    @Override
    public String getFactoryBeanMethodName() {
        return this.factoyBeanMethod;
    }

    @Override
    public List<?> getConstructorArgumentValues() {
        return this.constructorArgumentValues;
    }

    @Override
    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    @Override
    public Method getFactoryMethod() {
        return this.factroyMethod;
    }

    @Override
    public String getAliasName() {
        return this.aliasName;
    }

    @Override
    public List<String> getLimitBeanClass() {
        return this.limitBeanClass;
    }
}
