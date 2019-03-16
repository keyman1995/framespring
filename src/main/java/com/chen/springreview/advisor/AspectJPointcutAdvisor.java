package com.chen.springreview.advisor;

import com.chen.springreview.poincut.AspectJExpressionPointCut;
import com.chen.springreview.poincut.PointCut;

//用户创建了这个实例就表示已经创建了一个基于aspectj的切面
public class AspectJPointcutAdvisor extends AbstractPointcutAdvisor {


    private String expression;

    private PointCut pointCut;

    private String adviceBeanName;

    public AspectJPointcutAdvisor(String expression, String adviceBeanName) {
        this.expression = expression;
        this.adviceBeanName = adviceBeanName;
        this.pointCut = new AspectJExpressionPointCut(this.expression);
    }

    @Override
    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public void setPointCut(PointCut pointCut) {
        this.pointCut = pointCut;
    }

    @Override
    public void setAdviceBeanName(String adviceBeanName) {
        this.adviceBeanName = adviceBeanName;
    }

    @Override
    public PointCut getPointCut() {
        return this.pointCut;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public String getAdviceBeanName() {
        return this.adviceBeanName;
    }
}
