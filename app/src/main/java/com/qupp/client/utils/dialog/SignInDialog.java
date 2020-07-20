package com.qupp.client.utils.dialog;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * 签到
 */

public class SignInDialog {

    private Context context;
    private Dialog dialog;
    SignInDialog middleDialog;

    public SignInDialog(Context context) {
        super();
        this.context = context;
        middleDialog = this;
        // TODO Auto-generated constructor stub
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void show(EntityForSimple data) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_signin, null);
        ViewHolder viewHolder = new ViewHolder(view);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);//第一个参数开始的透明度，第二个参数结束的透明度
        alphaAnimation.setDuration(1000);//多长时间完成这个动作
        viewHolder.ivGuang.startAnimation(alphaAnimation);

       // setAnimation(viewHolder.progressBar, 50);
        //setTvAnimation(viewHolder.tvCount,9, 3);

        try {
            if (data.getSignCountDay() < 5) {
                setAnimation(viewHolder.progressBar, (data.getSignCountDay() - 1) * 25);
            }
            setTvAnimation(viewHolder.tvCount,9, data.getSignIntegral());
            viewHolder.tvDays.setText("已连续签到" + data.getSignCountDay() + "天");
            //签到列表计算start
            ArrayList<EntityForSimple> signList = new ArrayList<>();

            if (data.getSignCountDay() < 5) {
                for (int i = 0; i < data.getSignList().size(); i++) {
                    EntityForSimple signEntity = new EntityForSimple();
                    signEntity.setSignIntegral(data.getSignList().get(i).getSignIntegral());
                    signEntity.setChecked(true);
                    if (i == data.getSignList().size() - 1) {
                        signEntity.setSignName("今日签到");
                    } else {
                        signEntity.setSignName("已签到");
                    }
                    signList.add(signEntity);

                }
                for (int i = 0; i < 5 - data.getSignCountDay(); i++) {
                    EntityForSimple signEntity = new EntityForSimple();
                    signEntity.setSignIntegral(data.getSignIntegral() + 1 + i);
                    signEntity.setChecked(false);
                    signEntity.setSignName((data.getSignCountDay() + 1 + i) + "天");
                    signList.add(signEntity);
                }
            } else {
                EntityForSimple signEntity1 = new EntityForSimple();
                signEntity1.setSignIntegral(data.getSignList().get(data.getSignList().size() - 1).getSignIntegral());
                signEntity1.setChecked(true);
                signEntity1.setSignName("今日签到");
                signList.add(signEntity1);

                for (int i = 0; i < 4; i++) {
                    EntityForSimple signEntity = new EntityForSimple();
                    signEntity.setSignIntegral(5);
                    signEntity.setChecked(false);
                    signEntity.setSignName("+" + (data.getSignCountDay() + 1+i) + "");
                    signList.add(signEntity);
                }

            }
            ArrayList<TextView> integral = new ArrayList<>();
            integral.add(viewHolder.tvSignIntegral1);
            integral.add(viewHolder.tvSignIntegral2);
            integral.add(viewHolder.tvSignIntegral3);
            integral.add(viewHolder.tvSignIntegral4);
            integral.add(viewHolder.tvSignIntegral5);
            ArrayList<ImageView> signs = new ArrayList<>();
            signs.add(viewHolder.ivSign1);
            signs.add(viewHolder.ivSign2);
            signs.add(viewHolder.ivSign3);
            signs.add(viewHolder.ivSign4);
            signs.add(viewHolder.ivSign5);
            ArrayList<TextView> name = new ArrayList<>();
            name.add(viewHolder.tvSignName1);
            name.add(viewHolder.tvSignName2);
            name.add(viewHolder.tvSignName3);
            name.add(viewHolder.tvSignName4);
            name.add(viewHolder.tvSignName5);

            for (int i = 0; i < signList.size(); i++) {
                integral.get(i).setText("+" + signList.get(i).getSignIntegral());
                if (signList.get(i).isChecked()) {
                    signs.get(i).setImageResource(R.mipmap.jb_yqd);
                    name.get(i).setTextColor(Color.parseColor("#ffaf5c"));
                    integral.get(i).setTextColor(Color.parseColor("#ffaf5c"));
                } else {
                    signs.get(i).setImageResource(R.mipmap.jb_wqd);
                    name.get(i).setTextColor(Color.parseColor("#bbbbbb"));
                    integral.get(i).setTextColor(Color.parseColor("#bbbbbb"));
                }
                name.get(i).setText(signList.get(i).getSignName());
            }
        } catch (Exception e) {

        }

        //签到列表计算end
        viewHolder.ivCancel.setOnClickListener(v -> {
                    sure.onSure();
                    dialog.dismiss();
                }
        );

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
    public SignInDialog setSureListener(Sure sure) {
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


    private void setAnimation(final ProgressBar view, final int mProgressBar) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(10);
        animator.addUpdateListener(valueAnimator -> view.setProgress((int) valueAnimator.getAnimatedValue()));
        animator.start();
    }

    private void setTvAnimation(final TextView view, final int mProgressBar,int count) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(400);
        animator.addUpdateListener(valueAnimator -> view.setText(String.valueOf(valueAnimator.getAnimatedValue())));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setText(count+"");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


    static class ViewHolder {
        @BindView(R.id.iv_gif)
        GifImageView ivGif;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.tv_signIntegral1)
        TextView tvSignIntegral1;
        @BindView(R.id.iv_sign1)
        ImageView ivSign1;
        @BindView(R.id.tv_signName1)
        TextView tvSignName1;
        @BindView(R.id.tv_signIntegral2)
        TextView tvSignIntegral2;
        @BindView(R.id.iv_sign2)
        ImageView ivSign2;
        @BindView(R.id.tv_signName2)
        TextView tvSignName2;
        @BindView(R.id.tv_signIntegral3)
        TextView tvSignIntegral3;
        @BindView(R.id.iv_sign3)
        ImageView ivSign3;
        @BindView(R.id.tv_signName3)
        TextView tvSignName3;
        @BindView(R.id.tv_signIntegral4)
        TextView tvSignIntegral4;
        @BindView(R.id.iv_sign4)
        ImageView ivSign4;
        @BindView(R.id.tv_signName4)
        TextView tvSignName4;
        @BindView(R.id.tv_signIntegral5)
        TextView tvSignIntegral5;
        @BindView(R.id.iv_sign5)
        ImageView ivSign5;
        @BindView(R.id.tv_signName5)
        TextView tvSignName5;
        @BindView(R.id.tv_days)
        TextView tvDays;
        @BindView(R.id.ll_top)
        LinearLayout llTop;
        @BindView(R.id.iv_cancel)
        ImageView ivCancel;
        @BindView(R.id.iv_guang)
        ImageView ivGuang;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
