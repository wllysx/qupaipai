package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
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
 * 用户协议
 */

public class MiddleDialogForAgreement {

    private Context context;
    private Dialog dialog;
    MiddleDialogForAgreement middleDialog;

    public MiddleDialogForAgreement(Context context) {
        super();
        this.context = context;
        middleDialog  = this;
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @param titleName 标题
     * @param content 内容
     * @param cancelText 取消文字
     * @param sureText 确定文字
     */
    public void show(String titleName, String content,String cancelText,String sureText,boolean isOnlySure) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_middle_agreement, null);
        ViewHolder viewHolder = new ViewHolder(view);
        if(isOnlySure){
            viewHolder.vLine.setVisibility(View.GONE);
            viewHolder.tvCancel.setVisibility(View.GONE);
        }

        viewHolder.tvDialogTitle.setText(titleName);
        Spanned strA = Html.fromHtml("我们尊重并保护您的个人信息，并在<font color=#2872E2>" + "《趣拍拍用户隐私保护政策》" + "</font>详情说明了相关事项；");
        viewHolder.tvDialogContent.setText(strA);
        viewHolder.tvCancel.setText(cancelText);
        viewHolder.tvSure.setText(sureText);
        viewHolder.tvCancel.setOnClickListener(v -> {
            mCancel.onCancel();
        });
        viewHolder.tvSure.setOnClickListener(v -> {
            sure.onSure();
            dismiss();
        });
        viewHolder.tvDialogContent.setOnClickListener(v -> {
            msee.onSee();
        });

        dialog = new Dialog(context, R.style.ActionSheetDialogTopStyle);
        dialog.setContentView(view);

        dialog.setCanceledOnTouchOutside(false);

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
    public MiddleDialogForAgreement setSureListener(Sure sure) {
        this.sure = sure;
        return middleDialog;
    }

    public static interface MCancel {
        void onCancel();
    }


    public Msee msee;

    public MiddleDialogForAgreement setSeeListener(Msee msee) {
        this.msee = msee;
        return middleDialog;
    }

    public static interface Msee {
        void onSee();
    }


    public MCancel mCancel;

    /**
     * 点击取消回调的监听
     *
     * @param
     */
    public MiddleDialogForAgreement setCancelListener(MCancel mCancel) {
        this.mCancel = mCancel;
        return middleDialog;
    }

    static class ViewHolder {
        @BindView(R.id.tv_dialog_title)
        TextView tvDialogTitle;
        @BindView(R.id.tv_dialog_content)
        TextView tvDialogContent;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.tv_sure)
        TextView tvSure;
        @BindView(R.id.v_line)
        View vLine;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
