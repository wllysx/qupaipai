package com.qupp.client.utils.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 分类右侧
 * author: MrWang on 2019/8/12
 * email:773630555@qq.com
 * date: on 2019/8/12 17:31
 */

public class TypeRightAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public TypeRightAdapter(List<EntityForSimple> data) {
        super(R.layout.item_type_right, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        try {
            helper.setText(R.id.tv_name,item.getName());
            Glide.with(mContext).load(item.getPic()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
        }catch (Exception e){

        }

    }


}
