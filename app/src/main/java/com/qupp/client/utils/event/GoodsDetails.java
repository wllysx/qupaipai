package com.qupp.client.utils.event;


public class GoodsDetails {

    private String goodsDetail;
    private String showTitle;

    /**
     *
     * @param goodsDetail
     * @param showTitle 1显示
     */
    public GoodsDetails(String goodsDetail, String showTitle) {
        this.goodsDetail = goodsDetail;
        this.showTitle = showTitle;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public String getShowTitle() {
        return showTitle;
    }
}
