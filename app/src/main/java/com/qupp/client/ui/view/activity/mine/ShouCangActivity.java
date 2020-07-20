package com.qupp.client.ui.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.main.ShopActivity;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.ShoucangAdapter1;
import com.qupp.client.utils.view.springview.DefaultFooter;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的收藏
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class ShouCangActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.springview)
    SpringView springview;
    ShoucangAdapter1 mAdapter;
    ArrayList<EntityForSimple> datas = new ArrayList<>();
    int current = 1, size = 30;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.listview)
    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoucang);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(ShouCangActivity.this), 0, 0);
        setStateColor(false);
        initView();
        initSpringView();
        initData();

    }


    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
        springview.setFooter(new DefaultFooter(this));
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


    private void initView() {
        tvTitle.setText("我的收藏");
        initAdapter(datas);
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
       /* mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new ShouCangAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 1);
        // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            showToast("111");
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.hide_view) {
                delete(position);
            } else if (view.getId() == R.id.swipemenu) {
                showToast("111");
                CommodityDetailsActivity.startActivityInstance(ShouCangActivity.this, data.get(position).getGoodsId(), data.get(position).getPriceType(), data.get(position).getPrice(), data.get(position).getIntegral(), data.get(position).getGoodsName());
            }
        });

        MyApplication.setMaxFlingVelocity(mRecyclerView);*/
        mAdapter = new ShoucangAdapter1(this,datas);
       /* listview.setOnItemClickListener((parent, view, position, id) -> {
            CommodityDetailsActivity.startActivityInstance(ShouCangActivity.this, data.get(position).getGoodsId(), data.get(position).getPriceType(), data.get(position).getPrice(), data.get(position).getIntegral(), data.get(position).getGoodsName());

        });*/
        mAdapter.setClick(new ShoucangAdapter1.Click() {
            @Override
            public void listener(int position) {
                CommodityDetailsActivity.startActivityInstance(ShouCangActivity.this, data.get(position).getGoodsId());
            }
        });
        mAdapter.setClick1(new ShoucangAdapter1.Click1() {
            @Override
            public void listener(int position) {
                delete(position);
            }
        });
        listview.setAdapter(mAdapter);

    }

    public void delete(int position) {
        ApiUtil.getApiService().collectGoodsInfocancel(MyApplication.getToken(), datas.get(position).getGoodsId()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        //取消收藏成功
                        initData();
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


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, ShouCangActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        current = 1;
        initData();
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


    @OnClick({R.id.back, R.id.tv_goshop})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_goshop:
                ShopActivity.startActivityInstance(ShouCangActivity.this);
                break;
        }
    }

    private void initData() {
        ApiUtil.getApiService().collectGoodsInfolist(MyApplication.getToken(), current + "", size + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if (entity.getData() == null || entity.getData().getRecords().size() == 0) {
                                llDefault.setVisibility(View.VISIBLE);
                            } else {
                                llDefault.setVisibility(View.GONE);
                            }
                        }
                        datas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                        tvCount.setText(entity.getData().getTotal());
                    } else {
                        llDefault.setVisibility(View.VISIBLE);
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
                llDefault.setVisibility(View.VISIBLE);
            }
        });
    }




}
