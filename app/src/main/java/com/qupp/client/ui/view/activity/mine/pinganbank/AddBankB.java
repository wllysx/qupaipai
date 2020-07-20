package com.qupp.client.ui.view.activity.mine.pinganbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.TimeCount2;
import com.qupp.client.utils.event.CloseBandPage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 添加银行卡2
 */

public class AddBankB extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_phone)
    TextView etPhone;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bankb);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(AddBankB.this), 0, 0);
        setStateColor(false);
        initView();
        id = getIntent().getStringExtra("id");
        etPhone.setText(getIntent ().getStringExtra("phone"));
    }


    private void initView() {
        tvTitle.setText("添加银行卡");
    }


    /**
     * 启动页面
     *
     * @param activity
     */
    public static void startActivityInstance(Activity activity, String phone,String id) {
        activity.startActivity(new Intent(activity, AddBankB.class).putExtra("phone", phone).putExtra("id",id)
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


    @OnClick({R.id.bt_login, R.id.back, R.id.tv_wenti, R.id.tv_code})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.bt_login:
                verification();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_code:
                getcode();
                break;
            case R.id.tv_wenti:
                showToast("问题");
        }
    }

    /**
     * 获取验证码
     */
    private void getcode() {
        ApiUtil.getApiService().codebind(MyApplication.getToken(),id).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        TimeCount2 time = new TimeCount2(60000, 1000, tvCode, "获取");
                        time.start();
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });

    }


    /**
     * 提交
     */
    private void verification() {
        ApiUtil.getApiService().verification(MyApplication.getToken(),id,etCode.getText().toString()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        //成功到列表页面
                        EventBus.getDefault().post(new CloseBandPage());
                        finish();
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });

    }


}
