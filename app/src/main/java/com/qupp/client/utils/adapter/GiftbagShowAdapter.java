package com.qupp.client.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.scoreshop.MyOrderDetails;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单详情展示的礼包列表
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */

public class GiftbagShowAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public List<EntityForSimple> mDatas;

    public GiftbagShowAdapter(Context context, List<EntityForSimple> mDatas) {
        mContext = context;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_order_meal_show, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            Glide.with(mContext).load(mDatas.get(position).getLogo()).apply(new RequestOptions().placeholder(R.color.white).error(R.color.white)).into(holder.ivLogo);
            holder.tvGoodName.setText(mDatas.get(position).getMallGoodsName().replace("\n",""));
            if(mDatas.get(position).getOrderId()!=null){
                holder.tvSee.setVisibility(View.VISIBLE);
            }else{
                holder.tvSee.setVisibility(View.GONE);
            }
            holder.tvSee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyOrderDetails.startActivityInstance(mContext,mDatas.get(position).getOrderId());
                }
            });
        } catch (Exception e) {

        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_goodsName)
        TextView tvGoodName;
        @BindView(R.id.tv_see)
        TextView tvSee;



        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
