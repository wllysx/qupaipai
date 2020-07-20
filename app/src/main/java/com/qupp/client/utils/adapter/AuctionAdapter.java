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

/**
 * 我的竞拍
 */

public class AuctionAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public AuctionAdapter(List<EntityForSimple> data) {
        super(R.layout.item_auction, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
            helper.setText(R.id.tv_endTime,"结拍时间 "+ DateUtils.dateToDate(item.getEndTime(),"yyyy-MM-dd HH:mm:ss","HH:mm"));
            helper.setText(R.id.tv_topPrice,"当前价：￥"+item.getTopPrice());
            helper.setText(R.id.tv_goodsName,item.getGoodsName());
        }catch (Exception e){

        }
    }


}
