package com.chen.spring.aop.advisor;

import com.chen.spring.aop.pointcut.AspectJExpressionPointCut;
import com.chen.spring.aop.pointcut.PointCut;


public class AspectJPointcutAdvisor implements PointCutAdvisor {

    //beanName，值得是实例化完成之后的bean实例的名称
    private String beanName;

    private String expression;

    private PointCut pointcut;

    /**
     * 用户只需要实例化一个AspectJPointcutAdvisor即可
     * @param beanName
     * @param expression
     */
    public AspectJPointcutAdvisor(String beanName, String expression) {
        this.beanName = beanName;
        this.expression = expression;
        this.pointcut = new AspectJExpressionPointCut(expression);
    }

    @Override
    public PointCut getPointCut() {
        return this.pointcut;
    }

    @Override
    public String getAdviceBeanName() {
        return this.beanName;
    }



    @Override
    public String getExpression() {
        return this.expression;
    }
}
