package com.qupp.client.utils.event;


public class SocketEvent {

    private String auctionId;
    private String nextCashDeposit;
    private String brokerage;
    private String guarantyPrice;
    private String payPrice;
    private String markupTime;
    private String markupTimeString;
    private String avatar;
    private String userName;
    private String userId;

    public SocketEvent(String auctionId, String nextCashDeposit, String brokerage, String guarantyPrice, String payPrice, String markupTime, String markupTimeString, String avatar, String userName, String userId) {
        this.auctionId = auctionId;
        this.nextCashDeposit = nextCashDeposit;
        this.brokerage = brokerage;
        this.guarantyPrice = guarantyPrice;
        this.payPrice = payPrice;
        this.markupTime = markupTime;
        this.markupTimeString = markupTimeString;
        this.avatar = avatar;
        this.userName = userName;
        this.userId = userId;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public String getNextCashDeposit() {
        return nextCashDeposit;
    }

    public String getBrokerage() {
        return brokerage;
    }

    public String getGuarantyPrice() {
        return guarantyPrice;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public String getMarkupTime() {
        return markupTime;
    }

    public String getMarkupTimeString() {
        return markupTimeString;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }
}
