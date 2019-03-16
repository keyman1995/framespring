package com.chen.springreview.poincut;

import com.chen.bean.AspectJBean;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

public class AspectJExpressionPointCut implements PointCut {

    private String expression;

    //全局的切入点表达式
    static PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();


    private PointcutExpression pointcutExpression = null;

    public AspectJExpressionPointCut(String expression) {
        this.expression = expression;
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }


    @Override
    public boolean matchClass(Class<?> targetClass) {
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    @Override
    public boolean matchMethod(Method method, Class<?> targetClass) {
        ShadowMatch sm = pointcutExpression.matchesMethodExecution(method);
        return sm.alwaysMatches();
    }

    public String getExpression() {
        return expression;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        AspectJExpressionPointCut aspectJExpressionPointCut = new AspectJExpressionPointCut("execution(* com.chen.bean.AspectJBean.test1(..))");
        aspectJExpressionPointCut.matchClass(AspectJBean.class);
        Method method = AspectJBean.class.getMethod("test1",null);
        aspectJExpressionPointCut.matchMethod(method,AspectJBean.class);
    }


}
