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
 * 中间弹出dialog
 */

public class UnbandCardDialog {

    private Context context;
    private Dialog dialog;
    UnbandCardDialog middleDialog;

    public UnbandCardDialog(Context context) {
        super();
        this.context = context;
        middleDialog  = this;
        // TODO Auto-generated constructor stub
    }

    public void show(String titleName, String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_unbandcard, null);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.tvDialogTitle.setText(titleName);
        viewHolder.tvDialogContent.setText(content);
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
    public UnbandCardDialog setSureListener(Sure sure) {
        this.sure = sure;
        return middleDialog;
    }

    public static interface MCancel {
        void onCancel();
    }



    public MCancel mCancel;

    /**
     * 点击取消回调的监听
     *
     * @param
     */
    public void setCancelListener(MCancel mCancel) {
        this.mCancel = mCancel;
    }

    static class ViewHolder {
        @BindView(R.id.tv_dialog_title)
        TextView tvDialogTitle;
        @BindView(R.id.tv_dialog_content)
        TextView tvDialogContent;
        @BindView(R.id.tv_sure)
        TextView tvSure;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
