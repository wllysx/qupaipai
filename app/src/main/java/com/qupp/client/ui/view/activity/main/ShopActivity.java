package com.qupp.client.ui.view.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.ui.view.activity.scoreshop.CheckDistrictActivity;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.ui.view.activity.scoreshop.FaddishActivity;
import com.qupp.client.ui.view.activity.scoreshop.MyCheck;
import com.qupp.client.ui.view.activity.scoreshop.MyOrder;
import com.qupp.client.ui.view.activity.scoreshop.ShopCommodityTypeActivity;
import com.qupp.client.ui.view.activity.scoreshop.ShopSearchActivity;
import com.qupp.client.ui.view.fragment.son.MessageFragment;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CommonTabPagerAdapter;
import com.qupp.client.utils.adapter.ShopAdapter1;
import com.qupp.client.utils.dialog.MiddleDialog;
import com.qupp.client.utils.glide.GlideRoundTransform;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.qupp.client.utils.waterfall.StaggeredGridView;
import com.liaoinstan.springview.widget.SpringView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * VIP
 */

public class ShopActivity extends BaseActivity implements CommonTabPagerAdapter.TabPagerListener, AbsListView.OnScrollListener {


    Unbinder unbinder;
    @BindView(R.id.mRecyclerView)
    StaggeredGridView mRecyclerView;
    @BindView(R.id.ll_linear)
    LinearLayout llLinear;
    View headView;
    ViewHolder viewHolder;
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.iv_totop)
    ImageView ivTotop;
    boolean mHasRequestedMore = true;
    public Intent intent;
    public Bundle bundle;
    ShopAdapter1 mAdapter;
    int current = 1, size = 30;
    ArrayList<EntityForSimple> datas = new ArrayList<>();
    ArrayList<EntityForSimple> bannerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        llLinear.setPadding(0, MyApplication.getStateBar(ShopActivity.this), 0, 0);
        setStateColor(false);
        initView();
        initSpringView();
        initData();
        initBanner();
    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                current = 1;
                initData();
                new Handler().postDelayed(() -> {
                    springview.onFinishFreshAndLoad();
                }, 1000);
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();

            }
        });
    }


    private void initView() {
        initHeaderView();
        initAdapter(datas);
    }

    /**
     * 添加列表头部
     */
    private void initHeaderView() {
        headView = getLayoutInflater().inflate(R.layout.item_headerview, null);
        viewHolder = new ViewHolder(headView);
        viewHolder.tvType1.setOnClickListener(listener);
        viewHolder.tvType2.setOnClickListener(listener);
        viewHolder.tvType3.setOnClickListener(listener);
        viewHolder.tvType4.setOnClickListener(listener);
        viewHolder.tvType5.setOnClickListener(listener);
        viewHolder.tvTypemiddle.setOnClickListener(listener);
        mRecyclerView.addHeaderView(headView);

    }

    /**
     * 添加banner
     */
    public void initBanner() {

        ArrayList<String> list = new ArrayList<>();
        ApiUtil.getApiService().pageList("2").enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        bannerList.addAll(entity.getData());
                        for (EntityForSimple image : bannerList) {
                            list.add(image.getImage());
                        }
                        viewHolder.banner.setOnBannerListener(position -> {
                            if (bannerList.size() > 0) {
                                switch (bannerList.get(position).getType()) {
                                    case 1:
                                        //  Toast.makeText(getContext(), "h5" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        H5Web.startActivityInstance(ShopActivity.this,bannerList.get(position).getH5Id());
                                        break;
                                    case 2:
                                        //Toast.makeText(getContext(), "商品详情" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        //NewCommodityDetailsActivity.startActivityInstance(getActivity(), "", bannerList.get(position).getLinkId(), "","");
                                        CommodityList.startActivityInstance(ShopActivity.this,bannerList.get(position).getName(),bannerList.get(position).getLinkId());
                                        break;
                                    case 3:
                                        ShopCommodityTypeActivity.startActivityInstance(ShopActivity.this, bannerList.get(position).getName(), bannerList.get(position).getLinkId(), "", "");
                                        //Toast.makeText(getContext(), "商品分类" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        break;
                                    case 4:
                                        //小程序
                                        break;
                                    case 5:
                                        //拍卖区域

                                        if(bannerList.get(position).getLinkId().equals("9")){
                                            if (MyApplication.getToken().equals("")) {
                                                LoginActivity.startActivityInstance(ShopActivity.this);
                                            } else {
                                                isNewPersion();
                                            }
                                        }else {
                                            //Toast.makeText(getContext(), "app" + bannerList.get(position).getLinkId() + "" + bannerList.get(position).getName(), Toast.LENGTH_LONG).show();
                                            CommodityTypeActivity.startActivityInstance(ShopActivity.this, bannerList.get(position).getName(), bannerList.get(position).getLinkId(), "", "");
                                        }
                                        //Toast.makeText(getContext(), "app" + bannerList.get(position).getLinkId() + "" + bannerList.get(position).getName(), Toast.LENGTH_LONG).show();
                                        break;
                                    case 6:
                                        //无挑战
                                        //Toast.makeText(getContext(), "app" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        break;
                                    case 7:
                                        String url =  bannerList.get(position).getLinkId() + MyApplication.getToken();
                                        ActionH5Web.startActivityInstance(ShopActivity.this, url);
                                        break;
                                    case 8:
                                        CommodityDetailsActivity.startActivityInstance(ShopActivity.this, bannerList.get(position).getLinkId());
                                        break;

                                }
                            }
                        });
                        //设置图片加载器
                        viewHolder.banner.setImageLoader(new GlideImageLoader());
                        //设置图片集合
                        //  String[] images = {"http://img3.duitang.com/uploads/item/201504/02/20150402H2753_JuTG3.thumb.700_0.jpeg", "http://b-ssl.duitang.com/uploads/blog/201312/30/20131230161427_Ye4QQ.jpeg", "http://img.redocn.com/sheji/20150306/weimeiliuguangyinghuashubeijingshipin_3957894.jpg"};
                        viewHolder.banner.setImages(list);
                        //banner设置方法全部调用完毕时最后调用

                        //设置banner样式
                        viewHolder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                        //设置banner动画效果
                        // banner.setBannerAnimation(Transformer.DepthPage);
                        viewHolder.banner.setBannerAnimation(Transformer.Default);
                        //设置自动轮播，默认为true
                        viewHolder.banner.isAutoPlay(true);
                        //设置轮播时间
                        viewHolder.banner.setDelayTime(3000);
                        //设置指示器位置（当banner模式中有指示器时）
                        viewHolder.banner.setIndicatorGravity(BannerConfig.CENTER);
                        viewHolder.banner.start();

                    } else {
                        Toast.makeText(ShopActivity.this, entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });


    }

    private void isNewPersion() {
        ApiUtil.getApiService().isNewPeople(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {

                        if(entity.getData().equals("false")){
                            //非新人
                            MyApplication.isNewPeople = false;
                            new MiddleDialog(ShopActivity.this).setSureListener(() -> {
                                CommodityTypeActivity.startActivityInstance(ShopActivity.this, "新人区", "9", "", "");
                            }).show("温馨提示", "您已不是新人，确认要进入新人区吗?", "取消", "确定", false);
                        }else {
                            //新人
                            MyApplication.isNewPeople = true;
                            CommodityTypeActivity.startActivityInstance(ShopActivity.this, "新人区", "9", "", "");
                        }

                    } else {
                        Toast.makeText(ShopActivity.this, entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }

    /**
     * 获取商品列表
     */
    private void initData() {
        ApiUtil.getApiService().goodsInfolist(null, null, null, size + "", current + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if (entity.getData().getRecords().size() < 30) {
                                mHasRequestedMore = true;
                            } else {
                                mHasRequestedMore = false;
                            }
                        } else {
                            if (entity.getData().getRecords().size() == 0) {
                                showToast("没有更多了");
                                mHasRequestedMore = true;
                            } else {
                                mHasRequestedMore = false;
                            }
                        }
                        datas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
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


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mAdapter = new ShopAdapter1(ShopActivity.this, data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(this);
       /* mRecyclerView.setOnItemClickListener((parent, view, position, id) ->
        {
            if (position > 0) {
                CommodityDetailsActivity.startActivityInstance(ShopActivity.this, data.get(position - 1).getGoodsId());
            }
        });*/
    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, ShopActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFirstId();
        //getFirstVipId();
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

    @OnClick({R.id.back, R.id.tv_search, R.id.iv_totop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_search:
                ShopSearchActivity.startActivityInstance(this);
                break;
            case R.id.iv_totop:
                mRecyclerView.setAdapter(mAdapter);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //Log.d("11111", "onScroll firstVisibleItem:" + firstVisibleItem + " visibleItemCount:" + visibleItemCount + " totalItemCount:" + totalItemCount);
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                //Log.d("11111", "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                current++;
                initData();
            }
        }
        if (firstVisibleItem < 2) {
            springview.setEnableHeader(true);
        } else {
            springview.setEnableHeader(false);
        }
        if(firstVisibleItem>6){
            ivTotop.setVisibility(View.VISIBLE);
        }else{
            ivTotop.setVisibility(View.GONE);
        }
    }

    static class ViewHolder {
        @BindView(R.id.banner)
        Banner banner;
        @BindView(R.id.tv_type1)
        TextView tvType1;
        @BindView(R.id.tv_type2)
        TextView tvType2;
        @BindView(R.id.tv_type3)
        TextView tvType3;
        @BindView(R.id.tv_type4)
        TextView tvType4;
        @BindView(R.id.tv_type5)
        TextView tvType5;
        @BindView(R.id.tv_typemiddle)
        TextView tvTypemiddle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            RequestOptions myOptions = new RequestOptions()
                    .transform(new GlideRoundTransform(10));
            int margin = 30;
            margin = ScreenAdaptive.dp2px(ShopActivity.this, 15);
            imageView.setPadding(margin, 0, margin, 0);

            Glide.with(context)
                    .load(path)
                    .apply(myOptions)
                    .into(imageView);
        }

    }

    private View.OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.tv_type1:
                CheckDistrictActivity.startActivityInstance(ShopActivity.this, "2", 0);
                break;
            case R.id.tv_type2:
                CheckDistrictActivity.startActivityInstance(ShopActivity.this, "1", 0);
                break;
            case R.id.tv_type3:
                if (MyApplication.getToken().equals("")) {
                    MyApplication.toLogin(ShopActivity.this);
                } else {
                    MyCheck.startActivityInstance(ShopActivity.this);
                }
                break;
            case R.id.tv_type4:
                if (MyApplication.getToken().equals("")) {
                    MyApplication.toLogin(ShopActivity.this);
                } else {
                    MyOrder.startActivityInstance(ShopActivity.this);
                }
                break;
            case R.id.tv_type5:
                getId();
                break;
            case R.id.tv_typemiddle:
                CheckDistrictActivity.startActivityInstance(ShopActivity.this, "3", 0);
                break;
        }
    };

    /**
     * 获取系统参数（延迟时间）
     */
    private void getId() {
        ApiUtil.getApiService().syskey("internet_celebrity_category_id").enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.firstCategoryId = entity.getData();
                        FaddishActivity.startActivityInstance(ShopActivity.this,entity.getData());
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }

    private void getFirstId() {
        ApiUtil.getApiService().syskey("internet_celebrity_category_id").enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.firstCategoryId = entity.getData();
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
    private void getFirstVipId() {
        ApiUtil.getApiService().syskey("member_area_category_id").enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.firstvipCategoryId = entity.getData();
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




}
