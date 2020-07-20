package com.qupp.client.utils.adapter;


import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * vip店铺
 * author: MrWang on 2019/8/12
 * email:773630555@qq.com
 * date: on 2019/8/12 17:31
 */

public class VipRoomAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public VipRoomAdapter(List<EntityForSimple> data) {
        super(R.layout.item_viproom, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {

            if(item.getOpenCustom().equals("1")){
                //定制版本
                if (item.getOpenPassword().equals("0")) {
                    helper.setImageResource(R.id.iv_logo, R.mipmap.qjd_ms_vip);
                } else {
                    helper.setImageResource(R.id.iv_logo, R.mipmap.qjd_ys_vip);
                }
                helper.setText(R.id.tv_roomno,item.getName());
                helper.setText(R.id.tv_no,"");
                helper.setText(R.id.tv_count,item.getViewCount()+"人");
            }else {
                //普通版本
                if (item.getOpenPassword().equals("0")) {
                    helper.setTextColor(R.id.tv_no, Color.parseColor("#A6C9E3"));
                    helper.setImageResource(R.id.iv_logo, R.mipmap.qjd_ms);
                } else {
                    helper.setTextColor(R.id.tv_no, Color.parseColor("#fad994"));
                    helper.setImageResource(R.id.iv_logo, R.mipmap.qjd_ys);
                }
                helper.setText(R.id.tv_roomno,item.getName());
                helper.setText(R.id.tv_no,item.getNumber());
                helper.setText(R.id.tv_count,item.getViewCount()+"人");
            }
        }catch (Exception e){

        }


    }


}
