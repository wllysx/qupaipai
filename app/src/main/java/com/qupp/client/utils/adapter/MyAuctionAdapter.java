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
 * 我的活动
 */

public class MyAuctionAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public MyAuctionAdapter(List<EntityForSimple> data) {
        super(R.layout.item_auction_my, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            if(item.getPrizeType().equals("1")){
                helper.setVisible(R.id.tv_jifen,false);
                helper.setVisible(R.id.tv_content,true);
                helper.setVisible(R.id.tv_more,true);
                Glide.with(mContext).load(item.getImageUrl()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
                helper.setText(R.id.tv_title,item.getActivityName());
                helper.setText(R.id.tv_content,item.getPrizeName());

            }else if(item.getPrizeType().equals("2")){
                helper.setVisible(R.id.tv_jifen,true);
                helper.setVisible(R.id.tv_content,false);
                helper.setVisible(R.id.tv_more,false);
                helper.setImageResource(R.id.iv_logo, R.mipmap.icon_hb);
                helper.setText(R.id.tv_title,item.getActivityName());
                helper.setText(R.id.tv_jifen,item.getPrizeName());
            }else if(item.getPrizeType().equals("5")){
                helper.setVisible(R.id.tv_jifen,true);
                helper.setVisible(R.id.tv_content,false);
                helper.setVisible(R.id.tv_more,false);
                helper.setImageResource(R.id.iv_logo, R.mipmap.icon_jf);
                helper.setText(R.id.tv_title,item.getActivityName());
                helper.setText(R.id.tv_jifen,item.getPrizeName());
            }else{
                helper.setVisible(R.id.tv_jifen,true);
                helper.setVisible(R.id.tv_content,false);
                helper.setVisible(R.id.tv_more,false);
                helper.setImageResource(R.id.iv_logo, R.mipmap.icon_jf);
                helper.setText(R.id.tv_title,item.getActivityName());
                helper.setText(R.id.tv_jifen,item.getPrizeName());
            }
            helper.addOnClickListener(R.id.tv_more);
            helper.setText(R.id.tv_time, DateUtils.dateToDate(item.getCreateTime(),"yyyy-MM-dd HH:mm:ss","MM-dd HH:mm"));

        }catch (Exception e){

        }

    }


}
