package com.qupp.client.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * menuAdapter
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */

public class ShopMenuAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public ArrayList<EntityForSimple> mDatas;

    public ShopMenuAdapter(Context context, ArrayList<EntityForSimple> mDatas) {
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
            convertView = mInflater.inflate(R.layout.item_shop_menu, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (position){
            case 0:
                holder.tvContent.setText("全部");
                break;
            case 1:
                holder.tvContent.setText("寄拍区");
                break;
            case 2:
                holder.tvContent.setText("换购区");
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
