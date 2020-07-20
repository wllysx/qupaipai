package com.qupp.client.ui.view.activity.scoreshop;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.mine.ShardActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.MyImageGetter;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.ShareUtils;
import com.qupp.client.utils.ToastUtils;
import com.qupp.client.utils.dialog.BelowShardDialog;
import com.qupp.client.utils.dialog.MiddleDialog;
import com.qupp.client.utils.event.NetRefreshEvent;
import com.qupp.client.utils.view.ObservableScrollView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

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
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class CommodityDetailsActivity extends BaseActivity {

    public Intent intent;
    public Bundle bundle;
    @BindView(R.id.banner)
    Banner banner;
    Unbinder unbinder;
    @BindView(R.id.fl_layout_show)
    FrameLayout flLayoutShow;
    @BindView(R.id.fl_layout_hint)
    FrameLayout flLayoutHint;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.scrollview)
    ObservableScrollView scrollview;
  /*  @BindView(R.id.iv_longimage)
    ResizableImageView ivLongimage;*/

    String logourl = "";
    public String goodsId;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_assessPrice)
    TextView tvAssessPrice;
    @BindView(R.id.tv_goodsName)
    TextView tvGoodsName;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_longimage)
    TextView tvLongimage;
    EntityForSimple entityForSimple;
    String url = "";
    @BindView(R.id.tv_guzhi)
    TextView tvGuzhi;
    @BindView(R.id.iv_shoucang)
    ImageView ivShoucang;
    String shoucangtype = "1", priceType = "";
    @BindView(R.id.tv_shoucang)
    TextView tvShoucang;
    @BindView(R.id.ll_shoucang)
    LinearLayout llShoucang;
    MyImageGetter myImageGetter;
    String firstCategoryId = "", categoryId = "";
    @BindView(R.id.tv_kucun)
    TextView tvKucun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_details);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayoutShow.setPadding(0, stateBarHeight, 0, 0);
        flLayoutHint.setPadding(0, stateBarHeight, 0, 0);
        goodsId = getIntent().getStringExtra("goodsId");
        initView();
        initScrollView();
        // initBanner();
        geturl();

    }


    private void initView() {
        tvTitle.setText("商品详情");
    }

    private void geturl() {
        ApiUtil.getApiService().syskey("app_integral_share").enqueue(new Callback<EntityForSimpleString>() {
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

    private void initScrollView() {
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

    public static void startActivityInstance(Context activity, String goodsId) {
        activity.startActivity(new Intent(activity, CommodityDetailsActivity.class)
                .putExtra("goodsId", goodsId)
        );
    }

    public static void startActivityInstance1(Context activity, String goodsId) {
        activity.startActivity(new Intent(activity, CommodityDetailsActivity.class)
                .putExtra("goodsId", goodsId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        try {
            if (myImageGetter != null) {
                if (myImageGetter.getBitmap() != null && !myImageGetter.getBitmap().isRecycled()) {
                    myImageGetter.getBitmap().recycle();
                    myImageGetter.setBitmap(null);
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
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

    @OnClick({R.id.ll_shard, R.id.tv_submit, R.id.ll_shoucang})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_shard:

                if (MyApplication.getToken().equals("")) {
                    MyApplication.toLogin(CommodityDetailsActivity.this);
                } else {
                    if (MyApplication.getCode().equals("")) {
                        getBlanceAndIntegralAndFans();
                    } else {
                        Log.d("shard", url + goodsId + "/" + MyApplication.getCode() + "");
                        new BelowShardDialog(this).setSureListener(item -> {
                            switch (item) {
                                case 1:
                                    ShardActivity.startActivityInstance(this, entityForSimple, "1", url + goodsId + "/" + MyApplication.getCode() + "");
                                    break;
                                case 2:
                                    String path = "pages/mallGoodsDetail/mallGoodsDetail?id=" + goodsId + "&invitCode=" + MyApplication.getCode();
                                    ShareUtils.shareUmMin(this, url + goodsId + "/" + MyApplication.getCode(), tvGoodsName.getText().toString(), "", logourl, path);
                                    break;
                                case 3:
                                    ShareUtils.shareWeb(this, url + goodsId + "/" + MyApplication.getCode(), tvGoodsName.getText().toString(), "", logourl, R.mipmap.icon_wx, SHARE_MEDIA.WEIXIN_CIRCLE);
                                    break;
                            }
                        }).show();
                    }
                }
                break;
            case R.id.tv_submit:
                if (MyApplication.getToken().equals("")) {
                    MyApplication.toLogin(CommodityDetailsActivity.this);
                } else {
                    try {
                        firstCategoryId = categoryId.substring(0, MyApplication.firstCategoryId.length());
                        if (MyApplication.firstCategoryId.equals(firstCategoryId)) {
                            ApiUtil.getApiService().getPaymentSettings(MyApplication.getToken(), "1").enqueue(new Callback<MessageForSimple>() {
                                @Override
                                public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                                    MessageForSimple entity = response.body();
                                    try {
                                        if (entity.getCode().equals("0")) {
                                            String paymentMethod = entity.getData().getPaymentMethod();
                                            if (!paymentMethod.contains("1")) {
                                                //积分
                                                if (priceType.equals("1") || priceType.equals("2")) {
                                                    //积分不满足
                                                    showToast("该商品暂不支持购买");
                                                } else {
                                                    getPayData(goodsId);
                                                }
                                            } else {
                                                getPayData(goodsId);
                                            }
                                        } else {
                                            showToast(entity.getMsg());
                                        }
                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void onFailure(Call<MessageForSimple> call, Throwable t) {
                                }
                            });
                        } else {
                            getPayData(goodsId);
                        }
                    } catch (Exception e) {
                        getPayData(goodsId);
                    }
                }
                break;
            case R.id.ll_shoucang:
                if (MyApplication.getToken().equals("")) {
                    MyApplication.toLogin(CommodityDetailsActivity.this);
                } else {
                    if (shoucangtype.equals("1")) {
                        //去收藏
                        addShoucang();
                    } else {
                        //取消收藏
                        cancelShoucang();
                    }

                }
                break;
        }
    }


    private void addShoucang() {
        ApiUtil.getApiService().collectGoodsInfocollect(MyApplication.getToken(), goodsId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        //收藏成功
                        shoucangtype = "0";
                        ivShoucang.setImageResource(R.mipmap.shoucang);
                        tvShoucang.setText("已收藏");
                        ToastUtils.showAddShoucang(CommodityDetailsActivity.this);
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

    private void cancelShoucang() {
        ApiUtil.getApiService().collectGoodsInfocancel(MyApplication.getToken(), goodsId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        //取消收藏成功
                        shoucangtype = "1";
                        ivShoucang.setImageResource(R.mipmap.qxsc_x);
                        tvShoucang.setText("收藏");
                        ToastUtils.showCancelShoucang(CommodityDetailsActivity.this);
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

    /**
     * 获取邀请码
     */
    private void getBlanceAndIntegralAndFans() {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        Log.d("shard", url + goodsId + "/" + MyApplication.getCode() + "");
                        MyApplication.setCode(entity.getData().getInvitationCode());
                        new BelowShardDialog(CommodityDetailsActivity.this).setSureListener(item -> {
                            switch (item) {
                                case 1:
                                    ShardActivity.startActivityInstance(CommodityDetailsActivity.this, entityForSimple, "1", url + goodsId + "/" + MyApplication.getCode());
                                    break;
                                case 2:
                                    String path = "pages/mallGoodsDetail/mallGoodsDetail?id=" + goodsId + "&invitCode=" + MyApplication.getCode();
                                    ShareUtils.shareUmMin(CommodityDetailsActivity.this, url + goodsId + "/" + MyApplication.getCode(), tvGoodsName.getText().toString(), "", logourl, path);
                                    break;
                                case 3:
                                    ShareUtils.shareWeb(CommodityDetailsActivity.this, url + goodsId + "/" + MyApplication.getCode(), tvGoodsName.getText().toString(), "", logourl, R.mipmap.icon_wx, SHARE_MEDIA.WEIXIN_CIRCLE);
                                    break;
                            }
                        }).show();

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

    /**
     * 预支付订单
     */
    private void getPayData(String goodsId) {
        ApiUtil.getApiService().prePayOrder(MyApplication.getToken(), goodsId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (priceType.equals("2") || priceType.equals("3")||priceType.equals("4")) {
                            CommodityPay.startActivityInstance(CommodityDetailsActivity.this, "1", goodsId, entity.getData());
                        } else {
                            CommodityPay.startActivityInstance(CommodityDetailsActivity.this, "2", goodsId, entity.getData());
                        }
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(CommodityDetailsActivity.this).load(path).into(imageView);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取商品详情
     */
    private void initData() {
        String token = null;
        if (!MyApplication.getToken().equals("")) {
            token = MyApplication.getToken();
        }
        ApiUtil.getApiService().goodsInfodetail(token, goodsId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        entityForSimple = entity.getData();

                        priceType = entityForSimple.getPriceType();
                        tvGoodsName.setText(entityForSimple.getGoodsName().replace("\n", ""));
                        // Glide.with(CommodityDetailsActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566966429785&di=51672df4236f47a0c5d722fd0b910698&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20181103%2F2e411bf1933a4575b821c1f2d854aca6.jpeg").into(ivLongimage);
                        if (priceType.equals("1")) {
                            tvPrice.setText(entityForSimple.getIntegral().replace(".00", "") + "积分");
                            tvIntegral.setVisibility(View.GONE);
                            tvSubmit.setText("立即兑换");
                        } else if (priceType.equals("2")) {
                            tvIntegral.setText("+" + entityForSimple.getIntegral().replace(".00", "") + "积分");
                            tvPrice.setText("￥" + entityForSimple.getPrice().replace(".00", ""));
                            tvPrice.setVisibility(View.VISIBLE);
                            tvSubmit.setText("立即兑换");
                        } else if(priceType.equals("3")){
                            tvPrice.setText("￥" + entityForSimple.getPrice().replace(".00", ""));
                            tvIntegral.setVisibility(View.GONE);
                            tvSubmit.setText("立即购买");
                        }else{
                            tvIntegral.setText("+" + entityForSimple.getIntegral().replace(".00", "") + "钱包");
                            tvPrice.setText("￥" + entityForSimple.getPrice().replace(".00", ""));
                            tvPrice.setVisibility(View.VISIBLE);
                            tvSubmit.setText("立即兑换");
                        }
                        tvAssessPrice.setText("￥" + entity.getData().getAssessPrice());
                        myImageGetter = new MyImageGetter(getBaseContext(), tvLongimage);
                        tvLongimage.setText(Html.fromHtml(entity.getData().getGoodsDetail(), myImageGetter, null));
                        logourl = entity.getData().getLogo();
                        String headImage = entity.getData().getHeadImage();
                        ArrayList arrayList = new Gson().fromJson(headImage, new TypeToken<ArrayList<String>>() {
                        }.getType());
                        initBanner(arrayList);

                        if (entity.getData().getGoodsType().equals("2")) {
                            //换购

                            llShoucang.setVisibility(View.VISIBLE);
                            tvGuzhi.setVisibility(View.VISIBLE);
                            tvAssessPrice.setVisibility(View.VISIBLE);
                            tvAssessPrice.setText("￥" + entity.getData().getAssessPrice());
                            shoucangtype = entity.getData().getCollected();
                            if (shoucangtype.equals("1")) {
                                //未收藏
                                ivShoucang.setImageResource(R.mipmap.qxsc_x);
                                tvShoucang.setText("收藏");
                            } else {
                                ivShoucang.setImageResource(R.mipmap.shoucang);
                                tvShoucang.setText("已收藏");
                            }
                            if (entity.getData().getAssessPrice().equals("0")) {
                                //估值0的时候不显示
                                tvGuzhi.setVisibility(View.GONE);
                                tvAssessPrice.setVisibility(View.GONE);
                            }
                            //不显示库存
                            tvKucun.setVisibility(View.GONE);
                        } else {
                            if (entity.getData().getAssessPrice().equals("0")) {
                                //市场价0的时候不显示
                                tvGuzhi.setVisibility(View.GONE);
                                tvAssessPrice.setVisibility(View.GONE);
                            }
                        }

                        //显示库存(换购和寄拍)
                     /*   if (entity.getData().getGoodsType().equals("1")||entity.getData().getGoodsType().equals("2")) {*/
                            tvKucun.setVisibility(View.VISIBLE);

                            if (Integer.valueOf(entity.getData().getSoldNum()) >= Integer.valueOf(entity.getData().getInventory())) {
                                tvSubmit.setText("已售罄");
                                tvSubmit.setBackgroundColor(Color.parseColor("#AFAFAF"));
                                tvSubmit.setEnabled(false);
                                tvKucun.setText("库存：0件");
                            } else {
                                tvKucun.setText("库存：" + (Integer.valueOf(entity.getData().getInventory()) - Integer.valueOf(entity.getData().getSoldNum())) + "件");
                            }
                     /*   }else{
                            tvKucun.setVisibility(View.GONE);
                        }*/
                        tvSubmit.setVisibility(View.VISIBLE);

                    } else {
                        if (entity.getMsg().equals("该商品已经下架")) {
                            new MiddleDialog(CommodityDetailsActivity.this).setSureListener(() -> {
                                finish();
                            }).show("温馨提示", "该商品已经下架", "取消", "知道了", true);
                        } else {
                            showToast(entity.getMsg());
                        }
                    }
                    categoryId = entity.getData().getCategoryId();
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toLogin(NetRefreshEvent event) {
        if (event != null) {
            initData();
            geturl();
        }
    }


}
