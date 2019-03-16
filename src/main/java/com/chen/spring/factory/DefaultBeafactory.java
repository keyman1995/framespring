package com.chen.spring.factory;

import com.chen.spring.aop.postprocessor.BeanPostProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeafactory implements BeanFactory, BeanDefinitionRegistry {

    //bean 定义容器
    private static Map<String, BeanDefinitation> beanDefinitationMap = new ConcurrentHashMap<>();

    //bean 实例容器
    private static Map<String, Object> beanMap = new ConcurrentHashMap<>();

    private static List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

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

                if(!CollectionUtils.isEmpty(beanPostProcessors)){

                    for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
                       instance =  beanPostProcessor.postProcessAfterInitialization(beanName,instance,this);
                    }
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
            List<Object> constructValues = beanDefinitation.getConstructValue();
            if(CollectionUtils.isEmpty(constructValues)){
                return beanDefinitation.getBeanClass().newInstance();
            }else{
                //获取所有的参数
                Object[] realValues = getRealValues(constructValues);
                Constructor<?> constructor = this.determinConstructor(beanDefinitation,realValues);
                ((GenericBeanDefinition)beanDefinitation).setConstructor(constructor);
                return constructor.newInstance(realValues);
            }

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private Constructor<?> determinConstructor(BeanDefinitation beanDefinitation, Object[] realValues) {

        Constructor<?> constructor = null;
        constructor = beanDefinitation.getConstructor();
        if(constructor!=null){
            return constructor;
        }
        Class<?>[] paramTypes = new Class[realValues.length];
        int i = 0;
        for(Object p : realValues){
            paramTypes[i++] = p.getClass();
        }
        try{
            //根据参数类型进行匹配
            constructor = beanDefinitation.getBeanClass().getConstructor(paramTypes);
        }catch (Exception e){

        }
        if(constructor==null){
            outer:for(Constructor<?> constructor1 : beanDefinitation.getBeanClass().getConstructors()){
                Class<?>[] ctp1 = constructor1.getParameterTypes();
                if(realValues.length==ctp1.length){
                    for(int j=0;j<ctp1.length;j++){
                        //如果 参数值 不能复制给这个参数类型，就证明不是
                        if(!ctp1[j].isAssignableFrom(realValues[i].getClass())){
                            continue outer;
                        }
                    }
                    constructor = constructor1;
                    break outer;
                }
            }
        }
        if(constructor==null){
            throw new RuntimeException("没有找到相应的构造函数");
        }
        return constructor;

    }

    public Object[] getRealValues(List<Object> constructValues) throws Exception {
        Object[] realValues = new Object[constructValues.size()];
        int i = 0;
        for(Object o : constructValues){
            Object paramValue = null;
            if(o instanceof BeanReference){
                paramValue = this.getBean(((BeanReference) o).getBeanName());
            }else if(o instanceof Map){

            }else if(o instanceof Properties){

            }else{
                paramValue = o;
            }
            realValues[i++] = paramValue;
        }

        return realValues;
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

    @Override
    public void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
         beanPostProcessors.add(beanPostProcessor);
    }
}
