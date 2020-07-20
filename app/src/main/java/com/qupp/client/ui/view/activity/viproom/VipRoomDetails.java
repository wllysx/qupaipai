package com.qupp.client.ui.view.activity.viproom;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.fragment.son.VipRoomDetailsFragment;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CommonTabPagerAdapter;
import com.qupp.client.utils.dialog.MiddleDialogReverse;
import com.qupp.client.utils.view.magicIndicator.MyPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * VIP店铺详情
 */


@SuppressLint("all")
public class VipRoomDetails extends BaseActivity implements CommonTabPagerAdapter.TabPagerListener {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    CommonTabPagerAdapter adapter;
    ArrayList<String> titles = new ArrayList<>();
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    String roomId = "";
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_vipNickname)
    TextView tvVipNickname;
    @BindView(R.id.tv_vipPhone)
    TextView tvVipPhone;
    String number = "",vipNickname = "",vipPhone = "",name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viproom_details);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        roomId = getIntent().getStringExtra("roomId");
        number = getIntent().getStringExtra("number");
        vipNickname = getIntent().getStringExtra("vipNickname");
        vipPhone = getIntent().getStringExtra("vipPhone");
        name = getIntent().getStringExtra("name");
        initView();
        initMagicIndicator();
        //StatService.onEvent(this, "qijiandiandenglu", "旗舰店登录");

    }

    private void initView() {
        tvTitle.setText("店铺详情");
        titles.add("今日场次");
        titles.add("近期预告");
        tvNumber.setText(number);
        tvVipNickname.setText(vipNickname);
        tvVipPhone.setText(MyApplication.getPhonePass(vipPhone));
        adapter = new CommonTabPagerAdapter(getSupportFragmentManager(), 2, titles, this);
        adapter.setListener(this);
        viewpager.setAdapter(adapter);
    }

    private void initMagicIndicator() {
        //magicIndicator.setBackgroundColor(Color.BLACK);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                MyPagerTitleView simplePagerTitleView = new MyPagerTitleView(context);
                //magicIndicator.setBackgroundColor(Color.parseColor("#ffffff"));
                simplePagerTitleView.setNormalColor(Color.parseColor("#777777"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#2b2b2b"));
                simplePagerTitleView.setText(titles.get(index));
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setTextSize(15);
                simplePagerTitleView.setOnClickListener(v -> viewpager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(getResources().getColor(R.color.iscur));
                linePagerIndicator.setLineHeight(ScreenAdaptive.dp2px(VipRoomDetails.this, 2));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpager);
    }


    public static void startActivityInstance(Activity activity, String roomId, String number, String vipNickname, String vipPhone,String name) {
        activity.startActivity(new Intent(activity, VipRoomDetails.class)
                .putExtra("roomId", roomId)
                .putExtra("number", number)
                .putExtra("vipNickname", vipNickname)
                .putExtra("vipPhone", vipPhone)
                .putExtra("name", name)
        );
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


    @Override
    public Fragment getFragment(int position) {
        return VipRoomDetailsFragment.newInstance(position, roomId,name);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        new MiddleDialogReverse(this).setSureListener(() -> {
            finish();
        }).show("温馨提示", "是否要退出店铺", "取消", "确定", false);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {// MENU键
            // 监控/拦截菜单键
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            new MiddleDialogReverse(this).setSureListener(() -> {
                finish();
            }).show("温馨提示", "是否要退出店铺", "取消", "确定", false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
