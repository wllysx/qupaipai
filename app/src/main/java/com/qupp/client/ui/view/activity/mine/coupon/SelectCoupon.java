package com.qupp.client.ui.view.activity.mine.coupon;

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
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.SelectCouponAdapter;
import com.qupp.client.utils.event.CouponEvent;
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

/**
 * 选择优惠券
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class SelectCoupon extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;

    SelectCouponAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    int current = 1;
    String size = "20";
    String cid = "", orderId = "";
    @BindView(R.id.tv_menu)
    TextView tvMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_coupon);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(SelectCoupon.this), 0, 0);
        cid = getIntent().getStringExtra("cid");
        orderId = getIntent().getStringExtra("orderId");
        tvMenu.setVisibility(View.VISIBLE);
        setStateColor(false);
        initView();
        initSpringView();

    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
        //springview.setFooter(new DefaultFooter(this));
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
        tvTitle.setText("选择优惠券");
        ivDefault.setImageResource(R.mipmap.icon_default10);
        tvDefault.setText("亲，您还暂无优惠券");

       /* EntityForSimple entity = new EntityForSimple();
        entity.setType(0);
        EntityForSimple entity1 = new EntityForSimple();
        entity1.setType(0);
        EntityForSimple entity2 = new EntityForSimple();
        entity2.setType(1);
        datas.add(entity);
        datas.add(entity1);
        datas.add(entity2);*/

        initAdapter(datas);
        getData();
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new SelectCouponAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 10);
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (datas.get(position).isCanUse()) {
                for (EntityForSimple entity : datas) {
                    entity.setSelected(false);
                }
                datas.get(position).setSelected(true);
                mAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(new CouponEvent(datas.get(position).getUseLimitMaxPrice(), datas.get(position).getId()));
                finish();
            }
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);


    }


    public static void startActivityInstance(Activity activity, String cid, String orderId) {
        activity.startActivity(new Intent(activity, SelectCoupon.class)
                .putExtra("cid", cid)
                .putExtra("orderId", orderId)
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


    @OnClick({R.id.back, R.id.tv_menu, R.id.tv_quxiao})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_menu:
                CouponGuizeActivity.startActivityInstance(SelectCoupon.this);
                break;
            case R.id.tv_quxiao:
                EventBus.getDefault().post(new CouponEvent("", "0"));
                finish();
                break;
        }
    }

    private void getData() {
        ApiUtil.getApiService().getUseList(MyApplication.getToken(), orderId, null, null).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if (entity.getData().size() == 0) {
                                llDefault.setVisibility(View.VISIBLE);
                            } else {
                                llDefault.setVisibility(View.GONE);
                            }
                        } else {
                            if (entity.getData().size() == 0) {
                                showToast("没有更多了");
                            }
                        }
                        datas.addAll(entity.getData());
                        for (EntityForSimple data : datas) {
                            if (data.getId().equals(cid)) {
                                data.setSelected(true);
                            }
                            if (data.isCanUse()) {
                                data.setType(0);
                            } else {
                                data.setType(1);
                            }
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

}
