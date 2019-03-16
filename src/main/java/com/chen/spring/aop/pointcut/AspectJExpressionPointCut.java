package com.chen.spring.aop.pointcut;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;

import java.lang.reflect.Method;

public class AspectJExpressionPointCut implements PointCut {


    private String expression;

    //获取切点解析器
    private static PointcutParser pp = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();

    public AspectJExpressionPointCut(String expression) {
        this.expression = expression;
    }



    @Override
    public boolean matchClass(Class<?> targetClass) {
        PointcutExpression pe = pp.parsePointcutExpression(expression);
        return pe.couldMatchJoinPointsInType(targetClass);
    }

    @Override
    public boolean matchMethod(Method method) {
        PointcutExpression pe = pp.parsePointcutExpression(expression);
        return pe.matchesMethodExecution(method).alwaysMatches();
    }
}
