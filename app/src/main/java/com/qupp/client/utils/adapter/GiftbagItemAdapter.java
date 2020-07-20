package com.qupp.client.utils.adapter;



import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.glide.GlideRoundTransform;

import java.util.List;


public class GiftbagItemAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {

    List<EntityForSimple> data;
    boolean isFlag = true;

    public GiftbagItemAdapter(List<EntityForSimple> data, boolean isFlag) {
        super(R.layout.item_giftbag_item, data);
        this.data = data;
        this.isFlag = isFlag;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        int position = helper.getAdapterPosition();
        if(position==data.size()-1){
            //最后一条不显示加号
            helper.setVisible(R.id.tv_jia,false);
        }else{
            helper.setVisible(R.id.tv_jia,true);
        }
       // Glide.with(mContext).load(item.getLogo()).apply(new RequestOptions().placeholder(R.color.white).error(R.color.white)).into((ImageView) helper.getView(R.id.iv_logo));

        RequestOptions options1 = new RequestOptions()
                .centerCrop()
                .apply(new RequestOptions())
                .priority(Priority.NORMAL)//优先级
                .transform(new GlideRoundTransform(7));
        Glide.with(mContext).load(item.getLogo()).apply(options1).into((ImageView) helper.getView(R.id.iv_logo));
        helper.setText(R.id.tv_goodsName,item.getMallGoodsName());
        helper.addOnClickListener(R.id.iv_logo);
        if(isFlag){
            //有库存
            helper.setTextColor(R.id.tv_goodsName, Color.parseColor("#636363"));
        }else{
            helper.setTextColor(R.id.tv_goodsName,mContext.getResources().getColor(R.color.textcolor33));
        }

    }


}
