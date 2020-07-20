package com.qupp.client.utils.dialog;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 商品分类
 */

public class commodityTypeAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {

    List<EntityForSimple> data;
    public commodityTypeAdapter(List<EntityForSimple> data) {
        super(R.layout.item_commodity_type, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        int position = helper.getAdapterPosition();
        if(data!=null&&data.size()>0) {
            if(data.size()-1==position){
                //最后一条
                helper.setImageResource(R.id.iv_logo, R.mipmap.xpq);
                helper.setText(R.id.tv_name, "新人区");
            }else{
                Glide.with(mContext).load(item.getImage()).apply(new RequestOptions().error(R.color.white).dontAnimate()).into((ImageView) helper.getView(R.id.iv_logo));
                helper.setText(R.id.tv_name, item.getName());
            }

        }
    }


}
