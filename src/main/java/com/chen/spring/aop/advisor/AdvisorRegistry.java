package com.chen.spring.aop.advisor;

import java.util.List;

public interface AdvisorRegistry {

    void registAdvisor(Advisor advisor);

    List<Advisor> getAdvisors();

}
