package com.chen.springreview.advisor;

import java.util.List;

public interface AdvisorRegistory {

    void registAdvisor(Advisor advisor);

    List<Advisor> getAdvisors();
}
