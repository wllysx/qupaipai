package com.qupp.client.ui.view.activity.mine.order;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.mine.MyAddressActivity;
import com.qupp.client.ui.view.activity.mine.balance.SetPasswordAActivity;
import com.qupp.client.ui.view.activity.mine.coupon.SelectCoupon;
import com.qupp.client.ui.view.activity.mine.pinganbank.pinganpay.PayA;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.ui.view.activity.scoreshop.PhysicalActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DateUtils;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.CustomProgressDialog;
import com.qupp.client.utils.adapter.GiftbagShowAdapter;
import com.qupp.client.utils.dialog.BelowPayDialog;
import com.qupp.client.utils.dialog.MiddleDialogWithoutTitle;
import com.qupp.client.utils.dialog.PayPasswordDialog;
import com.qupp.client.utils.event.AddressEvent;
import com.qupp.client.utils.event.CouponEvent;
import com.qupp.client.utils.event.MealEvent;
import com.qupp.client.utils.event.OrderPaySuccess;
import com.qupp.client.utils.event.WxpayEvent;
import com.qupp.client.utils.glide.GlideRoundTransform;
import com.qupp.client.utils.pay.alipay.PayResult;
import com.qupp.client.utils.secretUtils.RsaCipherUtil;
import com.qupp.client.utils.view.FullListview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
public class OrderDetails extends BaseActivity {

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
    @BindView(R.id.tv_allprice)
    TextView tvAllprice;
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
    String orderId = "", orderStatus = "";
    @BindView(R.id.ll_order_no)
    LinearLayout llOrderNo;
    @BindView(R.id.ll_order_contract)
    LinearLayout llOrderContract;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_order_paytime)
    TextView tvOrderPaytime;
    @BindView(R.id.ll_gopay)
    LinearLayout llGopay;
    @BindView(R.id.ll_take)
    LinearLayout llTake;
    @BindView(R.id.ll_finish)
    LinearLayout llFinish;
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.v_allprice)
    View vAllprice;
    @BindView(R.id.v_realprice)
    View vRealprice;
    @BindView(R.id.tv_realprice)
    TextView tvRealprice;
    @BindView(R.id.ll_realprice)
    LinearLayout llRealprice;
    @BindView(R.id.tv_gopay)
    TextView tvGopay;
    @BindView(R.id.tv_seelogistics)
    TextView tvSeelogistics;
    @BindView(R.id.tv_submittake)
    TextView tvSubmittake;
    @BindView(R.id.tv_seelogistics1)
    TextView tvSeelogistics1;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_order_fahuo)
    TextView tvOrderFahuo;
    @BindView(R.id.tv_order_chengjiao)
    TextView tvOrderChengjiao;
    @BindView(R.id.iv_states)
    ImageView ivStates;
    @BindView(R.id.iv_address_right)
    ImageView ivAddressRight;
    @BindView(R.id.tv_order_close)
    TextView tvOrderClose;
    @BindView(R.id.ll_address_add)
    LinearLayout llAddressAdd;
    @BindView(R.id.tv_orderno)
    TextView tvOrderno;
    String addressId = "", orderPrice = "";
    @BindView(R.id.tv_endprice)
    TextView tvEndprice;
    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_meal)
    TextView tvMeal;
    @BindView(R.id.tv_youhuiquan)
    TextView tvYouhuiquan;
    @BindView(R.id.ll_youhuiquan)
    LinearLayout llYouhuiquan;
    @BindView(R.id.iv_youhuiquan)
    ImageView ivYouhuiquan;
    @BindView(R.id.ll_yhallprice)
    LinearLayout llYhallprice;
    @BindView(R.id.tv_daifukuan1)
    TextView tvDaifukuan1;
    @BindView(R.id.tv_daifukuan)
    TextView tvDaifukuan;
    @BindView(R.id.tv_shifukuan)
    TextView tvShifukuan;
    private boolean issetPassword = true;
    private String logo = "";
    private CustomProgressDialog pd;
    String logisticsName = "", logisticsNo = "";
    PayPasswordDialog payPasswordDialog;
    boolean isChange = false;

    @BindView(R.id.lv_meal)
    FullListview lvMeal;
    @BindView(R.id.ll_meal)
    LinearLayout llMeal;
    GiftbagShowAdapter adapter;
    ArrayList<EntityForSimple> datas = new ArrayList<>();
    @BindView(R.id.tv_selectmeal)
    TextView tvSelectmeal;
    @BindView(R.id.ll_selectmeal)
    LinearLayout llSelectmeal;
    String auctionId;
    //套餐默认选择
    int position = -1;
    String mealId = "";
    String integralType = "";
    String cid = "", allPrice = "", endprice = "'",actualPayment = "",wallet="0";
    String type = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        pd = CustomProgressDialog.createDialog(OrderDetails.this);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        orderId = getIntent().getStringExtra("orderId");
        orderStatus = getIntent().getStringExtra("orderStatus");
        lvMeal.setFocusable(false);

        initView();
        getData();

    }

    private void initView() {
        tvTitle.setText("订单详情");
        lvMeal.setOnItemClickListener((parent, view, position, id) -> CommodityDetailsActivity.startActivityInstance(OrderDetails.this, datas.get(position).getMallGoodsId()));
    }


    public static void startActivityInstance(Context activity, String orderId, String orderStatus) {
        activity.startActivity(new Intent(activity, OrderDetails.class)
                .putExtra("orderId", orderId)
                .putExtra("orderStatus", orderStatus)
        );
    }

    public static void startActivityInstance1(Context activity, String orderId, String orderStatus) {
        activity.startActivity(new Intent(activity, OrderDetails.class)
                .putExtra("orderId", orderId)
                .putExtra("orderStatus", orderStatus).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onResume() {
        isSetPassword();
        super.onResume();
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
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        super.onPause();
    }


    @OnClick({R.id.back, R.id.ll_edit, R.id.ll_address_add, R.id.tv_copy, R.id.tv_see, R.id.tv_gopay, R.id.tv_seelogistics, R.id.tv_submittake, R.id.tv_seelogistics1, R.id.tv_selectmeal, R.id.ll_selectmeal, R.id.ll_youhuiquan})
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
                MyAddressActivity.startActivityInstance(this, "1");
                break;
            case R.id.ll_address_add:
                MyAddressActivity.startActivityInstance(this, "1");
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
                if (cid.equals("0")) {
                    cid = "";
                }
                if (integralType.equals("2") && mealId.equals("")) {
                    showToast("请选择赠品后支付");
                    return;
                }

                new BelowPayDialog(this).setSureListener(item -> {
                    if (item == 3) {
                        pd.show();
                        gopayforAli();
                    } else if (item == 2) {
                        pd.show();
                        goPayforWx();
                    } else if (item == 1||item==6) {
                        if (issetPassword) {
                            payPasswordDialog = new PayPasswordDialog(OrderDetails.this);
                            payPasswordDialog.setSureListener(code -> {
                                goPaybalance(code,item);
                                // showToast(code);
                            }).show();

                        } else {
                            new MiddleDialogWithoutTitle(OrderDetails.this).setSureListener(() -> {
                                SetPasswordAActivity.startActivityInstance(OrderDetails.this);
                            }).setCancelListener(() -> finish()).show("设置安全密码才可以支付，去设置？", "关闭", "去设置", false);
                        }
                    } else {
                        showToast(getResources().getString(R.string.pay_no));
                    }
                }).setSureListener1((type, bankid) -> goPayforBank(bankid)).show(tvAllprice.getText().toString().replace("￥", ""), "aaaa",type,wallet);
                break;
            case R.id.tv_seelogistics:
                PhysicalActivity.startActivityInstance(this, orderId, "1", logo, logisticsNo, logisticsName, "1");
                break;
            case R.id.tv_submittake:
                receipt(orderId);
                break;
            case R.id.tv_seelogistics1:
                PhysicalActivity.startActivityInstance(this, orderId, "1", logo, logisticsNo, logisticsName, "1");
                break;
            case R.id.tv_selectmeal:
                SelectMealActivity.startActivityInstance(OrderDetails.this, "2", "", auctionId, position);
                break;
            case R.id.ll_selectmeal:
                SelectMealActivity.startActivityInstance(OrderDetails.this, "2", "", auctionId, position);
                break;
            case R.id.ll_youhuiquan:
                SelectCoupon.startActivityInstance(OrderDetails.this, cid, orderId);
                break;
        }
    }

    /**
     * 确认收货
     */
    private void receipt(String orderId) {
        ApiUtil.getApiService().receipt(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        isChange = true;
                        getData();
                    } else {
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
        ApiUtil.getApiService().orderorderdetail(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData() == null) {
                            return;
                        }

                        EntityForSimple entity1 = entity.getData();

                        orderStatus = entity1.getOrderStatus();
                        auctionId = entity1.getAuctionId();
                        llGopay.setVisibility(View.GONE);
                        llTake.setVisibility(View.GONE);
                        llFinish.setVisibility(View.GONE);
                        tvLabel.setVisibility(View.GONE);
                        logisticsName = entity1.getLogisticsName();
                        logisticsNo = entity1.getLogisticsNo();

                        //地址
                        tvName.setText("收货人：" + entity1.getConsignee());
                        tvAddress.setText("地址：" + entity1.getAddress());
                        tvPhone.setText(entity1.getMobile());
                        //商品详情
                        logo = entity1.getLogo();
                        // Glide.with(OrderDetails.this).load(entity1.getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into(ivLogo);

                        RequestOptions options1 = new RequestOptions()
                                .centerCrop()
                                .apply(new RequestOptions())
                                .priority(Priority.NORMAL)//优先级
                                .transform(new GlideRoundTransform(7));
                        Glide.with(OrderDetails.this).load(entity1.getLogo()).apply(options1).into(ivLogo);
                        tvCommoditytitle.setText(entity1.getGoodsName());
                        allPrice = entity1.getTransactionPrice();
                        tvPrice.setText("￥" + allPrice);
                        tvFirstprice.setText("￥" + entity1.getPledgePrice());
                        endprice = entity1.getOrderPrice();
                        actualPayment =entity1.getActualPayment();
                        tvFinalprice.setText("￥" + endprice);
                        orderPrice = entity1.getOrderPrice();
                        tvAllprice.setText("￥" + actualPayment);
                        tvRealprice.setText("￥" + actualPayment);
                        //订单信息
                        tvOrderno.setText("订单编号：" + entity1.getOrderId());
                        tvOrderTime.setText("下单时间：" + entity1.getCreateTime());
                        tvOrderPaytime.setText("付款时间：" + entity1.getPayTime());
                        tvOrderFahuo.setText("发货时间：" + entity1.getDeliverTime());

                        integralType = entity.getData().getIntegralType();

                        tvDaifukuan.setText("实付款");
                        tvDaifukuan1.setText("实付款");
                        tvShifukuan.setText("实付款");
                        if (orderStatus.equals("0")) {
                            tvAllprice.setText("￥" + endprice);
                            tvRealprice.setText("￥" + endprice);
                            getOorderprePayOrder();
                            //待付款
                            tvStates.setText("待付款");
                            tvCountdown.setVisibility(View.VISIBLE);
                            llGopay.setVisibility(View.VISIBLE);
                            //显示底部提示语
                            tvLabel.setVisibility(View.VISIBLE);
                            tvOrderClose.setText("成交时间：" + entity1.getUpdateTime());

                            tvDaifukuan.setText("待付款");
                            tvDaifukuan1.setText("待付款");
                            tvShifukuan.setText("待付款");

                        } else if (orderStatus.equals("1")) {
                            //待发货
                            tvCountdown.setVisibility(View.GONE);
                            tvStates.setText("待发货");
                            tvOrderPaytime.setVisibility(View.VISIBLE);
                            //显示实付款
                            vRealprice.setVisibility(View.VISIBLE);
                            llRealprice.setVisibility(View.VISIBLE);
                            vAllprice.setVisibility(View.GONE);
                            //显示状态标
                            ivStates.setVisibility(View.VISIBLE);
                            ivStates.setImageResource(R.mipmap.icon_states1);
                            //地址不能编辑
                            ivAddressRight.setVisibility(View.GONE);

                            llGopay.setVisibility(View.GONE);
                            tvOrderClose.setText("成交时间：" + entity1.getUpdateTime());

                            if (entity.getData().getDiscountAmount().equals("0") || entity.getData().getDiscountAmount().equals("0.00") || entity.getData().getDiscountAmount().equals("0.0")) {
                                //没用优惠券
                                llYouhuiquan.setVisibility(View.GONE);
                                llYhallprice.setVisibility(View.GONE);
                            } else {
                                llYhallprice.setVisibility(View.GONE);
                                llYouhuiquan.setVisibility(View.VISIBLE);
                                tvYouhuiquan.setText("-￥" + allPrice);
                                tvYouhuiquan.setTextColor(Color.parseColor("#FF9E0E"));
                                tvRealprice.setText("￥0.01");
                                tvEndprice.setText("￥0.01");
                                ivYouhuiquan.setVisibility(View.GONE);
                                llYouhuiquan.setEnabled(false);
                            }

                        } else if (orderStatus.equals("2")) {
                            //待收货
                            tvStates.setText("待收货");
                            tvOrderPaytime.setVisibility(View.VISIBLE);
                            tvOrderFahuo.setVisibility(View.VISIBLE);
                            llTake.setVisibility(View.VISIBLE);
                            //显示实付款
                            vRealprice.setVisibility(View.VISIBLE);
                            llRealprice.setVisibility(View.VISIBLE);
                            vAllprice.setVisibility(View.GONE);
                            //显示状态标
                            ivStates.setVisibility(View.VISIBLE);
                            ivStates.setImageResource(R.mipmap.icon_states2);
                            //地址不能编辑
                            ivAddressRight.setVisibility(View.GONE);
                            llGopay.setVisibility(View.GONE);
                            tvOrderClose.setText("成交时间：" + entity1.getUpdateTime());

                            if (entity.getData().getDiscountAmount().equals("0") || entity.getData().getDiscountAmount().equals("0.00") || entity.getData().getDiscountAmount().equals("0.0")) {
                                //没用优惠券
                                llYouhuiquan.setVisibility(View.GONE);
                                llYhallprice.setVisibility(View.GONE);
                            } else {
                                llYhallprice.setVisibility(View.GONE);
                                llYouhuiquan.setVisibility(View.VISIBLE);
                                tvYouhuiquan.setText("-￥" + allPrice);
                                tvYouhuiquan.setTextColor(Color.parseColor("#FF9E0E"));
                                tvRealprice.setText("￥0.01");
                                tvEndprice.setText("￥0.01");
                                ivYouhuiquan.setVisibility(View.GONE);
                                llYouhuiquan.setEnabled(false);
                            }

                        } else if (orderStatus.equals("3")) {
                            //交易成功
                            tvStates.setText("交易成功");
                            tvOrderPaytime.setVisibility(View.VISIBLE);
                            tvOrderFahuo.setVisibility(View.VISIBLE);
                            tvOrderChengjiao.setVisibility(View.VISIBLE);
                            llFinish.setVisibility(View.VISIBLE);

                            //显示实付款
                            vRealprice.setVisibility(View.VISIBLE);
                            llRealprice.setVisibility(View.VISIBLE);
                            vAllprice.setVisibility(View.GONE);

                            //显示状态标
                            ivStates.setVisibility(View.VISIBLE);
                            ivStates.setImageResource(R.mipmap.icon_states3);
                            //地址不能编辑
                            ivAddressRight.setVisibility(View.GONE);

                            llGopay.setVisibility(View.GONE);
                            tvOrderChengjiao.setText("成交时间：" + entity1.getUpdateTime());

                            if (entity.getData().getDiscountAmount().equals("0") || entity.getData().getDiscountAmount().equals("0.00") || entity.getData().getDiscountAmount().equals("0.0")) {
                                //没用优惠券
                                llYouhuiquan.setVisibility(View.GONE);
                                llYhallprice.setVisibility(View.GONE);
                            } else {
                                llYhallprice.setVisibility(View.GONE);
                                llYouhuiquan.setVisibility(View.VISIBLE);
                                tvYouhuiquan.setText("-￥" + allPrice);
                                tvYouhuiquan.setTextColor(Color.parseColor("#FF9E0E"));
                                tvRealprice.setText("￥0.01");
                                tvEndprice.setText("￥0.01");
                                ivYouhuiquan.setVisibility(View.GONE);
                                llYouhuiquan.setEnabled(false);
                            }

                        } else {
                            //已关闭
                            llAddress.setVisibility(View.GONE);
                            tvStates.setText("已关闭");
                            tvOrderPaytime.setVisibility(View.VISIBLE);
                            tvOrderClose.setVisibility(View.VISIBLE);
                            //显示状态标
                            ivStates.setVisibility(View.VISIBLE);
                            ivStates.setImageResource(R.mipmap.icon_states4);
                            //地址不能编辑
                            ivAddressRight.setVisibility(View.GONE);
                            llRealprice.setVisibility(View.VISIBLE);

                            llGopay.setVisibility(View.GONE);
                            tvOrderClose.setText("关闭时间：" + entity1.getUpdateTime());
                            if (entity1.getPayTime() == null) {
                                tvOrderPaytime.setVisibility(View.GONE);
                            }

                            if (entity.getData().getDiscountAmount().equals("0") || entity.getData().getDiscountAmount().equals("0.00") || entity.getData().getDiscountAmount().equals("0.0")) {
                                //没用优惠券
                                llYouhuiquan.setVisibility(View.GONE);
                                llYhallprice.setVisibility(View.GONE);
                            } else {
                                llYhallprice.setVisibility(View.GONE);
                                llYouhuiquan.setVisibility(View.VISIBLE);
                                tvYouhuiquan.setText("-￥" + allPrice);
                                tvYouhuiquan.setTextColor(Color.parseColor("#FF9E0E"));
                                tvRealprice.setText("￥0.01");
                                tvEndprice.setText("￥0.01");
                                ivYouhuiquan.setVisibility(View.GONE);
                                llYouhuiquan.setEnabled(false);
                            }

                        }

                        if (entity.getData().getIntegralType().equals("2")) {
                            //有礼包
                            llMeal.setVisibility(View.VISIBLE);
                            if (orderStatus.equals("0") && mealId.equals("")) {
                                llSelectmeal.setVisibility(View.VISIBLE);
                            } else {
                                llSelectmeal.setVisibility(View.GONE);
                            }
                            if (!orderStatus.equals("0")) {
                                tvMeal.setVisibility(View.VISIBLE);
                                String integral = entity.getData().getIntegralRatio();
                                String worthintegral = entity.getData().getWorthRatio();
                                String mealtx = "";
                                if (!integral.equals("0") && !worthintegral.equals("0")) {
                                    mealtx = "<font color='#DD0000'>" + integral + "超值积分</font>" + "+" + "<font color='#DD0000'>" + worthintegral + "积分" + "</font>";
                                } else if (!integral.equals("0")) {
                                    mealtx = "<font color='#DD0000'>" + integral + "超值积分</font>";
                                } else if (!worthintegral.equals("0")) {
                                    mealtx = "<font color='#DD0000'>" + worthintegral + "积分" + "</font>";
                                }
                                if (entity.getData().getMallOrderList() != null && entity.getData().getMallOrderList().size() > 0) {
                                    if (mealtx.equals("")) {
                                        tvMeal.setText(Html.fromHtml("以下商品"));
                                    } else {
                                        tvMeal.setText(Html.fromHtml(mealtx + "加以下商品"));
                                    }

                                } else {
                                    tvMeal.setText(Html.fromHtml(mealtx));
                                }
                                if (entity.getData().getMallOrderList() != null && entity.getData().getMallOrderList().size() > 0) {
                                    //已支付状态可以拿到大礼包
                                    tvSelectmeal.setVisibility(View.GONE);
                                    //写入数据列表
                                    datas.clear();

                                    for (EntityForSimple entity2 : entity.getData().getMallOrderList()) {
                                        EntityForSimple e = new EntityForSimple();
                                        e.setMallGoodsId(entity2.getGoodsId());
                                        e.setMallGoodsName(entity2.getGoodsName());
                                        e.setOrderId(entity2.getOrderId());
                                        e.setLogo(entity2.getLogo());
                                        datas.add(e);
                                    }
                                    adapter = new GiftbagShowAdapter(OrderDetails.this, datas);
                                    lvMeal.setAdapter(adapter);
                                }
                            }
                        } else {
                            //无礼包
                            llMeal.setVisibility(View.GONE);
                        }

                        if(entity.getData().getDisplayArea().equals("3")){
                            //vip
                            type = "4";
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


    /**
     * 预支付订单(待支付状态下调用)
     */
    private void getOorderprePayOrder() {
        ApiUtil.getApiService().orderprePayOrder(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        //判断是否有地址
                        if (entity.getData().getMemberAddress() == null) {
                            //无默认地址
                            llEdit.setVisibility(View.GONE);
                            llAddressAdd.setVisibility(View.VISIBLE);
                            tvEndprice.setText("￥" + entity.getData().getOrderViewVo().getOrderPrice());
                        } else {
                            //有地址
                            llEdit.setVisibility(View.VISIBLE);
                            llAddressAdd.setVisibility(View.GONE);
                            tvName.setText("收货人：" + entity.getData().getMemberAddress().getConsignee());
                            tvAddress.setText("地址：" + entity.getData().getMemberAddress().getProvinceText() + entity.getData().getMemberAddress().getCityText() + entity.getData().getMemberAddress().getDistrictText() + entity.getData().getMemberAddress().getAddress());
                            tvPhone.setText(entity.getData().getMemberAddress().getMobile());
                            tvEndprice.setText("￥" + entity.getData().getOrderViewVo().getOrderPrice());
                            addressId = entity.getData().getMemberAddress().getAddressId();
                        }

                        //未支付订单倒计时
                        if (orderStatus.equals("0")) {
                            try {
                                Date d1 = new Date();
                                long d2 = Long.valueOf(entity.getData().getOrderViewVo().getEndTime());
                                long diff = d2 - d1.getTime();
                                TimeCount time = new TimeCount(diff, 1000);
                                time.start();
                            } catch (Exception e) {
                            }
                        }

                      /*  tvEndprice.setText("￥" + entity.getData().getOrderViewVo().getOrderPrice());
                        tvCommoditytitle.setText(entity.getData().getOrderViewVo().getGoodsName());
                        tvFirstprice.setText("￥" + entity.getData().getOrderViewVo().getPledgePrice());
                        tvFinalprice.setText("￥" + entity.getData().getOrderViewVo().getOrderPrice());
                        allPrice = entity.getData().getOrderViewVo().getTransactionPrice();
                        tvPrice.setText("￥" + allPrice);
                        endprice = entity.getData().getOrderViewVo().getOrderPrice();
                        tvAllprice.setText("￥" + endprice);*/
                        //已经绑定优惠券
                        if (entity.getData().getMemberCoupon() == null || entity.getData().getMemberCoupon().getId() == null) {
                            //获取可用优惠券
                            getUseCount();
                        } else {
                            cid = entity.getData().getMemberCoupon().getId();
                            tvYouhuiquan.setText("-￥" + allPrice);
                            tvYouhuiquan.setTextColor(Color.parseColor("#FF9E0E"));
                            tvAllprice.setText("￥0.01");
                            tvEndprice.setText("￥0.01");
                            ivYouhuiquan.setVisibility(View.GONE);
                            llYouhuiquan.setEnabled(false);
                        }


                        if (integralType.equals("2")) {
                            //有礼包
                            llMeal.setVisibility(View.VISIBLE);
                            tvSelectmeal.setVisibility(View.VISIBLE);
                            llSelectmeal.setVisibility(View.GONE);
                          /*  if (orderStatus.equals("0") && mealId.equals("")) {
                                llSelectmeal.setVisibility(View.VISIBLE);
                            } else {
                                llSelectmeal.setVisibility(View.GONE);
                            }*/
                            //llSelectmeal.setVisibility(View.VISIBLE);
                            tvMeal.setVisibility(View.VISIBLE);
                            String integral = entity.getData().getOrderViewVo().getIntegralRatio();
                            String worthintegral = entity.getData().getOrderViewVo().getWorthRatio();
                            String mealtx = "";
                            if (!integral.equals("0") && !worthintegral.equals("0")) {
                                mealtx = "<font color='#DD0000'>" + integral + "超值积分</font>" + "+" + "<font color='#DD0000'>" + worthintegral + "积分" + "</font>";
                            } else if (!integral.equals("0")) {
                                mealtx = "<font color='#DD0000'>" + integral + "超值积分</font>";
                            } else if (!worthintegral.equals("0")) {
                                mealtx = "<font color='#DD0000'>" + worthintegral + "积分" + "</font>";
                            }
                            if (entity.getData().getOrderViewVo().getMallOrderList() != null && entity.getData().getOrderViewVo().getMallOrderList().size() > 0) {
                                if (mealtx.equals("")) {
                                    tvMeal.setText(Html.fromHtml("以下商品"));
                                } else {
                                    tvMeal.setText(Html.fromHtml(mealtx + "加以下商品"));
                                }

                            } else {
                                tvMeal.setText(Html.fromHtml(mealtx));
                            }
                            if (entity.getData().getOrderViewVo().getMallOrderList() != null && entity.getData().getOrderViewVo().getMallOrderList().size() > 0) {
                                //写入数据列表
                                datas.clear();

                                for (EntityForSimple entity2 : entity.getData().getOrderViewVo().getMallOrderList()) {
                                    EntityForSimple e = new EntityForSimple();
                                    e.setMallGoodsId(entity2.getGoodsId());
                                    e.setMallGoodsName(entity2.getGoodsName());
                                    e.setOrderId(entity2.getOrderId());
                                    e.setLogo(entity2.getLogo());
                                    datas.add(e);
                                }
                                adapter = new GiftbagShowAdapter(OrderDetails.this, datas);
                                lvMeal.setAdapter(adapter);
                                if (entity.getData().getAuctionMeal() != null) {
                                    mealId = entity.getData().getAuctionMeal().getId();
                                }
                            } else {
                                tvSelectmeal.setVisibility(View.GONE);
                                llSelectmeal.setVisibility(View.VISIBLE);
                            }
                        } else {
                            //无礼包
                            //llMeal.setVisibility(View.GONE);
                            tvSelectmeal.setVisibility(View.GONE);
                            llSelectmeal.setVisibility(View.VISIBLE);
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

    /**
     * 获取可用优惠券数量
     */
    /**
     * 获取可用优惠券数量
     */
    private void getUseCount() {
        ApiUtil.getApiService().getUseCount(MyApplication.getToken(), orderId).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData().equals("0")) {
                            tvYouhuiquan.setText("暂无可用");
                            llYouhuiquan.setEnabled(false);
                            tvYouhuiquan.setTextColor(Color.parseColor("#B0B0B1"));
                        } else {
                            llYouhuiquan.setEnabled(true);
                            tvYouhuiquan.setText(entity.getData() + "张优惠券可用");
                            tvYouhuiquan.setTextColor(Color.parseColor("#FF9E0E"));
                            ivYouhuiquan.setVisibility(View.VISIBLE);
                        }

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
     * 预支付订单(待支付状态下调用)
     */
    private void goPaybalance(String payPassWor,int type) {
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

        ApiUtil.getApiService().orderorderPay(MyApplication.getToken(), orderId, addressId, type+"", pass, mealId, cid).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        payPasswordDialog.dismiss();
                        isChange = true;
                        toSuccessPage("0");
                        //finish();
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
     * 预支付订单(待支付状态下调用)
     */
    private void goPayforWx() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderorderPay(MyApplication.getToken(), orderId, addressId, "2", payPassWor, mealId, cid).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        wxPay(entity.getData(), "2");
                        //锁定优惠券
                        if (!cid.equals("")) {
                            llYouhuiquan.setEnabled(false);
                            ivYouhuiquan.setVisibility(View.GONE);
                        }
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
     * 预支付订单(待支付状态下调用)
     */
    private void goPayforBank(String id) {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderorderPay1(MyApplication.getToken(), orderId, addressId, "5", payPassWor, mealId, cid,id).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        PayA.startActivityInstance(OrderDetails.this,id,entity.getData().getOrderNo(),tvAllprice.getText().toString(),"2");
                        //锁定优惠券
                        if (!cid.equals("")) {
                            llYouhuiquan.setEnabled(false);
                            ivYouhuiquan.setVisibility(View.GONE);
                        }
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

    private void gopayforAli() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderorderPay(MyApplication.getToken(), orderId, addressId, "3", payPassWor, "", mealId, cid).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        payV2(entity.getData(), "2", mHandler);
                        //锁定优惠券
                        if (!cid.equals("")) {
                            llYouhuiquan.setEnabled(false);
                            ivYouhuiquan.setVisibility(View.GONE);
                        }
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
                tvCountdown.setText("剩余支付时间：" + DateUtils.getDistanceTime2(millisUntilFinished));
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
                        Toast.makeText(OrderDetails.this, "支付成功", Toast.LENGTH_SHORT).show();
                        //getBlanceAndIntegralAndFans();
                        isChange = true;
                        toSuccessPage("0");
                        getData();
                        //finish();
                    } else {
                        Toast.makeText(OrderDetails.this, "支付失败", Toast.LENGTH_SHORT).show();
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

    /**
     * 是否设置支付密码
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void address(AddressEvent event) {
        if (event != null) {
            llEdit.setVisibility(View.VISIBLE);
            llAddressAdd.setVisibility(View.GONE);
            addressId = event.getAddressId();
            tvAddress.setText("地址：" + event.getAddress());
            tvName.setText(event.getName());
            tvPhone.setText(event.getPhone());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wxpay(WxpayEvent event) {
        if (event != null) {
            if (event.getType().equals("2")) {
                //getBlanceAndIntegralAndFans();
                isChange = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOorderprePayOrderWX();
                    }
                }, 300);
                //finish();
                getData();
            }
        }
    }

    private void toSuccessPage(String count) {
        getOorderprePayOrder1();
    }

    /**
     * 获取状态判断是否跳转
     */
    private void getOorderprePayOrder1() {
        ApiUtil.getApiService().orderorderdetail(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                if (entity.getCode().equals("0")) {
                    if (entity.getData().getGiveIntegral() == 0) {
                        MyOrderPaySuccess.startActivityInstance(OrderDetails.this, "0");
                    } else {
                        MyOrderPaySuccess.startActivityInstance(OrderDetails.this, entity.getData().getGiveIntegral() + "");
                    }
                    finish();
                } else {
                    showToast(entity.getMsg());
                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    /**
     * 获取状态判断是否跳转
     */
    private void getOorderprePayOrderWX() {
        ApiUtil.getApiService().orderorderdetail(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                try {
                    MessageForSimple entity = response.body();
                    if (entity.getCode().equals("0")) {
                        if (entity.getData().getOrderStatus().equals("1")) {
                            //微信支付成功
                            if (entity.getData().getGiveIntegral() == 0) {
                                MyOrderPaySuccess.startActivityInstance(OrderDetails.this, "0");
                            } else {
                                MyOrderPaySuccess.startActivityInstance(OrderDetails.this, entity.getData().getGiveIntegral() + "");
                            }
                        } else {
                            showToast("支付处理中");
                        }
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void meal(MealEvent event) {
        if (event != null) {
            datas.clear();
            mealId = event.getData().getId();
            llSelectmeal.setVisibility(View.GONE);
            tvSelectmeal.setVisibility(View.VISIBLE);
            position = event.getPositon();

            position = event.getPositon();
            String integral = event.getData().getIntegral();
            String worthintegral = event.getData().getWorthIntegral();
            String mealtx = "";
            if (!integral.equals("0") && !worthintegral.equals("0")) {
                mealtx = "<font color='#DD0000'>" + integral + "超值积分</font>" + "+" + "<font color='#DD0000'>" + worthintegral + "积分" + "</font>";
            } else if (!integral.equals("0")) {
                mealtx = "<font color='#DD0000'>" + integral + "超值积分</font>";
            } else if (!worthintegral.equals("0")) {
                mealtx = "<font color='#DD0000'>" + worthintegral + "积分" + "</font>";
            }
            if (event.getData() != null && event.getData().getAuctionMealGoodsVoList().size() > 0) {
                if (mealtx.equals("")) {
                    tvMeal.setText(Html.fromHtml("套餐" + (position + 1) + "：" + "以下商品"));
                } else {
                    tvMeal.setText(Html.fromHtml("套餐" + (position + 1) + "：" + mealtx + "加以下商品"));
                }

            } else {
                tvMeal.setText(Html.fromHtml("套餐" + (position + 1) + "：" + mealtx));
            }

            datas = event.getData().getAuctionMealGoodsVoList();
            adapter = new GiftbagShowAdapter(OrderDetails.this, datas);
            lvMeal.setAdapter(adapter);
        }
    }

    /**
     * 优惠券回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void coupon(CouponEvent event) {
        if (event != null) {
            cid = event.getId();
            if (cid.equals("0")) {
                getUseCount();
                tvAllprice.setText("￥" + endprice);
                tvEndprice.setText("￥" + endprice);
            } else {
                tvYouhuiquan.setText("-￥" + allPrice);
                tvYouhuiquan.setTextColor(Color.parseColor("#FF9E0E"));
                tvAllprice.setText("￥0.01");
                tvEndprice.setText("￥0.01");
            }

        }

    }


}
