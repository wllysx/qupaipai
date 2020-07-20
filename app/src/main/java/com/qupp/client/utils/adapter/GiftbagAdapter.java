package com.qupp.client.utils.adapter;


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

import java.util.ArrayList;
import java.util.List;


/**
 * 大礼包
 */

public class GiftbagAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public GiftbagAdapter(List<EntityForSimple> data) {
        super(R.layout.item_giftbag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {

        helper.setText(R.id.tv_mealno,"套餐"+(helper.getAdapterPosition()+1)+"：");

        String mealtx = "";
        if(!item.getIntegralRatio().equals("0")&&!item.getWorthRatio().equals("0")){
            //有积分和有超值积分
            String integralRatio =  ((Double.valueOf(item.getIntegralRatio())*1000)/10+"%").replace(".0","");
            String worthRatio =  ((Double.valueOf(item.getWorthRatio())*1000)/10+"%").replace(".0","");
            mealtx = "成交价（"+"<font color='#DD0000'>"+integralRatio+"</font>"+"超值积分+"+"<font color='#DD0000'>"+worthRatio+"</font>"+"积分)";

        }else if(!item.getIntegralRatio().equals("0")){
            //只有积分
            String integralRatio =  ((Double.valueOf(item.getIntegralRatio())*1000)/10+"%").replace(".0","");
            mealtx = "成交价"+"<font color='#DD0000'>"+integralRatio+"</font>"+"超值积分";


        }else if(!item.getWorthRatio().equals("0")){
            //只有超值
            String worthRatio =  ((Double.valueOf(item.getWorthRatio())*1000)/10+"%").replace(".0","");
            mealtx = "成交价"+"<font color='#DD0000'>"+worthRatio+"</font>"+"积分";
        }
        ((TextView) helper.getView(R.id.tv_mealcontent)).setText( Html.fromHtml(mealtx));

        //上部
        if(item.getAuctionMealGoodsVoList()!=null&&item.getAuctionMealGoodsVoList().size()>0) {
            helper.getView(R.id.mRecyclerView).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_zeng).setVisibility(View.VISIBLE);
            initAdapter(helper.getView(R.id.mRecyclerView), item.getAuctionMealGoodsVoList());
            helper.getView(R.id.ll_meal).setVisibility(View.VISIBLE);
        }else{
            helper.getView(R.id.mRecyclerView).setVisibility(View.GONE);
            helper.getView(R.id.iv_zeng).setVisibility(View.GONE);
            helper.getView(R.id.ll_meal).setVisibility(View.GONE);

        }

    }


    private void initAdapter(RecyclerView mRecyclerView, ArrayList<EntityForSimple> data) {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        GiftbagItemAdapter adapter = new GiftbagItemAdapter(data,true);
        adapter.bindToRecyclerView(mRecyclerView);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            CommodityDetailsActivity.startActivityInstance(mContext,data.get(position).getMallGoodsId());
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }




}
