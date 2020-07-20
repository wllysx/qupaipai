package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.GiftbagAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 下面弹出dialo分享
 */


public class BelowGiftbagDialog {

    private Context context;
    private Dialog dialog;
    BelowGiftbagDialog belowDialog;


    public BelowGiftbagDialog(Context context) {
        super();
        this.context = context;
        belowDialog = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show(ArrayList<EntityForSimple> datas) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_giftbag, null);

        ViewHolder holder = new ViewHolder(view);
        holder.ivClose.setOnClickListener(v -> dialog.dismiss());
        holder.vClose.setOnClickListener(v -> dialog.dismiss());

        initAdapter(holder.mRecyclerView, datas);


        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);

    }

    private void initAdapter(RecyclerView mRecyclerView, ArrayList<EntityForSimple> datas) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 1));

        GiftbagAdapter mAdapter = new GiftbagAdapter(datas);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(context, 1); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);
    }

    static class ViewHolder {
        @BindView(R.id.v_close)
        View vClose;
        @BindView(R.id.iv_close)
        ImageView ivClose;
        @BindView(R.id.mRecyclerView)
        RecyclerView mRecyclerView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

