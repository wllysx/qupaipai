package com.qupp.client.ui.view.activity.mine.balance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.WalletAdapter;
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
 * 我的钱包
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class WalletActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    String integral = "";
    @BindView(R.id.springviewtop)
    SpringView springviewtop;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    List<EntityForSimple> datas = new ArrayList<>();

    WalletAdapter mAdapter;
    int current = 1, size = 30;
    @BindView(R.id.springviewblow)
    SpringView springviewblow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(WalletActivity.this), 0, 0);
        integral = getIntent().getStringExtra("integral");
        setStateColor(true);
        initView();
        initSpringView();
        initSpringViewBlow();
        initData();
        getData();

    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new WalletAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 0);
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);
    }

    /**
     * 获取账户信息
     */
    private void getData() {
        ApiUtil.getApiService().memberWalletdetail(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvAmount.setText(entity.getData().getAmount());
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


    private void initData() {
        ApiUtil.getApiService().memberWalletDetaillist(MyApplication.getToken(), current + "", size + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if (entity.getData().getRecords().size() == 0) {
                                llDefault.setVisibility(View.VISIBLE);
                            } else {
                                llDefault.setVisibility(View.GONE);
                            }
                        } else {
                            if (entity.getData().getRecords().size() == 0) {
                                showToast("没有更多了");
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

    private void initSpringView() {
        springviewtop.setHeader(new DefaultHeader(this));
        springviewtop.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springviewtop.onFinishFreshAndLoad();
                current = 1;
                initData();
                getData();

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

    private void initSpringViewBlow() {
        springviewblow.setFooter(new DefaultHeader(this));
        springviewblow.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springviewblow.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                current++;
                initData();
                springviewblow.onFinishFreshAndLoad();
            }
        });
    }


    private void initView() {
        tvTitle.setText("钱包");
        ivDefault.setImageResource(R.mipmap.icon_default10);
        tvDefault.setText("亲，暂无数据");

        initAdapter(datas);
    }

    public static void startActivityInstance(Activity activity, String integral) {
        activity.startActivity(new Intent(activity, WalletActivity.class)
                .putExtra("integral", integral)
        );
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

}
