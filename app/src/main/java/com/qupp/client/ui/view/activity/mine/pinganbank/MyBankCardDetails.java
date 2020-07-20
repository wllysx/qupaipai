package com.qupp.client.ui.view.activity.mine.pinganbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.event.ExitEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 银行卡详情
 */

public class MyBankCardDetails extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_logoUrl)
    ImageView ivLogoUrl;
    @BindView(R.id.tv_bankName)
    TextView tvBankName;
    @BindView(R.id.tv_useType1)
    TextView tvUseType1;
    @BindView(R.id.tv_useType)
    TextView tvUseType;
    @BindView(R.id.tv_shortCardCode)
    TextView tvShortCardCode;
    @BindView(R.id.tv_time)
    TextView tvTime;
    EntityForSimple data;
    String useType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_details);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(MyBankCardDetails.this), 0, 0);
        data = ((EntityForSimple) getIntent().getSerializableExtra("data"));
        setStateColor(false);
        initView();

    }


    private void initView() {
        useType = data.getUseType();
        tvTitle.setText("银行卡详情");
        Glide.with(this).load(data.getLogoUrl()).apply(new RequestOptions().placeholder(R.color.bg_main).error(R.color.bg_main)).into(ivLogoUrl);
        tvBankName.setText(data.getBankName());
        tvShortCardCode.setText("**** **** **** "+data.getShortCardCode());
        tvUseType.setText(data.getUseType().equals("0")?"提现卡":"支付卡");
        tvUseType1.setText(data.getUseType().equals("0")?"本卡仅用于提现":"本卡仅用于支付");
        tvTime.setText(data.getBindDateTime());
    }


    public static void startActivityInstance(Activity activity, EntityForSimple data) {
        activity.startActivity(new Intent(activity, MyBankCardDetails.class).putExtra("data",data));
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

    @OnClick({R.id.back, R.id.tv_unbind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_unbind:
                //解绑银行卡
                UnBandCard.startActivityInstance(MyBankCardDetails.this,data.getAccountName(),data.getShortCardCode(),data.getId(),useType);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exit(ExitEvent event) {
        if (event != null) {
            finish();
        }
    }
}
