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
import android.widget.TextView;

import com.qupp.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择支付方式
 * author: MrWang on 2019/8/21
 * email:773630555@qq.com
 * date: on 2019/8/21 13:20
 */


public class BelowPayTypeDialog {

    private Context context;
    private Dialog dialog;
    BelowPayTypeDialog belowDialog;

    public BelowPayTypeDialog(Context context) {
        super();
        this.context = context;
        belowDialog = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show(String type,String wxName,String aliName) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_paytype, null);

        ViewHolder holder = new ViewHolder(view);
        holder.ivClose.setOnClickListener(v -> dialog.dismiss());
        holder.tvWxname.setText(wxName);
        holder.tvAliname.setText(aliName);
        holder.llPay1.setOnClickListener(v -> {
            sure.onSure(0);
            dialog.dismiss();
        });
        holder.llPay2.setOnClickListener(v -> {
            sure.onSure(1);
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

    public BelowPayTypeDialog setSureListener(Sure sure) {
        this.sure = sure;
        return belowDialog;
    }


    static class ViewHolder {
        @BindView(R.id.iv_close)
        ImageView ivClose;
        @BindView(R.id.tv_sure)
        TextView tvSure;
        @BindView(R.id.tv_wxname)
        TextView tvWxname;
        @BindView(R.id.tv_aliname)
        TextView tvAliname;
        @BindView(R.id.ll_pay1)
        LinearLayout llPay1;
        @BindView(R.id.ll_pay2)
        LinearLayout llPay2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

