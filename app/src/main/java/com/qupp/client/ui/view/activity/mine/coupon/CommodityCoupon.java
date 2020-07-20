package com.qupp.client.ui.view.activity.mine.coupon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.MainItemAdapter1;
import com.qupp.client.utils.event.NetRefreshEvent;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.qupp.client.utils.waterfall.StaggeredGridView;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 优惠券指定商品
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class CommodityCoupon extends BaseActivity implements AbsListView.OnScrollListener{

    @BindView(R.id.linear)
    View linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.mRecyclerView)
    StaggeredGridView mRecyclerView;
    @BindView(R.id.springview)
    SpringView springview;
    MainItemAdapter1 mAdapter;
    List<EntityForSimple> mDatas = new ArrayList<>();
    String title = "",goodsId="";
    int current = 1, size = 30;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    private boolean mHasRequestedMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commoditytype);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(CommodityCoupon.this), 0, 0);
        setStateColor(false);
        title = getIntent().getStringExtra("title");
        goodsId = getIntent().getStringExtra("goodsId");

        initView();

    }


    private void initView() {
        tvTitle.setText(title);
        tvDefault.setText("暂无数据");
        ivDefault.setImageResource(R.mipmap.icon_default3);
        initAdapter(mDatas);
        initSpringView();
        initData();
    }

    private void initAdapter(List<EntityForSimple> data) {
       mAdapter = new MainItemAdapter1(this,data);
       mRecyclerView.setAdapter(mAdapter);
       mRecyclerView.setOnScrollListener(this);

    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                current = 1;
                initData();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                current++;
                initData();
            }
        });
    }

    public static void startActivityInstance(Context activity, String title, String goodsId) {
        activity.startActivity(new Intent(activity, CommodityCoupon.class)
                .putExtra("title", title)
                .putExtra("goodsId",goodsId));
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

    /**
     * 获取商品列表
     */
    private void initData() {
        try {
            ApiUtil.getApiService().listFatherCategoryArray(MyApplication.getToken(),current + "", size + "",goodsId).enqueue(new Callback<MessageForSimple>() {
                @Override
                public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                    MessageForSimple entity = response.body();
                    try {
                        if (entity.getCode().equals("0")) {
                            if (current == 1) {
                                mDatas.clear();

                                if(entity.getData().getRecords().size()==0){
                                    llDefault.setVisibility(View.VISIBLE);
                                }else{
                                    llDefault.setVisibility(View.GONE);
                                }
                                if(entity.getData().getRecords().size()<30) {
                                    mHasRequestedMore = true;
                                }else{
                                    mHasRequestedMore = false;
                                }
                            }else{
                                if (entity.getData().getRecords().size() == 0) {
                                    Toast.makeText(CommodityCoupon.this, "没有更多了", Toast.LENGTH_LONG).show();
                                    mHasRequestedMore = true;
                                }else{
                                    mHasRequestedMore = false;
                                }
                            }
                            mDatas.addAll(entity.getData().getRecords());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            llDefault.setVisibility(View.VISIBLE);
                            showToast(entity.getMsg());
                        }
                    } catch (Exception e) {
                    }

                }

                @Override
                public void onFailure(Call<MessageForSimple> call, Throwable t) {
                }
            });

        }catch (Exception e){

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
               // Log.d("11111", "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                current++;
                initData();
            }
        }
        if(firstVisibleItem<4){
            springview.setEnableHeader(true);
        }else{
            springview.setEnableHeader(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toLogin(NetRefreshEvent event) {
        if (event != null) {
            current = 1;
            initData();
        }
    }
}
