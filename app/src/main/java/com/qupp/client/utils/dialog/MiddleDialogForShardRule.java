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
import com.qupp.client.utils.view.ResizableImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分享规则
 */

public class MiddleDialogForShardRule {

    private Context context;
    private Dialog dialog;
    MiddleDialogForShardRule middleDialog;

    public MiddleDialogForShardRule(Context context) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_middle_shardrule, null);
        ViewHolder viewHolder = new ViewHolder(view);
        if(isOnlySure){
            viewHolder.vLine.setVisibility(View.GONE);
            viewHolder.tvCancel.setVisibility(View.GONE);
        }

        viewHolder.tvDialogTitle.setText(titleName);
        //Glide.with(context).load(item.getImage()).apply(new RequestOptions().error(R.color.line).dontAnimate()).into(viewHolder.ivContent);

        viewHolder.tvCancel.setText(cancelText);
        viewHolder.tvSure.setText(sureText);
        viewHolder.tvCancel.setOnClickListener(v -> {
            mCancel.onCancel();
        });
        viewHolder.tvSure.setOnClickListener(v -> {
            sure.onSure();
            dismiss();
        });

        dialog = new Dialog(context, R.style.ActionSheetDialogTopStyle);
        dialog.setContentView(view);

//        dialog.setCanceledOnTouchOutside(false);

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
    public MiddleDialogForShardRule setSureListener(Sure sure) {
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
    public MiddleDialogForShardRule setCancelListener(MCancel mCancel) {
        this.mCancel = mCancel;
        return middleDialog;
    }

    static class ViewHolder {
        @BindView(R.id.tv_dialog_title)
        TextView tvDialogTitle;
        @BindView(R.id.iv_content)
        ResizableImageView ivContent;
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
