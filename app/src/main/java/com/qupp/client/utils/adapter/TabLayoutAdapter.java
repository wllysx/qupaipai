package com.qupp.client.utils.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 明星榜单
 */

public class TabLayoutAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {

    List<EntityForSimple> data;

    public TabLayoutAdapter(List<EntityForSimple> data) {
        super(R.layout.item_tablayout, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {
        int position = helper.getAdapterPosition();

        if(data.size()-position>2) {
            //时间点的数据
            helper.setGone(R.id.ll_content,false);
            helper.setText(R.id.tv_time,item.getHour()+":00");
            helper.setText(R.id.tv_states,item.getCurrentStatusName());
            if (item.isChecked()) {
                helper.setTextColor(R.id.tv_time, mContext.getResources().getColor(R.color.iscur));
                helper.setTextColor(R.id.tv_states, mContext.getResources().getColor(R.color.white));
                helper.setBackgroundRes(R.id.tv_states, R.drawable.bg_iscur_10);
            } else {
                helper.setTextColor(R.id.tv_time, mContext.getResources().getColor(R.color.textcolor31));
                helper.setTextColor(R.id.tv_states, mContext.getResources().getColor(R.color.textcolor31));
                helper.setBackgroundColor(R.id.tv_states, mContext.getResources().getColor(R.color.transparency));
            }
        }else{
            helper.setGone(R.id.ll_content,true);
            if(data.size()-1 == position){
                //最后一位
                if(item.isChecked()) {
                    helper.setImageResource(R.id.iv_content, R.mipmap.hryg_selected);
                }else{
                    helper.setImageResource(R.id.iv_content, R.mipmap.hryg);
                }
            }else{
                //倒数第二位
                if(item.isChecked()) {
                    helper.setImageResource(R.id.iv_content, R.mipmap.msyg_selected);
                }else {
                    helper.setImageResource(R.id.iv_content, R.mipmap.msyg);
                }
            }
        }
    }


}
