package com.qupp.client.ui.view.activity.mine.order;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.fragment.son.OrderListFragment;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CommonTabPagerAdapter;
import com.qupp.client.utils.view.magicIndicator.MyPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的订单
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class OrderListActivity extends BaseActivity implements CommonTabPagerAdapter.TabPagerListener {

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
    int position = 0;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_orderlist);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        position = getIntent().getIntExtra("position", 0);
        initView();
    }

    private void initView() {
        tvTitle.setText("我的订单");
        adapter = new CommonTabPagerAdapter(getSupportFragmentManager(), 5, Arrays.asList("全部", "待付款", "待发货", "待收货", "已完成"), this);
        adapter.setListener(this);
        viewpager.setAdapter(adapter);
        initMagicIndicator(Arrays.asList("全部", "待付款", "待发货", "待收货", "已完成"));


        viewpager.setCurrentItem(position);
    }

    private void initMagicIndicator(List<String> titles) {
        //magicIndicator.setBackgroundColor(Color.BLACK);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return 5;
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
                linePagerIndicator.setLineHeight(ScreenAdaptive.dp2px(OrderListActivity.this, 2));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpager);
    }


    public static void startActivityInstance(Context activity, int positon) {
        activity.startActivity(new Intent(activity, OrderListActivity.class).putExtra("position", positon));
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
        return OrderListFragment.newInstance(position);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        finish();

    }
}
