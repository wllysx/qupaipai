/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-09-18 23:47:01
 *
 * GitHub: https://github.com/GcsSloop
 * WeiBo: http://weibo.com/GcsSloop
 * WebSite: http://www.gcssloop.com
 */

package com.m7.imkfsdk.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.m7.imkfsdk.R;
import com.moor.imkf.model.entity.FlowBean;

import java.util.ArrayList;
import java.util.List;


public class FlowAdapter extends RecyclerView.Adapter<FlowAdapter.MyViewHolder> {
    private OnItemClickListenr onItemClickListenr;
    public List<FlowBean> data = new ArrayList<>();
    private Context context;

    public FlowAdapter(Context context, List<FlowBean> strings, OnItemClickListenr onItemClickListenr) {
        this.onItemClickListenr = onItemClickListenr;
        this.data = strings;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //    Log.i("GCS", "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //   Log.i("GCS","onBindViewHolder = "+position);
        final FlowBean flowBean = data.get(position);
        holder.tv_flowItem.setText(flowBean.getButton());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListenr.setOnButtonClickListenr(flowBean.getText());
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_flowItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_flowItem = (TextView) itemView.findViewById(R.id.tv_flowItem);//文字item
        }
    }

    public interface OnItemClickListenr {
        void setOnButtonClickListenr(String msg);
    }
}
