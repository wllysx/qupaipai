package com.qupp.client.utils.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 寄拍订单
 */

public class VipRoomDetailsAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public VipRoomDetailsAdapter(List<EntityForSimple> data) {
        super(R.layout.item_viproomdetails, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
            helper.setText(R.id.tv_commoditytitle,item.getGoodsName());
            if(item.getStatus().equals("3")){
                //竞拍中
                helper.setText(R.id.tv_pricetype,"当前价：");
                helper.setText(R.id.tv_states,"竞拍中");
                helper.setText(R.id.tv_time,"距结束："+ DateUtils.getDistanceTime(Long.valueOf(item.getEndTimes())));
                helper.setTextColor(R.id.tv_time, mContext.getResources().getColor(R.color.iscur));
                helper.setTextColor(R.id.tv_states, mContext.getResources().getColor(R.color.iscur));
                helper.setVisible(R.id.tv_number,true);
                helper.setText(R.id.tv_number,item.getMarkupNum()+"次出价");
                helper.setText(R.id.tv_price,"￥"+item.getTopPrice().replace(".00",""));

            }else if(item.getStatus().equals("2")){
                //未开始
                helper.setText(R.id.tv_pricetype,"起拍价：");
                helper.setText(R.id.tv_states,"未开始");


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                if(DateUtils.dateToDate(item.getStartTime(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd").equals(simpleDateFormat.format(date))){
                    helper.setText(R.id.tv_time,"开拍时间:"+ DateUtils.dateToDate(item.getStartTime(),"yyyy-MM-dd HH:mm:ss","HH:mm"));
                }else{
                    helper.setText(R.id.tv_time,"开拍时间:"+ DateUtils.dateToDate(item.getStartTime(),"yyyy-MM-dd HH:mm:ss","MM-dd HH:mm"));
                }

                helper.setTextColor(R.id.tv_time, mContext.getResources().getColor(R.color.iscur));
                helper.setTextColor(R.id.tv_states, mContext.getResources().getColor(R.color.textcolor6));
                helper.setVisible(R.id.tv_number,true);
                helper.setText(R.id.tv_number,item.getViewCount()+"人关注");
                helper.setText(R.id.tv_price,"￥"+item.getStartPrice().replace(".00",""));
            }else{
                //已结束
                helper.setText(R.id.tv_pricetype,"成交价：");
                helper.setText(R.id.tv_states,"已结束");
                helper.setText(R.id.tv_time,"结束时间:"+ DateUtils.dateToDate(item.getEndTime(),"yyyy-MM-dd HH:mm:ss","HH:mm"));
                helper.setTextColor(R.id.tv_time, mContext.getResources().getColor(R.color.textcolor16));
                helper.setTextColor(R.id.tv_states, mContext.getResources().getColor(R.color.textcolor16));
                helper.setVisible(R.id.tv_number,true);
                helper.setText(R.id.tv_number,item.getMarkupNum()+"次出价");
                helper.setText(R.id.tv_price,"￥"+item.getTransactionPrice().replace(".00",""));

            }
        }catch (Exception e){

        }


    }


}
