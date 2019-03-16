package com.chen.springreview.factoryreview;

import com.chen.springreview.beanpost.BeanPostProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommonBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    //这个是用来存放初始化的bean实例
    private static Map<String, Object> beanMap = new ConcurrentHashMap<>();

    //这个用来存放BeanDefinitionBack
    private static Map<String, BeanDefinitionBack> beanDefinitionMap = new ConcurrentHashMap<>();

    //这个是用来存放class名称的list
    private static List<String> classList = new ArrayList<>();

    //这个是用来存放观察者接口的
    private  List<BeanPostProcessor> beanPosts = Collections.synchronizedList(new ArrayList<BeanPostProcessor>());


    @Override
    public void setBeanDefitionBack(String beanName, BeanDefinitionBack beanDefitionBack) {
        if (!this.isContainBeanDefinitionBack(beanName)) {
            if (StringUtils.isNoneEmpty(beanName)) {
                beanDefinitionMap.put(beanName, beanDefitionBack);
            }
            if (StringUtils.isNoneEmpty(beanDefitionBack.getAliasName())) {
                beanDefinitionMap.put(beanDefitionBack.getAliasName(), beanDefitionBack);
            }
            if(beanDefitionBack.getBeanClass()!=null){
                classList.add(beanDefitionBack.getBeanClass().getName());
            }
        } else {
            throw new RuntimeException("beanName" + beanName + "已存在");
        }
    }

    @Override
    public BeanDefinitionBack getBeanDefinitionBack(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean isContainBeanDefinitionBack(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return this.doGetBean(beanName);
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        for (Map.Entry<String, BeanDefinitionBack> beanDefinition : beanDefinitionMap.entrySet()) {
            if (beanDefinition.getValue().getBeanClass() == beanClass) {
                return this.doGetBean(beanDefinition.getKey());
            }
        }
        return null;
    }

    @Override
    public void registerBeanPostProcessor(BeanPostProcessor bpp) {
        this.beanPosts.add(bpp);
        if(bpp instanceof BeanFactoryAware){
            ((BeanFactoryAware) bpp).setBeanFactory(this);
        }

    }

    private Object doGetBean(String beanName) throws Exception {

        Object instance = beanMap.get(beanName);
        if (instance != null) {
            return instance;
        }
        BeanDefinitionBack beanDefinitionBack = beanDefinitionMap.get(beanName);
        if (beanDefinitionBack != null) {
            Class<?> beanClass = beanDefinitionBack.getBeanClass();
            if (beanClass != null) {
                if (StringUtils.isEmpty(beanDefinitionBack.getFactoryMethodBean())) {
                    instance = this.newInstanceByConstructor(beanDefinitionBack);
                } else {
                    instance = this.newInstanceByFactoryMethod(beanDefinitionBack);
                }
            } else {
                instance = this.newInstanceByFactoryBean(beanDefinitionBack);
            }
        } else {
            throw new RuntimeException("BeanDefinition为空");
        }



        this.setPropertyValue(beanDefinitionBack, instance);

        if (!StringUtils.isEmpty(beanDefinitionBack.getInitMethod())) {
            this.doInitMethod(instance, beanDefinitionBack);
        }

        //获取该实例的所有方法，再来判断是否是需要增强的bean
        instance = this.applyPostProcessorAfterInitializer(instance,beanName);

        if (beanDefinitionBack.isSingle()) {
            beanMap.put(beanName, instance);
        }

        return instance;

    }

    private Object applyPostProcessorAfterInitializer(Object bean,String beanName){
        for(BeanPostProcessor bpp :  this.beanPosts){
            bean = bpp.postProcessAfterInitialization(bean,beanName);
        }
        return bean;
    }

    private void setPropertyValue(BeanDefinitionBack beanDefinitionBack, Object instance) throws Exception {
        List<PropertyValue> propertyValues = beanDefinitionBack.getPropertyValues();
        if (CollectionUtils.isEmpty(propertyValues)) {
            return;
        }
        for (PropertyValue propertyValue : propertyValues) {
            Field field = instance.getClass().getDeclaredField(propertyValue.getName());
            field.setAccessible(true);
            Object o = propertyValue.getValue();
            Object rv = null;
            if (o == null) {
                rv = null;
            } else if (o instanceof BeanReference) {
                rv = this.getBean(((BeanReference) o).getBeanName());
            } else if (o instanceof Object[]) {

            } else if (o instanceof Collection) {

            } else if (o instanceof Properties) {
                rv = this.getValue(propertyValue.getName(), o);
            } else if (o instanceof Map) {

            } else {
                rv = o;
            }
            //在所有限制的beanNames
            List<String> limitBeanName = beanDefinitionBack.getLimitBeanClass();
            if(!limitBeanName.contains(instance.getClass().getName())){
                field.set(instance, rv);
            }
        }

    }

    private Object getValue(String name, Object o) {
        Properties properties = (Properties) o;
        return properties.getProperty(name);
    }

    //获取构造函数中的参数
    public Object[] getConstructorArgumentValues(BeanDefinitionBack beanDefinitionBack) throws Exception {
        return this.getRealValue(beanDefinitionBack.getConstructorArgumentValues());
    }

    //确定构造函数的方法
    private Constructor<?> determineConstructor(BeanDefinitionBack definitionBack, Object[] objects) throws Exception {
        Constructor<?> constructor = null;
        if (objects == null) {
            return definitionBack.getBeanClass().getConstructor(null);
        }
        //对于原型bean 从第二次开始获取bean实例时，可以直接获得第一次缓存的构造方法
        constructor = definitionBack.getConstructor();
        if (constructor != null) {
            return constructor;
        }
        //精确匹配 根据参数类型获取精确的构造方法
        Class<?>[] paramTypes = new Class[objects.length];
        int j = 0;
        for (Object o : objects) {
            paramTypes[j++] = o.getClass();
        }
        try {
            constructor = definitionBack.getBeanClass().getConstructor(paramTypes);
        } catch (Exception e) {
            //TODO 异常不处理 只是表示没有精确匹配没有匹配到
        }
        if (constructor == null) {
            //精确匹配如果没有匹配上，则需要先遍历所有构造方法。
            //判断逻辑是 先判断参数数量相同吗，再来判断每一个参数类型相同吗
            outer:
            for (Constructor<?> constructor1 : definitionBack.getBeanClass().getConstructors()) {
                Class<?>[] ptypes = constructor1.getParameterTypes();
                if (ptypes.length == objects.length) {
                    for (int i = 0; i < ptypes.length; i++) {
                        if (!ptypes[i].isAssignableFrom(objects[i].getClass())) {
                            continue outer;
                        }
                    }
                    constructor = constructor1;
                    break outer;
                }
            }
        }

        if (constructor != null) {
            if (!definitionBack.isSingle()) {
                definitionBack.setConstructor(constructor);
            }
        } else {
            throw new RuntimeException("找不到对应的构造方法");
        }

        return constructor;
    }

    private Object[] getRealValue(List<?> args) throws Exception {
        if (CollectionUtils.isEmpty(args)) {
            return null;
        }
        Object[] objects = new Object[args.size()];

        int i = 0;
        Object v = null;
        for (Object rv : args) {
            if (rv == null) {
                v = null;
            } else if (rv instanceof BeanReference) {
                v = this.doGetBean(((BeanReference) rv).getBeanName());
            } else if (rv instanceof Object[]) {
                //TODO 处理集合中的bean引用
            } else if (rv instanceof Collection) {
                //TODO 处理集合中的引用
            } else if (rv instanceof Properties) {
                //TODO  处理properties中的引用
            } else if (rv instanceof Map) {
                //TODO 处理Map中的引用
            } else {
                v = rv;
            }
            objects[i++] = v;
        }

        return objects;
    }

    //初始化bean实例
    private void doInitMethod(Object instance, BeanDefinitionBack beanDefinitionBack) throws Exception {
        Method method = instance.getClass().getMethod(beanDefinitionBack.getInitMethod(), null);
        method.invoke(instance, null);
    }

    //通bean工厂容器创建bean实例
    private Object newInstanceByFactoryBean(BeanDefinitionBack beanDefinitionBack) throws Exception {
        Object factory = this.doGetBean(beanDefinitionBack.getBeanByFactory());
        //获取参数
        Object[] args = this.getRealValue(beanDefinitionBack.getConstructorArgumentValues());

        Method method = this.determineFactoryMethod(beanDefinitionBack, args, factory.getClass());
        return method.invoke(factory, args);
    }

    //通bean工厂静态方法创建bean实例
    private Object newInstanceByFactoryMethod(BeanDefinitionBack beanDefinitionBack) throws Exception {
        try {
            //Method method = beanClass.getMethod(beanDefinitionBack.getFactoryMethodBean(), null);
            //获取参数
            Object[] args = this.getRealValue(beanDefinitionBack.getConstructorArgumentValues());
            Method method = this.determineFactoryMethod(beanDefinitionBack, args, null);
            return method.invoke(beanDefinitionBack.getBeanClass(), args);
        } catch (Exception e) {
            throw e;
        }
    }

    private Method determineFactoryMethod(BeanDefinitionBack beanDefinitionBack, Object[] args, Class<?> type) throws Exception {
        if (type == null) {
            type = beanDefinitionBack.getBeanClass();
        }
        String methodName = beanDefinitionBack.getFactoryMethodBean();
        if (args == null) {
            return type.getMethod(methodName, null);
        }
        Method m = null;
        m = beanDefinitionBack.getFactoryMethod();
        if (m != null) {
            return m;
        }
        Class[] parameTypes = new Class[args.length];
        int j = 0;
        for (Object p : args) {
            parameTypes[j++] = p.getClass();
        }
        try {
            m = type.getMethod(methodName, parameTypes);
        } catch (Exception e) {

        }
        if (m == null) {
            outer:
            for (Method m1 : type.getMethods()) {
                if (!m1.getName().equals(methodName)) {
                    continue;
                }
                Class[] methodParame = m.getParameterTypes();
                if (methodParame.length == args.length) {
                    for (int i = 0; i < methodParame.length; i++) {
                        if (methodParame[i].isAssignableFrom(args[i].getClass())) {
                            continue outer;
                        }
                    }
                    m = m1;
                    break outer;
                }
            }
        }
        if (m != null) {
            if (!beanDefinitionBack.isSingle()) {
                beanDefinitionBack.setFactoryMethod(m);
            }
            return m;
        } else {
            throw new RuntimeException("没有找到静态方法");
        }

    }

    //构造函数直接创建bean
    private Object newInstanceByConstructor(BeanDefinitionBack definitionBack) throws Exception {
        try {
            Object[] args = this.getConstructorArgumentValues(definitionBack);
            if (args == null) {
                return definitionBack.getBeanClass().newInstance();
            } else {
                Constructor<?> constructor = this.determineConstructor(definitionBack, args);
                definitionBack.setConstructor(constructor);
                return constructor.newInstance(args);
            }

        } catch (Exception e) {
            throw e;
        }
    }
}
