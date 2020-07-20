package com.qupp.client.utils.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 优惠券
 */

public class CouponAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public CouponAdapter(List<EntityForSimple> data, int type) {
        super(R.layout.item_coupon, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        helper.setText(R.id.tv_showSymbol, item.getShowSymbol());
        helper.setText(R.id.tv_showPrice, item.getShowPrice());
        helper.setText(R.id.tv_showRemark, item.getShowRemark());
        helper.setText(R.id.tv_couponShowName, item.getCouponShowName());
        helper.setText(R.id.tv_useTime, item.getUseTime());
        helper.setText(R.id.tv_useLimit, item.getUseLimit());
        helper.addOnClickListener(R.id.tv_gouse);
        helper.setText(R.id.tv_man,item.getShowPrice()+"元以内免单");
    }

}
