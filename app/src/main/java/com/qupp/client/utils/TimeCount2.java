package com.qupp.client.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;


public class TimeCount2 extends CountDownTimer {
    private TextView submitView;
    private String finishTip = "";
    public TimeCount2(long millisInFuture, long countDownInterval, TextView submitView, String finishTip) {
        super(millisInFuture, countDownInterval);
        this.submitView = submitView;
        this.finishTip = finishTip;
    }

    @Override
    public void onFinish() {// 计时完毕
        submitView.setText(finishTip);
        submitView.setTextColor(Color.parseColor("#DD0000"));
        submitView.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
        submitView.setEnabled(false);// 防止重复点击
        submitView.setTextColor(Color.parseColor("#9A9A9B"));
        submitView.setText(millisUntilFinished / 1000 + "s");
    }
}