package com.qupp.client.utils.event;


public class NewPeople {
    boolean isCanClick = true;

    public NewPeople(boolean isCanClick) {
        this.isCanClick = isCanClick;
    }

    public boolean isCanClick() {
        return isCanClick;
    }
}
