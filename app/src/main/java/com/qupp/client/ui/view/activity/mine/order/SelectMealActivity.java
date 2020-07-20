package com.qupp.client.ui.view.activity.mine.order;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
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
import com.qupp.client.utils.adapter.GiftbagOrderAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.event.MealEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 选择套餐
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class SelectMealActivity extends BaseActivity {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    ArrayList<EntityForSimple> datas = new ArrayList<>();
    GiftbagOrderAdapter mAdapter;
    @BindView(R.id.bt_next)
    TextView btNext;
    String from, orderId, auctionId;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_selectmeal);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        from = getIntent().getStringExtra("from");
        orderId = getIntent().getStringExtra("orderId");
        auctionId = getIntent().getStringExtra("auctionId");
        position = getIntent().getIntExtra("position", -1);
        initView();
        getData();
    }

    private void initView() {
        tvTitle.setText("选择赠品");
        initAdapter(datas);
    }

    private void getData() {
        ApiUtil.getApiService().listByAuctionId(MyApplication.getToken(), auctionId).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        datas.clear();
                        if (position == -1 || position + 1 > entity.getData().size()) {
                            datas.addAll(entity.getData());
                            btNext.setBackgroundResource(R.drawable.bg_login_unable);
                            btNext.setEnabled(false);
                        } else {
                            ArrayList<EntityForSimple> ds = entity.getData();
                            ds.get(position).setSelected(true);
                            datas.addAll(ds);
                            btNext.setBackgroundResource(R.drawable.bg_login_enable);
                            btNext.setEnabled(true);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }


    private void initAdapter(ArrayList<EntityForSimple> datas) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(SelectMealActivity.this, 1));

        mAdapter = new GiftbagOrderAdapter(datas);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(SelectMealActivity.this, 10); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //先判断库存
            if (datas.get(position).isFlag()) {
                for (EntityForSimple entity : datas) {
                    entity.setSelected(false);
                }
                datas.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
                btNext.setBackgroundResource(R.drawable.bg_login_enable);
                btNext.setEnabled(true);
                this.position = position;
            }
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);
    }


    /**
     * 1 来自列表  2来自详情
     *
     * @param activity
     * @param from
     */
    public static void startActivityInstance(Context activity, String from, String orderId, String auctionId, int position) {
        activity.startActivity(new Intent(activity, SelectMealActivity.class)
                .putExtra("from", from)
                .putExtra("orderId", orderId)
                .putExtra("auctionId", auctionId)
                .putExtra("position", position));
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

    @OnClick({R.id.back, R.id.bt_next})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bt_next:
                if (from.equals("1")) {
                    OrderPay.startActivityInstance(SelectMealActivity.this, orderId, new MealEvent(datas.get(position), position, auctionId));
                } else {
                    //eventbus传入套餐
                    EventBus.getDefault().post(new MealEvent(datas.get(position), position, auctionId));
                    finish();
                }
                break;
        }
    }
}
