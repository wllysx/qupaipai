package com.qupp.client.utils.adapter;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 充值记录
 */

public class RecordAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public RecordAdapter(List<EntityForSimple> data) {
        super(R.layout.item_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {

        helper.setText(R.id.tv_payType,"充值来自-"+item.getRemark());
        helper.setText(R.id.tv_payTime,item.getPayTime());
        helper.setText(R.id.tv_money,item.getMoney());

    }


}
