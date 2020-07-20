package com.qupp.client.utils.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.DateUtils;

import java.util.List;


public class MainItemAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {

    protected boolean isScrolling = false;

    public MainItemAdapter(List<EntityForSimple> data) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            //if(!isScrolling) {
            Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
            //}
            if(item.getStatus().equals("2")){
                //未开始
                helper.setText(R.id.tv_goodsName,item.getGoodsName());
                helper.setText(R.id.tv_pricetype,"起拍价");
                helper.setGone(R.id.tv_count,true);
                helper.setText(R.id.tv_count,item.getViewCount()+"人关注");
                helper.setText(R.id.tv_time,"开拍时间:"+ DateUtils.dateToDate(item.getStartTime(),"yyyy-MM-dd HH:mm:ss","HH:mm"));
                helper.setText(R.id.tv_price,"￥"+item.getStartPrice().replace(".00",""));
                // helper.setTextColor(R.id.tv_time,mContext.getResources().getColor(R.color.yellow5));
                //helper.setGone(R.id.tv_goprice,false);
                helper.setGone(R.id.tv_finish,false);
            }else if(item.getStatus().equals("3")){
                //进行中
                helper.setText(R.id.tv_goodsName,item.getGoodsName());
                helper.setText(R.id.tv_pricetype,"当前价");
                helper.setText(R.id.tv_count,item.getMarkupNum()+"次出价");
                helper.setGone(R.id.tv_count,true);
                helper.setText(R.id.tv_time,"结拍时间:"+ DateUtils.dateToDate(item.getEndTime(),"yyyy-MM-dd HH:mm:ss","HH:mm"));
                helper.setText(R.id.tv_price,"￥"+item.getTopPrice().replace(".00",""));
                helper.setTextColor(R.id.tv_time,mContext.getResources().getColor(R.color.yellow5));
                //helper.setVisible(R.id.tv_goprice,true);
                helper.setGone(R.id.tv_finish,false);
            }else{
                //已结束
                helper.setText(R.id.tv_goodsName,item.getGoodsName());
                helper.setText(R.id.tv_pricetype,"成交价");
                helper.setText(R.id.tv_count,item.getMarkupNum()+"次出价");
                helper.setGone(R.id.tv_count,true);
                helper.setText(R.id.tv_time,"结拍时间:"+ DateUtils.dateToDate(item.getEndTime(),"yyyy-MM-dd HH:mm:ss","HH:mm"));
                helper.setText(R.id.tv_price,"￥"+item.getTransactionPrice().replace(".00",""));
                helper.setTextColor(R.id.tv_time,mContext.getResources().getColor(R.color.textcolor23));
                //helper.setGone(R.id.tv_goprice,false);
                helper.setGone(R.id.tv_finish,true);
            }
        }catch (Exception e){

        }


    }

    public void setScrolling(boolean scrolling){
        isScrolling = scrolling;
    }


}
