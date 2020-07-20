package com.qupp.client.utils.dialog;



import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 商品出价
 */

public class AddPriceAdapter extends BaseMultiItemQuickAdapter<EntityForSimple, BaseViewHolder> {


    private List<EntityForSimple> data;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AddPriceAdapter(List<EntityForSimple> data) {
        super(data);
        this.data = data;
        addItemType(EntityForSimple.TYPE_TOP, R.layout.item_addprice_typetop);
        addItemType(EntityForSimple.TYPE_OTHER, R.layout.item_addprice_typeother);
        addItemType(EntityForSimple.TYPE_SELF, R.layout.item_addprice_typeself);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {

        int position = helper.getAdapterPosition();

        switch (helper.getItemViewType()) {
            case EntityForSimple.TYPE_TOP:
                helper.setText(R.id.tv_goodsName,item.getGoodsName());
                helper.setText(R.id.tv_time,item.getTime());

                break;
            case EntityForSimple.TYPE_OTHER:
                Glide.with(mContext).load(item.getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into((ImageView) helper.getView(R.id.iv_logo));
                helper.setText(R.id.tv_markupTimeString,item.getMarkupTimeString());
                helper.setText(R.id.tv_nickname,item.getUserName());
                helper.setText(R.id.tv_payPrice,"出价：￥"+item.getPayPrice());
                helper.setText(R.id.tv_brokerage,"￥"+item.getBrokerage());
                if(data.size()==position+1){
                    helper.setGone(R.id.ll_hongbao,false);
                }else{
                    helper.setGone(R.id.ll_hongbao,true);
                }
                //是否是定制机
                if(item.isCustomMarkUp()){
                    helper.setVisible(R.id.iv_hg,true);
                    helper.setTextColor(R.id.tv_huo, Color.parseColor("#FFB103"));
                    helper.setTextColor(R.id.tv_brokerage, Color.parseColor("#FFB103"));
                    helper.setTextColor(R.id.tv_payPrice, Color.parseColor("#FFB103"));
                    helper.setBackgroundRes(R.id.ll_price, R.drawable.bg_addprice_other1);
                }else{
                    helper.setVisible(R.id.iv_hg,false);
                    helper.setTextColor(R.id.tv_huo, Color.parseColor("#8C6934"));
                    helper.setTextColor(R.id.tv_brokerage, Color.parseColor("#8C6934"));
                    helper.setTextColor(R.id.tv_payPrice, Color.parseColor("#8C6934"));
                    helper.setBackgroundRes(R.id.ll_price, R.drawable.bg_addprice_other);
                }
                break;
            case EntityForSimple.TYPE_SELF:
                Glide.with(mContext).load(item.getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into((ImageView) helper.getView(R.id.iv_logo));
                helper.setText(R.id.tv_markupTimeString,item.getMarkupTimeString());
                helper.setText(R.id.tv_nickname,item.getUserName());
                helper.setText(R.id.tv_payPrice,"出价：￥"+item.getPayPrice());
                helper.setText(R.id.tv_brokerage,"￥"+item.getBrokerage());
                if(data.size()==position+1){
                    helper.setGone(R.id.ll_hongbao,false);
                }else{
                    helper.setGone(R.id.ll_hongbao,true);
                }
                break;
            default:
                break;
        }

    }


}
