package com.qupp.client.utils.event;


import com.qupp.client.network.bean.EntityForSimple;

import java.io.Serializable;

public class MealEvent implements Serializable {

    private EntityForSimple data;
    private int positon;
    private String auctionId;

    public MealEvent(EntityForSimple data, int positon,String auctionId) {
        this.data = data;
        this.positon = positon;
        this.auctionId = auctionId;
    }

    public EntityForSimple getData() {
        return data;
    }

    public void setData(EntityForSimple data) {
        this.data = data;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }

    public String getAuctionId() {
        return auctionId;
    }
}
