package com.guoliang.commonlib.entity;

import java.util.ArrayList;
import java.util.List;

public class PersonPerformanceList {
    private static final int MAX_COUNT = 5;
    private List<PersonPerformance> personPerformanceList;

    public PersonPerformanceList() {

    }

    public void setPersonPerformanceList(List<PersonPerformance> personPerformanceList) {
        this.personPerformanceList = personPerformanceList;
    }

    public List<PersonPerformance> getPersonPerformanceList() {
        return personPerformanceList;
    }

    public int fill(List<PersonPerformance> personPerformanceList, int start) {
        int ret = 0;
        if (start >= personPerformanceList.size()) {
            ret = 0;
        }
        else if (start + MAX_COUNT > personPerformanceList.size()){
            this.personPerformanceList = personPerformanceList.subList(start, personPerformanceList.size());
            ret = personPerformanceList.size() - start;
        }
        else {
            this.personPerformanceList = personPerformanceList.subList(start, start + 5);
            ret = 5;
        }

        return ret;
    }
}
