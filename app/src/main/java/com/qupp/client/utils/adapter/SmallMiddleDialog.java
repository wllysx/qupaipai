package com.qupp.client.utils.adapter;

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
 * author: MrWang on 2019/8/21
 * email:773630555@qq.com
 * date: on 2019/8/21 13:20
 */

public class SmallMiddleDialog {

    private Context context;
    private Dialog dialog;
    SmallMiddleDialog middleDialog;

    public SmallMiddleDialog(Context context) {
        super();
        this.context = context;
        middleDialog  = this;
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @param content 内容
     * @param sureText 确定文字
     */
    public void show(String content,String sureText) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_middle_small, null);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.tvDialogContent.setText(content);
        viewHolder.tvSure.setText(sureText);
        viewHolder.tvSure.setOnClickListener(v -> {
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
    public SmallMiddleDialog setSureListener(Sure sure) {
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
        @BindView(R.id.tv_dialog_content)
        TextView tvDialogContent;
        @BindView(R.id.tv_sure)
        TextView tvSure;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
