package com.qupp.client.utils.adapter;



import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 提现记录
 * author: MrWang on 2019/8/12
 * email:773630555@qq.com
 * date: on 2019/8/12 17:31
 */

public class WithDrawRecordAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public WithDrawRecordAdapter(List<EntityForSimple> data) {
        super(R.layout.item_withdraw, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        helper.setText(R.id.tv_amount,item.getAmount());
        helper.setText(R.id.tv_time,item.getCreateTime());
        helper.setText(R.id.tv_title,(item.getType()==0?"余额提现-到微信（"+item.getWithdrawName()+"）":"余额提现-到支付宝（"+item.getWithdrawAccount()+"）"));
        int status = Integer.valueOf(item.getStatus());
        switch (status){
            case 1:
                //审核中
                helper.setGone(R.id.tv_states,true);
                helper.setTextColor(R.id.tv_states,mContext.getResources().getColor(R.color.yellow2));
                helper.setText(R.id.tv_states,"审核中");
                helper.setGone(R.id.tv_back,true);
                break;
            case 2:
                //成功
                helper.setGone(R.id.tv_states,false);
                helper.setGone(R.id.tv_back,false);
                break;
            case 3:
                //失败
                helper.setGone(R.id.tv_states,true);
                helper.setTextColor(R.id.tv_states,mContext.getResources().getColor(R.color.iscur));
                helper.setText(R.id.tv_states,"失败");
                helper.setGone(R.id.tv_back,false);
                break;
            case 4:
                //提现撤回
                helper.setGone(R.id.tv_states,true);
                helper.setTextColor(R.id.tv_states, Color.parseColor("#ff8b8b8b"));
                helper.setText(R.id.tv_states,"提现撤回");
                helper.setGone(R.id.tv_back,false);
                break;
        }
        helper.addOnClickListener(R.id.tv_back);
    }


}
