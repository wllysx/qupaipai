package com.qupp.client.utils.adapter;




import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;


public class HotSearchAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public HotSearchAdapter(List<EntityForSimple> data) {
        super(R.layout.item_hotsearch, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        helper.setText(R.id.tv_name,item.getName());
    }


}
