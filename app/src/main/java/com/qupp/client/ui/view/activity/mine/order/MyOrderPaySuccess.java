package com.qupp.client.ui.view.activity.mine.order;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.MainActivity;
import com.qupp.client.ui.view.activity.mine.integral.IntegralActivity;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.CustomProgressDialog;
import com.qupp.client.utils.adapter.ShopAdapter1;
import com.qupp.client.utils.waterfall.StaggeredGridView;

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
 * 个人中心订单付款成功
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class MyOrderPaySuccess extends BaseActivity {

    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.mRecyclerView)
    StaggeredGridView mRecyclerView;
    public Intent intent;
    public Bundle bundle;
    List<EntityForSimple> datas = new ArrayList<>();
    ShopAdapter1 mAdapter;
    String orderId = "", count = "";
    private CustomProgressDialog pd;
    View headView;
    ViewHolder viewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderpay_success);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        orderId = getIntent().getStringExtra("orderId");
        count = getIntent().getStringExtra("count");
        pd = CustomProgressDialog.createDialog(this);
        initHeaderView();
        initView();
        initData();
    }

    private void initView() {
        tvTitle.setText("支付成功");
        initAdapter(datas);
        if (!count.equals("0")) {
            viewHolder.tvCount.setText("支付成功，获得" + count + "积分！");
        } else {
            viewHolder.tvCount.setText("支付成功");
        }
    }


    private View.OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.tv_tointegral:
                getBlanceAndIntegralAndFans();
                break;
            case R.id.tv_tomain:
                MyApplication.toMain = true;
                startActivity(new Intent(MyOrderPaySuccess.this, MainActivity.class));
                break;
        }
    };


    private void initHeaderView() {
        headView = getLayoutInflater().inflate(R.layout.item_pay_header2, null);
        viewHolder = new ViewHolder(headView);
        viewHolder.tvTomain.setOnClickListener(listener);
        viewHolder.tvTointegral.setOnClickListener(listener);
        mRecyclerView.addHeaderView(headView);

    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mAdapter = new ShopAdapter1(MyOrderPaySuccess.this, data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemClickListener((parent, view, position, id) ->
        {
            CommodityDetailsActivity.startActivityInstance(MyOrderPaySuccess.this, data.get(position).getGoodsId());
        });

    }


    /**
     * 获取商品列表
     */
    private void initData() {
        ApiUtil.getApiService().goodsInfolist(null, null, null, null, null).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
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

    public static void startActivityInstance(Activity activity, String count) {
        activity.startActivity(new Intent(activity, MyOrderPaySuccess.class)
                .putExtra("count", count)
        );
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
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
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
     * 获取用户信息积分等信息
     */
    private void getBlanceAndIntegralAndFans() {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        IntegralActivity.startActivityInstance(MyOrderPaySuccess.this, entity.getData().getIntegralAmount());
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

    static class ViewHolder {
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_tomain)
        TextView tvTomain;
        @BindView(R.id.tv_tointegral)
        TextView tvTointegral;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
