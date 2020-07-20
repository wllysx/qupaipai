package com.qupp.client.ui.view.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.fragment.son.MessageFragment;
import com.qupp.client.utils.DateUtils;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息
 */

public class MessageActivity extends BaseActivity implements CommonTabPagerAdapter.TabPagerListener{

    @BindView(R.id.linear)
    View linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    CommonTabPagerAdapter adapter;
    ArrayList<String> titles = new ArrayList<>();
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(MessageActivity.this), 0, 0);
        setStateColor(false);
        initView();
        initMagicIndicator();
        MyApplication.setMessageTime(DateUtils.getNowTime());
    }


    private void initView() {
        tvTitle.setText("消息中心");

        titles.add("通知");
        titles.add("消息");
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
                linePagerIndicator.setLineHeight(ScreenAdaptive.dp2px(MessageActivity.this, 2));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpager);
    }



    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, MessageActivity.class));
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
    @Override
    public Fragment getFragment(int position) {
        return MessageFragment.newInstance(position);
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
