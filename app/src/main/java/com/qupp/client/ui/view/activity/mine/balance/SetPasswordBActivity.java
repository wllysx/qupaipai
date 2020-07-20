package com.qupp.client.ui.view.activity.mine.balance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.secretUtils.RsaCipherUtil;
import com.qupp.client.utils.view.VerifyCodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 设置支付密码
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class SetPasswordBActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_code)
    VerifyCodeView etCode;
    boolean isFirstPass = true;
    String firstPass = "", secondPass = "";
    @BindView(R.id.tv_content)
    TextView tvContent;
    String phone="",code = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_setpaaasordb);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(SetPasswordBActivity.this), 0, 0);
        setStateColor(false);
        code = getIntent().getStringExtra("code");
        phone = getIntent().getStringExtra("phone");
        initView();
    }


    private void initView() {
        tvTitle.setText("设置支付密码");

        etCode.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (isFirstPass) {
                    //第一次输入
                    isFirstPass = false;
                    firstPass = etCode.getEditContent();
                    etCode.clear();
                    tvContent.setText("再次输入支付密码");
                }else{
                    secondPass = etCode.getEditContent();
                    if(firstPass.equals(secondPass)&&!firstPass.equals("")){
                        //成功
                        //showToast("两次密码一致");
                        //去掉接口
                        try {
                            submit(RsaCipherUtil.encrypt(firstPass));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        //失败
                        showToast("两次密码不一致请重新输入");
                        etCode.clear();
                        secondPass = "";
                    }
                }

            }

            @Override
            public void invalidContent() {

            }
        });

        new Handler().postDelayed(() -> {
            etCode.getView().setFocusable(true);
            etCode.getView().setFocusableInTouchMode(true);
            etCode.getView().requestFocus();
            InputMethodManager inputManager = (InputMethodManager) etCode.getView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(etCode.getView(), 0);
        }, 500);
    }

    public static void startActivityInstance(Activity activity,String phone,String code) {
        activity.startActivity(new Intent(activity, SetPasswordBActivity.class)
                .putExtra("phone",phone)
                .putExtra("code",code)
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


    @OnClick(R.id.back)
    public void onViewClicked() {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        finish();
    }


    /**
     * 设置密码
     */
    private void submit(String pas) {

        ApiUtil.getApiService().setPassword(MyApplication.getToken(),pas,code,phone).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
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
