package com.qupp.client.ui.view.activity.scoreshop;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.HotSearchAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.ShopAdapter1;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.qupp.client.utils.waterfall.StaggeredGridView;
import com.liaoinstan.springview.widget.SpringView;

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
 * 积分商城搜索
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class ShopSearchActivity extends BaseActivity implements AbsListView.OnScrollListener {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.mRecyclerView)
    StaggeredGridView mRecyclerView;
    List<EntityForSimple> datas = new ArrayList<>();
    ShopAdapter1 mAdapter;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rcv_hot)
    RecyclerView rcvHot;
    HotSearchAdapter searchAdapter;
    ArrayList<EntityForSimple> searchDatas = new ArrayList<>();
    int current = 1, size = 30;
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.ll_hot)
    LinearLayout llHot;
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
        setContentView(R.layout.activity_shopsearch);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        initSpringView();
        initView();
        initSearch();
    }

    private void initView() {
        /*假数据*/
        initAdapter(datas);
        initSearchAdapter(searchDatas);
        getSearchHot();
        tvDefault.setText("暂无搜索内容");
        ivDefault.setImageResource(R.mipmap.icon_default9);
    }


    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
        // springview.setFooter(new DefaultFooter(this));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                try {
                    current = 1;
                    searchData(etSearch.getText().toString());
                    new Handler().postDelayed(() -> {
                        springview.onFinishFreshAndLoad();
                    }, 1000);
                }catch (Exception e){
                }
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();

            }
        });
    }

    private void initSearch() {
        etSearch.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (etSearch.getText().toString().equals("")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        current = 1;
                        datas.clear();
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setVisibility(View.GONE);
                        llHot.setVisibility(View.VISIBLE);
                        llDefault.setVisibility(View.GONE);
                    }
                }, 1000);

            } else {
                searchData(etSearch.getText().toString());
            }

            return false;
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            current = 1;
                            datas.clear();
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setVisibility(View.GONE);
                            llHot.setVisibility(View.VISIBLE);
                            llDefault.setVisibility(View.GONE);
                        }
                    }, 1000);
                } else {
                    searchData(etSearch.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void searchData(String goodsName) {
        ApiUtil.getApiService().goodsInfolist(goodsName, "1", size + "", current + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if (entity.getData().getRecords().size() < 30) {
                                mHasRequestedMore = true;
                                if(entity.getData().getRecords().size()==0){
                                    llDefault.setVisibility(View.VISIBLE);
                                }else{
                                    llDefault.setVisibility(View.GONE);
                                }

                            } else {
                                mHasRequestedMore = false;
                            }
                        } else {
                            if (entity.getData().getRecords().size() == 0) {
                                mHasRequestedMore = true;
                            } else {
                                mHasRequestedMore = false;
                            }
                        }
                        datas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setVisibility(View.VISIBLE);
                        llHot.setVisibility(View.GONE);

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
        mAdapter = new ShopAdapter1(ShopSearchActivity.this, data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(this);
        mRecyclerView.setOnItemClickListener((parent, view, position, id) ->
        {
            CommodityDetailsActivity.startActivityInstance(ShopSearchActivity.this, data.get(position).getGoodsId());
        });

    }


    /**
     * 设置RecyclerView属性
     */
    private void initSearchAdapter(List<EntityForSimple> data) {
        rcvHot.setLayoutManager(new GridLayoutManager(ShopSearchActivity.this, 4));
        searchAdapter = new HotSearchAdapter(data);
        searchAdapter.bindToRecyclerView(rcvHot);
        int spanCount = 4; // 2 columns
        int spacing = ScreenAdaptive.dp2px(ShopSearchActivity.this, 15); // 50px
        boolean includeEdge = false;
        rcvHot.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        searchAdapter.setOnItemClickListener((adapter, view, position) -> {
            //etSearch.setText(((TextView) view.findViewById(R.id.tv_name)).getText().toString());
            //etSearch.setSelection(((TextView) view.findViewById(R.id.tv_name)).getText().toString().length());//将光标移至文字末尾
            if(position<=3){
                CheckDistrictActivity.startActivityInstance(ShopSearchActivity.this, "2",position+1);
            }else{
                CheckDistrictActivity.startActivityInstance(ShopSearchActivity.this, "1",position-4+1);
            }
        });
        MyApplication.setMaxFlingVelocity(rcvHot);

    }

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, ShopSearchActivity.class));
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


    @OnClick({R.id.back, R.id.tv_search})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_search:
                if (etSearch.getText().toString().equals("")) {
                    showToast("请输入搜索内容");
                    return;
                }
                searchData(etSearch.getText().toString());
                break;
        }
    }

    private void getSearchHot() {
        ApiUtil.getApiService().categorypopular("4").enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData() != null) {
                            searchDatas.clear();
                            searchDatas.addAll(entity.getData());
                            searchAdapter.notifyDataSetChanged();
                        }
                    } else {
                        showToast(entity.getMsg());
                        datas.clear();
                        searchAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d("11111", "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d("11111", "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                current++;
                searchData(etSearch.getText().toString());
            }
        }
        try {
            if (firstVisibleItem < 4) {
                springview.setEnableHeader(true);
            } else {
                springview.setEnableHeader(false);
            }
        }catch (Exception e){

        }

    }
}
