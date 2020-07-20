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
 * 寄拍订单
 * author: MrWang on 2019/8/12
 * email:773630555@qq.com
 * date: on 2019/8/12 17:31
 */

public class OrderAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public OrderAdapter(List<EntityForSimple> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
            helper.setText(R.id.tv_commoditytitle, item.getGoodsName());
            helper.setText(R.id.tv_price,"成交价：￥"+item.getTransactionPrice());
            int position = helper.getAdapterPosition();
            if(item.getOrderStatus().equals("0")){
                //待付款
                helper.setText(R.id.tv_states,"待付款");
                helper.setBackgroundRes(R.id.tv_states,R.drawable.bg_order_states1);
                helper.setText(R.id.tv_submit,"去支付");
                helper.setVisible(R.id.tv_submit,true);

            }else if(item.getOrderStatus().equals("1")){
                //待发货
                helper.setText(R.id.tv_states,"待发货");
                helper.setBackgroundRes(R.id.tv_states,R.drawable.bg_order_states2);
                //如果为申请寄拍显示  如果拒绝寄拍不显示
                helper.setVisible(R.id.tv_submit,false);

            }else if(item.getOrderStatus().equals("2")){
                //待收货
                helper.setText(R.id.tv_states,"待收货");
                helper.setBackgroundRes(R.id.tv_states,R.drawable.bg_order_states2);
                helper.setText(R.id.tv_submit,"确认收货");
                helper.setVisible(R.id.tv_submit,true);

            }else if(item.getOrderStatus().equals("3")){
                //已收货
                helper.setText(R.id.tv_states,"交易成功");
                helper.setBackgroundRes(R.id.tv_states,R.drawable.bg_order_states2);
                helper.setVisible(R.id.tv_submit,false);

            }else{
                helper.setText(R.id.tv_states,"已关闭");
                helper.setBackgroundRes(R.id.tv_states,R.drawable.bg_order_states2);
                helper.setVisible(R.id.tv_submit,false);

            }

            helper.addOnClickListener(R.id.tv_submit);
        }catch (Exception e){

        }


    }


}
