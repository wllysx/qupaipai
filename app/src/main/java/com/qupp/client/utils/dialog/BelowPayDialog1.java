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
import android.widget.LinearLayout;

import com.qupp.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 下面弹出dialog
 * author: MrWang on 2019/8/21
 * email:773630555@qq.com
 * date: on 2019/8/21 13:20
 */


public class BelowPayDialog1 {

    private Context context;
    private Dialog dialog;
    BelowPayDialog1 belowDialog;

    public BelowPayDialog1(Context context) {
        super();
        this.context = context;
        belowDialog = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show(boolean iswx, boolean isali) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay1, null);

        ViewHolder holder = new ViewHolder(view);

        if (iswx && isali) {
            holder.llPay1.setVisibility(View.VISIBLE);
            holder.llPay2.setVisibility(View.VISIBLE);
        } else if (iswx) {
            holder.llPay1.setVisibility(View.VISIBLE);
            holder.llPay2.setVisibility(View.GONE);
        } else if (isali) {
            holder.llPay1.setVisibility(View.GONE);
            holder.llPay2.setVisibility(View.VISIBLE);
        }

        holder.ivClose.setOnClickListener(v -> dialog.dismiss());
        holder.llPay1.setOnClickListener(v -> {
            sure.onSure(2);
        });
        holder.llPay2.setOnClickListener(v -> {
            sure.onSure(1);
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

    public BelowPayDialog1 setSureListener(Sure sure) {
        this.sure = sure;
        return belowDialog;
    }

    static class ViewHolder {
        @BindView(R.id.iv_close)
        ImageView ivClose;
        @BindView(R.id.ll_pay1)
        LinearLayout llPay1;
        @BindView(R.id.ll_pay2)
        LinearLayout llPay2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

