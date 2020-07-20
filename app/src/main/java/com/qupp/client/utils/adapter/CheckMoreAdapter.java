package com.qupp.client.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 寄拍明细
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */

public class CheckMoreAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public List<EntityForSimple> mDatas;

    public CheckMoreAdapter(Context context, List<EntityForSimple> mDatas) {
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
            convertView = mInflater.inflate(R.layout.item_check_more, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (position){
            case 0:
                holder.tvTitle.setText("第一次寄拍");
                break;
            case 1:
                holder.tvTitle.setText("第二次寄拍");
                break;
            case 2:
                holder.tvTitle.setText("第三次寄拍");
                break;
            case 3:
                holder.tvTitle.setText("第四次寄拍");
                break;
            case 4:
                holder.tvTitle.setText("第五次寄拍");
                break;
            case 5:
                holder.tvTitle.setText("第六次寄拍");
                break;
            case 6:
                holder.tvTitle.setText("第七次寄拍");
                break;
            case 7:
                holder.tvTitle.setText("第八次寄拍");
                break;
            case 8:
                holder.tvTitle.setText("第九次寄拍");
                break;
            case 9:
                holder.tvTitle.setText("第十次寄拍");
                break;
        }
        holder.tvTime.setText(mDatas.get(position).getStartDay());
        if(position==0){
            holder.tvTopline.setVisibility(View.INVISIBLE);
            holder.tvEndline.setVisibility(View.VISIBLE);
            holder.tvPotin.setBackgroundResource(R.drawable.check_time_begin);
        }else if(position == mDatas.size()-1){
            holder.tvTopline.setVisibility(View.VISIBLE);
            holder.tvEndline.setVisibility(View.INVISIBLE);
            holder.tvPotin.setBackgroundResource(R.drawable.check_time_end);
        }else {
            holder.tvTopline.setVisibility(View.VISIBLE);
            holder.tvEndline.setVisibility(View.VISIBLE);
            holder.tvPotin.setBackgroundResource(R.drawable.check_time_begin);
        }
        if(mDatas.size()==1){
            holder.tvTopline.setVisibility(View.INVISIBLE);
            holder.tvEndline.setVisibility(View.INVISIBLE);
            holder.tvPotin.setBackgroundResource(R.drawable.check_time_begin);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_topline)
        TextView tvTopline;
        @BindView(R.id.tv_potin)
        TextView tvPotin;
        @BindView(R.id.tv_endline)
        TextView tvEndline;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
