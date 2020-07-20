package com.qupp.client.utils.dialog;


import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.adapter.GiftbagItemAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 大礼包
 */

public class GiftbagOrderAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public GiftbagOrderAdapter(List<EntityForSimple> data) {
        super(R.layout.item_giftbag_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {

        helper.setText(R.id.tv_mealno,"套餐"+(helper.getAdapterPosition()+1)+"：");

        String mealtx = "";
        String integralRatio =  item.getIntegral();
        String worthRatio =  item.getWorthIntegral();
        if(!integralRatio.equals("0")&&!worthRatio.equals("0")){
            //有积分和有超值积分
            mealtx = "<font color='#DD0000'>"+integralRatio+"</font>"+"购物积分+"+"<font color='#DD0000'>"+worthRatio+"</font>"+"消费积分";
        }else if(!integralRatio.equals("0")){
            //只有积分
            mealtx = "<font color='#DD0000'>"+integralRatio+"</font>"+"购物积分";
        }else if(!worthRatio.equals("0")){
            //只有超值
            mealtx = "<font color='#DD0000'>"+worthRatio+"</font>"+"消费积分";
        }
        ((TextView) helper.getView(R.id.tv_mealcontent)).setText( Html.fromHtml(mealtx));
        //上部
        if(item.getAuctionMealGoodsVoList()!=null&&item.getAuctionMealGoodsVoList().size()>0) {
            helper.getView(R.id.ll_meal).setVisibility(View.VISIBLE);
            if(item.isFlag()) {
                //有库存
                initAdapter(helper.getView(R.id.mRecyclerView), item.getAuctionMealGoodsVoList(),true);
            }else{
                //无库存
                initAdapter(helper.getView(R.id.mRecyclerView), item.getAuctionMealGoodsVoList(),false);
            }
        }else{
            helper.getView(R.id.ll_meal).setVisibility(View.GONE);
        }
        //先判断库存
        if(item.isFlag()) {
            if (item.isSelected()) {
                helper.setImageResource(R.id.iv_select, R.mipmap.xz);
            } else {
                helper.setImageResource(R.id.iv_select, R.mipmap.wxz);
            }

            helper.setVisible(R.id.iv_guang,false);
            helper.setTextColor(R.id.tv_mealno,mContext.getResources().getColor(R.color.textcolor7));
            helper.setTextColor(R.id.tv_mealno,mContext.getResources().getColor(R.color.textcolor7));
        }else{
            helper.setImageResource(R.id.iv_select, R.mipmap.bkxz);

            helper.setVisible(R.id.iv_guang,true);
            helper.setTextColor(R.id.tv_mealno,mContext.getResources().getColor(R.color.textcolor33));
            helper.setTextColor(R.id.tv_mealno,mContext.getResources().getColor(R.color.textcolor33));
        }

    }


    private void initAdapter(RecyclerView mRecyclerView, ArrayList<EntityForSimple> data, boolean isFlag) {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        GiftbagItemAdapter adapter = new GiftbagItemAdapter(data,isFlag);
        adapter.bindToRecyclerView(mRecyclerView);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            CommodityDetailsActivity.startActivityInstance(mContext,data.get(position).getMallGoodsId());
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }




}
