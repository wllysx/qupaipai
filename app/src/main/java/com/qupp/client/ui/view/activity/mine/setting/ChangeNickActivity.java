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
import com.qupp.client.utils.event.RefreshUserInfo;
import com.qupp.client.utils.secretUtils.JwtUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 修改昵称
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class ChangeNickActivity extends BaseActivity {

    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.linear)
    LinearLayout linear;


    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    String nick="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_changenick);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(ChangeNickActivity.this), 0, 0);
        setStateColor(false);
        nick = getIntent().getStringExtra("nick");
        initView();
    }


    private void initView() {
        etNick.addTextChangedListener(textWatcher);
        tvTitle.setText("修改昵称");
        etNick.setText(nick);
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
            String content = etNick.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ivClose.setVisibility(View.GONE);
            } else {
                ivClose.setVisibility(View.VISIBLE);
            }

        }
    };

    public static void startActivityInstance(Activity activity,String nick) {
        activity.startActivity(new Intent(activity, ChangeNickActivity.class).putExtra("nick",nick));
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
                initData();
                break;
            case R.id.iv_close:
                etNick.setText("");

                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void initData() {
        ApiUtil.getApiService().memberupdateAvatar(MyApplication.getToken(),null,etNick.getText().toString()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.setToken(response.headers().get("Authorization"));
                        JwtUtils.setUserId(response.headers().get("Authorization"));
                        EventBus.getDefault().post(new RefreshUserInfo());
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
