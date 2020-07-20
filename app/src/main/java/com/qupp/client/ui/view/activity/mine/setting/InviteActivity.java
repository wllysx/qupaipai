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
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InviteActivity extends BaseActivity {

    @BindView(R.id.et_invite)
    EditText etInvite;
    @BindView(R.id.linear)
    LinearLayout linear;


    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_invite);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(InviteActivity.this), 0, 0);
        setStateColor(false);
        initView();
    }


    private void initView() {
        etInvite.addTextChangedListener(textWatcher);
        tvTitle.setText("我的资料");
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
            String content = etInvite.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ivClose.setVisibility(View.GONE);
            } else {
                ivClose.setVisibility(View.VISIBLE);
            }

        }
    };

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, InviteActivity.class));
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


    @OnClick({R.id.tv_sure, R.id.iv_close, R.id.back})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_sure:
                getInviteCode();
                break;
            case R.id.iv_close:
                etInvite.setText("");
                break;
            case R.id.back:
                finish();
                break;
        }
    }



    /**
     * 获取用户信息积分等信息
     */
    private void getInviteCode() {
        ApiUtil.getApiService().inviteCode(MyApplication.getToken(),etInvite.getText().toString()).enqueue(new Callback<MessageForSimple>() {
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
