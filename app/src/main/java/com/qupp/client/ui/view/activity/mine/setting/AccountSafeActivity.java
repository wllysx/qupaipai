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
import com.qupp.client.ui.view.activity.scoreshop.Authentication;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.utils.DoubleClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountSafeActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_states)
    TextView tvStates;
    @BindView(R.id.tv_phone)
    TextView tvPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_accountsafe);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(AccountSafeActivity.this), 0, 0);
        setStateColor(false);
        initView();
    }


    private void initView() {
        tvTitle.setText("账号安全");
        EntityForSimple entityForSimple = Paper.book("jyk").read("userdata", new EntityForSimple());
        tvPhone.setText(MyApplication.getPhonePass(entityForSimple.getPhone()));
    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, AccountSafeActivity.class));
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRealPerson();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.back, R.id.ll_authentication})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_changephone:
                ChangePhoneActivity.startActivityInstance(this);
                break;
            case R.id.ll_authentication:
                if (tvStates.getText().toString().equals("未认证")) {
                    Authentication.startActivityInstance(this);
                } else {
                    AuthenticationDetails.startActivityInstance(AccountSafeActivity.this);
                }
                break;
        }
    }


    /**
     * 获取实名认证状态
     */
    private void checkRealPerson() {
        ApiUtil.getApiService().checkRealPersonNum(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData().equals("1") || entity.getData().equals("2")) {
                            tvStates.setText("已认证");
                        } else {
                            tvStates.setText("未认证");
                        }
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }
}
