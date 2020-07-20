package com.qupp.client.utils.event;


public class OrderPaySuccess {

    /**
     * 1 拍卖订单  2 积分商城订单
     */
    private String type;

    public OrderPaySuccess(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
