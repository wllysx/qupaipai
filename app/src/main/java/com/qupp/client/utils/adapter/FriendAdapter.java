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
 * 我的朋友
 */

public class FriendAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public FriendAdapter(List<EntityForSimple> data) {
        super(R.layout.item_friend, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            Glide.with(mContext).load(item.getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into((ImageView) helper.getView(R.id.iv_photo));
            helper.setText(R.id.tv_nickname,item.getNickname());
            helper.setText(R.id.tv_indirectNum,"已推"+item.getIndirectNum()+"人");
            helper.setText(R.id.tv_createTime,item.getCreateTime());
        }catch (Exception e){

        }



    }


}
