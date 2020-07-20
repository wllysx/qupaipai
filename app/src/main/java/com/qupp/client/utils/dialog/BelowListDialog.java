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
 * 选择证件类型
 */


public class BelowListDialog {

    private Context context;
    private Dialog dialog;
    BelowListDialog belowDialog;


    public BelowListDialog(Context context) {
        super();
        this.context = context;
        belowDialog = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_below_list, null);

        ViewHolder holder = new ViewHolder(view);
        holder.tvType1.setOnClickListener(v -> {
            sure.onSure(1);
            dialog.dismiss();
        });
        holder.tvType2.setOnClickListener(v -> {
            sure.onSure(2);
            dialog.dismiss();
        });
        holder.tvType3.setOnClickListener(v -> {
            sure.onSure(3);
            dialog.dismiss();
        });
        holder.tvType4.setOnClickListener(v -> {
            sure.onSure(4);
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

    public BelowListDialog setSureListener(Sure sure) {
        this.sure = sure;
        return belowDialog;
    }

    static class ViewHolder {
        @BindView(R.id.tv_type1)
        TextView tvType1;
        @BindView(R.id.tv_type2)
        TextView tvType2;
        @BindView(R.id.tv_type3)
        TextView tvType3;
        @BindView(R.id.tv_type4)
        TextView tvType4;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

