package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * 版本更新
 */

public class UpdataVersionDialog extends Dialog {
    private int res;

    public UpdataVersionDialog(Context context, int theme, int res) {
        super(context, theme);
        // TODO 自动生成的构造函数存根
        setContentView(res);
        this.res = res;
        setCanceledOnTouchOutside(false);
    }

}