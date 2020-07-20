package com.qupp.client.ui.view.activity.main;

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
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.MainItemAdapter2;
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
 * 分类商品列表
 */

public class CommodityTypeActivity extends BaseActivity implements AbsListView.OnScrollListener{

    @BindView(R.id.linear)
    View linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.mRecyclerView)
    StaggeredGridView mRecyclerView;
    @BindView(R.id.springview)
    SpringView springview;
    MainItemAdapter2 mAdapter;
    List<EntityForSimple> mDatas = new ArrayList<>();
    String title = "";
    int current = 1, size = 30;
    String displayArea="",isGiveIntegral="",isLimitPrice="";
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
        linear.setPadding(0, MyApplication.getStateBar(CommodityTypeActivity.this), 0, 0);
        setStateColor(false);
        title = getIntent().getStringExtra("title");
        displayArea = getIntent().getStringExtra("displayArea");
        isGiveIntegral = getIntent().getStringExtra("isGiveIntegral");
        isLimitPrice = getIntent().getStringExtra("isLimitPrice");

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
       mAdapter = new MainItemAdapter2(this,data);
       mRecyclerView.setAdapter(mAdapter);
       mRecyclerView.setOnScrollListener(this);
       mRecyclerView.setOnItemClickListener((parent, view, position, id) -> NewCommodityDetailsActivity.startActivityInstance(CommodityTypeActivity.this, data.get(position).getStatus(), data.get(position).getAuctionId(), data.get(position).getTopPrice(),data.get(position).getGoodsName()));

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

    public static void startActivityInstance(Context activity, String title, String displayArea, String isGiveIntegral, String isLimitPrice) {
        activity.startActivity(new Intent(activity, CommodityTypeActivity.class)
                .putExtra("title", title)
                .putExtra("displayArea", displayArea)
                .putExtra("isGiveIntegral", isGiveIntegral)
                .putExtra("isLimitPrice", isLimitPrice));

    }

    @Override
    protected void onResume() {
        super.onResume();
        isNewPersion();
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
                        }else {
                            //新人
                            MyApplication.isNewPeople = true;
                        }

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
            ApiUtil.getApiService().listFatherCategory(current + "", size + "", displayArea.equals("")?null:displayArea,isGiveIntegral.equals("")?null:isGiveIntegral,isLimitPrice.equals("")?null:isLimitPrice).enqueue(new Callback<MessageForSimple>() {
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
                                    Toast.makeText(CommodityTypeActivity.this, "没有更多了", Toast.LENGTH_LONG).show();
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
