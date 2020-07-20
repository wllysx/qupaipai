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


public class HistoryAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public HistoryAdapter(List<EntityForSimple> data) {
        super(R.layout.item_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {

        try {
            Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
            helper.setText(R.id.tv_goodsName,item.getGoodsName());
            helper.setText(R.id.tv_endTime,item.getEndTime());
            helper.setText(R.id.tv_endTime,"结拍时间:"+ DateUtils.dateToDate(item.getEndTime(),"yyyy-MM-dd HH:mm:ss","MM-dd HH:mm"));
            helper.setText(R.id.tv_markupNum,item.getMarkupNum()+"次出价");
            helper.setText(R.id.tv_transactionPrice,"￥"+item.getTransactionPrice().replace(".00",""));
        }catch (Exception e){

        }
    }


}
