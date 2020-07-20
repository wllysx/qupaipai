package com.qupp.client.ui.view.activity.mine.pinganbank.pinganpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.utils.DoubleClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付结果
 */

public class PayB extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    String amount = "";
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_bankName)
    TextView tvBankName;
    String bankname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bankb);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(PayB.this), 0, 0);
        amount = getIntent().getStringExtra("amount");
        bankname = getIntent().getStringExtra("bankname");
        setStateColor(false);
        initView();
    }


    private void initView() {
        tvTitle.setText("银行卡支付");
        tvAmount.setText(amount);
        tvBankName.setText(bankname);
    }


    /**
     * 启动页面
     *
     * @param activity
     */
    public static void startActivityInstance(Activity activity, String amount,String bankname) {
        activity.startActivity(new Intent(activity, PayB.class).putExtra("amount", amount).putExtra("bankname",bankname)
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


    @OnClick({R.id.bt_login, R.id.back, R.id.tv_wenti})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.bt_login:
                finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_wenti:
                showToast("问题");
        }
    }


}
