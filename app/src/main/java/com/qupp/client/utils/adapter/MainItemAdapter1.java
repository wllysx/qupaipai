package com.qupp.client.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.main.NewCommodityDetailsActivity;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.DateUtils;
import com.qupp.client.utils.DoubleClick;
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

public class MainItemAdapter1 extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public List<EntityForSimple> mDatas;

    public MainItemAdapter1(Context context, List<EntityForSimple> mDatas) {
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
            convertView = mInflater.inflate(R.layout.item_main, null);
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
            holder.rlView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DoubleClick.isFastDoubleClick()) {
                        return;
                    }
                    NewCommodityDetailsActivity.startActivityInstance(mContext, mDatas.get(position).getStatus(), mDatas.get(position).getAuctionId(), mDatas.get(position).getTopPrice(), mDatas.get(position).getGoodsName());
                }
            });

            holder.tvGoodsName.setText(mDatas.get(position).getGoodsName().replace("\n",""));

            if(mDatas.get(position).getStatus().equals("2")){
                //未开始

                holder.tvPricetype.setText("起拍价");
                holder.tvCount.setVisibility(View.VISIBLE);
                holder.tvCount.setText(mDatas.get(position).getViewCount()+"人关注");
                holder.tvTime.setText("开拍时间:"+ DateUtils.dateToDate(mDatas.get(position).getStartTime(),"yyyy-MM-dd HH:mm:ss","HH:mm"));
                holder.tvPrice.setText("￥"+mDatas.get(position).getStartPrice().replace(".00",""));
                holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.yellow5));
                holder.tvGoprice.setVisibility(View.GONE);
                holder.tvFinish.setVisibility(View.GONE);
            }else if(mDatas.get(position).getStatus().equals("3")){
                //进行中
                holder.tvPricetype.setText("当前价");
                holder.tvCount.setVisibility(View.VISIBLE);
                holder.tvCount.setText(mDatas.get(position).getMarkupNum()+"次出价");
                holder.tvTime.setText("结拍时间:"+ DateUtils.dateToDate(mDatas.get(position).getEndTime(),"yyyy-MM-dd HH:mm:ss","HH:mm"));
                holder.tvPrice.setText("￥"+mDatas.get(position).getTopPrice().replace(".00",""));
                holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.yellow5));
                holder.tvGoprice.setVisibility(View.VISIBLE);
                holder.tvFinish.setVisibility(View.GONE);
            }else {
                //已结束
                holder.tvPricetype.setText("成交价");
                holder.tvCount.setVisibility(View.VISIBLE);
                holder.tvCount.setText(mDatas.get(position).getMarkupNum() + "次出价");
                holder.tvTime.setText("结拍时间:" + DateUtils.dateToDate(mDatas.get(position).getEndTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                holder.tvPrice.setText("￥" + mDatas.get(position).getTopPrice().replace(".00", ""));
                holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.textcolor23));
                holder.tvGoprice.setVisibility(View.GONE);
                holder.tvFinish.setVisibility(View.VISIBLE);
            }
            return convertView;
        }catch (Exception e){
        }
        return convertView;

    }

    static class ViewHolder {
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_finish)
        TextView tvFinish;
        @BindView(R.id.tv_goodsName)
        TextView tvGoodsName;
        @BindView(R.id.tv_pricetype)
        TextView tvPricetype;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_goprice)
        TextView tvGoprice;
        @BindView(R.id.rl_view)
        RelativeLayout rlView;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
