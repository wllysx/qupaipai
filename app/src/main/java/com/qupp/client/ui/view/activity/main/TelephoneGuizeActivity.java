package com.qupp.client.ui.view.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.MyImageGetter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 充值说明
 */

public class TelephoneGuizeActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_longimage)
    TextView tvLongimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_know);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(TelephoneGuizeActivity.this), 0, 0);
        setStateColor(false);
        initView();
        getData();

    }

    /**
     * 规则须知
     */
    private void getData() {
        ApiUtil.getApiService().ruleByKey("prepaid_refill").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvLongimage.setText(Html.fromHtml(entity.getData().getContent(), new MyImageGetter(TelephoneGuizeActivity.this, tvLongimage), null));
                    } else {

                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    private void initView() {
        tvTitle.setText("充值说明");
    }

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, TelephoneGuizeActivity.class));
    }

    @Override
    protected void onResume() {
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


    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }


}
