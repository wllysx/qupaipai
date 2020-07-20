package com.qupp.client.utils.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 我的朋友
 * author: MrWang on 2019/8/12
 * email:773630555@qq.com
 * date: on 2019/8/12 17:31
 */

public class MessageAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public MessageAdapter(List<EntityForSimple> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            helper.setText(R.id.tv_time,item.getSendTime());
            helper.setText(R.id.tv_title,item.getTitle());
            helper.setText(R.id.tv_content,item.getContent());
        }catch (Exception e){


        }
    }


}
