package com.qupp.client.ui.view.activity.scoreshop;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.utils.DoubleClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的寄拍详情
 */

@Deprecated
@SuppressLint("all")
public class MyCheckDetails extends BaseActivity {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_commoditytitle)
    TextView tvCommoditytitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_firstprice)
    TextView tvFirstprice;
    @BindView(R.id.tv_finalprice)
    TextView tvFinalprice;
    @BindView(R.id.tv_allprice)
    TextView tvAllprice;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.tv_states)
    TextView tvStates;
    @BindView(R.id.tv_countdown)
    TextView tvCountdown;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycheck_details);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        initView();
    }

    private void initView() {
        tvTitle.setText("订单详情");
        tvStates.setText("订单状态");
        tvCountdown.setText("距结束：56天12小时");
    }

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, MyCheckDetails.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back, R.id.ll_edit, R.id.tv_copy, R.id.tv_see})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_edit:
                showToast("地址");
                break;
            case R.id.tv_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", "1111111");
                cm.setPrimaryClip(mClipData);
                showToast("复制成功");
                break;
            case R.id.tv_see:
                showToast("查看");
                break;
        }
    }
}
