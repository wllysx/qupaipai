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
import com.qupp.client.ui.view.activity.MainActivity;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.TimeCount;
import com.qupp.client.utils.secretUtils.RsaCipherUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的-安全-修改密码
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */
@Deprecated
public class ChangePassActivity extends BaseActivity {

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
    @BindView(R.id.et_newpass)
    EditText etNewpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_changepass);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(ChangePassActivity.this), 0, 0);
        setStateColor(false);
        initView();
    }


    private void initView() {
        etPhone.addTextChangedListener(textWatcher);
        etCode.addTextChangedListener(textWatcher);
        etNewpass.addTextChangedListener(textWatcher);
        tvTitle.setText("修改登录密码");
        etPhone.setText( MyApplication.getPhone());
        etPhone.setSelection(MyApplication.getPhone().length());
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


            if (!TextUtils.isEmpty(etPhone.getText()) && !TextUtils.isEmpty(etCode.getText()) && !TextUtils.isEmpty(etNewpass.getText())) {
                btLogin.setClickable(true);
                btLogin.setEnabled(true);
                btLogin.setBackgroundResource(R.drawable.bg_login_enable);
            } else {
                btLogin.setClickable(false);
                btLogin.setEnabled(false);
                btLogin.setBackgroundResource(R.drawable.bg_login_unable);
            }
        }
    };

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, ChangePassActivity.class));
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
                changePassword();
                break;
            case R.id.iv_close:
                etPhone.setText("");
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_code:
                if (etPhone.getText().toString().isEmpty()) {
                    showToast("请输入手机号");
                    return;
                }
                getCode();
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getCode() {
        if(etPhone.getText().toString().length()<11){
            showToast("请输入正确手机号");
            return;
        }
        ApiUtil.getApiService().codesms(etPhone.getText().toString(),"changeLoginPassWord").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        TimeCount time = new TimeCount(60000, 1000, tvCode, "获取验证码");
                        time.start();
                    } else {
                        showToast(entity.getMsg());
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });

    }


    /**
     * 修改密码
     */
    private void changePassword() {
        String password = "";
        try {
            password =   RsaCipherUtil.encrypt(etNewpass.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiUtil.getApiService().changeLoginCode(MyApplication.getToken(),etPhone.getText().toString(), etCode.getText().toString(), password).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.setToken("");
                        startActivity(new Intent(ChangePassActivity.this, MainActivity.class));
                        LoginActivity.startActivityInstance(ChangePassActivity.this);
                        finish();
                    } else {
                        showToast(entity.getMsg());
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });


    }


}
