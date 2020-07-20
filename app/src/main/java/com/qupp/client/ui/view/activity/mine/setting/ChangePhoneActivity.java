package com.qupp.client.ui.view.activity.mine.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.TimeCount1;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChangePhoneActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.linear)
    LinearLayout linear;


    @BindView(R.id.bt_login)
    TextView btLogin;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_code)
    TextView tvCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_changephone);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(ChangePhoneActivity.this), 0, 0);
        setStateColor(false);
        initView();
    }


    private void initView() {
        etPhone.addTextChangedListener(textWatcher);
        etCode.addTextChangedListener(textWatcher);
        tvTitle.setText("更换手机号");
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ivClose.setVisibility(View.GONE);
            } else {
                ivClose.setVisibility(View.VISIBLE);
            }


           /* if (!TextUtils.isEmpty(etPhone.getText()) && !TextUtils.isEmpty(etCode.getText())) {
                btLogin.setClickable(true);
                btLogin.setEnabled(true);
                btLogin.setBackgroundResource(R.drawable.bg_login_enable);
            } else {
                btLogin.setClickable(false);
                btLogin.setEnabled(false);
                btLogin.setBackgroundResource(R.drawable.bg_login_unable);
            }*/
        }
    };

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, ChangePhoneActivity.class));
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


    @OnClick({R.id.bt_login, R.id.iv_close, R.id.back, R.id.tv_code})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.bt_login:
                finish();
                break;
            case R.id.iv_close:
                etPhone.setText("");
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_code:
                TimeCount1 time = new TimeCount1(60000, 1000, tvCode, "获取验证码");
                time.start();
                break;
        }
    }




}
