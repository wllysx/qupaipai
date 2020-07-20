package com.qupp.client.utils.adapter;


import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 提现记录
 */

public class TelephoneRecordAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {



    public TelephoneRecordAdapter(List<EntityForSimple> data) {
        super(R.layout.item_telephone_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        helper.setText(R.id.tv_title,"话费充值 "+item.getRechargeAmount().replace(".00",""));
        helper.setText(R.id.tv_rechargeAmount,item.getPaymentAmount().replace(".00",""));
        if(item.getStatus().equals("0")) {
            helper.setText(R.id.tv_status,"充值中");
            helper.setTextColor(R.id.tv_status, Color.parseColor("#E49725"));
        }else if(item.getStatus().equals("1")){
            helper.setText(R.id.tv_status,"充值成功");
            helper.setTextColor(R.id.tv_status, Color.parseColor("#368BE8"));
        }else{
            helper.setText(R.id.tv_status,"充值失败");
            helper.setTextColor(R.id.tv_status, Color.parseColor("#DD0000"));
        }
        helper.setText(R.id.tv_createTime,item.getCreateTime());
        helper.setText(R.id.tv_typeName,item.getTypeName()+" "+item.getMobile());
    }


}
