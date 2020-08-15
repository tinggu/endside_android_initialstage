package com.ctfww.module.keepwatch.entity;

import java.util.List;

public class KeepWatchRoute {
    KeepWatchRouteSummary routeSummary;
    private List<KeepWatchRouteDesk> deskList;

    public String toString() {
        return routeSummary.toString()
                + ", deskList.size() = " + deskList.size();
    }

    public KeepWatchRoute() {

    }

    public void setRouteSummary(KeepWatchRouteSummary routeSummary) {
        this.routeSummary = routeSummary;
    }

    public KeepWatchRouteSummary getRouteSummary() {
        return routeSummary;
    }

    public void setDeskList(List<KeepWatchRouteDesk> deskList) {
        this.deskList = deskList;
    }

    public List<KeepWatchRouteDesk> getDeskList() {
        return deskList;
    }
}
