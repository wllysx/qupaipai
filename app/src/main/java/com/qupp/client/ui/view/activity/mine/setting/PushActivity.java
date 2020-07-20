package com.qupp.client.ui.view.activity.mine.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.view.SwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 邀请码
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class PushActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.swithview)
    SwitchView swithview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_push);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(PushActivity.this), 0, 0);
        setStateColor(false);
        initView();

    }


    private void initView() {
        tvTitle.setText("消息推送");
    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, PushActivity.class));
    }

    @Override
    protected void onResume() {
        swithview.setOpened(MyApplication.isNotificationEnabled(this));
        swithview.setChangeListener((v, ischecked) -> {
        });
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


    @OnClick({R.id.back,R.id.tv_set,R.id.v_hint})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_set:
                MyApplication.toNotificationSetting(this);
                break;
            case R.id.v_hint:
                break;
        }
    }


}
