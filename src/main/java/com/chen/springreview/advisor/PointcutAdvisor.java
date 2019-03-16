package com.chen.springreview.advisor;

import com.chen.springreview.poincut.PointCut;

public interface PointcutAdvisor extends Advisor {

    PointCut getPointCut();

}
