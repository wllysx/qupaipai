package com.qupp.client.utils.adapter.pingan;




import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 银行卡
 * author: MrWang on 2019/8/12
 * email:773630555@qq.com
 * date: on 2019/8/12 17:31
 */

public class BankCardAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public BankCardAdapter(List<EntityForSimple> data) {
        super(R.layout.item_bank_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            Glide.with(mContext).load(item.getLogoUrl()).apply(new RequestOptions().placeholder(R.color.bg_main).error(R.color.bg_main)).into((ImageView) helper.getView(R.id.iv_logoUrl));
            helper.setText(R.id.tv_bankName,item.getBankName());
            helper.setText(R.id.tv_shortCardCode,"**** **** **** "+item.getShortCardCode());
            helper.setText(R.id.tv_useType,item.getUseType().equals("0")?"提现卡":"支付卡");
            if(item.getUseType().equals("0")){
                //提现卡
                helper.setBackgroundRes(R.id.ll_main, R.drawable.bg_bank_car);
            }else{
                //支付卡
                helper.setBackgroundRes(R.id.ll_main, R.drawable.bg_bank_car1);            }
        }catch (Exception e){

        }
    }


}
