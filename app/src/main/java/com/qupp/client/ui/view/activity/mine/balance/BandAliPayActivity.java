package com.qupp.client.ui.view.activity.mine.balance;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的-绑定支付宝
 */

public class BandAliPayActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;


    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_close1)
    ImageView ivClose1;
    String aLiccount = "",aLiname = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_ailipay);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(BandAliPayActivity.this), 0, 0);
        aLiccount = getIntent().getStringExtra("aLiccount");
        aLiname = getIntent().getStringExtra("aLiname");
        setStateColor(false);
        initView();
    }


    private void initView() {
        etName.addTextChangedListener(textWatcher);
        etAccount.addTextChangedListener(textWatcher);
        tvTitle.setText("支付宝绑定");
        etAccount.setText(aLiccount);
        etName.setText(aLiname);

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
            String content1 = etAccount.getText().toString().trim();
            if (TextUtils.isEmpty(content1)) {
                ivClose.setVisibility(View.GONE);
            } else {
                ivClose.setVisibility(View.VISIBLE);
            }

            String content2 = etName.getText().toString().trim();
            if (TextUtils.isEmpty(content2)) {
                ivClose.setVisibility(View.GONE);
            } else {
                ivClose.setVisibility(View.VISIBLE);
            }

        }
    };

    public static void startActivityInstance(Activity activity,String aLiccount,String aLiname) {
        activity.startActivity(new Intent(activity, BandAliPayActivity.class)
            .putExtra("aLiccount",aLiccount)
                .putExtra("aLiname",aLiname)
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


    @OnClick({R.id.iv_close1,R.id.iv_close, R.id.back, R.id.bg_submit})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.bg_submit:
                if(etAccount.getText().toString().equals("")){
                    showToast("请输入账号");
                    return;
                }
                if(etName.getText().toString().equals("")){
                    showToast("请输入姓名");
                    return;
                }
                withdrawAccountadd();
                break;
            case R.id.iv_close:
                etAccount.setText("");
                break;
            case R.id.iv_close1:
                etName.setText("");
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    private void withdrawAccountadd() {
        ApiUtil.getApiService().withdrawAccountadd(MyApplication.getToken(),etAccount.getText().toString(),etName.getText().toString(),"1").enqueue(new Callback<MessageForSimple>() {
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
