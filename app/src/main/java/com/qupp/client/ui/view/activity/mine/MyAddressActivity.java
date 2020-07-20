package com.qupp.client.ui.view.activity.mine;

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
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.AddressAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.event.AddressEvent;
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
 * 我的地址
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class MyAddressActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    AddressAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    int current = 1, size = 30;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    String form = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaddress);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(MyAddressActivity.this), 0, 0);
        form = getIntent().getStringExtra("form");
        setStateColor(false);
        initView();
        initSpringView();
        initData();

    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
        // springview.setFooter(new DefaultFooter(this));
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
        tvTitle.setText("收货地址");
        ivDefault.setImageResource(R.mipmap.icon_default2);
        tvDefault.setText("亲，您还暂无添加地址");
        initAdapter(datas);
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new AddressAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 10);
        ; // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (form.equals("1")) {
                String address  =data.get(position).getProvinceText()+data.get(position).getCityText()+data.get(position).getDistrictText()+data.get(position).getStreetText()+data.get(position).getAddress();
                EventBus.getDefault().post(new AddressEvent(data.get(position).getAddressId(),address,data.get(position).getConsignee(),data.get(position).getMobile()));
                finish();

            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_delete:
                    addressdel(position, data.get(position).getAddressId());
                    break;
                case R.id.tv_edit:
                    MyAddressAddActivity.startActivityInstance(this, "0", data.get(position));
                    break;
                case R.id.ll_default_address:
                    if (!data.get(position).getTolerant().equals("0")) {
                        //去设置为默认
                        settingTolerant(position, data.get(position).getAddressId());
                    }
            }
        });


    }

    /**
     * 删除地址
     *
     * @param position
     * @param id
     */
    private void addressdel(int position, String id) {
        ApiUtil.getApiService().addressdel(MyApplication.getToken(),id).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        initData();
                    } else {
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

    /**
     * 设置默认
     *
     * @param position
     * @param id
     */
    private void settingTolerant(int position, String id) {
        ApiUtil.getApiService().settingTolerant(MyApplication.getToken(),id).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                       initData();
                    } else {
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


    private void initData() {
        ApiUtil.getApiService().addresslist(MyApplication.getToken(),current + "", size + "").enqueue(new Callback<MessageForSimple>() {
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
                        }
                        datas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
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

    /**
     * @param activity
     * @param form     1选择地址
     */
    public static void startActivityInstance(Activity activity, String form) {
        activity.startActivity(new Intent(activity, MyAddressActivity.class)
                .putExtra("form", form)
        );
    }

    @Override
    protected void onResume() {
        current = 1;
        initData();
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


    @OnClick({R.id.back, R.id.tv_add})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_add:
                if (datas.size() > 0) {
                    MyAddressAddActivity.startActivityInstance(this, "0", null);
                } else {
                    MyAddressAddActivity.startActivityInstance(this, "1", null);
                }
                break;
        }
    }


}
