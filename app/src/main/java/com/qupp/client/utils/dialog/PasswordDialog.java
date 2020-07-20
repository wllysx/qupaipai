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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qupp.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 密码dialog
 */


public class PasswordDialog {

    private Context context;
    private Dialog dialog;
    private PasswordDialog passwordDialog;

    public PasswordDialog(Context context) {
        super();
        this.context = context;
        passwordDialog = this;
        // TODO Auto-generated constructor stub
    }

    public void show(String nickname) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_password, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.etPassword.setFocusableInTouchMode(true);
        viewHolder.etPassword.requestFocus();
        viewHolder.tvRoomno.setText(nickname);

        dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(true);
        dialog.show();

        //dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        new Handler().postDelayed(() -> {
            InputMethodManager inputManager = (InputMethodManager) viewHolder.etPassword.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput( viewHolder.etPassword, 0);
        }, 100);

        viewHolder.tvSure.setOnClickListener(v -> {
            sure.onSure(viewHolder.etPassword.getText().toString());
            dialog.dismiss();
        });
        viewHolder.ivClose.setOnClickListener(v -> dialog.dismiss());

    }


    public static interface Sure {
        void onSure(String code);
    }

    public Sure sure;

    public PasswordDialog setSureListener(Sure sure) {
        this.sure = sure;
        return passwordDialog;
    }

    static class ViewHolder {
        @BindView(R.id.iv_close)
        ImageView ivClose;
        @BindView(R.id.tv_roomno)
        TextView tvRoomno;
        @BindView(R.id.et_password)
        EditText etPassword;
        @BindView(R.id.tv_sure)
        TextView tvSure;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
