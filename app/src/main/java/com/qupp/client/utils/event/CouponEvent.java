package com.qupp.client.utils.event;



import java.io.Serializable;

public class CouponEvent implements Serializable {

    private String useLimitMaxPrice;
    private String id;

    public CouponEvent(String useLimitMaxPrice, String id) {
        this.useLimitMaxPrice = useLimitMaxPrice;
        this.id = id;
    }

    public String getUseLimitMaxPrice() {
        return useLimitMaxPrice;
    }

    public void setUseLimitMaxPrice(String useLimitMaxPrice) {
        this.useLimitMaxPrice = useLimitMaxPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
