package com.chen.spring.factory;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeafactory implements BeanFactory, BeanDefinitionRegistry {

    //bean 定义容器
    private static Map<String, BeanDefinitation> beanDefinitationMap = new ConcurrentHashMap<>();

    //bean 实例容器
    private static Map<String, Object> beanMap = new ConcurrentHashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinitation beanDefinitation) {
        if (StringUtils.isEmpty(beanName)) {
            throw new RuntimeException("beanName 不能为空");
        }
        if (beanDefinitation == null) {
            throw new RuntimeException("bean定义不能为空");
        }
        if (!beanDefinitationMap.containsKey(beanName)) {
            beanDefinitationMap.put(beanName, beanDefinitation);
        }

    }

    @Override
    public BeanDefinitation getBeanDefinition(String beanName) {
        return DefaultBeafactory.beanDefinitationMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitationMap.containsKey(beanName);
    }

    //创建bean实例，进行初始化
    @Override
    public Object getBean(String name) throws Exception {
        return this.doGetBean(name);
    }

    protected Object doGetBean(String beanName) throws Exception {
        if (StringUtils.isEmpty(beanName)) {
            throw new RuntimeException("beanName不能为空");
        }

        Object instance = beanMap.get(beanName);

        if (instance == null) {
            BeanDefinitation beanDefinitation = this.getBeanDefinition(beanName);
            if(beanDefinitation!=null){
                Class<?> beanClass = beanDefinitation.getBeanClass();
                if(beanClass!=null){
                    if(StringUtils.isEmpty(beanDefinitation.getFactoryMethodName())){
                        //表示采用构造方法创建bean对象
                        instance = this.createInstanceByConstructor(beanDefinitation);
                    }else{
                        //表示采用静态工厂方法创建beand对象
                        instance = this.createInstanceByFactoryMethod(beanDefinitation);
                    }
                }else{
                    //采用bean方式来创建bean对象
                    instance = this.createInstanceByFactoryBean(beanDefinitation);
                }
                //执行初始化方法
                this.doinit(beanDefinitation,instance);
                
                if(beanDefinitation.isSingleton()){
                    beanMap.put(beanName,instance);
                }
            }else {
                throw new RuntimeException("beanDefinitation 为空");
            }
          
        }
        

        return instance;
    }

    private Object createInstanceByFactoryBean(BeanDefinitation beanDefinitation) throws Exception {


        Object factoryBean = this.doGetBean(beanDefinitation.getFactoryBeanName());
        Method method = factoryBean.getClass().getMethod(beanDefinitation.getFactoryMethodName(),null);
        return method.invoke(factoryBean,null);

    }

    //静态工厂方法
    private Object createInstanceByFactoryMethod(BeanDefinitation beanDefinitation) throws Exception {
        Class<?> type = beanDefinitation.getBeanClass();
        Method method = type.getMethod(beanDefinitation.getFactoryMethodName(),null);
        return method.invoke(type,null);
    }

    //采用bean 方式来构造对象
    private Object createInstanceByConstructor(BeanDefinitation beanDefinitation) throws Exception{
        try{
            return beanDefinitation.getBeanClass().newInstance();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private void doinit(BeanDefinitation beanDefinitation, Object instance) {
        if(StringUtils.isNoneEmpty(beanDefinitation.getInitMethodName())){
            try {
                Method method = instance.getClass().getMethod(beanDefinitation.getInitMethodName(),null);
                method.invoke(instance,null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    //单例实例的销毁方法
    @Override
    public void close() {
        for(Map.Entry<String,BeanDefinitation> entry : beanDefinitationMap.entrySet()){
            String beanName = entry.getKey();
            BeanDefinitation beanDefination = entry.getValue();
            if(beanDefination.isSingleton() && StringUtils.isNoneEmpty(beanDefination.getDesttoryMethodName())){
                Object instance = beanMap.get(beanName);
                try{
                    Method method = instance.getClass().getMethod(beanDefination.getDesttoryMethodName(),null);
                    method.invoke(instance,null);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
