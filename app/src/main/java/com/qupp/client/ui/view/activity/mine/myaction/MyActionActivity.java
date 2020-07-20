package com.qupp.client.ui.view.activity.mine.myaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MyAuctionAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
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
 * 我的活动
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class MyActionActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    MyAuctionAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    int size = 30;
    String id= "0";
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    @BindView(R.id.iv_default)
    ImageView ivDefault;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_my);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(MyActionActivity.this), 0, 0);
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
                id = "0";
                initData();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                if(datas.size()>0){
                    id = datas.get(datas.size()-1).getActivityLogId();
                }
                initData();
            }
        });
    }


    private void initView() {
        tvTitle.setText("我的活动");
        ivDefault.setImageResource(R.mipmap.icon_default3);
        tvDefault.setText("亲，您还没有参加活动");
        initAdapter(datas);
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new MyAuctionAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 10);
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MyActionDetails.startActivityInstance(MyActionActivity.this,datas.get(position).getActivityLogId());
        });

        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, MyActionActivity.class));
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

    private void initData() {
        ApiUtil.getApiService().activitypageList(MyApplication.getToken(),id, size + "").enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (id.equals("0")) {
                            datas.clear();
                            if (entity.getData()==null||entity.getData().size() == 0) {
                                llDefault.setVisibility(View.VISIBLE);
                            } else {
                                llDefault.setVisibility(View.GONE);
                            }
                        }
                        datas.addAll(entity.getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        llDefault.setVisibility(View.VISIBLE);
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
                llDefault.setVisibility(View.VISIBLE);
            }
        });
    }


}
