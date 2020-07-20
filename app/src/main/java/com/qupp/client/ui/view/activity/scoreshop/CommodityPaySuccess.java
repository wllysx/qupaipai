package com.qupp.client.ui.view.activity.scoreshop;


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
import com.qupp.client.ui.view.activity.customerservice.ContractWebCenter;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.CustomProgressDialog;
import com.qupp.client.utils.adapter.ShopAdapter1;
import com.qupp.client.utils.dialog.MiddleDialog;
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
 * 积分商城 商品支付成功
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class CommodityPaySuccess extends BaseActivity {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.mRecyclerView)
    StaggeredGridView mRecyclerView;
    List<EntityForSimple> datas = new ArrayList<>();
    ShopAdapter1 mAdapter;
    String goodstype = "", orderId = "";
    private CustomProgressDialog pd;

    View headView;
    ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_success);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        orderId = getIntent().getStringExtra("orderId");
        goodstype = getIntent().getStringExtra("goodstype");
        pd = CustomProgressDialog.createDialog(this);
        initHeaderView();
        initView();
        initData();
    }

    private void initView() {
        tvTitle.setText("兑换成功");
        if (goodstype.equals("1") || goodstype.equals("3")) {
            viewHolder.tvGocheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvGocheck.setVisibility(View.GONE);
        }
        initAdapter(datas);
    }

    private View.OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.tv_gocheck:
                checkRealPerson();
                break;
        }
    };


    private void initHeaderView() {
        headView = getLayoutInflater().inflate(R.layout.item_pay_header, null);
        viewHolder = new ViewHolder(headView);
        viewHolder.tvGocheck.setOnClickListener(listener);
        mRecyclerView.addHeaderView(headView);

    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mAdapter = new ShopAdapter1(CommodityPaySuccess.this, data);
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.setOnScrollListener(this);
        mRecyclerView.setOnItemClickListener((parent, view, position, id) ->
        {
            CommodityDetailsActivity.startActivityInstance(CommodityPaySuccess.this, data.get(position).getGoodsId());
        });

    }


    /**
     * 获取商品列表
     */
    private void initData() {
        ApiUtil.getApiService().goodsInfolist(null, null, null, "10", "0").enqueue(new Callback<MessageForSimple>() {
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

    public static void startActivityInstance(Activity activity, String goodstype, String orderId) {
        activity.startActivity(new Intent(activity, CommodityPaySuccess.class)
                .putExtra("goodstype", goodstype)
                .putExtra("orderId", orderId)
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
     * 获取实名认证状态
     */
    private void checkRealPerson() {
        ApiUtil.getApiService().checkRealPersonNum(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData().equals("1")) {
                            pd.show();
                            applyMailing(orderId);
                        } else if(entity.getData().equals("0")){
                            new MiddleDialog(CommodityPaySuccess.this).setSureListener(() -> {
                                Authentication.startActivityInstance(CommodityPaySuccess.this);
                            }).show("温馨提示", "为了保障您的账户安全，请完成实名认证后在进行提现！", "取消", "去认证", false);


                        }else if(entity.getData().equals("2")){
                            new MiddleDialog(CommodityPaySuccess.this).setSureListener(() -> {
                                Authentication.startActivityInstance(CommodityPaySuccess.this);
                            }).show("温馨提示", "一个身份证只能绑定一个账号，请更换身份信息！", "取消", "去更换", false);
                        }
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

    /**
     * 申请寄拍
     *
     * @param orderId
     */
    private void applyMailing(String orderId) {
        ApiUtil.getApiService().applyMailing(MyApplication.getToken(), orderId).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        //EventBus.getDefault().post(new OrderPaySuccess("1"));
                        startActivity(new Intent(CommodityPaySuccess.this, ContractWebCenter.class).putExtra("OpenUrl", entity.getData()));
                        finish();
                    } else {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
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


    static
    class ViewHolder {
        @BindView(R.id.tv_gocheck)
        TextView tvGocheck;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
