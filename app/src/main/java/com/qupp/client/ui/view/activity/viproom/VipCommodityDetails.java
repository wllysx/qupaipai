package com.qupp.client.ui.view.activity.viproom;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.fragment.son.AddPriceFragment;
import com.qupp.client.ui.view.fragment.son.CommodityDetailsFragment;
import com.qupp.client.ui.view.fragment.son.CommodityEarningsListFragment;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DateUtils;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CommonTabPagerAdapter;
import com.qupp.client.utils.event.CloseDrawlayoutEvent;
import com.qupp.client.utils.event.DetailStatesEvent;
import com.qupp.client.utils.event.GoodsDetails;
import com.qupp.client.utils.event.SendPrice;
import com.qupp.client.utils.view.magicIndicator.MyPagerTitleView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商品详情（出价）
 */


@SuppressLint("all")
public class VipCommodityDetails extends BaseActivity implements CommonTabPagerAdapter.TabPagerListener {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    CommonTabPagerAdapter adapter;
    @BindView(R.id.fl_layout_show)
    FrameLayout flLayoutShow;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_layout_hint)
    FrameLayout flLayoutHint;
    String states = "", auctionId = "";
    int pagecounts = 1;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.ll_nowprice)
    LinearLayout llNowprice;
    @BindView(R.id.ll_blow_state1)
    LinearLayout llBlowState1;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.ll_blow_state2)
    LinearLayout llBlowState2;
    String logourl = "";
    @BindView(R.id.tv_topprice)
    TextView tvTopprice;
    @BindView(R.id.tv_goodsName)
    TextView tvGoodsName;
    @BindView(R.id.tv_startPrice)
    TextView tvStartPrice;
    @BindView(R.id.tv_assessPrice)
    TextView tvAssessPrice;
    @BindView(R.id.tv_markupPrice)
    TextView tvMarkupPrice;
    @BindView(R.id.tv_brokerage)
    TextView tvBrokerage;
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.tv_counttime)
    TextView tvCounttime;
    @BindView(R.id.tv_nextCashDeposit)
    TextView tvNextCashDeposit;
    @BindView(R.id.tv_nextprice)
    TextView tvNextprice;
    Double markupPrice;
    String topPrice = "", number = "";
    @BindView(R.id.tv_price_title)
    TextView tvPriceTitle;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    String goodsname = "";
    @BindView(R.id.ll_xiangqing)
    LinearLayout llXiangqing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian_commodity_details_vip);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayoutShow.setPadding(0, stateBarHeight, 0, 0);
        flLayoutHint.setPadding(0, stateBarHeight, 0, 0);
        setStateColor(false);
        states = getIntent().getStringExtra("states");
        auctionId = getIntent().getStringExtra("auctionId");
        topPrice = getIntent().getStringExtra("topPrice");
        number = getIntent().getStringExtra("number");

        goodsname = getIntent().getStringExtra("goodsname");
        tvMarkupPrice.setText(goodsname);

        tvMarkupPrice.post(new Runnable() {
            @Override
            public void run() {
                if (tvMarkupPrice.getLineCount() == 1||tvMarkupPrice.getText().toString().length()<=18) {
                    ViewGroup.LayoutParams para;
                    para = llName.getLayoutParams();
                    para.height = ScreenAdaptive.dp2px(VipCommodityDetails.this, 50);
                    llName.setLayoutParams(para);

                    collapsingToolbar.post(() -> {
                        ViewGroup.LayoutParams para1;
                        para1 = collapsingToolbar.getLayoutParams();
                        para1.height = collapsingToolbar.getHeight() - stateBarHeight;
                        collapsingToolbar.setLayoutParams(para1);
                    });
                } else {
                    ViewGroup.LayoutParams para;
                    para = llName.getLayoutParams();
                    para.height = ScreenAdaptive.dp2px(VipCommodityDetails.this, 80);
                    llName.setLayoutParams(para);

                    collapsingToolbar.post(() -> {
                        ViewGroup.LayoutParams para1;
                        para1 = collapsingToolbar.getLayoutParams();
                        para1.height = collapsingToolbar.getHeight() - stateBarHeight;
                        collapsingToolbar.setLayoutParams(para1);
                    });
                }
            }
        });
        initView();
        initData();

    }

    private void initMagicIndicator(List<String> list) {
        //magicIndicator.setBackgroundColor(Color.BLACK);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                MyPagerTitleView simplePagerTitleView = new MyPagerTitleView(context);
                //magicIndicator.setBackgroundColor(Color.parseColor("#ffffff"));
                simplePagerTitleView.setNormalColor(Color.parseColor("#777777"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#2b2b2b"));
                simplePagerTitleView.setText(list.get(index));
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
                linePagerIndicator.setLineHeight(ScreenAdaptive.dp2px(VipCommodityDetails.this, 2));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpager);
    }

    /**
     * 添加加价fragment
     */
    private void initAddPriceView(String topPrice, String markupPrice, double nextCashDeposit, String goodsname, String starttime, String viewCount) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.contentfragment, AddPriceFragment.newInstance("0", states, auctionId, topPrice, markupPrice, nextCashDeposit, goodsname, starttime, logourl, number, viewCount,""));
        ft.addToBackStack("tag");
        ft.commit();
    }

    private void initView() {
        tvTitle.setText("商品详情");
        drawerLayout.setFocusableInTouchMode(false);


        int height = ScreenAdaptive.dp2px(this, 230);
        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int y = Math.abs(verticalOffset);
            if (height > y && y > 100) {
                Log.d("height2323", height + "," + y);
                flLayoutHint.setAlpha((float) (y - 100) / height);
            } else if (height <= y) {
                flLayoutHint.setAlpha(1f);
            } else {
                flLayoutHint.setAlpha(0f);
            }
        });


        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });


    }


    /**
     * 获取商品详情
     */
    private void initData() {
        ApiUtil.getApiService().auctionRecordInfodetail(auctionId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        states = entity.getData().getStatus();
                        if (states.equals("2")) {

                            //未开始
                            magicIndicator.setVisibility(View.GONE);
                            pagecounts = 1;
                            llNowprice.setVisibility(View.GONE);
                            llBlowState1.setVisibility(View.GONE);
                            llBlowState2.setVisibility(View.VISIBLE);
                            //预展中倒计时
                            countDown(entity.getData().getStartTimes());

                           /* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
                            Date date = new Date(System.currentTimeMillis());
                            if (DateUtils.dateToDate(entity.getData().getStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").equals(simpleDateFormat.format(date))) {
                                tvCounttime.setText(DateUtils.dateToDate(entity.getData().getStartTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                            } else {
                                tvCounttime.setText(DateUtils.dateToDate(entity.getData().getStartTime(), "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm"));
                            }*/
                            EventBus.getDefault().postSticky(new GoodsDetails(entity.getData().getGoodsDetail(), "1"));
                            //结束倒计时
                            countDown1(entity.getData().getEndTimes());
                            //隐藏右边进入排入口
                            llRight.setVisibility(View.GONE);
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            //倒计时
                            adapter = new CommonTabPagerAdapter(getSupportFragmentManager(), 1, Arrays.asList("拍品详情"), VipCommodityDetails.this);
                            adapter.setListener(VipCommodityDetails.this);
                            viewpager.setAdapter(adapter);

                            initMagicIndicator(Arrays.asList("拍品详情"));
                            llXiangqing.setVisibility(View.VISIBLE);
                        } else if (states.equals("3")) {
                            //竞拍中
                            ViewGroup.LayoutParams para1;
                            para1 = collapsingToolbar.getLayoutParams();
                            para1.height = collapsingToolbar.getHeight()+ScreenAdaptive.dp2px(VipCommodityDetails.this,40);
                            collapsingToolbar.setLayoutParams(para1);

                            magicIndicator.setVisibility(View.GONE);
                            pagecounts = 1;
                            llNowprice.setVisibility(View.VISIBLE);
                            llNowprice.setBackgroundColor(getResources().getColor(R.color.iscur));
                            tvPriceTitle.setText("当前价：");
                            llBlowState1.setVisibility(View.VISIBLE);
                            llBlowState2.setVisibility(View.GONE);
                            //结束倒计时
                            countDown1(entity.getData().getEndTimes());

                            adapter = new CommonTabPagerAdapter(getSupportFragmentManager(), 1, Arrays.asList("拍品详情"), VipCommodityDetails.this);
                            adapter.setListener(VipCommodityDetails.this);
                            viewpager.setAdapter(adapter);

                            initMagicIndicator(Arrays.asList("拍品详情"));
                            EventBus.getDefault().postSticky(new GoodsDetails(entity.getData().getGoodsDetail(), "1"));
                            llXiangqing.setVisibility(View.VISIBLE);
                        } else {

                            ViewGroup.LayoutParams para1;
                            para1 = collapsingToolbar.getLayoutParams();
                            para1.height = collapsingToolbar.getHeight()+ScreenAdaptive.dp2px(VipCommodityDetails.this,40);
                            collapsingToolbar.setLayoutParams(para1);

                            EventBus.getDefault().postSticky(new GoodsDetails(entity.getData().getGoodsDetail(), "0"));
                            //已结束
                            magicIndicator.setVisibility(View.VISIBLE);
                            pagecounts = 2;
                            llBlowState1.setVisibility(View.GONE);
                            llBlowState2.setVisibility(View.GONE);

                            llNowprice.setVisibility(View.VISIBLE);
                            llNowprice.setBackgroundColor(getResources().getColor(R.color.textcolor29));
                            tvPriceTitle.setText("成交价：");
                            // setDrawerLeftEdgeSize(drawerLayout, 0.3f);
                            adapter = new CommonTabPagerAdapter(getSupportFragmentManager(), 2, Arrays.asList("拍品详情", "收益排行榜"), VipCommodityDetails.this);
                            adapter.setListener(VipCommodityDetails.this);
                            viewpager.setAdapter(adapter);

                            initMagicIndicator(Arrays.asList("拍品详情", "收益排行榜"));
                            llXiangqing.setVisibility(View.GONE);
                        }
                        logourl = entity.getData().getLogo();
                        initAddPriceView(entity.getData().getTopPrice(), entity.getData().getMarkupPrice(), entity.getData().getNextCashDeposit(), entity.getData().getGoodsName(), entity.getData().getStartTime(), entity.getData().getViewCount());
                        tvAssessPrice.setText("￥" + entity.getData().getAssessPrice());
                        tvGoodsName.setText(entity.getData().getGoodsName());
                        tvBrokerage.setText("￥" + entity.getData().getBrokerage());
                        tvMarkupPrice.setText("￥" + entity.getData().getMarkupPrice());
                        tvStartPrice.setText("￥" + entity.getData().getStartPrice());
                        tvTopprice.setText("￥" + entity.getData().getTopPrice());
                        tvNextCashDeposit.setText("（保证金：￥" + entity.getData().getNextCashDeposit() + "）");
                        markupPrice = Double.valueOf(entity.getData().getMarkupPrice());
                        setNextPirce(entity.getData().getTopPrice());
                        String headImage = entity.getData().getHeadImage();
                        ArrayList arrayList = new Gson().fromJson(headImage, new TypeToken<ArrayList<String>>() {
                        }.getType());
                        initBanner(arrayList);

                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {

                Log.d("1111", t.toString());
            }
        });
    }

    private void setNextPirce(String topPrice) {
        tvNextprice.setText(("￥" + (markupPrice + Double.valueOf(topPrice))).replace(".00", "").replace(".0", ""));
    }

    public void initBanner(ArrayList<String> arrayList) {
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        //String[] images = {"http://img3.duitang.com/uploads/item/201504/02/20150402H2753_JuTG3.thumb.700_0.jpeg", "http://b-ssl.duitang.com/uploads/blog/201312/30/20131230161427_Ye4QQ.jpeg", "http://img.redocn.com/sheji/20150306/weimeiliuguangyinghuashubeijingshipin_3957894.jpg"};
        banner.setImages(arrayList);
        //banner设置方法全部调用完毕时最后调用

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置banner动画效果
        // banner.setBannerAnimation(Transformer.DepthPage);
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
    }


    public static void startActivityInstance(Activity activity, String states, String auctionId, String topPrice, String number, String goodsname) {
        activity.startActivity(new Intent(activity, VipCommodityDetails.class)
                .putExtra("states", states)
                .putExtra("auctionId", auctionId)
                .putExtra("topPrice", topPrice)
                .putExtra("goodsname", goodsname)
                .putExtra("number", number));
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


    @OnClick({R.id.back, R.id.ll_right, R.id.rl_chujia})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_right:
                drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.rl_chujia:
                if (MyApplication.getToken().equals("")) {
                    MyApplication.toLogin(VipCommodityDetails.this);
                } else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                    drawerLayout.openDrawer(Gravity.RIGHT);
                    EventBus.getDefault().postSticky(new SendPrice());
                }
                break;
        }
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(VipCommodityDetails.this).load(path).into(imageView);
        }

    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return CommodityDetailsFragment.newInstance(states, auctionId);
        } else {
            return CommodityEarningsListFragment.newInstance(auctionId);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {// MENU键
            // 监控/拦截菜单键
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawers();
            } else {
                finish();
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exit(CloseDrawlayoutEvent event) {
        if (event != null) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawers();
            }
        }
    }


    private void countDown(String startTimes) {
        try {
            TimeCount time = new TimeCount(Long.valueOf(startTimes), 1000);
            time.start();
        } catch (Exception e) {
        }

    }

    private void countDown1(String endtimes) {
        try {
            TimeCount1 time = new TimeCount1(Long.valueOf(endtimes), 1000);
            time.start();
        } catch (Exception e) {
        }

    }

    //开始倒计时
    public class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            try {
                llBlowState1.setVisibility(View.VISIBLE);
                llBlowState2.setVisibility(View.GONE);
                llNowprice.setVisibility(View.VISIBLE);
                //隐藏右边进入排入口
                llRight.setVisibility(View.VISIBLE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                //出价fragment显示出价按钮
                EventBus.getDefault().post(new DetailStatesEvent("3"));

                ViewGroup.LayoutParams para1;
                para1 = collapsingToolbar.getLayoutParams();
                para1.height = collapsingToolbar.getHeight()+ScreenAdaptive.dp2px(VipCommodityDetails.this,40);
                collapsingToolbar.setLayoutParams(para1);
            } catch (Exception e) {

            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            try {
                tvCounttime.setText(DateUtils.getDistanceTime(millisUntilFinished));
            } catch (Exception e) {

            }
        }
    }

    //结束倒计时
    public class TimeCount1 extends CountDownTimer {
        public TimeCount1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            try {

                if(llNowprice.getVisibility()!=View.VISIBLE){
                    ViewGroup.LayoutParams para1;
                    para1 = collapsingToolbar.getLayoutParams();
                    para1.height = collapsingToolbar.getHeight()+ScreenAdaptive.dp2px(VipCommodityDetails.this,40);
                    collapsingToolbar.setLayoutParams(para1);
                }

                llBlowState1.setVisibility(View.GONE);
                llBlowState2.setVisibility(View.GONE);
                //出价fragment显示出价按钮
                llNowprice.setVisibility(View.VISIBLE);
                llNowprice.setBackgroundColor(getResources().getColor(R.color.textcolor29));
                tvPriceTitle.setText("成交价：");
                EventBus.getDefault().postSticky(new DetailStatesEvent("4"));
            } catch (Exception e) {

            }

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
        }
    }


    /**
     * socket数据刷新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void socket(EntityForSimple event) {
        if (event != null) {
            //当前价
            tvTopprice.setText("￥" + event.getPayPrice());
            //下一口价格
            tvNextprice.setText(("￥" + (markupPrice + Double.valueOf(event.getPayPrice()))).replace(".00", "").replace(".0", ""));
            //保证金
            tvNextCashDeposit.setText("（保证金：￥" + event.getNextCashDeposit() + "）");

        }
    }

}
