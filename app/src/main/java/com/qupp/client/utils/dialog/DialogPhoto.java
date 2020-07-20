package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.qupp.client.R;


/**
 * 选择照片dialog
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */
public class DialogPhoto {

    private Context context;
    private Dialog dialog;
    DialogPhoto dialogPhoto;

    public DialogPhoto(Context context) {
        super();
        this.context = context;
        dialogPhoto = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_photo,null);
        view.findViewById(R.id.tv_take).setOnClickListener(v -> {
            sure.onSure(1);
            dialog.dismiss();
        });
        view.findViewById(R.id.tv_album).setOnClickListener(v -> {
            sure.onSure(2);
            dialog.dismiss();
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> dialog.dismiss());
        // 从下面滑
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width= LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);

    }

    /**
     * item 1微信 2朋友圈 3QQ 4QQ空间
     */
    public static interface Sure {
        void onSure(int item);
    }
    public Sure sure;
    /**
     * 点击确定回调的监听
     *
     * @param sure
     */
    public DialogPhoto setSureListener(Sure sure) {
        this.sure = sure;
        return dialogPhoto;
    }
}

