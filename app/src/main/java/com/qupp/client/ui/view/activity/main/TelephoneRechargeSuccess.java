package com.qupp.client.ui.view.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.utils.DoubleClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class TelephoneRechargeSuccess extends BaseActivity {

    @BindView(R.id.linear)
    View linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephonerecharge_success);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(TelephoneRechargeSuccess.this), 0, 0);
        setStateColor(false);
        initView();
    }


    private void initView() {
        tvTitle.setText("支付成功");
    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, TelephoneRechargeSuccess.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
