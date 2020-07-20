package com.qupp.client.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcssloop.widget.RCImageView;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * menuAdapter
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */

public class EarningsAdapter1 extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public List<EntityForSimple> mDatas;

    public EarningsAdapter1(Context context, List<EntityForSimple> mDatas) {
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
            convertView = mInflater.inflate(R.layout.item_earnings, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            Glide.with(mContext).load(mDatas.get(position).getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into(holder.ivPhoto);
            //  Glide.with(mContext).load(item.getAvatar()).placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default).into((ImageView) helper.getView(R.id.iv_logo));
            holder.tvName.setText(mDatas.get(position).getUserName());
            holder.tvName.setText(mDatas.get(position).getUserName());
            holder.tvNum.setText(mDatas.get(position).getNum()+"次出价");
            holder.tvPrice.setText("收益 ￥"+mDatas.get(position).getTotalPrice());
        }catch (Exception e){

        }

        if(position==0){
            //第一名
            holder.tvNo.setText("");
            holder.tvNo.setBackgroundResource(R.mipmap.spxq_1);
        }else if(position==1){
            //第二名
            holder.tvNo.setText("");
            holder.tvNo.setBackgroundResource(R.mipmap.spxq_2);
        }else if(position==2){
            //第三名
            holder.tvNo.setText("");
            holder.tvNo.setBackgroundResource(R.mipmap.spxq_3);
        }else{
            holder.tvNo.setText((position+1)+"");
            holder.tvNo.setBackgroundResource(R.drawable.bg_circle_graw);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.iv_photo)
        RCImageView ivPhoto;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_price)
        TextView tvPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
