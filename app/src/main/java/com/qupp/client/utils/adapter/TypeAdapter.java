package com.qupp.client.utils.adapter;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 商品分类
 * author: MrWang on 2019/8/12
 * email:773630555@qq.com
 * date: on 2019/8/12 17:31
 */

public class TypeAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public TypeAdapter(List<EntityForSimple> data) {
        super(R.layout.item_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        if(item.isChecked()){
            helper.setVisible(R.id.v_left,true);
            helper.setTextColor(R.id.tv_name,mContext.getResources().getColor(R.color.iscur));
        }else{
            helper.setVisible(R.id.v_left,false);
            helper.setTextColor(R.id.tv_name,mContext.getResources().getColor(R.color.textcolor7));
        }
        helper.setText(R.id.tv_name,item.getName());

    }


}
