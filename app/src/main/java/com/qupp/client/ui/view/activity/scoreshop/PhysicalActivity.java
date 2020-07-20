package com.qupp.client.ui.view.activity.scoreshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcssloop.widget.RCImageView;
import com.google.gson.Gson;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.KdAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 物流信息
 */

public class PhysicalActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    String orderId = "";
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    KdAdapter mAdapter;
    ArrayList<EntityForSimple> datas = new ArrayList<>();
    String isshop = "", logo = "";
    @BindView(R.id.iv_logo)
    RCImageView ivLogo;
    String logisticsNo = "", logisticsName = "",num = "1";
    @BindView(R.id.tv_states)
    TextView tvStates;
    @BindView(R.id.tv_orderno)
    TextView tvOrderno;
    @BindView(R.id.tv_kdname)
    TextView tvKdname;
    @BindView(R.id.tv_kdcode)
    TextView tvKdcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(PhysicalActivity.this), 0, 0);
        setStateColor(false);
        orderId = getIntent().getStringExtra("orderId");
        isshop = getIntent().getStringExtra("isshop");
        logo = getIntent().getStringExtra("logo");
        logisticsNo = getIntent().getStringExtra("logisticsNo");
        logisticsName = getIntent().getStringExtra("logisticsName");
        num = getIntent().getStringExtra("num");
        initView();
        if (isshop.equals("1")) {
            initdata1();
        } else if(isshop.equals("0")){
            tvStates.setText("共"+num+"件商品");
            initdata();
        }else{
            tvOrderno.setText("订单编号:"+orderId);
            initdata2();
        }
    }


    private void initView() {
        tvTitle.setText("物流详情");
        Glide.with(PhysicalActivity.this).load(logo).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into(ivLogo);
        initAdapter(datas);
        tvOrderno.setText("订单编号:"+orderId);
        tvKdname.setText("物流公司:"+logisticsName);
        tvKdcode.setText("物流单号:"+logisticsNo);
    }


    private void initdata() {

        ApiUtil.getApiService().kuaiDiQuery(MyApplication.getToken(), orderId).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        EntityForSimple entityForSimple = new Gson().fromJson(entity.getData(), EntityForSimple.class);
                        datas.clear();
                        datas.addAll(entityForSimple.getData());
                        mAdapter.notifyDataSetChanged();
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

    private void initdata1() {

        ApiUtil.getApiService().orderkuaiDiQuery(MyApplication.getToken(), orderId).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        EntityForSimple entityForSimple = new Gson().fromJson(entity.getData(), EntityForSimple.class);
                        datas.clear();
                        datas.addAll(entityForSimple.getData());
                        mAdapter.notifyDataSetChanged();
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

    private void initdata2() {
        ApiUtil.getApiService().activitykuaiDiQuery(MyApplication.getToken(), orderId).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        EntityForSimple entityForSimple = new Gson().fromJson(entity.getData(), EntityForSimple.class);
                        datas.clear();
                        datas.addAll(entityForSimple.getData());
                        mAdapter.notifyDataSetChanged();
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


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(PhysicalActivity.this, 1));
        mAdapter = new KdAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Toast.makeText(PhysicalActivity.this, position + "", Toast.LENGTH_LONG).show();
        });
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(PhysicalActivity.this, 0);
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    /**
     * @param activity
     * @param orderId
     * @param isshop   0 商城 1拍卖
     */
    public static void startActivityInstance(Activity activity, String orderId, String isshop, String logo, String logisticsNo, String logisticsName,String num) {
        activity.startActivity(new Intent(activity, PhysicalActivity.class)
                .putExtra("orderId", orderId)
                .putExtra("isshop", isshop)
                .putExtra("logo", logo)
                .putExtra("logisticsNo", logisticsNo)
                .putExtra("logisticsName", logisticsName)
                .putExtra("num",num)

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
