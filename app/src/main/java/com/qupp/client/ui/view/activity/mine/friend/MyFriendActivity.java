package com.qupp.client.ui.view.activity.mine.friend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcssloop.widget.RCImageView;
import com.google.android.material.appbar.AppBarLayout;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.fragment.son.FriendsFragment;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CommonTabPagerAdapter;
import com.qupp.client.utils.event.RefreshFriend;
import com.qupp.client.utils.view.magicIndicator.MyPagerTitleView;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的朋友
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class MyFriendActivity extends BaseActivity implements CommonTabPagerAdapter.TabPagerListener {

    @BindView(R.id.linear)
    View linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    CommonTabPagerAdapter adapter;

    String allFriendNum = "";
    @BindView(R.id.tv_allfriendnum)
    TextView tvAllfriendnum;
    @BindView(R.id.iv_photo)
    RCImageView ivPhoto;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.ll_top)
    LinearLayout llTop;

    ArrayList<String> titles = new ArrayList<>();
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.springviewtop)
    SpringView springviewtop;
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(MyFriendActivity.this), 0, 0);
        setStateColor(true);
        allFriendNum = getIntent().getStringExtra("allFriendNum");
        initFriend();
        initData();

    }

    private void initFriend() {
        ApiUtil.getApiService().getMyFans(MyApplication.getToken(), "1", "2").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        initView(entity.getData().getTotal());
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


    private void initView(String count) {
        tvTitle.setText("我的朋友");
        tvAllfriendnum.setText(allFriendNum);
        titles.add("全部朋友(" + allFriendNum + ")");
        titles.add("我的好友(" + count + ")");
        adapter = new CommonTabPagerAdapter(getSupportFragmentManager(), 2, titles, this);
        adapter.setListener(this);
        viewpager.setAdapter(adapter);
        initMagicIndicator();
        initSpringView();
    }

    private void initSpringView() {
        springviewtop.setHeader(new DefaultHeader(this));
        springviewtop.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new RefreshFriend());
                springviewtop.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                springviewtop.onFinishFreshAndLoad();
            }
        });
        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                springviewtop.setEnable(true);
            } else {
                springviewtop.setEnable(false);
            }
        });


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
                linePagerIndicator.setLineHeight(ScreenAdaptive.dp2px(MyFriendActivity.this, 2));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpager);
    }


    public static void startActivityInstance(Activity activity, String allFriendNum) {
        activity.startActivity(new Intent(activity, MyFriendActivity.class)
                .putExtra("allFriendNum", allFriendNum)
        );
    }

    /**
     * 获取上级信息
     */
    private void initData() {
        ApiUtil.getApiService().superiorInfo(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        llTop.setVisibility(View.VISIBLE);
                        tvNickname.setText(entity.getData().getNickname());
                        Glide.with(MyFriendActivity.this).load(entity.getData().getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into(ivPhoto);
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
        return FriendsFragment.newInstance(position);
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
