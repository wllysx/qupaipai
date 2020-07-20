package com.qupp.client.ui.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.ui.view.activity.main.CommodityList;
import com.qupp.client.ui.view.activity.main.H5Web;
import com.qupp.client.ui.view.activity.main.ShopActivity;
import com.qupp.client.ui.view.activity.mine.coupon.MyCoupon;
import com.qupp.client.ui.view.activity.mine.myaction.MyActionActivity;
import com.qupp.client.ui.view.activity.scoreshop.ShopCommodityTypeActivity;
import com.qupp.client.utils.MySharePerference;
import com.qupp.client.utils.ScreenAdaptive;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.ll_linear)
    LinearLayout llLinear;
    @BindView(R.id.v)
    TextView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setStateColor(true);
        MyApplication.itemHeight = (ScreenAdaptive.getWidthSizeByAllSize(this) - ScreenAdaptive.dp2px(this, 30)) / 2;
        Intent intent = getIntent();
        Uri uri = intent.getData();
        Log.d("height", (ScreenAdaptive.getWidthSizeByAllSize(this) - ScreenAdaptive.dp2px(this, 30)) / 2 + "");


        /*  new Handler().postDelayed(() -> {*/
        if (uri != null) {
            // 获取参数的值
            String type = uri.getQueryParameter("type");
            String name = uri.getQueryParameter("name");
            String id = uri.getQueryParameter("id");
            if (type != null) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                if (type.equals("1")) {
                    CommodityList.startActivityInstance(WelcomeActivity.this, name, id);
                } else if (type.equals("2")) {
                    ShopCommodityTypeActivity.startActivityInstance(WelcomeActivity.this, name, id, "", "");
                } else if (type.equals("3")) {
                    ShopActivity.startActivityInstance(this);
                } else if (type.equals("4")) {
                    LoginActivity.startActivityInstance(this);
                } else if (type.equals("5")) {
                    if(MyApplication.getToken().equals("")){
                        LoginActivity.startActivityInstance(WelcomeActivity.this);
                    }else {
                        MyActionActivity.startActivityInstance(this);
                    }
                } else if (type.equals("6")) {
                    H5Web.startActivityInstance(this, id,name);
                } else if (type.equals("7")) {
                    if(MyApplication.getToken().equals("")){
                        LoginActivity.startActivityInstance(WelcomeActivity.this);
                    }else {
                        MyCoupon.startActivityInstance(this);
                    }
                }
            } else {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        } else {
            if (MySharePerference.getShardPerferience(this, "FIRST2APP", "").equals("1")) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
            }
        }
        finish();
        /* }, 1000);*/

    }


}
