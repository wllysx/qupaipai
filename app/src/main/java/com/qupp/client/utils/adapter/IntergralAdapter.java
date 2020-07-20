package com.qupp.client.utils.adapter;



import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 我的积分
 */

public class IntergralAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public IntergralAdapter(List<EntityForSimple> data) {
        super(R.layout.item_intergral, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        helper.setText(R.id.tv_integralAmount,item.getType()==0?"-"+item.getIntegralAmount():"+"+item.getIntegralAmount());
        helper.setTextColor(R.id.tv_integralAmount,item.getType()==0? Color.parseColor("#2B2B2B"):Color.parseColor("#DD0000"));
        String frontTilee = "";
        if(item.getTradeType().equals("0")){
            frontTilee = "订单赠送积分-";
        }
        if(item.getTradeType().equals("1")){
            frontTilee = "平台赠送-";
        }
        if(item.getTradeType().equals("2")){
            frontTilee = "积分兑换-";
        }
        if(item.getTradeType().equals("3")){
            frontTilee = "取消订单回退积分-";
        }
        if(item.getTradeType().equals("4")){
            frontTilee = "新用户注册-";
        }
        if(item.getTradeType().equals("5")){
            frontTilee = "签到送积分-";
        }
        if(item.getTradeType().equals("6")){
            frontTilee = "提现手续费扣除-";
        }
        if(item.getTradeType().equals("7")){
            frontTilee = "提现失败手续费退还-";
        }
        if(item.getTradeType().equals("8")){
            frontTilee = "活动消耗-";
        }
        if(item.getTradeType().equals("9")){
            frontTilee = "活动获得-";
        }
        helper.setText(R.id.tv_title,frontTilee+item.getTitle());
        helper.setText(R.id.tv_createTime,item.getCreateTime());

    }


}
