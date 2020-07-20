package com.qupp.client.utils.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 积分商城
 */

public class ShopAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public ShopAdapter(List<EntityForSimple> data) {
        super(R.layout.item_shop, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            helper.setText(R.id.tv_goodsName,item.getGoodsName());

            Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
            if(item.getPriceType().equals("1")){
                helper.setText(R.id.tv_integral,item.getIntegral().replace(".00","")+"积分");
                helper.setGone(R.id.tv_price,false);
            }else if(item.getPriceType().equals("2")){
                helper.setText(R.id.tv_integral,item.getIntegral().replace(".00","")+"积分");
                helper.setText(R.id.tv_price,"+￥"+item.getPrice().replace(".00",""));
                helper.setGone(R.id.tv_price,true);
            }else{
                helper.setText(R.id.tv_integral,"￥"+item.getPrice().replace(".00",""));
                helper.setGone(R.id.tv_price,false);
            }
        }catch (Exception e){

        }


    }


}
