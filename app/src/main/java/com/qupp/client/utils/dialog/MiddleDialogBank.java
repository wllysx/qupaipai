package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qupp.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 银行
 */

public class MiddleDialogBank {

    private Context context;
    private Dialog dialog;
    MiddleDialogBank middleDialog;

    public MiddleDialogBank(Context context) {
        super();
        this.context = context;
        middleDialog  = this;
        // TODO Auto-generated constructor stub
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_middle_bank, null);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.tvSure.setOnClickListener(v -> {
            sure.onSure();
            dismiss();
        });

        dialog = new Dialog(context, R.style.ActionSheetDialogTopStyle);
        dialog.setContentView(view);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();


    }


    public void dismiss() {
        dialog.dismiss();
    }

    public static interface Sure {
        void onSure();
    }

    public Sure sure;

    /**
     * 点击确定回调的监听
     *
     * @param sure
     */
    public MiddleDialogBank setSureListener(Sure sure) {
        this.sure = sure;
        return middleDialog;
    }

    public static interface MCancel {
        void onCancel();
    }

    public MCancel mCancel;

    public void setCancelListener(MCancel mCancel) {
        this.mCancel = mCancel;
    }

    static class ViewHolder {
        @BindView(R.id.tv_sure)
        TextView tvSure;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
