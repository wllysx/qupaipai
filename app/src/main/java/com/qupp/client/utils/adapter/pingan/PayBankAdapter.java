package com.qupp.client.utils.adapter.pingan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 支付
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */

public class PayBankAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public List<EntityForSimple> mDatas;

    public PayBankAdapter(Context context, List<EntityForSimple> mDatas) {
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
            convertView = mInflater.inflate(R.layout.item_pay_bank, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(mDatas.get(position).getLogoUrl()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into(holder.ivLogoUrl);
        holder.tvBankName.setText(mDatas.get(position).getBankName() + "(" + mDatas.get(position).getShortCardCode() + ")");
        if (mDatas.get(position).getBankCardType().equals("1")) {
            holder.tvCardtype1.setVisibility(View.VISIBLE);
            holder.tvCardtype2.setVisibility(View.GONE);
        } else {
            holder.tvCardtype2.setVisibility(View.VISIBLE);
            holder.tvCardtype1.setVisibility(View.GONE);
        }
        if (mDatas.get(position).isChecked()) {
            holder.checkboxbank.setChecked(true);
        } else {
            holder.checkboxbank.setChecked(false);
        }
        holder.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,"111",Toast.LENGTH_SHORT).show();
                sure.onSure(position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.checkboxbank)
        CheckBox checkboxbank;
        @BindView(R.id.iv_logoUrl)
        ImageView ivLogoUrl;
        @BindView(R.id.tv_bankName)
        TextView tvBankName;
        @BindView(R.id.tv_cardtype1)
        TextView tvCardtype1;
        @BindView(R.id.tv_cardtype2)
        TextView tvCardtype2;
        @BindView(R.id.ll_view)
        LinearLayout llView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static interface Sure {
        void onSure(int position);
    }

    public Sure sure;

    public void setSureListener(Sure sure) {
        this.sure = sure;
    }

}
