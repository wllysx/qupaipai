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
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.glide.GlideRoundTransform1;
import com.qupp.client.utils.waterfall.CalculateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * menuAdapter
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */

public class ShopAdapter1 extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public List<EntityForSimple> mDatas;

    public ShopAdapter1(Context context, List<EntityForSimple> mDatas) {
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
            convertView = mInflater.inflate(R.layout.item_shop, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            //计算高度start
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) holder.ivLogo.getLayoutParams();
            linearParams.height = CalculateUtils.getHeightByUrl(mDatas.get(position).getLogo());
            holder.ivLogo.setLayoutParams(linearParams);

            RequestOptions options1 = new RequestOptions()
                    .centerCrop()
                    .apply(new RequestOptions())
                    .priority(Priority.NORMAL)//优先级
                    .transform(new GlideRoundTransform1(6));
            Glide.with(mContext).load(mDatas.get(position).getLogo()).apply(options1).into(holder.ivLogo);
            //计算高度end

            //寄拍计算库存呢
            try {
               /* if (mDatas.get(position).getGoodsType().equals("1")) {*/

                    if (Integer.valueOf(mDatas.get(position).getSoldNum()) >= Integer.valueOf(mDatas.get(position).getInventory())) {
                        holder.tvFinish.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvFinish.setVisibility(View.INVISIBLE);
                    }
              /*  }else{
                    holder.tvFinish.setVisibility(View.INVISIBLE);
                }*/

            }catch (Exception e){

            }

            holder.tvGoodsName.setText(mDatas.get(position).getGoodsName().replace("\n",""));
            if(mDatas.get(position).getPriceType().equals("1")){
                holder.tvPrice.setText(mDatas.get(position).getIntegral().replace(".00","")+"积分");
                holder.tvIntegral.setVisibility(View.GONE);
            }else if(mDatas.get(position).getPriceType().equals("2")){
                holder.tvIntegral.setText(" +"+mDatas.get(position).getIntegral().replace(".00","")+"积分");
                holder.tvPrice.setText("￥"+mDatas.get(position).getPrice().replace(".00",""));
                holder.tvPrice.setVisibility(View.VISIBLE);
                holder.tvIntegral.setVisibility(View.VISIBLE);
            }else if(mDatas.get(position).getPriceType().equals("3")){
                holder.tvPrice.setText("￥"+mDatas.get(position).getPrice().replace(".00",""));
                holder.tvIntegral.setVisibility(View.GONE);
            }else{
                holder.tvIntegral.setText(" +"+mDatas.get(position).getIntegral().replace(".00","")+"钱包");
                holder.tvPrice.setText("￥"+mDatas.get(position).getPrice().replace(".00",""));
                holder.tvPrice.setVisibility(View.VISIBLE);
                holder.tvIntegral.setVisibility(View.VISIBLE);
            }
            holder.vView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommodityDetailsActivity.startActivityInstance(mContext, mDatas.get(position).getGoodsId());
                }
            });

            return convertView;
        }catch (Exception e){

        }
        return convertView;

    }

    static class ViewHolder {
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_goodsName)
        TextView tvGoodsName;
        @BindView(R.id.tv_integral)
        TextView tvIntegral;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.v_view)
        View vView;
        @BindView(R.id.tv_finish)
        TextView tvFinish;



        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
