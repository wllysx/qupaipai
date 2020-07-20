package com.qupp.client.ui.view.activity.mine.myaction;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.mine.MyAddressActivity;
import com.qupp.client.ui.view.activity.scoreshop.PhysicalActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DateUtils;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.event.AddressEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 抽奖活动详情
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class MyActionDetails extends BaseActivity {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.tv_states)
    TextView tvStates;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    String orderId = "", orderStatus = "";
    @BindView(R.id.iv_states)
    ImageView ivStates;
    @BindView(R.id.iv_address_right)
    ImageView ivAddressRight;
    @BindView(R.id.ll_address_add)
    LinearLayout llAddressAdd;
    String addressId = "";
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_activityName)
    TextView tvActivityName;
    @BindView(R.id.tv_prizeName)
    TextView tvPrizeName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_finish)
    LinearLayout llFinish;
    @BindView(R.id.tv_submittake)
    TextView tvSubmittake;
    private String logo = "";
    String logisticsName = "", logisticsNo = "", activityId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactiondetails);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        orderId = getIntent().getStringExtra("orderId");
        initView();
        getData();
    }

    private void initView() {
        tvTitle.setText("订单详情");
    }

    public static void startActivityInstance(Context activity, String orderId) {
        activity.startActivity(new Intent(activity, MyActionDetails.class)
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
    }


    @OnClick({R.id.back, R.id.ll_edit, R.id.ll_address_add, R.id.tv_seelogistics, R.id.tv_sure, R.id.tv_submittake})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_edit:
                MyAddressActivity.startActivityInstance(this, "1");
                break;
            case R.id.ll_address_add:
                MyAddressActivity.startActivityInstance(this, "1");
                break;
            case R.id.tv_seelogistics:
                PhysicalActivity.startActivityInstance(this, orderId, "2", logo, logisticsNo, logisticsName, activityId);
                break;
            case R.id.tv_sure:
                addAddress();
                break;
            case R.id.tv_submittake:
                activityreceipt();
                break;
        }
    }


    private void addAddress() {
        ApiUtil.getApiService().settingReceivingAddress(MyApplication.getToken(), orderId, addressId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        finish();
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


    private void activityreceipt() {
        ApiUtil.getApiService().activityreceipt(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        showToast("确认收货成功");
                        getData();
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
     * 订单详情
     */
    private void getData() {
        ApiUtil.getApiService().activitydetail(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {

                        tvActivityName.setText(entity.getData().getActivityName());
                        tvPrizeName.setText(entity.getData().getPrizeName());
                        logisticsNo = entity.getData().getLogisticsNo();
                        logisticsName = entity.getData().getLogisticsName();
                        logo = entity.getData().getImageUrl();
                        activityId = entity.getData().getActivityId();
                        if (entity.getData().getStatus().equals("0")) {
                            //待发货
                            tvStates.setText("待发货");
                            ivStates.setImageResource(R.mipmap.icon_states1);
                            //显示添加地址
                            llAddressAdd.setVisibility(View.VISIBLE);
                            llEdit.setVisibility(View.GONE);
                            tvSure.setVisibility(View.VISIBLE);
                            llFinish.setVisibility(View.GONE);
                            tvSubmittake.setVisibility(View.GONE);
                        } else if (entity.getData().getStatus().equals("1")) {
                            //待发货
                            tvStates.setText("待发货");
                            ivStates.setImageResource(R.mipmap.icon_states1);
                            //显示添加地址
                            llEdit.setEnabled(false);
                            llEdit.setVisibility(View.VISIBLE);
                            llAddressAdd.setVisibility(View.GONE);
                            tvSure.setVisibility(View.GONE);
                            llFinish.setVisibility(View.GONE);
                            tvSubmittake.setVisibility(View.GONE);
                        } else if (entity.getData().getStatus().equals("2")) {
                            //待收货
                            tvStates.setText("待收货");
                            ivStates.setImageResource(R.mipmap.icon_states2);
                            //显示地址
                            llEdit.setEnabled(false);
                            llEdit.setVisibility(View.VISIBLE);
                            llAddressAdd.setVisibility(View.GONE);
                            tvSure.setVisibility(View.GONE);
                            llFinish.setVisibility(View.VISIBLE);
                            tvSubmittake.setVisibility(View.VISIBLE);
                        } else if (entity.getData().getStatus().equals("3")) {
                            //交易完成
                            tvStates.setText("已签收");
                            ivStates.setImageResource(R.mipmap.icon_states3);
                            //显示地址
                            llEdit.setEnabled(false);
                            llEdit.setVisibility(View.VISIBLE);
                            llAddressAdd.setVisibility(View.GONE);
                            tvSure.setVisibility(View.GONE);
                            llFinish.setVisibility(View.VISIBLE);
                            tvSubmittake.setVisibility(View.GONE);
                        } else {
                            //交易失败
                            tvStates.setText("交易失败");
                            ivStates.setImageResource(R.mipmap.icon_states4);
                            //显示地址
                            llEdit.setEnabled(false);
                            llEdit.setVisibility(View.VISIBLE);
                            llAddressAdd.setVisibility(View.GONE);
                            tvSure.setVisibility(View.GONE);
                            llFinish.setVisibility(View.VISIBLE);
                            tvSubmittake.setVisibility(View.GONE);
                        }

                        //地址
                        tvName.setText("收货人：" + entity.getData().getConsignee());
                        tvAddress.setText("地址：" + entity.getData().getAddress());
                        tvPhone.setText(entity.getData().getPhone());
                        //商品详情
                        Glide.with(MyActionDetails.this).load(entity.getData().getImageUrl()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into(ivLogo);
                        tvTime.setText(DateUtils.dateToDate(entity.getData().getCreateTime(), "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm"));
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void address(AddressEvent event) {
        if (event != null) {
            llEdit.setVisibility(View.VISIBLE);
            llAddressAdd.setVisibility(View.GONE);
            addressId = event.getAddressId();
            tvAddress.setText("地址：" + event.getAddress());
            tvName.setText(event.getName());
            tvPhone.setText(event.getPhone());

            tvSure.setEnabled(true);
            tvSure.setBackgroundResource(R.drawable.bg_login_enable);
        }
    }

}
