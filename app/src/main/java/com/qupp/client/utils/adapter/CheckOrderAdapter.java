package com.qupp.client.utils.adapter;


import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 寄拍订单
 */

public class CheckOrderAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public CheckOrderAdapter(List<EntityForSimple> data) {
        super(R.layout.item_checkorder, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
        helper.setText(R.id.tv_commoditytitle, item.getGoodsName());
        if (item.getOrderType().equals("2")) {
            //寄拍中
            helper.setText(R.id.tv_states, "寄拍中");
            helper.setBackgroundRes(R.id.tv_states, R.drawable.bg_order_states1);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date d1 = new Date();
                Log.d("starttimt", d1.toString());
                Date d2 = df.parse(item.getAcceptTime());
                long diff = d1.getTime() - d2.getTime();
                helper.setText(R.id.tv_time, "已寄拍：" + DateUtils.getDistanceTime1(diff));
            } catch (Exception e) {
            }


        } else if (item.getOrderType().equals("3")) {
            //寄拍成功
            helper.setText(R.id.tv_states, "寄拍成功");
            helper.setBackgroundRes(R.id.tv_states, R.drawable.bg_order_states2);
            helper.setText(R.id.tv_time, "完成时间：" +  DateUtils.dateToDate(item.getUpdateTime(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"));
        } else {
            //待寄拍
            helper.setText(R.id.tv_states, "待接拍");
            helper.setBackgroundRes(R.id.tv_states, R.drawable.bg_order_states2);
            helper.setText(R.id.tv_time, "申请时间：" + DateUtils.dateToDate(item.getApplyTime(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"));
        }
        helper.addOnClickListener(R.id.tv_submit);
        helper.addOnClickListener(R.id.tv_mingxi);
        if(item.getIsViewMallAuctionDetail().equals("true")){
            //展示明细
            helper.setVisible(R.id.tv_mingxi,true);
        }else{
            helper.setVisible(R.id.tv_mingxi,false);
        }

    }


}
