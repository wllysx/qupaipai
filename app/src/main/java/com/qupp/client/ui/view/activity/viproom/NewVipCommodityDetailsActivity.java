package com.qupp.client.ui.view.activity.viproom;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.fragment.son.AddPriceFragment;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DateUtils;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.MyImageGetter;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.EarningsAdapter1;
import com.qupp.client.utils.dialog.BelowGiftbagDialog;
import com.qupp.client.utils.event.CloseDrawlayoutEvent;
import com.qupp.client.utils.event.DetailStatesEvent;
import com.qupp.client.utils.event.GoodsDetails;
import com.qupp.client.utils.event.SendPrice;
import com.qupp.client.utils.view.FullListview;
import com.qupp.client.utils.view.ObservableScrollView;
import com.umeng.socialize.UMShareAPI;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 积分商城 商品详情
 */


@SuppressLint("all")
public class NewVipCommodityDetailsActivity extends BaseActivity {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.fl_layout_show)
    FrameLayout flLayoutShow;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_layout_hint)
    FrameLayout flLayoutHint;
    String states = "", auctionId = "";
    int pagecounts = 1;
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
    @BindView(R.id.tv_counttime)
    TextView tvCounttime;
    @BindView(R.id.tv_nextCashDeposit)
    TextView tvNextCashDeposit;
    @BindView(R.id.tv_nextprice)
    TextView tvNextprice;
    Double markupPrice;
    String topPrice = "";
    EntityForSimple entityForSimple;
    @BindView(R.id.tv_price_title)
    TextView tvPriceTitle;
    String url = "";
    String goodsname = "";
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.scrollview)
    ObservableScrollView scrollview;
    @BindView(R.id.tv_longimage)
    TextView tvLongimage;
    @BindView(R.id.listview)
    FullListview listview;
    @BindView(R.id.ll_rank)
    LinearLayout llRank;
    EarningsAdapter1 adapter;
    ArrayList<EntityForSimple> datas = new ArrayList<>();
    String number = "";
    @BindView(R.id.tv_busong)
    TextView tvBusong;
    @BindView(R.id.tv_xiaogaoorice)
    TextView tvXiaogaoorice;
    @BindView(R.id.ll_xiaojia)
    LinearLayout llXiaojia;
    private MyImageGetter myImageGetter;

    @BindView(R.id.tv_meal)
    TextView tvMeal;
    @BindView(R.id.iv_meal)
    ImageView ivMeal;
    @BindView(R.id.ll_zeng)
    LinearLayout llZeng;
    ArrayList<EntityForSimple> mealDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_details_new_vip);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayoutShow.setPadding(0, stateBarHeight, 0, 0);
        flLayoutHint.setPadding(0, stateBarHeight, 0, 0);
        states = getIntent().getStringExtra("states");
        auctionId = getIntent().getStringExtra("auctionId");
        topPrice = getIntent().getStringExtra("topPrice");
        number = getIntent().getStringExtra("number");
        goodsname = getIntent().getStringExtra("goodsname");
        //tvMarkupPrice.setText(goodsname);

        initView();
        initData();
        geturl();
    }


    private void initView() {
        tvTitle.setText("商品详情");
        drawerLayout.setFocusableInTouchMode(false);


        int height = ScreenAdaptive.dp2px(this, 230);
        scrollview.setOnScollChangedListener((ObservableScrollView scrollView, int x, int y, int oldx, int oldy) -> {

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


        listview.setFocusable(false);
        adapter = new EarningsAdapter1(NewVipCommodityDetailsActivity.this, datas);
        listview.setAdapter(adapter);

    }

    private void setNextPirce(String topPrice) {
        tvNextprice.setText("￥" + MyApplication.rvZeroAndDot(MyApplication.fourAndFive((markupPrice + Double.valueOf(topPrice))+"")));

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
                        entityForSimple = entity.getData();
                        states = entity.getData().getStatus();

                        if (entity.getData().getIsLimitPrice().equals("1")) {
                            llXiaojia.setVisibility(View.VISIBLE);
                            tvXiaogaoorice.setText(entity.getData().getLimitPrice());
                            tvStartPrice.setTextColor(getResources().getColor(R.color.textcolor23));
                        }
                        //送积分或者大礼包判断start
                        if (entity.getData().getIntegralType().equals("0")) {
                            tvBusong.setText("本商品无积分");
                            tvBusong.setVisibility(View.VISIBLE);
                        } else if (entity.getData().getIntegralType().equals("1")) {
                            //积分赠送比例
                            tvBusong.setText("按成交价100%送超值积分");
                            tvBusong.setVisibility(View.VISIBLE);
                        } else if (entity.getData().getIntegralType().equals("2")){
                            //大礼包
                            mealDatas = entity.getData().getAuctionMealViewVoList();
                            tvBusong.setVisibility(View.GONE);
                            llZeng.setVisibility(View.VISIBLE);
                            if(mealDatas!=null){
                                if(mealDatas.size()>1||(mealDatas.size()==1&&mealDatas.get(0).getAuctionMealGoodsVoList().size()>0)) {
                                    //可点击
                                    ivMeal.setVisibility(View.VISIBLE);
                                }else{
                                    //不可点击
                                    ivMeal.setVisibility(View.INVISIBLE);
                                    llZeng.setEnabled(false);
                                    tvMeal.setSingleLine(false);
                                }

                                String mealtx="";
                                if(!mealDatas.get(0).getIntegralRatio().equals("0")){
                                    //有积分
                                    String integralRatio =  ((Double.valueOf(mealDatas.get(0).getIntegralRatio())*1000)/10+"%").replace(".0","");
                                    mealtx = "【成交价"+integralRatio+"超值积分】+";
                                }
                                if(!mealDatas.get(0).getWorthRatio().equals("0")){
                                    //有超值积分
                                    String worthRatio =  ((Double.valueOf(mealDatas.get(0).getWorthRatio())*1000)/10+"%").replace(".0","");
                                    mealtx = mealtx+"【成交价"+worthRatio+"积分】+";
                                }
                                if(mealDatas.get(0).getAuctionMealGoodsVoList()!=null&&mealDatas.get(0).getAuctionMealGoodsVoList().size()>0){
                                    //有商品
                                    mealtx = mealtx+mealDatas.get(0).getAuctionMealGoodsVoList().get(0).getMallGoodsName()+"+";
                                }
                                mealtx = mealtx.substring(0,mealtx.length()-1);
                                tvMeal.setText(mealtx);

                            }else{
                                //不可点击
                                ivMeal.setVisibility(View.INVISIBLE);
                                llZeng.setEnabled(false);
                            }
                        }
                        //送积分或者大礼包判断end


                        if (states.equals("2")) {
                            EventBus.getDefault().postSticky(new GoodsDetails(entity.getData().getGoodsDetail(), "1"));
                            //未开始
                            pagecounts = 1;
                            llNowprice.setVisibility(View.GONE);
                            llBlowState1.setVisibility(View.GONE);
                            llBlowState2.setVisibility(View.VISIBLE);
                            //预展中倒计时
                            countDown(entity.getData().getStartTimes());
                            //结束倒计时
                            countDown1(entity.getData().getEndTimes());
                            //隐藏右边进入排入口
                            llRight.setVisibility(View.GONE);
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                            //倒计时
                        } else if (states.equals("3")) {

                            //竞拍中
                            EventBus.getDefault().postSticky(new GoodsDetails(entity.getData().getGoodsDetail(), "1"));
                            pagecounts = 1;
                            llNowprice.setVisibility(View.VISIBLE);
                            llNowprice.setBackgroundColor(getResources().getColor(R.color.iscur));
                            tvPriceTitle.setText("当前价：");
                            llBlowState1.setVisibility(View.VISIBLE);
                            llBlowState2.setVisibility(View.GONE);
                            //结束倒计时
                            countDown1(entity.getData().getEndTimes());
                        } else {
                            //已结束
                            EventBus.getDefault().postSticky(new GoodsDetails(entity.getData().getGoodsDetail(), "0"));
                            pagecounts = 2;
                            llNowprice.setVisibility(View.VISIBLE);
                            llNowprice.setBackgroundColor(getResources().getColor(R.color.yellowend));
                            tvPriceTitle.setText("成交价：");
                            llBlowState1.setVisibility(View.GONE);
                            llBlowState2.setVisibility(View.GONE);
                            llRank.setVisibility(View.VISIBLE);

                            //已结束请求排行榜列表
                            getRankData();
                        }

                        logourl = entity.getData().getLogo();
                        initAddPriceView(entity.getData().getTopPrice(), entity.getData().getMarkupPrice(), entity.getData().getNextCashDeposit(), entity.getData().getGoodsName(), entity.getData().getStartTime(), entity.getData().getViewCount());
                        tvAssessPrice.setText("￥" + entity.getData().getAssessPrice());
                        tvGoodsName.setText(entity.getData().getGoodsName().replace("\n",""));
                        // tvGoodsName.setText("到附近的路口附近是的事件的看法了就收到了会计分录的飞机螺丝钉解放螺丝钉看见发的放假啦空手道解放昆仑山绝对拉风的的飞机螺丝钉解放螺丝钉解放");
                        tvBrokerage.setText("￥" + entity.getData().getBrokerage());
                        tvMarkupPrice.setText("￥" + entity.getData().getMarkupPrice());
                        tvStartPrice.setText("￥" + entity.getData().getStartPrice());
                        tvTopprice.setText("￥" + entity.getData().getTopPrice());
                        tvNextCashDeposit.setText("（保证金：￥" + MyApplication.fourAndFive(entity.getData().getNextCashDeposit()+"") + "）");

                        markupPrice = Double.valueOf(entity.getData().getMarkupPrice());
                        setNextPirce(entity.getData().getTopPrice());
                        String headImage = entity.getData().getHeadImage();
                        ArrayList arrayList = new Gson().fromJson(headImage, new TypeToken<ArrayList<String>>() {
                        }.getType());
                        initBanner(arrayList);
                        myImageGetter =  new MyImageGetter(NewVipCommodityDetailsActivity.this, tvLongimage);
                        tvLongimage.setText(Html.fromHtml(entity.getData().getGoodsDetail(), myImageGetter, null));
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {

                Log.d("json", t.toString());

            }
        });
    }

    private void getRankData() {
        ApiUtil.getApiService().rankingList(auctionId).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        datas.addAll(entity.getData());
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

    private void geturl() {
        ApiUtil.getApiService().syskey("app_goods_share").enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        url = entity.getData();
                    } else {
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
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
        activity.startActivity(new Intent(activity, NewVipCommodityDetailsActivity.class)
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
        try {
            if(myImageGetter!=null) {
                if (myImageGetter.getBitmap() != null && !myImageGetter.getBitmap().isRecycled()) {
                    myImageGetter.getBitmap().recycle();
                    myImageGetter.setBitmap(null);
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        finish();
    }

    @OnClick({R.id.back, R.id.ll_right, R.id.rl_chujia, R.id.ll_zeng})
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
                    MyApplication.toLogin(NewVipCommodityDetailsActivity.this);
                } else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                    drawerLayout.openDrawer(Gravity.RIGHT);
                    EventBus.getDefault().postSticky(new SendPrice());
                }
                break;
            case R.id.ll_zeng:
                new BelowGiftbagDialog(NewVipCommodityDetailsActivity.this).show(mealDatas);
                break;
        }
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(NewVipCommodityDetailsActivity.this).load(path).into(imageView);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
                //隐藏右边进入排入口
                llRight.setVisibility(View.VISIBLE);
                llNowprice.setVisibility(View.VISIBLE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                //出价fragment显示出价按钮
                EventBus.getDefault().post(new DetailStatesEvent("3"));

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

                llBlowState1.setVisibility(View.GONE);
                llBlowState2.setVisibility(View.GONE);
                llNowprice.setVisibility(View.VISIBLE);
                llNowprice.setBackgroundColor(getResources().getColor(R.color.yellowend));
                tvPriceTitle.setText("成交价：");
                states = "4";
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
            tvNextprice.setText("￥" + MyApplication.rvZeroAndDot(MyApplication.fourAndFive((markupPrice + Double.valueOf(event.getPayPrice())+""))));
            //保证金
            tvNextCashDeposit.setText("（保证金：￥" + MyApplication.fourAndFive(event.getNextCashDeposit()+"") + "）");

        }
    }


}
