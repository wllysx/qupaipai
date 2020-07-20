package com.qupp.client.utils.dialog;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 优惠券
 */

public class CouponAdapter1 extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {

    private int type=0;

    public CouponAdapter1(List<EntityForSimple> data, int type) {
        super(R.layout.item_coupon1, data);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        helper.setText(R.id.tv_showSymbol, item.getShowSymbol());
        helper.setText(R.id.tv_showPrice, item.getShowPrice());
        helper.setText(R.id.tv_showRemark, item.getShowRemark());
        helper.setText(R.id.tv_couponShowName, item.getCouponShowName());
        helper.setText(R.id.tv_useTime, item.getUseTime());
        helper.setText(R.id.tv_useLimit, item.getUseLimit());
        if(type==1){
            helper.setImageResource(R.id.tv_finish, R.mipmap.icon_ysy);
        }else{
            helper.setImageResource(R.id.tv_finish, R.mipmap.icon_ygq);

        }
    }

}
