package com.qupp.client.ui.view.fragment.main;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.MainActivity;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.ui.view.activity.main.ActionH5Web;
import com.qupp.client.ui.view.activity.main.CommodityList;
import com.qupp.client.ui.view.activity.main.CommodityTypeActivity;
import com.qupp.client.ui.view.activity.main.H5Web;
import com.qupp.client.ui.view.activity.main.MessageActivity;
import com.qupp.client.ui.view.activity.main.MyActionListActivity;
import com.qupp.client.ui.view.activity.main.ShopActivity;
import com.qupp.client.ui.view.activity.main.TelephoneRechargeActivity;
import com.qupp.client.ui.view.activity.main.VipActivity;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.ui.view.activity.scoreshop.ShopCommodityTypeActivity;
import com.qupp.client.ui.view.fragment.son.MainItemFragment1;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleB;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.utils.DateUtils;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.ScrollSpeedLinearLayoutManger;
import com.qupp.client.utils.adapter.CommonTabPagerAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.TabLayoutAdapter;
import com.qupp.client.utils.adapter.commodityType1Adapter;
import com.qupp.client.utils.adapter.commodityTypeAdapter;
import com.qupp.client.utils.dialog.MiddleDialog;
import com.qupp.client.utils.event.MainPageRefresh;
import com.qupp.client.utils.event.NetRefreshEvent;
import com.qupp.client.utils.event.ToTopEvent;
import com.qupp.client.utils.glide.GlideRoundTransform;
import com.qupp.client.utils.view.headerviewpager.HeaderViewPager;
import com.qupp.client.utils.view.headerviewpager.HeaderViewPagerFragment;
import com.qupp.client.utils.view.springview.MainHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * 首页
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 13:51
 */


@SuppressLint("all")
public class FragmentMainNewSpecial extends Fragment implements CommonTabPagerAdapter.TabPagerListener {

    View rootView;
    Unbinder unbinder;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    CommonTabPagerAdapter adapter;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.banner1)
    Banner banner1;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_linear)
    LinearLayout llLinear;
    int stateBarHeight = 0;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    commodityTypeAdapter mAdapter;
    @BindView(R.id.springview)
    SpringView springview;

    ArrayList<EntityForSimple> bannerList = new ArrayList<>();
    ArrayList<EntityForSimple> bannerList1 = new ArrayList<>();
    ArrayList<EntityForSimple> data = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    ArrayList<HeaderViewPagerFragment> fragments = new ArrayList<>();
    @BindView(R.id.ll_topview)
    LinearLayout llTopview;
    boolean isToTop = false;
    @BindView(R.id.mRecyclerView1)
    RecyclerView mRecyclerView1;
    commodityType1Adapter mAdapter1;
    ArrayList<EntityForSimple> data1 = new ArrayList<>();
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.iv_totop)
    ImageView ivTotop;

    TabLayoutAdapter mTimeAdapter;
    @BindView(R.id.tablayout)
    RecyclerView tablayout;
    ArrayList<EntityForSimple> tabLists = new ArrayList<>();
    ScrollSpeedLinearLayoutManger manager;
    @BindView(R.id.guide_ll_point)
    LinearLayout guideLlPoint;
    @BindView(R.id.ll_action)
    LinearLayout llAction;
    private ImageView[] ivPointArray;

    @BindView(R.id.guide_ll_point1)
    LinearLayout guideLlPoint1;
    private ImageView[] ivPointArray1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_special, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        stateBarHeight = MyApplication.getStateBar(getActivity());
        llLinear.setPadding(0, stateBarHeight, 0, 0);
        ((MainActivity) getActivity()).setStateColor(false);
        titles.add("今日热拍");
        fragments.add(MainItemFragment1.newInstance());

        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));
        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer(fragments.get(position));
            }
        });
        adapter = new CommonTabPagerAdapter(getActivity().getSupportFragmentManager(), 1, titles, getActivity());
        adapter.setListener(this);
        viewpager.setAdapter(adapter);

        initTabLayout();
        initView();
        initCategory();
        initBanner();
        initBanner1();
        initSpringView();
        initTabData();
        return rootView;
    }

    public void initView() {
        tvTitle.setText("趣拍拍");
        data.clear();
        data.add(new EntityForSimple());
        data.add(new EntityForSimple());
        data.add(new EntityForSimple());
        data.add(new EntityForSimple());
        data.add(new EntityForSimple());
        initAdapter(data);
        data1.clear();
        data1.add(new EntityForSimple());
        data1.add(new EntityForSimple());
        data1.add(new EntityForSimple());
        data1.add(new EntityForSimple());
        data1.add(new EntityForSimple());
        initAdapter1(data1);

    }

    private void initTabLayout() {

        manager = new ScrollSpeedLinearLayoutManger(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tablayout.setLayoutManager(manager);
        mTimeAdapter = new TabLayoutAdapter(tabLists);
        mTimeAdapter.bindToRecyclerView(tablayout);
        mTimeAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (EntityForSimple entity : tabLists) {
                entity.setChecked(false);
            }
            tabLists.get(position).setChecked(true);
            mTimeAdapter.notifyDataSetChanged();

          /*  LinearSmoothScroller s1 = new TopSmoothScroller(getActivity());
            s1.setTargetPosition(position);
            manager.startSmoothScroll(s1);*/

            tablayout.smoothScrollToPosition((position + 5) < tabLists.size() ? (position + 4) : tabLists.size() - 1);
            if(position==tabLists.size()-1){
                //后日
                EventBus.getDefault().post(new MainPageRefresh("3", tabLists.get(position).getEndTime()));
            }else if(position==tabLists.size()-2){
                //明日
                EventBus.getDefault().post(new MainPageRefresh("2", tabLists.get(position).getEndTime()));
            }else {
                EventBus.getDefault().post(new MainPageRefresh(tabLists.get(position).getStartTime(), tabLists.get(position).getEndTime()));
            }
            Log.d("startendtime", tabLists.get(position).getStartTime() + "," + tabLists.get(position).getEndTime());
        });

    }

    /**
     * 获取时间点
     */
    private void initTabData() {
        ApiUtil.getApiService().hoursToday().enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tabLists.clear();
                        tabLists.addAll(entity.getData());
                        //明日 后日
                        EntityForSimple day1 = new EntityForSimple();
                        day1.setStartTime(DateUtils.getStartTime(1));
                        day1.setEndTime(DateUtils.getEndtime(1));
                        tabLists.add(day1);
                        EntityForSimple day2 = new EntityForSimple();
                        day2.setStartTime(DateUtils.getStartTime(2));
                        day2.setEndTime(DateUtils.getEndtime(2));
                        tabLists.add(day2);

                        //判断刷新后选中的位置（没有热拍中的定位到第一个即将开始）
                        int current = 0;
                        for (int i = 0; i < tabLists.size(); i++) {
                            if (tabLists.get(i).getCurrentStatus().equals("2")) {
                                current = i;
                                tabLists.get(i).setChecked(true);
                                break;
                            } else if (tabLists.get(i).getCurrentStatus().equals("3")) {
                                //即将开始
                                if (i == 0) {
                                    //第一条就是未开始
                                    current = i;
                                    tabLists.get(i).setChecked(true);
                                    break;
                                } else {
                                    if (tabLists.get(i - 1).getCurrentStatus().equals("1")) {
                                        //即将开始的前一条是已结束 定位到当前未开始
                                        current = i;
                                        tabLists.get(i).setChecked(true);
                                        break;
                                    }
                                }
                            }
                        }
                        mTimeAdapter.notifyDataSetChanged();
                        manager.scrollToPositionWithOffset(current, 0);
                        if(current==tabLists.size()-1){
                            //后日
                            EventBus.getDefault().post(new MainPageRefresh("3", tabLists.get(current).getEndTime()));
                        }else if(current==tabLists.size()-2){
                            //明日
                            EventBus.getDefault().post(new MainPageRefresh("2", tabLists.get(current).getEndTime()));
                        }else {
                            EventBus.getDefault().post(new MainPageRefresh(tabLists.get(current).getStartTime(), tabLists.get(current).getEndTime()));
                        }
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

    /**
     * 分类
     */
    private void initCategory() {

        ApiUtil.getApiService().pageList("3").enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        data.clear();

                        if (entity.getData().size() <= 4) {
                            data.addAll(entity.getData());
                        } else {
                            for (int i = 0; i < 4; i++) {
                                data.add(entity.getData().get(i));
                            }

                        }
                        data.add(new EntityForSimple());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

    private void initSpringView() {
        scrollableLayout.setOnScrollListener((currentY, maxY) -> {
            if (currentY == 0) {
                springview.setEnable(true);
            } else {
                springview.setEnable(false);
            }
            if (currentY < llTopview.getHeight()) {
                if (!isToTop) {
                    EventBus.getDefault().post(new ToTopEvent(viewpager.getCurrentItem()));
                    isToTop = true;
                    ivTotop.setVisibility(View.GONE);
                }
            } else {
                isToTop = false;
                ivTotop.setVisibility(View.VISIBLE);
            }
        });
        springview.setHeader(new MainHeader(getActivity()));
        // springview.setFooter(new DefaultFooter(getActivity()));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                try {
                    initTabData();
                    initBanner();
                    initBanner1();
                    initCategory();
                    mAdapter1.notifyDataSetChanged();
                    if (springview != null) {
                        springview.onFinishFreshAndLoad();
                    }
                    new Handler().postDelayed(() -> {
                        if (springview != null) {
                            springview.onFinishFreshAndLoad();
                        }
                    }, 2000);
                } catch (Exception e) {
                }
            }

            @Override
            public void onLoadmore() {
                if (springview != null) {
                    springview.onFinishFreshAndLoad();
                }
            }
        });
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        mAdapter = new commodityTypeAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 5; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getActivity(), 0); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            if (DoubleClick.isFastDoubleClick()) {
                return;
            }

            if (position == data.size() - 1) {
                if (MyApplication.getToken().equals("")) {
                    LoginActivity.startActivityInstance(getActivity());
                } else {
                    isNewPersion();
                }
            } else {
                CommodityTypeActivity.startActivityInstance(getActivity(), ((TextView) view.findViewById(R.id.tv_name)).getText().toString(), data.get(position).getLinkId(), "", "");
            }
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    private void isNewPersion() {
        ApiUtil.getApiService().isNewPeople(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {

                        if (entity.getData().equals("false")) {
                            //非新人
                            MyApplication.isNewPeople = false;
                            new MiddleDialog(getActivity()).setSureListener(() -> {
                                CommodityTypeActivity.startActivityInstance(getActivity(), "新人区", "9", "", "");
                            }).show("温馨提示", "您已不是新人，确认要进入新人区吗", "取消", "确定", false);
                        } else {
                            //新人
                            MyApplication.isNewPeople = true;
                            CommodityTypeActivity.startActivityInstance(getActivity(), "新人区", "9", "", "");
                        }

                    } else {
                        Toast.makeText(getActivity(), entity.getMsg(), Toast.LENGTH_LONG).show();
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
     * 设置RecyclerView属性
     */
    private void initAdapter1(List<EntityForSimple> data) {
        mRecyclerView1.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        mAdapter1 = new commodityType1Adapter(data);
        mAdapter1.bindToRecyclerView(mRecyclerView1);
        int spanCount = 5; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getActivity(), 0); // 50px
        boolean includeEdge = false;
        mRecyclerView1.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter1.setOnItemClickListener((adapter, view, position) -> {
            switch (position) {
                case 0:
                    //超值拍
                    CommodityTypeActivity.startActivityInstance(getActivity(), ((TextView) view.findViewById(R.id.tv_name)).getText().toString(), "8", "", "");
                    break;
                case 1:
                    //限价拍
                    VipActivity.startActivityInstance(getActivity());
                    break;
                case 2:
                    ShopActivity.startActivityInstance(getActivity());
                    break;
                case 3:
                    //游戏
                    //getGameurl();
                    MyActionListActivity.startActivityInstance(getActivity(),"5");
                    break;
                case 4:
                    if (MyApplication.getToken().equals("")) {
                        LoginActivity.startActivityInstance(getActivity());
                    } else {
                        TelephoneRechargeActivity.startActivityInstance(getActivity());
                    }
                    break;
            }
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView1);
    }

    private void getGameurl() {
        ApiUtil.getApiService().syskey("game_enter_key").enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        String url = entity.getData();
                        url = url + MyApplication.getToken();
                        ActionH5Web.startActivityInstance(getActivity(), url);
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


    public void initBanner1() {

        float height = ((float) ScreenAdaptive.getWidthSizeByAllSize(getActivity())) * 110 / 690;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) banner1.getLayoutParams();
        linearParams.height = (int) height;
        banner1.setLayoutParams(linearParams);

        ArrayList<String> list = new ArrayList<>();
        ApiUtil.getApiService().pageList("4").enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if(entity.getData().size()>0){
                            llAction.setVisibility(View.VISIBLE);
                        }else{
                            llAction.setVisibility(View.GONE);
                        }

                        bannerList1.clear();
                        list.clear();
                        bannerList1.addAll(entity.getData());
                        for (EntityForSimple image : bannerList1) {
                            list.add(image.getMinImage());
                        }

                        banner1.setOnBannerListener(position -> {
                            if (bannerList1.size() > 0) {
                                switch (bannerList1.get(position).getType()) {
                                    case 1:
                                        //Toast.makeText(getContext(), "h5" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        H5Web.startActivityInstance(getActivity(),bannerList1.get(position).getH5Id());
                                        break;
                                    case 2:
                                        //Toast.makeText(getContext(), "商品详情" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        //NewCommodityDetailsActivity.startActivityInstance(getActivity(), "", bannerList.get(position).getLinkId(), "","");
                                        CommodityList.startActivityInstance(getActivity(), bannerList1.get(position).getName(), bannerList1.get(position).getLinkId());
                                        break;
                                    case 3:
                                        ShopCommodityTypeActivity.startActivityInstance(getActivity(), bannerList1.get(position).getName(), bannerList1.get(position).getLinkId(), "", "");
                                        //Toast.makeText(getContext(), "商品分类" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        break;
                                    case 4:
                                        //小程序
                                        break;
                                    case 5:
                                        //拍卖区域
                                        if (bannerList1.get(position).getLinkId().equals("9")) {
                                            if (MyApplication.getToken().equals("")) {
                                                LoginActivity.startActivityInstance(getActivity());
                                            } else {
                                                isNewPersion();
                                            }
                                        } else {
                                            //Toast.makeText(getContext(), "app" + bannerList.get(position).getLinkId() + "" + bannerList.get(position).getName(), Toast.LENGTH_LONG).show();
                                            CommodityTypeActivity.startActivityInstance(getActivity(), bannerList1.get(position).getName(), bannerList1.get(position).getLinkId(), "", "");
                                        }
                                        break;
                                    case 6:
                                        //无挑战
                                        //Toast.makeText(getContext(), "app" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        break;
                                    case 7:
                                        String url = bannerList1.get(position).getLinkId() + MyApplication.getToken();
                                        ActionH5Web.startActivityInstance(getActivity(), url);
                                        break;
                                    case 8:
                                        CommodityDetailsActivity.startActivityInstance(getActivity(), bannerList1.get(position).getLinkId());
                                        break;
                                }
                            }
                        });
                        //设置图片加载器
                        banner1.setImageLoader(new GlideImageLoader1());
                        //设置图片集合
                        //  String[] images = {"http://img3.duitang.com/uploads/item/201504/02/20150402H2753_JuTG3.thumb.700_0.jpeg", "http://b-ssl.duitang.com/uploads/blog/201312/30/20131230161427_Ye4QQ.jpeg", "http://img.redocn.com/sheji/20150306/weimeiliuguangyinghuashubeijingshipin_3957894.jpg"};
                        banner1.setImages(list);
                        //banner设置方法全部调用完毕时最后调用

                        //设置banner样式
                        banner1.setBannerStyle(BannerConfig.NOT_INDICATOR);
                        //设置banner动画效果
                        // banner.setBannerAnimation(Transformer.DepthPage);
                        banner1.setBannerAnimation(Transformer.Default);

                        //设置自动轮播，默认为true
                        banner1.isAutoPlay(false);
                        //设置轮播时间
                        banner1.setDelayTime(3000);
                        //设置指示器位置（当banner模式中有指示器时）
                        banner1.setIndicatorGravity(BannerConfig.CENTER);

                        banner1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                int length = bannerList1.size();
                                try {
                                    for (int i = 0; i < length; i++) {
                                        ivPointArray1[position].setBackgroundResource(R.mipmap.icon_main_selected_is);
                                        if (position != i) {
                                            ivPointArray1[i].setBackgroundResource(R.mipmap.icon_main_selected_un3);
                                        }
                                    }
                                } catch (Exception e) {

                                }

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        banner1.start();

                        initPoint1();
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });

    }

    /**
     * 加载底部圆点
     */
    private void initPoint1() {
        guideLlPoint1.removeAllViews();

        ImageView iv_point;
        ivPointArray1 = new ImageView[bannerList1.size()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 0, 10, 0);
        for (int i = 0; i < bannerList1.size(); i++) {
            iv_point = new ImageView(getActivity());
            iv_point.setLayoutParams(layoutParams);
            ivPointArray1[i] = iv_point;
            if (i == 0) {
                iv_point.setBackgroundResource(R.mipmap.icon_main_selected_is);
            } else {
                iv_point.setBackgroundResource(R.mipmap.icon_main_selected_un3);
            }
            guideLlPoint1.addView(ivPointArray1[i]);
        }
    }

    public void initBanner() {
        ArrayList<String> list = new ArrayList<>();
        ApiUtil.getApiService().pageList("1").enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        bannerList.clear();
                        list.clear();
                        bannerList.addAll(entity.getData());
                        for (EntityForSimple image : bannerList) {
                            list.add(image.getImage());
                        }

                        banner.setOnBannerListener(position -> {
                            if (bannerList.size() > 0) {
                                switch (bannerList.get(position).getType()) {
                                    case 1:
                                        //Toast.makeText(getContext(), "h5" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        H5Web.startActivityInstance(getActivity(),bannerList.get(position).getH5Id());
                                        break;
                                    case 2:
                                        //Toast.makeText(getContext(), "商品详情" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        //NewCommodityDetailsActivity.startActivityInstance(getActivity(), "", bannerList.get(position).getLinkId(), "","");
                                        CommodityList.startActivityInstance(getActivity(), bannerList.get(position).getName(), bannerList.get(position).getLinkId());
                                        break;
                                    case 3:
                                        ShopCommodityTypeActivity.startActivityInstance(getActivity(), bannerList.get(position).getName(), bannerList.get(position).getLinkId(), "", "");
                                        //Toast.makeText(getContext(), "商品分类" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        break;
                                    case 4:
                                        //小程序
                                        break;
                                    case 5:
                                        //拍卖区域
                                        if (bannerList.get(position).getLinkId().equals("9")) {
                                            if (MyApplication.getToken().equals("")) {
                                                LoginActivity.startActivityInstance(getActivity());
                                            } else {
                                                isNewPersion();
                                            }
                                        } else {
                                            //Toast.makeText(getContext(), "app" + bannerList.get(position).getLinkId() + "" + bannerList.get(position).getName(), Toast.LENGTH_LONG).show();
                                            CommodityTypeActivity.startActivityInstance(getActivity(), bannerList.get(position).getName(), bannerList.get(position).getLinkId(), "", "");
                                        }
                                        break;
                                    case 6:
                                        //无挑战
                                        //Toast.makeText(getContext(), "app" + bannerList.get(position).getLinkId(), Toast.LENGTH_LONG).show();
                                        break;
                                    case 7:
                                        String url = bannerList.get(position).getLinkId() + MyApplication.getToken();
                                        ActionH5Web.startActivityInstance(getActivity(), url);
                                        break;
                                    case 8:
                                        CommodityDetailsActivity.startActivityInstance(getActivity(), bannerList.get(position).getLinkId());
                                        break;
                                }
                            }
                        });
                        //设置图片加载器
                        banner.setImageLoader(new GlideImageLoader());
                        //设置图片集合
                        //  String[] images = {"http://img3.duitang.com/uploads/item/201504/02/20150402H2753_JuTG3.thumb.700_0.jpeg", "http://b-ssl.duitang.com/uploads/blog/201312/30/20131230161427_Ye4QQ.jpeg", "http://img.redocn.com/sheji/20150306/weimeiliuguangyinghuashubeijingshipin_3957894.jpg"};
                        banner.setImages(list);
                        //banner设置方法全部调用完毕时最后调用

                        //设置banner样式
                        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                        //设置banner动画效果
                        // banner.setBannerAnimation(Transformer.DepthPage);
                        banner.setBannerAnimation(Transformer.Default);

                        //设置自动轮播，默认为true
                        banner.isAutoPlay(true);
                        //设置轮播时间
                        banner.setDelayTime(3000);
                        //设置指示器位置（当banner模式中有指示器时）
                        banner.setIndicatorGravity(BannerConfig.CENTER);

                        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                int length = bannerList.size();
                                try {
                                    for (int i = 0; i < length; i++) {
                                        ivPointArray[position].setBackgroundResource(R.mipmap.icon_main_selected_is);
                                        if (position != i) {
                                            ivPointArray[i].setBackgroundResource(R.mipmap.icon_main_selected_un);
                                        }
                                    }
                                } catch (Exception e) {

                                }

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        banner.start();

                        initPoint();
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });

    }

    /**
     * 加载底部圆点
     */
    private void initPoint() {
        guideLlPoint.removeAllViews();

        ImageView iv_point;
        ivPointArray = new ImageView[bannerList.size()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 0, 10, 0);
        for (int i = 0; i < bannerList.size(); i++) {
            iv_point = new ImageView(getActivity());
            iv_point.setLayoutParams(layoutParams);
            ivPointArray[i] = iv_point;
            if (i == 0) {
                iv_point.setBackgroundResource(R.mipmap.icon_main_selected_is);
            } else {
                iv_point.setBackgroundResource(R.mipmap.icon_main_selected_un);
            }
            guideLlPoint.addView(ivPointArray[i]);
        }
    }


    public static FragmentMainNewSpecial newInstance() {
        FragmentMainNewSpecial fragment = new FragmentMainNewSpecial();
        return fragment;
    }


    public void refreshBar() {
        try {
            ((MainActivity) getActivity()).setStateColor(true);
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MyApplication.getToken().equals("")) {
            pushMessageisRead();
        }
    }


    @Override
    public Fragment getFragment(int position) {
        return fragments.get(position);
    }


    @OnClick({R.id.iv_message, R.id.iv_totop, R.id.tv_more})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_message:
                if (MyApplication.getToken().equals("")) {
                    MyApplication.toLogin(getActivity());
                } else {
                    MessageActivity.startActivityInstance(getActivity());
                    ivMessage.setImageResource(R.mipmap.icon_main_message);
                }

                break;
            case R.id.iv_totop:
                if (viewpager.getCurrentItem() == 0) {
                    EventBus.getDefault().post(new ToTopEvent(1));
                } else {
                    EventBus.getDefault().post(new ToTopEvent(0));
                }
                scrollToPosition(0, 0);
                break;
            case R.id.tv_more:
                MyActionListActivity.startActivityInstance(getActivity(),"4");
                break;
        }
    }

    public void scrollToPosition(int x, int y) {

        ObjectAnimator xTranslate = ObjectAnimator.ofInt(scrollableLayout, "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(scrollableLayout, "scrollY", y);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(400L);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            RequestOptions myOptions = new RequestOptions()
                    .transform(new GlideRoundTransform(15));
            int margin = 30;
            margin = ScreenAdaptive.dp2px(getContext(), 10);
            imageView.setPadding(margin, 0, margin, 0);

            Glide.with(context)
                    .load(path)
                    .apply(myOptions)
                    .into(imageView);
        }

    }

    public class GlideImageLoader1 extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            RequestOptions myOptions = new RequestOptions()
                    .transform(new GlideRoundTransform(15));
            int margin = 30;
            margin = ScreenAdaptive.dp2px(getContext(), 10);
            imageView.setPadding(margin, 0, margin, 0);

            Glide.with(context)
                    .load(path)
                    .apply(myOptions)
                    .into(imageView);
        }
    }


    private void pushMessageisRead() {
        ApiUtil.getApiService().pushMessageisRead(MyApplication.getToken(), MyApplication.getMessageTime()).enqueue(new Callback<EntityForSimpleB>() {
            @Override
            public void onResponse(Call<EntityForSimpleB> call, Response<EntityForSimpleB> response) {
                EntityForSimpleB entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.isData()) {
                            ivMessage.setImageResource(R.mipmap.icon_xiaoxioint);
                        } else {
                            ivMessage.setImageResource(R.mipmap.icon_main_message);
                        }
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleB> call, Throwable t) {
            }
        });
    }

    public static class TopSmoothScroller extends LinearSmoothScroller {
        TopSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getHorizontalSnapPreference() {
            return SNAP_TO_START;//具体见源码注释
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;//具体见源码注释
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toLogin(NetRefreshEvent event) {
        if (event != null) {
            initTabData();
            initBanner();
            initBanner1();
            initCategory();
            mAdapter1.notifyDataSetChanged();
        }
    }


}
