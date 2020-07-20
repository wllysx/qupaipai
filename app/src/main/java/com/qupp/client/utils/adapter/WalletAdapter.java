package com.qupp.client.utils.adapter;




import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 钱包
 */

public class WalletAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public WalletAdapter(List<EntityForSimple> data) {
        super(R.layout.item_wallet, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            if(item.getTradeType().equals("1")) {
                helper.setText(R.id.tv_title, "平台操作");
            }else if(item.getTradeType().equals("2")){
                if(item.getType()==1){
                    helper.setText(R.id.tv_title, "积分商城订单取消");
                }else{
                    helper.setText(R.id.tv_title, "积分商城订单支付");
                }

            }else if(item.getTradeType().equals("3")){
                helper.setText(R.id.tv_title, "旗舰店订单支付尾款");
            }else{
                helper.setText(R.id.tv_title, "金额变动");
            }
            helper.setText(R.id.tv_time,item.getCreateTime());
            helper.setText(R.id.tv_cause,item.getRemark());

            if(item.getType()==1){
                //收入
                helper.setText(R.id.tv_amount,"+"+item.getAmount());
                helper.setTextColor(R.id.tv_amount,mContext.getResources().getColor(R.color.iscur));
            }else{
                //支出
                helper.setText(R.id.tv_amount,"-"+item.getAmount());
                helper.setTextColor(R.id.tv_amount, Color.parseColor("#262626"));
            }
        }catch (Exception e){

        }
    }


}
