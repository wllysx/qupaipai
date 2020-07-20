package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qupp.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 签到规则
 */

public class SignInGzDialog {

    private Context context;
    private Dialog dialog;
    SignInGzDialog middleDialog;

    public SignInGzDialog(Context context) {
        super();
        this.context = context;
        middleDialog = this;
        // TODO Auto-generated constructor stub
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_signin_gz, null);
        ViewHolder viewHolder = new ViewHolder(view);

        //签到列表计算end
        viewHolder.tvSubmit.setOnClickListener(v -> {
                    dialog.dismiss();
                }
        );

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
    public SignInGzDialog setSureListener(Sure sure) {
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


    static
    class ViewHolder {
        @BindView(R.id.tv_submit)
        TextView tvSubmit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
