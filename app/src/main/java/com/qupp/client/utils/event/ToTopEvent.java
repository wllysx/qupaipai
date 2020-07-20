package com.qupp.client.utils.event;


public class ToTopEvent {

   private int position;

    public ToTopEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
