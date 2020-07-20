package com.qupp.client.utils.event;


public class WxpayEvent {

    private String type;

    public WxpayEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
