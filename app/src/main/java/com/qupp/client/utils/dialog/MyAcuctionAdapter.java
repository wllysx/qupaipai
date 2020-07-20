package com.qupp.client.utils.dialog;



import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.glide.GlideRoundTransform1;

import java.util.List;

/**
 * 我的活动
 */

public class MyAcuctionAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public MyAcuctionAdapter(List<EntityForSimple> data) {
        super(R.layout.item_myauction, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
      //  Glide.with(mContext).load(item.getImage()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));
        helper.setText(R.id.tv_title,item.getName());
        helper.setText(R.id.tv_content,item.getPrompt());
        RequestOptions options1 = new RequestOptions()
                .centerCrop()
                .apply(new RequestOptions())
                .priority(Priority.NORMAL)//优先级
                .transform(new GlideRoundTransform1(12));
        Glide.with(mContext).load(item.getImage()).apply(options1).into((ImageView) helper.getView(R.id.iv_logo));

    }


}
