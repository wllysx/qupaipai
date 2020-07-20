package com.qupp.client.ui.view.activity.mine.setting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.login.KnowWeb;
import com.qupp.client.ui.view.activity.login.YinsiWeb;
import com.qupp.client.utils.DoubleClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_version)
    TextView tvVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(AboutActivity.this), 0, 0);
        setStateColor(false);
        initView();

    }


    private void initView() {
        tvTitle.setText("关于趣拍拍");
        tvVersion.setText("趣拍拍 V"+ MyApplication.getVersion());
    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, AboutActivity.class));
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


    @OnClick({R.id.back, R.id.ll_good, R.id.ll_yinsi, R.id.ll_xieyi})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_good:
                launchAppDetail("com.jyk.client","");
                break;
            case R.id.ll_yinsi:
                startActivity(new Intent(AboutActivity.this, YinsiWeb.class));
                break;
            case R.id.ll_xieyi:
                startActivity(new Intent(AboutActivity.this, KnowWeb.class));
                break;
        }
    }

    /**
     * 启动到app详情界面
     *
     * @param appPkg
     *            App的包名
     * @param marketPkg
     *            应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)){
                return;
            }
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)){
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("打开应用市场失败");
        }
    }




}
