package com.qupp.client.utils.event;

/**
 * 首页
 */
public class MainPageRefresh {

    private String startTime = "";
    private String endTime = "";

    public MainPageRefresh() {
    }

    public MainPageRefresh(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
