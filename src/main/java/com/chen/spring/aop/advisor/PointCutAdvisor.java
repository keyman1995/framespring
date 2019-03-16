package com.chen.spring.aop.advisor;


import com.chen.spring.aop.pointcut.PointCut;

public interface PointCutAdvisor extends Advisor {

    PointCut getPointCut();
}
