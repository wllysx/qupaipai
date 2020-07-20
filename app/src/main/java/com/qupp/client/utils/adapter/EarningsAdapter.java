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
 * 收益排行
 */

public class EarningsAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public EarningsAdapter(List<EntityForSimple> data) {
        super(R.layout.item_earnings, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
       int position = helper.getAdapterPosition();
       try {
           Glide.with(mContext).load(item.getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into((ImageView) helper.getView(R.id.iv_photo));
           //  Glide.with(mContext).load(item.getAvatar()).placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default).into((ImageView) helper.getView(R.id.iv_logo));
           helper.setText(R.id.tv_name,item.getUserName());
           helper.setText(R.id.tv_num,item.getNum()+"次出价");
           helper.setText(R.id.tv_price,"收益 ￥"+item.getTotalPrice());
           if(position==0){
               //第一名
               helper.setText(R.id.tv_no,"");
               helper.setBackgroundRes(R.id.tv_no,R.mipmap.spxq_1);

           }else if(position==1){
               //第二名
               helper.setText(R.id.tv_no,"");
               helper.setBackgroundRes(R.id.tv_no,R.mipmap.spxq_2);
           }else if(position==2){
               //第三名
               helper.setText(R.id.tv_no,"");
               helper.setBackgroundRes(R.id.tv_no,R.mipmap.spxq_3);
           }else{
               helper.setText(R.id.tv_no,(position+1)+"");
               helper.setBackgroundRes(R.id.tv_no,R.drawable.bg_circle_graw);
           }
       }catch (Exception e){

       }


    }


}
