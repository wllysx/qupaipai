package com.qupp.client.utils.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qupp.client.R;
import com.qupp.client.utils.view.VerifyCodeViewSmall;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 密码dialog
 */


public class PayPasswordDialog {

    private Context context;
    private Dialog dialog;
    private PayPasswordDialog passwordDialog;

    public PayPasswordDialog(Context context) {
        super();
        this.context = context;
        passwordDialog = this;
        // TODO Auto-generated constructor stub
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_paypasswords, null);
        ViewHolder viewHolder = new ViewHolder(view);
        //viewHolder.etPassword.setFocusableInTouchMode(true);
        //viewHolder.etPassword.requestFocus();

        dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(true);
        dialog.show();

        new Handler().postDelayed(() -> {
            viewHolder.etCode.getView().setFocusable(true);
            viewHolder.etCode.getView().setFocusableInTouchMode(true);
            viewHolder.etCode.getView().requestFocus();
            InputMethodManager inputManager = (InputMethodManager) viewHolder.etCode.getView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(viewHolder.etCode.getView(), 0);
        }, 500);

        //dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        viewHolder.ivClose.setOnClickListener(v -> dialog.dismiss());

        viewHolder.etCode.setInputCompleteListener(new VerifyCodeViewSmall.InputCompleteListener() {
            @Override
            public void inputComplete() {
                sure.onSure(viewHolder.etCode.getEditContent());
            }

            @Override
            public void invalidContent() {

            }
        });


        viewHolder.tvSure.setOnClickListener(v -> {
            sure.onSure(viewHolder.etCode.getEditContent());
           // dialog.dismiss();
        });
        viewHolder.tvCancel.setOnClickListener(v -> dialog.dismiss());

    }

    public void dismiss(){
        if(dialog!=null&&dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static interface Sure {
        void onSure(String code);
    }

    public Sure sure;

    public PayPasswordDialog setSureListener(Sure sure) {
        this.sure = sure;
        return passwordDialog;
    }

    static class ViewHolder {
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.et_code)
        VerifyCodeViewSmall etCode;
        @BindView(R.id.tv_sure)
        TextView tvSure;
        @BindView(R.id.iv_close)
        ImageView ivClose;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
