package com.chen.springreview.advisor;

import com.chen.springreview.poincut.PointCut;

public abstract class AbstractPointcutAdvisor implements PointcutAdvisor {

    private String expression;

    private PointCut pointCut;

    private String adviceBeanName;

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setPointCut(PointCut pointCut) {
        this.pointCut = pointCut;
    }

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
