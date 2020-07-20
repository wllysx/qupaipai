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
import android.widget.TextView;

import com.qupp.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 下面弹出dialo分享
 */


public class BelowShardDialogH5 {

    private Context context;
    private Dialog dialog;
    BelowShardDialogH5 belowDialog;


    public BelowShardDialogH5(Context context) {
        super();
        this.context = context;
        belowDialog = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show(String shareConfig) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_below_shard_h5, null);
        ViewHolder holder = new ViewHolder(view);
        if(shareConfig.contains("1")){
            holder.tvWx.setVisibility(View.VISIBLE);
        }
        if(shareConfig.contains("2")){
            holder.tvFriend.setVisibility(View.VISIBLE);
        }
        if(shareConfig.contains("3")){
            holder.tvWb.setVisibility(View.VISIBLE);
        }

        holder.ivClose.setOnClickListener(v -> dialog.dismiss());
        holder.tvWb.setOnClickListener(v -> {
            sure.onSure(3);
            dialog.dismiss();
        });
        holder.tvWx.setOnClickListener(v -> {
            sure.onSure(1);
            dialog.dismiss();
        });
        holder.tvFriend.setOnClickListener(v -> {
            sure.onSure(2);
            dialog.dismiss();
        });

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

    public static interface Sure {
        void onSure(int item);
    }

    public Sure sure;

    public BelowShardDialogH5 setSureListener(Sure sure) {
        this.sure = sure;
        return belowDialog;
    }

    static class ViewHolder {
        @BindView(R.id.iv_close)
        ImageView ivClose;
        @BindView(R.id.tv_wx)
        TextView tvWx;
        @BindView(R.id.tv_friend)
        TextView tvFriend;
        @BindView(R.id.tv_wb)
        TextView tvWb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

