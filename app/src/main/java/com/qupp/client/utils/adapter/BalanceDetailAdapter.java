package com.qupp.client.utils.adapter;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.DateUtils;

import java.util.List;

/**
 * 余额明细
 */

public class BalanceDetailAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public BalanceDetailAdapter(List<EntityForSimple> data) {
        super(R.layout.item_balancedetail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        if(item.isChecked()){
            helper.setGone(R.id.tv_more,true);
            helper.setGone(R.id.ll_top,false);
        }else{
            helper.setGone(R.id.tv_more,false);
            helper.setGone(R.id.ll_top,true);
            if(item.getType()==0){
                helper.setTextColor(R.id.tv_amount,mContext.getResources().getColor(R.color.textcolor30));
            }else{
                helper.setTextColor(R.id.tv_amount,mContext.getResources().getColor(R.color.iscur));
            }
            helper.setText(R.id.tv_amount,item.getType()==0?"-"+item.getAmount():"+"+item.getAmount());
            helper.setText(R.id.tv_remark,item.getTradeTypeName());
            helper.setText(R.id.tv_content,item.getRemark());
            try {
                helper.setText(R.id.tv_createTime, DateUtils.dateToDate(item.getCreateTime(),"yyyy-MM-dd HH:mm:ss","MM-dd HH:mm"));
            }catch (Exception e){

            }
        }
        helper.addOnClickListener(R.id.tv_more);


    }


}
