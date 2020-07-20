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

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.WithDrawRecordAdapter;
import com.qupp.client.utils.dialog.MiddleDialog;
import com.qupp.client.utils.event.AmountEvent;
import com.qupp.client.utils.view.springview.DefaultFooter;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WithDrawRecordActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    WithDrawRecordAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    int current = 1, size = 30;
    String amount = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_record);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(WithDrawRecordActivity.this), 0, 0);
        amount = getIntent().getStringExtra("amount");
        setStateColor(false);
        initView();
        initSpringView();
        getData();


    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
        springview.setFooter(new DefaultFooter(this));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                current = 1;
                getData();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                current++;
                getData();
            }
        });
    }


    private void initView() {
        tvTitle.setText("提现记录");
        ivDefault.setImageResource(R.mipmap.icon_default10);
        tvDefault.setText("亲，您还暂无余额提现");
        tvAmount.setText(amount + "元");
        initAdapter(datas);
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new WithDrawRecordAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setOnItemChildClickListener((adapter, view, position) ->{
            new MiddleDialog(WithDrawRecordActivity.this).setSureListener(() ->{
                withdrawcancel(datas.get(position).getId());
            }).show("温馨提示", "确定要撤回提现申请吗？", "取消", "确定", false);

        } );
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 10);
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);
    }

    public static void startActivityInstance(Activity activity, String amount) {
        activity.startActivity(new Intent(activity, WithDrawRecordActivity.class).putExtra("amount", amount));
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
     * 获取会员提现纪录列表
     */
    private void getData() {
        ApiUtil.getApiService().withdrawlist(MyApplication.getToken(), current + "", size + "").enqueue(new Callback<MessageForSimple>() {
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

    private void withdrawcancel(String id) {
        ApiUtil.getApiService().withdrawcancel(MyApplication.getToken(),id).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        showToast("撤回成功！");
                        current = 1;
                    } else {
                        showToast(entity.getMsg());
                    }
                    getBlanceAndIntegralAndFans();
                    getData();
                    EventBus.getDefault().post(new AmountEvent());
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
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

}
