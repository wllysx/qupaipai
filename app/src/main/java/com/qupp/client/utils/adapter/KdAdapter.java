package com.qupp.client.utils.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;


/**
 * 快递
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */

public class KdAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {

    List<EntityForSimple> data;

    public KdAdapter(List<EntityForSimple> data) {
        super(R.layout.item_kd, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        int  position = helper.getAdapterPosition();
        try {
            if (position ==0) {
                // 第一行头的竖线不显示
                helper.setVisible(R.id.tvTopLine,false);
            } else if(position == data.size()-1){
                helper.setVisible(R.id.tv_line,false);
            }else{
                helper.setVisible(R.id.tvTopLine,true);
                helper.setVisible(R.id.tv_line,true);
            }
        }catch (Exception e){

        }
        helper.setText(R.id.tvAcceptTime,item.getTime());
        helper.setText(R.id.tvAcceptStation,item.getContext());

    }

}