package com.qupp.client.ui.view.activity.scoreshop;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.customerservice.ContractWebCenter;
import com.qupp.client.ui.view.activity.mine.MyAddressActivity;
import com.qupp.client.ui.view.activity.mine.balance.SetPasswordAActivity;
import com.qupp.client.ui.view.activity.mine.pinganbank.pinganpay.PayA;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DateUtils;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.CustomProgressDialog;
import com.qupp.client.utils.dialog.BelowPayDialog;
import com.qupp.client.utils.dialog.MiddleDialog;
import com.qupp.client.utils.dialog.MiddleDialogWithoutTitle;
import com.qupp.client.utils.dialog.PayPasswordDialog;
import com.qupp.client.utils.event.AddressEvent;
import com.qupp.client.utils.event.OrderPaySuccess;
import com.qupp.client.utils.event.WxpayEvent;
import com.qupp.client.utils.glide.GlideRoundTransform;
import com.qupp.client.utils.pay.alipay.PayResult;
import com.qupp.client.utils.secretUtils.RsaCipherUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的订单详情
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class MyOrderDetails extends BaseActivity {

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
    @BindView(R.id.tv_commoditytitle)
    TextView tvCommoditytitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_firstprice)
    TextView tvFirstprice;
    @BindView(R.id.tv_finalprice)
    TextView tvFinalprice;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.tv_states)
    TextView tvStates;
    @BindView(R.id.tv_countdown)
    TextView tvCountdown;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.tv_firstpricetitle)
    TextView tvFirstpricetitle;
    @BindView(R.id.tv_finalpricetitle)
    TextView tvFinalpricetitle;
    String orderId = "";
    @BindView(R.id.ll_order_no)
    LinearLayout llOrderNo;
    @BindView(R.id.ll_order_contract)
    LinearLayout llOrderContract;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_order_paytime)
    TextView tvOrderPaytime;
    @BindView(R.id.tv_order_checktime)
    TextView tvOrderChecktime;
    @BindView(R.id.ll_gopay)
    LinearLayout llGopay;
    @BindView(R.id.ll_gocheck)
    LinearLayout llGocheck;
    @BindView(R.id.ll_take)
    LinearLayout llTake;
    @BindView(R.id.ll_finish)
    LinearLayout llFinish;
    @BindView(R.id.iv_address_right)
    ImageView ivAddressRight;
    @BindView(R.id.tv_order_close)
    TextView tvOrderClose;
    @BindView(R.id.ll_address_add)
    LinearLayout llAddressAdd;
    String addressId = "";
    @BindView(R.id.ll_integral)
    LinearLayout llIntegral;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.tv_orderno)
    TextView tvOrderno;
    @BindView(R.id.tv_order_finishtime)
    TextView tvOrderFinishtime;
    String orderPrice = "";
    boolean issetPassword = true;
    String goodsType = "";
    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.tv_allprice)
    TextView tvAllprice;
    String orderStates = "", logo = "";
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.ll_remark)
    LinearLayout llRemark;
    private CustomProgressDialog pd;
    String logisticsName = "", logisticsNo = "";
    PayPasswordDialog payPasswordDialog;
    String num = "";
    boolean isChange = false;
    String firstCategoryId = "",type = "2",wallet="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        pd = CustomProgressDialog.createDialog(MyOrderDetails.this);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        orderId = getIntent().getStringExtra("orderId");
        initView();
        orderdetail(orderId);
    }

    private void initView() {
        tvTitle.setText("订单详情");

    }

    public static void startActivityInstance(Activity activity, String orderId) {
        activity.startActivity(new Intent(activity, MyOrderDetails.class)
                .putExtra("orderId", orderId)
        );
    }

    public static void startActivityInstance(Context activity, String orderId) {
        activity.startActivity(new Intent(activity, MyOrderDetails.class)
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
        isSetPassword();
        memberWalletdetail();
    }

    /**
     * 获取账户信息
     */
    private void memberWalletdetail() {
        ApiUtil.getApiService().memberWalletdetail(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        wallet = entity.getData().getAmount();
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


    @Override
    protected void onPause() {
        super.onPause();
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }


    @OnClick({R.id.back, R.id.ll_edit, R.id.ll_address_add, R.id.tv_copy, R.id.tv_see, R.id.tv_gopay, R.id.tv_gocheck, R.id.tv_cancel, R.id.tv_seelogistics, R.id.tv_submittake, R.id.tv_seelogistics1})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                if (isChange) {
                    EventBus.getDefault().post(new OrderPaySuccess("1"));
                }
                finish();
                break;
            case R.id.ll_edit:
                if (orderStates.equals("0")) {
                    MyAddressActivity.startActivityInstance(this, "1");
                }
                break;
            case R.id.ll_address_add:
                if (orderStates.equals("0")) {
                    MyAddressActivity.startActivityInstance(this, "1");
                }
                break;
            case R.id.tv_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", tvOrderno.getText().toString().replace("订单编号：", ""));
                cm.setPrimaryClip(mClipData);
                showToast("复制成功");
                break;
            case R.id.tv_see:
                showToast("查看");
                break;
            case R.id.tv_gopay:
                if(firstCategoryId.equals(MyApplication.firstCategoryId)){
                    type  ="1";
                }else{
                    type = "2";
                }
                new BelowPayDialog(this).setSureListener(item -> {
                    if (item == 3) {
                        pd.show();
                        gopayforAliOrder();
                    } else if (item == 2) {
                        pd.show();
                        goPayforWxOrder();
                    } else if (item == 1||item==6) {
                        if (issetPassword) {
                            payPasswordDialog = new PayPasswordDialog(MyOrderDetails.this);
                            payPasswordDialog.setSureListener(code -> goPaybalanceOrder(code,item)).show();

                        } else {
                            new MiddleDialogWithoutTitle(MyOrderDetails.this).setSureListener(() -> {
                                SetPasswordAActivity.startActivityInstance(MyOrderDetails.this);
                            }).setCancelListener(() -> finish()).show("设置安全密码才可以提现，去设置？", "关闭", "去设置", false);
                        }
                    } else {
                        showToast(getResources().getString(R.string.pay_no));
                    }
                }).setSureListener1((type, bankid) -> goPayforBankOrder(bankid)).show(orderPrice, firstCategoryId,type,wallet);
                break;
            case R.id.tv_gocheck:
                pd.show();
                checkRealPerson(orderId);
                //StatService.onEvent(this, "shenqingjipai", "申请寄拍");
                // applyMailing(orderId);
                break;
            case R.id.tv_seelogistics:
                PhysicalActivity.startActivityInstance(this, orderId, "0", logo, logisticsNo, logisticsName, num);
                break;
            case R.id.tv_submittake:
                orderreceipt(orderId);
                break;
            case R.id.tv_seelogistics1:
                PhysicalActivity.startActivityInstance(this, orderId, "0", logo, logisticsNo, logisticsName, num);
                break;
            case R.id.tv_cancel:
                closeOrder();
                break;
        }
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
                        isChange = true;
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        orderdetail(orderId);
                        startActivity(new Intent(MyOrderDetails.this, ContractWebCenter.class).putExtra("OpenUrl", entity.getData()));
                        finish();
                    } else {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }

    /**
     * 余额支付(order)
     *
     * @param payPassWor
     */
    private void goPaybalanceOrder(String payPassWor,int type) {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String pass = "";
        try {
            pass = RsaCipherUtil.encrypt(payPassWor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiUtil.getApiService().orderPay2order(MyApplication.getToken(), orderId, addressId, type+"", pass).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (payPasswordDialog != null) {
                            payPasswordDialog.dismiss();
                        }
                        isChange = true;
                        orderdetail(orderId);
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
     * 微信支付(order)
     */
    private void goPayforWxOrder() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderPay1order(MyApplication.getToken(), orderId, addressId, "2", payPassWor,null).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        wxPay(entity.getData(), "3");
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
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


    /**
     * 银行支付(order)
     */
    private void goPayforBankOrder(String bankid) {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderPay1order(MyApplication.getToken(), orderId, addressId, "5", payPassWor,bankid).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        PayA.startActivityInstance(MyOrderDetails.this,bankid,entity.getData().getOrderNo(),tvAllprice.getText().toString(),"3");
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
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


    /**
     * 支付宝订单（orderid）
     */
    private void gopayforAliOrder() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        ApiUtil.getApiService().orderPay1order(MyApplication.getToken(), orderId, addressId, "3", null,null).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        payV2(entity.getData().getOrderStr(), "3", mHandler);
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
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    /**
     * 取消订单
     */
    private void closeOrder() {
        ApiUtil.getApiService().closeOrder(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        isChange = true;
                        orderdetail(orderId);
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
     * 是否设置密码
     */
    private void isSetPassword() {
        ApiUtil.getApiService().isSetPassword(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (!entity.getData().equals("true")) {
                            issetPassword = false;
                        } else {
                            issetPassword = true;
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
     * 确认收货
     */
    private void orderreceipt(String orderId) {
        ApiUtil.getApiService().orderreceipt(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        isChange = true;
                        orderdetail(orderId);
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
    private void orderdetail(String orderId) {
        ApiUtil.getApiService().orderdetail(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        firstCategoryId = entity.getData().getFirstCategoryId();
                        orderStates = entity.getData().getOrderStatus();
                        goodsType = entity.getData().getGoodsType();
                        llFinish.setVisibility(View.GONE);
                        llTake.setVisibility(View.GONE);
                        llGopay.setVisibility(View.GONE);
                        llGocheck.setVisibility(View.GONE);
                        tvCountdown.setVisibility(View.GONE);
                        logo = entity.getData().getLogo();
                        // Glide.with(MyOrderDetails.this).load(entity.getData().getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into(ivLogo);

                        RequestOptions options1 = new RequestOptions()
                                .centerCrop()
                                .apply(new RequestOptions())
                                .priority(Priority.NORMAL)//优先级
                                .transform(new GlideRoundTransform(7));
                        Glide.with(MyOrderDetails.this).load(entity.getData().getLogo()).apply(options1).into(ivLogo);
                        if (entity.getData().getAddressId() == null || entity.getData().getAddressId().isEmpty()) {
                            //无默认地址
                            llEdit.setVisibility(View.GONE);
                            llAddressAdd.setVisibility(View.VISIBLE);
                        } else {
                            addressId = entity.getData().getAddressId();
                            tvAddress.setText(entity.getData().getAddress());
                            tvName.setText("收货人：" + entity.getData().getConsignee() + "  " + entity.getData().getMobile());
                        }

                        tvCommoditytitle.setText(entity.getData().getGoodsName());
                        if (entity.getData().getPriceType().equals("1")) {
                            tvJifen.setVisibility(View.GONE);
                            llPrice.setVisibility(View.GONE);
                            tvPrice.setText(entity.getData().getIntegral().replace(".00", "") + "积分");
                            tvFirstprice.setText(entity.getData().getTotalIntegral().replace(".00", ""));
                        } else if (entity.getData().getPriceType().equals("2")) {
                            tvJifen.setText("+"+entity.getData().getIntegral().replace(".00", "") + "积分");
                            tvFirstprice.setText(entity.getData().getTotalIntegral().replace(".00", ""));
                            tvPrice.setText("￥" + entity.getData().getPrice());
                            orderPrice =entity.getData().getTotalPrice();
                            tvFinalprice.setText("￥" + entity.getData().getTotalPrice());
                        } else if(entity.getData().getPriceType().equals("3")){
                            tvPrice.setVisibility(View.GONE);
                            llIntegral.setVisibility(View.GONE);
                            tvJifen.setText("￥" + entity.getData().getPrice());
                            orderPrice = entity.getData().getTotalPrice();
                            tvFinalprice.setText("￥" + entity.getData().getTotalPrice());
                        }else{
                            tvFirstpricetitle.setText("钱包");
                            tvJifen.setText("+"+entity.getData().getIntegral().replace(".00", "") + "钱包");
                            tvFirstprice.setText(entity.getData().getTotalIntegral().replace(".00", ""));
                            tvPrice.setText("￥" + entity.getData().getPrice());
                            orderPrice =entity.getData().getTotalPrice();
                            tvFinalprice.setText("￥" + entity.getData().getTotalPrice());
                        }
                        num = entity.getData().getNum();
                        tvNum.setText("×" + entity.getData().getNum());
                        tvAllprice.setText("￥" +orderPrice);
                        tvOrderno.setText("订单编号：" + entity.getData().getOrderId());
                        tvOrderTime.setText("下单时间：" + entity.getData().getCreateTime());
                        tvOrderPaytime.setText("支付时间：" + entity.getData().getPayTime());
                        tvOrderChecktime.setText("寄拍时间：" + entity.getData().getApplyTime());
                        tvOrderClose.setText("关闭时间：" + entity.getData().getUpdateTime());
                        tvOrderFinishtime.setText("完成时间：" + entity.getData().getUpdateTime());

                        logisticsName = entity.getData().getLogisticsName();
                        logisticsNo = entity.getData().getLogisticsNo();

                        if (orderStates.equals("0")) {
                            //待付款
                            tvStates.setText("待付款");
                            tvCountdown.setVisibility(View.VISIBLE);
                            llGopay.setVisibility(View.VISIBLE);
                            try {
                                Date d1 = new Date();
                                long d2 = Long.valueOf(entity.getData().getEndTime());
                                long diff = d2 - d1.getTime();
                                TimeCount time = new TimeCount(diff, 1000);
                                time.start();
                            } catch (Exception e) {
                            }

                        } else if (orderStates.equals("1")) {
                            //待发货
                            tvStates.setText("待发货");
                            tvOrderPaytime.setVisibility(View.VISIBLE);
                            //如果没有拒绝寄拍时显示
                            if (entity.getData().getGoodsType().equals("1") || entity.getData().getGoodsType().equals("3")) {
                                if (entity.getData().getOrderType().equals("0")) {
                                    llGocheck.setVisibility(View.VISIBLE);
                                } else {
                                    llGocheck.setVisibility(View.GONE);
                                }
                            } else {
                                llGocheck.setVisibility(View.GONE);
                            }
                            //地址不可编辑
                            ivAddressRight.setVisibility(View.GONE);

                        } else if (orderStates.equals("2")) {
                            //待收货
                            tvStates.setText("待收货");
                            tvOrderPaytime.setVisibility(View.VISIBLE);
                            llTake.setVisibility(View.VISIBLE);
                            //如果是寄拍过的
                            // llOrderContract.setVisibility(View.VISIBLE);
                            //tvOrderChecktime.setVisibility(View.VISIBLE);
                            //地址不可编辑
                            ivAddressRight.setVisibility(View.GONE);
                            tvOrderClose.setVisibility(View.VISIBLE);
                            tvOrderClose.setText("发货时间：" + entity.getData().getDeliverTime());


                        } else if (orderStates.equals("3")) {
                            //交易成功
                            tvStates.setText("交易成功");
                            tvOrderPaytime.setVisibility(View.VISIBLE);
                            llFinish.setVisibility(View.VISIBLE);
                            //如果是寄拍过的
                            // llOrderContract.setVisibility(View.VISIBLE);
                            //tvOrderChecktime.setVisibility(View.VISIBLE);
                            //地址不可编辑
                            ivAddressRight.setVisibility(View.GONE);

                            tvOrderClose.setText("发货时间：" + entity.getData().getDeliverTime());
                            tvOrderFinishtime.setText("完成时间：" + entity.getData().getUpdateTime());
                            tvOrderClose.setVisibility(View.VISIBLE);
                            tvOrderFinishtime.setVisibility(View.VISIBLE);


                        } else {
                            //交易成功
                            llAddress.setVisibility(View.GONE);
                            tvStates.setText("已关闭");
                            tvOrderClose.setVisibility(View.VISIBLE);
                            //地址不可编辑
                            ivAddressRight.setVisibility(View.GONE);

                        }

                        if(entity.getData().getRemark()==null||entity.getData().getRemark().equals("")){

                        }else{
                            llRemark.setVisibility(View.VISIBLE);
                            tvRemark.setText(entity.getData().getRemark());
                        }

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

    public class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            try {
                tvStates.setText("已关闭");
                tvCountdown.setVisibility(View.GONE);
                llGopay.setVisibility(View.GONE);
                llAddress.setVisibility(View.GONE);
            } catch (Exception e) {

            }

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            try {
                tvCountdown.setText("剩余支付时间：\n" + DateUtils.getDistanceTime2(millisUntilFinished));
            } catch (Exception e) {

            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(MyOrderDetails.this, "支付成功", Toast.LENGTH_SHORT).show();
                        isChange = true;
                        orderdetail(orderId);
                        //CommodityPaySuccess.startActivityInstance(MyOrderDetails.this, goodsType, orderId);
                    } else {
                        Toast.makeText(MyOrderDetails.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void address(AddressEvent event) {
        if (event != null) {
            llEdit.setVisibility(View.VISIBLE);
            llAddressAdd.setVisibility(View.GONE);
            addressId = event.getAddressId();
            tvAddress.setText(event.getAddress());
            tvName.setText(event.getName());
            tvPhone.setText(event.getPhone());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wxpay(WxpayEvent event) {
        if (event != null) {
            if (event.getType().equals("3")) {
                isChange = true;
                orderdetail(orderId);
                //CommodityPaySuccess.startActivityInstance(MyOrderDetails.this, goodsType, orderId);
            }
        }
    }

    /**
     * 获取实名认证状态
     */
    private void checkRealPerson(String orderId) {
        ApiUtil.getApiService().checkRealPersonNum(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData().equals("1")) {
                            applyMailing(orderId);
                        } else if (entity.getData().equals("0")) {
                            new MiddleDialog(MyOrderDetails.this).setSureListener(() -> {
                                Authentication.startActivityInstance(MyOrderDetails.this);
                            }).show("温馨提示", "为了保障您的账户安全，请完成实名认证后在进行提现！", "取消", "去认证", false);


                        } else if (entity.getData().equals("2")) {
                            new MiddleDialog(MyOrderDetails.this).setSureListener(() -> {
                                Authentication.startActivityInstance(MyOrderDetails.this);
                            }).show("温馨提示", "一个身份证只能绑定一个账号，请更换身份信息！", "取消", "去更换", false);
                        }
                    } else {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(MyOrderDetails.this, entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }


}
