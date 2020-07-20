package com.qupp.client.utils.adapter;



import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 话费充值
 * author: MrWang on 2019/8/12
 * email:773630555@qq.com
 * date: on 2019/8/12 17:31
 */

public class TelephoneAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {

    String paytype;

    public TelephoneAdapter(List<EntityForSimple> data,String paytype) {
        super(R.layout.item_telephone, data);
        this.paytype = paytype;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {

        helper.setText(R.id.tv_price,item.getMoney().replace(".00",""));
        if(paytype.equals("1")){
            helper.setText(R.id.tv_content,"售价 "+item.getIntegral()+"积分");
        }else if(paytype.equals("2")){
            helper.setText(R.id.tv_content,"售价 "+item.getIntegral()+"积分/\n"+item.getPanelAmount().replace(".00","")+"元");
        }else{
            helper.setText(R.id.tv_content,"售价 "+item.getPanelAmount().replace(".00","")+"元");
        }

        if(item.isChecked()) {
            if(item.isSelected()){
                helper.setTextColor(R.id.tv_price, mContext.getResources().getColor(R.color.white));
                helper.setTextColor(R.id.tv_yuan, mContext.getResources().getColor(R.color.white));
                helper.setTextColor(R.id.tv_content,mContext.getResources().getColor(R.color.white));
                helper.setBackgroundRes(R.id.ll_bg, R.drawable.bg_telephone_select1);
            }else {
                helper.setTextColor(R.id.tv_price, mContext.getResources().getColor(R.color.iscur));
                helper.setTextColor(R.id.tv_yuan, mContext.getResources().getColor(R.color.iscur));
                helper.setTextColor(R.id.tv_content, Color.parseColor("#565656"));
                helper.setBackgroundRes(R.id.ll_bg, R.drawable.bg_telephone_select);
            }
        }else {
            helper.setTextColor(R.id.tv_price,Color.parseColor("#b0b1b1"));
            helper.setTextColor(R.id.tv_yuan, Color.parseColor("#b0b1b1"));
            helper.setTextColor(R.id.tv_content,Color.parseColor("#b0b1b1"));
            helper.setBackgroundRes(R.id.ll_bg,R.drawable.bg_telephone_select_un);
        }

    }


}
