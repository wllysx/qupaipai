package com.qupp.client.utils.adapter;



import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;


public class MyOrderAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public MyOrderAdapter(List<EntityForSimple> data) {
        super(R.layout.item_myorder, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
        helper.setText(R.id.tv_commoditytitle, item.getGoodsName());
        helper.setText(R.id.tv_num,"×"+item.getNum());
       if(item.getOrderStatus().equals("0")){
            //待付款
           helper.setText(R.id.tv_states,"待付款");
           helper.setBackgroundRes(R.id.tv_states, R.drawable.bg_order_states1);
           helper.setText(R.id.tv_submit,"去支付");
           helper.setVisible(R.id.tv_submit,true);

       }else if(item.getOrderStatus().equals("1")){
            //待发货
           helper.setText(R.id.tv_states,"待发货");
           helper.setBackgroundRes(R.id.tv_states, R.drawable.bg_order_states2);
           //如果为申请寄拍显示  如果拒绝寄拍不显示
           if(item.getGoodsType().equals("1")||item.getGoodsType().equals("3")) {
               if(item.getOrderType().equals("0")) {
                   helper.setText(R.id.tv_submit, "申请寄拍");
                   helper.setVisible(R.id.tv_submit, true);
               }else{
                   helper.setGone(R.id.tv_submit, false);
               }
           }else{
               helper.setGone(R.id.tv_submit, false);
           }
       }else if(item.getOrderStatus().equals("2")){
           //待收货
           helper.setText(R.id.tv_states,"待收货");
           helper.setBackgroundRes(R.id.tv_states, R.drawable.bg_order_states2);
           helper.setText(R.id.tv_submit,"确认收货");
           helper.setVisible(R.id.tv_submit,true);
       }else if(item.getOrderStatus().equals("3")){
           //已收货
           helper.setText(R.id.tv_states,"交易成功");
           helper.setBackgroundRes(R.id.tv_states, R.drawable.bg_order_states2);
           helper.setVisible(R.id.tv_submit,false);
       }else{
            //已关闭
            helper.setText(R.id.tv_states,"已关闭");
            helper.setBackgroundRes(R.id.tv_states, R.drawable.bg_order_states2);
            helper.setVisible(R.id.tv_submit,false);
        }

        if(item.getPriceType().equals("1")){
            helper.setText(R.id.tv_price,item.getIntegral().replace(".00","")+"积分");
            helper.setGone(R.id.tv_integral,false);
        }else if(item.getPriceType().equals("2")){
            helper.setText(R.id.tv_integral,"+"+item.getIntegral().replace(".00","")+"积分");
            helper.setText(R.id.tv_price,"￥"+item.getPrice().replace(".00",""));
            helper.setGone(R.id.tv_price,true);
        }else if(item.getPriceType().equals("3")){
            helper.setText(R.id.tv_price,"￥"+item.getPrice().replace(".00",""));
            helper.setGone(R.id.tv_integral,false);
        }else{
            helper.setText(R.id.tv_integral,"+"+item.getIntegral().replace(".00","")+"钱包");
            helper.setText(R.id.tv_price,"￥"+item.getPrice().replace(".00",""));
            helper.setGone(R.id.tv_price,true);
        }

       helper.addOnClickListener(R.id.tv_submit);

    }


}
