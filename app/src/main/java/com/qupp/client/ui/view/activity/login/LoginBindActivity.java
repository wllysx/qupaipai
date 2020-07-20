package com.qupp.client.ui.view.activity.login;

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
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.TimeCount1;
import com.qupp.client.utils.event.RemoveLogin;
import com.qupp.client.utils.event.SendTokenToH5;
import com.qupp.client.utils.secretUtils.JwtUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录绑定
 */

public class LoginBindActivity extends BaseActivity {

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
    String openid = "",  nickName = "",  avatar="",  unionid="",gender="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bind);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(LoginBindActivity.this), 0, 0);
        openid = getIntent().getStringExtra("openid");
        nickName = getIntent().getStringExtra("nickName");
        avatar = getIntent().getStringExtra("avatar");
        unionid = getIntent().getStringExtra("unionid");
        gender = getIntent().getStringExtra("gender");
        if(gender.equals("男")){
            gender = "1";
        }else if(gender.equals("女")){
            gender = "2";
        }else{
            gender = "0";
        }
        setStateColor(false);
        initView();
    }


    private void initView() {
        etPhone.addTextChangedListener(textWatcher);
        etCode.addTextChangedListener(textWatcher);
        tvTitle.setText("绑定手机号");
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


            if (!TextUtils.isEmpty(etPhone.getText()) && !TextUtils.isEmpty(etCode.getText())) {
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

    public static void startActivityInstance(Activity activity,String openid,String nickName,String avatar,String unionid,String gender) {
        activity.startActivity(new Intent(activity, LoginBindActivity.class)
                .putExtra("openid",openid)
                .putExtra("nickName",nickName)
                .putExtra("avatar",avatar)
                .putExtra("unionid",unionid)
                .putExtra("gender",gender)
        );
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
                bandmoblie();
                break;
            case R.id.iv_close:
                etPhone.setText("");
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_code:
                getcode();
                break;
        }
    }


    /**
     * 登录
     */
    private void getcode() {
        if(etPhone.getText().toString().length()<11){
            showToast("请输入正确手机号");
            return;
        }
        ApiUtil.getApiService().codesms(etPhone.getText().toString(),null).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        TimeCount1 time = new TimeCount1(60000, 1000, tvCode, "获取验证码");
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


    private void bandmoblie(){
        ApiUtil.getApiService().mobile(etPhone.getText().toString(), etCode.getText().toString(), openid,unionid,nickName,avatar,gender,"4","8").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.setToken(response.headers().get("Authorization"));
                        JwtUtils.setUserId(response.headers().get("Authorization"));
                        EventBus.getDefault().post(new RemoveLogin());
                        JPushInterface.setAlias(LoginBindActivity.this, ((int) System.currentTimeMillis()), MyApplication.getUserId());
                        EventBus.getDefault().post(new SendTokenToH5());
                        finish();
                    } else {
                        showToast(entity.getMsg());
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {

                try {
                    MessageForSimple entity =  call.execute().body();
                    showToast(entity.getMsg());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
