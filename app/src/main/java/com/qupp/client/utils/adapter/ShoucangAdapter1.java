package com.qupp.client.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.glide.GlideRoundTransform1;
import com.qupp.client.utils.view.swipedel.SwipeMenuLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * menuAdapter
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */

public class ShoucangAdapter1 extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public List<EntityForSimple> mDatas;

    public ShoucangAdapter1(Context context, List<EntityForSimple> mDatas) {
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
            convertView = mInflater.inflate(R.layout.item_myshoucang, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {


        RequestOptions options1 = new RequestOptions()
                .centerCrop()
                .apply(new RequestOptions())
                .priority(Priority.NORMAL)//优先级
                //.skipMemoryCache(true)
                .override(MyApplication.itemHeight,MyApplication.itemHeight)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
                .transform(new GlideRoundTransform1(6));
        Glide.with(mContext).load(mDatas.get(position).getLogo()).apply(options1).into(holder.ivLogo);
        holder.tvCommoditytitle.setText(mDatas.get(position).getGoodsName());
        if(mDatas.get(position).getPriceType().equals("1")){
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvIntegral.setText(mDatas.get(position).getIntegral().replace(".00","")+"积分");
        }else if(mDatas.get(position).getPriceType().equals("2")){
            holder.tvIntegral.setText(mDatas.get(position).getIntegral().replace(".00","")+"积分");
            holder.tvPrice.setText("+￥"+mDatas.get(position).getPrice().replace(".00",""));
            holder.tvPrice.setVisibility(View.VISIBLE);
        }else{
            holder.tvIntegral.setText("￥"+mDatas.get(position).getPrice().replace(".00",""));
            holder.tvPrice.setVisibility(View.GONE);
        }
        holder.llLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.listener(position);
            }
        });
        holder.hideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click1.listener(position);
            }
        });
        }catch (Exception e){

        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_commoditytitle)
        TextView tvCommoditytitle;
        @BindView(R.id.tv_integral)
        TextView tvIntegral;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.ll_linear)
        LinearLayout llLinear;
        @BindView(R.id.hide_delete)
        TextView hideDelete;
        @BindView(R.id.hide_view)
        LinearLayout hideView;
        @BindView(R.id.swipemenu)
        SwipeMenuLayout swipemenu;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static interface Click{
        void listener(int position);
    }

    private Click click;

    public void setClick(Click click){
        this.click = click;
    }

    public static interface Click1{
        void listener(int position);
    }

    private Click1 click1;

    public void setClick1(Click1 click1){
        this.click1 = click1;
    }


}
