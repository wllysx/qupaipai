package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qupp.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加银行卡
 */


public class BankAddDialog {

    private Context context;
    private Dialog dialog;
    BankAddDialog belowDialog;


    public BankAddDialog(Context context) {
        super();
        this.context = context;
        belowDialog = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_addbank, null);

        ViewHolder holder = new ViewHolder(view);
        holder.tvType2.setOnClickListener(v -> {
            sure.onSure(2);
            dialog.dismiss();
        });
        holder.tvType3.setOnClickListener(v -> {
            sure.onSure(3);
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

    public BankAddDialog setSureListener(Sure sure) {
        this.sure = sure;
        return belowDialog;
    }

    static class ViewHolder {
        @BindView(R.id.tv_type2)
        TextView tvType2;
        @BindView(R.id.tv_type3)
        TextView tvType3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

