package com.qupp.client.utils.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 商品分类
 */

public class commodityType1Adapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {

    String[] urls = {"https://oss.jiayikou.com/wechat_img/super.png", "https://oss.jiayikou.com/wechat_img/vips.png", "https://oss.jiayikou.com/wechat_img/malls.png", "https://oss.jiayikou.com/wechat_img/game.png", "https://oss.jiayikou.com/wechat_img/payPhone.png"};

    public commodityType1Adapter(List<EntityForSimple> data) {
        super(R.layout.item_commodity_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        int position = helper.getAdapterPosition();

      //  Glide.with(mContext).load(urls[position]).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into((ImageView) helper.getView(R.id.iv_logo));

        switch (position) {
            case 0:
                helper.setImageResource(R.id.iv_logo, R.mipmap.icon_main_type1);
                helper.setText(R.id.tv_name, "超值拍");
                break;
            case 1:
                helper.setImageResource(R.id.iv_logo, R.mipmap.icon_main_type3);
                helper.setText(R.id.tv_name, "VIP房间");
                break;
            case 2:
                helper.setImageResource(R.id.iv_logo, R.mipmap.icon_main_type4);
                helper.setText(R.id.tv_name, "商城");
                break;
            case 3:
                helper.setImageResource(R.id.iv_logo, R.mipmap.yx);
                helper.setText(R.id.tv_name, "游戏");
                break;
            case 4:
                helper.setImageResource(R.id.iv_logo, R.mipmap.hfcz_type);
                helper.setText(R.id.tv_name, "话费充值");
                break;
        }
    }


}
