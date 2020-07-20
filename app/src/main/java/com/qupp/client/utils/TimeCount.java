package com.qupp.client.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.qupp.client.R;


public class TimeCount extends CountDownTimer {
    private TextView submitView;
    private String finishTip = "";
    public TimeCount(long millisInFuture, long countDownInterval, TextView submitView, String finishTip) {
        super(millisInFuture, countDownInterval);
        this.submitView = submitView;
        this.finishTip = finishTip;
    }

    @Override
    public void onFinish() {// 计时完毕
        submitView.setText(finishTip);
        submitView.setTextColor(Color.parseColor("#DD0000"));
        submitView.setBackgroundResource(R.drawable.bg_code_unselect);
        submitView.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
        submitView.setEnabled(false);// 防止重复点击
        submitView.setTextColor(Color.parseColor("#9A9A9B"));
        submitView.setBackgroundResource(R.drawable.bg_code_isselect);
        submitView.setText(millisUntilFinished / 1000 + "s");
    }
}