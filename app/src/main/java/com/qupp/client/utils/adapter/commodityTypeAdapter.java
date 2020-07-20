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
 * 商品分类
 * author: MrWang on 2019/8/12
 * email:773630555@qq.com
 * date: on 2019/8/12 17:31
 */

public class commodityTypeAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {

    List<EntityForSimple> data;
    String url = "https://oss.jiayikou.com/wechat_img/newPople.png";
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
                helper.setImageResource(R.id.iv_logo,R.mipmap.xpq);
                helper.setText(R.id.tv_name, "新人区");

              /*  Glide
                        .with(mContext)
                        .asBitmap()
                        .load(url)
                        .apply(new RequestOptions() .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE) .dontAnimate()) // 不使用内存缓存  )
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                ((ImageView) helper.getView(R.id.iv_logo)).setImageBitmap(resource);
                            }

                        });*/

            }else{
                Glide.with(mContext).load(item.getImage()).apply(new RequestOptions().error(R.color.line).dontAnimate()).into((ImageView) helper.getView(R.id.iv_logo));
                Glide.with(mContext).load(item.getImage()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));

                helper.setText(R.id.tv_name, item.getName());
            }

        }
    }


}
