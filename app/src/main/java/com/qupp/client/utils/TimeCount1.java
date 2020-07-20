package com.qupp.client.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.qupp.client.R;


public class TimeCount1 extends CountDownTimer {
    private TextView submitView;
    private String finishTip = "";
    public TimeCount1(long millisInFuture, long countDownInterval, TextView submitView, String finishTip) {
        super(millisInFuture, countDownInterval);
        this.submitView = submitView;
        this.finishTip = finishTip;
    }

    @Override
    public void onFinish() {// 计时完毕
        submitView.setText(finishTip);
        submitView.setTextColor(Color.parseColor("#B30202"));
        submitView.setBackgroundResource(R.drawable.bg_code_unselect);
        submitView.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
        submitView.setEnabled(false);// 防止重复点击
        submitView.setText(millisUntilFinished / 1000 + "s");
        submitView.setBackgroundResource(R.drawable.bg_code_isselect);
        submitView.setTextColor(Color.parseColor("#9A9A9B"));
    }
}