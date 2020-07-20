package com.qupp.client.ui.view.activity.mine.order;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
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
import com.qupp.client.ui.view.activity.mine.MyAddressActivity;
import com.qupp.client.ui.view.activity.mine.balance.SetPasswordAActivity;
import com.qupp.client.ui.view.activity.mine.coupon.SelectCoupon;
import com.qupp.client.ui.view.activity.mine.pinganbank.pinganpay.PayA;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.CustomProgressDialog;
import com.qupp.client.utils.adapter.GiftbagShowAdapter;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 拍卖订单支付
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class OrderPay extends BaseActivity {

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
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.checkbox1)
    CheckBox checkbox1;
    @BindView(R.id.checkbox2)
    CheckBox checkbox2;
    @BindView(R.id.checkbox3)
    CheckBox checkbox3;

    String orderId = "";
    @BindView(R.id.tv_commoditytitle)
    TextView tvCommoditytitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.ll_address_add)
    LinearLayout llAddressAdd;
    @BindView(R.id.tv_firstprice)
    TextView tvFirstprice;
    @BindView(R.id.tv_finalprice)
    TextView tvFinalprice;
    @BindView(R.id.tv_allprice)
    TextView tvAllprice;
    @BindView(R.id.tv_endprice)
    TextView tvEndprice;
    String addressId = "";
    boolean issetPassword = true;
    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    String endprice = "";
    @BindView(R.id.ll_pay1)
    LinearLayout llPay1;
    @BindView(R.id.tv_meal)
    TextView tvMeal;
    @BindView(R.id.ll_meal)
    LinearLayout llMeal;
    @BindView(R.id.ll_pay2)
    LinearLayout llPay2;
    @BindView(R.id.ll_pay3)
    LinearLayout llPay3;
    @BindView(R.id.tv_youhuiquan)
    TextView tvYouhuiquan;
    @BindView(R.id.ll_youhuiquan)
    LinearLayout llYouhuiquan;
    @BindView(R.id.iv_youhuiquan)
    ImageView ivYouhuiquan;
    @BindView(R.id.tv_daifukuan)
    TextView tvDaifukuan;
    @BindView(R.id.tv_daifukuan1)
    TextView tvDaifukuan1;
    @BindView(R.id.checkbox1_1)
    CheckBox checkbox11;
    @BindView(R.id.iv_wallet)
    ImageView ivWallet;
    @BindView(R.id.tv_balance_1)
    TextView tvBalance1;
    @BindView(R.id.ll_pay1_1)
    LinearLayout llPay11;
    @BindView(R.id.ll_paytype)
    LinearLayout llPaytype;
    @BindView(R.id.iv_yue)
    ImageView ivYue;
    private CustomProgressDialog pd;
    @BindView(R.id.tv_selectmeal)
    TextView tvSelectmeal;
    @BindView(R.id.lv_meal)
    FullListview lvMeal;
    @BindView(R.id.ll_selectmeal)
    LinearLayout llSelectmeal;

    GiftbagShowAdapter adapter;
    ArrayList<EntityForSimple> datas = new ArrayList<>();
    String auctionId;
    int position = -1;
    MealEvent meal;
    String mealId;
    String cid = "";
    String allPrice = "";
    String wallet = "0";
    String displayArea = "0";
    int item = 0;
    String bankId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        pd = CustomProgressDialog.createDialog(OrderPay.this);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        orderId = getIntent().getStringExtra("orderId");
        meal = ((MealEvent) getIntent().getSerializableExtra("meal"));
        lvMeal.setFocusable(false);
        initView();
        initMeal(meal);
        memberWalletdetail();
        tvDaifukuan.setText("待付款");
        tvDaifukuan1.setText("待付款");
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
                    getOorderprePayOrder();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    private void initView() {
        tvTitle.setText("支付订单");
    }

    private void initMeal(MealEvent meal) {
        if (meal != null) {
            llMeal.setVisibility(View.VISIBLE);
            llSelectmeal.setVisibility(View.GONE);
            tvSelectmeal.setVisibility(View.VISIBLE);

            mealId = meal.getData().getId();
            auctionId = meal.getAuctionId();
            position = meal.getPositon();
            String integral = meal.getData().getIntegral();
            String worthintegral = meal.getData().getWorthIntegral();
            String mealtx = "";
            if (!integral.equals("0") && !worthintegral.equals("0")) {
                mealtx = "<font color='#DD0000'>" + integral + "超值积分</font>" + "+" + "<font color='#DD0000'>" + worthintegral + "积分" + "</font>";
            } else if (!integral.equals("0")) {
                mealtx = "<font color='#DD0000'>" + integral + "超值积分</font>";
            } else if (!worthintegral.equals("0")) {
                mealtx = "<font color='#DD0000'>" + worthintegral + "积分" + "</font>";
            }
            if (meal.getData() != null && meal.getData().getAuctionMealGoodsVoList().size() > 0) {
                if (mealtx.equals("")) {
                    tvMeal.setText(Html.fromHtml("套餐" + (position + 1) + "：" + "以下商品"));
                } else {
                    tvMeal.setText(Html.fromHtml("套餐" + (position + 1) + "：" + mealtx + "加以下商品"));
                }
            } else {
                tvMeal.setText(Html.fromHtml("套餐" + (position + 1) + "：" + mealtx));
            }
            datas = meal.getData().getAuctionMealGoodsVoList();
            adapter = new GiftbagShowAdapter(OrderPay.this, datas);
            lvMeal.setAdapter(adapter);
            lvMeal.setOnItemClickListener((parent, view, position1, id) -> {
                CommodityDetailsActivity.startActivityInstance(OrderPay.this, datas.get(position).getMallGoodsId());
            });
        } else {
            //非礼包类型
            llMeal.setVisibility(View.GONE);
        }
    }

    /**
     * 预支付订单(待支付状态下调用)
     */
    private void getOorderprePayOrder() {
        ApiUtil.getApiService().orderprePayOrder(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                try {
                    MessageForSimple entity = response.body();
                    if (entity.getCode().equals("0")) {
                        //判断是否有地址
                        if (entity.getData().getMemberAddress() == null) {
                            //无默认地址
                            llEdit.setVisibility(View.GONE);
                            llAddressAdd.setVisibility(View.VISIBLE);

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
                        //商品详情
                        RequestOptions options1 = new RequestOptions()
                                .centerCrop()
                                .apply(new RequestOptions())
                                .priority(Priority.NORMAL)//优先级
                                .transform(new GlideRoundTransform(7));
                        Glide.with(OrderPay.this).load(entity.getData().getOrderViewVo().getLogo()).apply(options1).into(ivLogo);
                        tvEndprice.setText("￥" + entity.getData().getOrderViewVo().getOrderPrice());
                        tvCommoditytitle.setText(entity.getData().getOrderViewVo().getGoodsName());
                        tvFirstprice.setText("￥" + entity.getData().getOrderViewVo().getPledgePrice());
                        tvFinalprice.setText("￥" + entity.getData().getOrderViewVo().getOrderPrice());
                        allPrice = entity.getData().getOrderViewVo().getTransactionPrice();
                        tvPrice.setText("￥" + allPrice);
                        endprice = entity.getData().getOrderViewVo().getOrderPrice();
                        tvAllprice.setText("￥" + endprice);

                        displayArea = entity.getData().getOrderViewVo().getDisplayArea();
                        //已经绑定优惠券
                        if (entity.getData().getMemberCoupon() == null || entity.getData().getMemberCoupon().getId() == null) {
                            //获取可用优惠券
                            getUseCount();
                            getBlanceAndIntegralAndFans();
                        } else {
                            cid = entity.getData().getMemberCoupon().getId();
                            tvYouhuiquan.setText("-￥" + allPrice);
                            tvYouhuiquan.setTextColor(Color.parseColor("#FF9E0E"));
                            tvAllprice.setText("￥0.01");
                            tvEndprice.setText("￥0.01");
                            ivYouhuiquan.setVisibility(View.GONE);
                            llYouhuiquan.setEnabled(false);
                            getBlanceAndIntegralAndFans1("0.01");
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


    public static void startActivityInstance(Activity activity, String orderId, MealEvent meal) {
        activity.startActivity(new Intent(activity, OrderPay.class)
                .putExtra("orderId", orderId)
                .putExtra("meal", meal)
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
    }


    @Override
    protected void onPause() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        super.onPause();
    }


    @OnClick({R.id.back, R.id.ll_edit, R.id.ll_pay1, R.id.ll_pay2, R.id.ll_pay3, R.id.ll_submit, R.id.ll_address_add, R.id.tv_selectmeal, R.id.ll_youhuiquan, R.id.ll_pay1_1})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_address_add:
                MyAddressActivity.startActivityInstance(this, "1");
                break;
            case R.id.ll_edit:
                MyAddressActivity.startActivityInstance(this, "1");
                break;
            case R.id.ll_pay1:
                checkbox11.setChecked(false);
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
                checkbox1.setChecked(true);
                bankId = "";
                break;
            case R.id.ll_pay1_1:
                checkbox11.setChecked(true);
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
                checkbox1.setChecked(false);
                bankId = "";
                break;
            case R.id.ll_pay2:
                checkbox11.setChecked(false);
                checkbox2.setChecked(true);
                checkbox3.setChecked(false);
                checkbox1.setChecked(false);
                bankId = "";
                break;
            case R.id.ll_pay3:
                checkbox11.setChecked(false);
                checkbox2.setChecked(false);
                checkbox3.setChecked(true);
                checkbox1.setChecked(false);
                bankId = "";
                break;
            case R.id.ll_submit:
                if (cid.equals("0")) {
                    cid = "";
                }
                item = 0;
                if (checkbox1.isChecked()) {
                    item = 1;
                }
                if (checkbox2.isChecked()) {
                    item = 2;
                }
                if (checkbox3.isChecked()) {
                    item = 3;
                }
                if (checkbox11.isChecked()) {
                    item = 6;
                }

                if (item == 3) {
                    pd.show();
                    gopayforAli();
                } else if (item == 2) {
                    pd.show();
                    goPayforWx();
                } else if (item == 1 || item == 6) {
                    if (issetPassword) {
                        new PayPasswordDialog(OrderPay.this).setSureListener(code -> {
                            goPaybalance(code, item + "");
                        }).show();

                    } else {
                        new MiddleDialogWithoutTitle(OrderPay.this).setSureListener(() -> {
                            SetPasswordAActivity.startActivityInstance(OrderPay.this);
                        }).setCancelListener(() -> finish()).show("设置安全密码才可以提现，去设置？", "关闭", "去设置", false);
                    }
                } else {
                   // showToast(getResources().getString(R.string.pay_no));
                    if(bankId!=null&&!bankId.equals("")){
                        goPayforBnak();
                    }
                }
                break;

            case R.id.tv_selectmeal:
                SelectMealActivity.startActivityInstance(OrderPay.this, "2", "", auctionId, position);
                break;
            case R.id.ll_youhuiquan:
                SelectCoupon.startActivityInstance(OrderPay.this, cid, orderId);
                break;
        }
    }


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
     * 获取用户信息积分等信息
     */
    private void getBlanceAndIntegralAndFans() {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvBalance.setText("（余额￥" + entity.getData().getAmount() + "）");
                        if (Double.valueOf(entity.getData().getAmount()) < Double.valueOf(endprice)) {
                            tvBalance.setText("（余额￥" + entity.getData().getAmount() + ",余额不足）");
                            ivYue.setImageResource(R.mipmap.ye_unenable);
                            //余额不能点击
                            checkbox1.setChecked(false);
                            llPay1.setEnabled(false);

                            if (displayArea.equals("3")) {
                                getTerminalPayment(false);
                            } else {
                                llPay11.setVisibility(View.GONE);
                                payment(false);
                            }
                        } else {
                            llPay1.setEnabled(true);
                            ivYue.setImageResource(R.mipmap.zffs_zhye);
                            if (displayArea.equals("3")) {
                                getTerminalPayment(true);
                            } else {
                                llPay11.setVisibility(View.GONE);
                                payment(true);
                            }
                        }
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
     * 判断第三方支付状态(一对一)
     *
     * @param isbalance 积分是否可用
     */
    private void getTerminalPayment(boolean isbalance) {
        ApiUtil.getApiService().getPaymentSettings(MyApplication.getToken(), "4").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        String paymentMethod = entity.getData().getPaymentMethod();
                        //有积分支付（积分商城积分支付不起作用）
                        paymentMethod = paymentMethod.replace("1", "");
                        if (paymentMethod.contains("4")) {
                            //有钱包支付
                            llPay11.setVisibility(View.VISIBLE);
                        } else {
                            llPay11.setVisibility(View.GONE);
                        }
                        paymentMethod = paymentMethod.replace("4", "");
                        if (paymentMethod.contains("2") && paymentMethod.contains("3")) {
                            //可用余额和现金（和原来逻辑不变）
                            payment(isbalance);
                        } else if (paymentMethod.contains("2")) {
                            //只余额
                            llPay2.setVisibility(View.GONE);
                            llPay3.setVisibility(View.GONE);
                        } else if (paymentMethod.contains("3")) {
                            //只现金
                            llPay1.setVisibility(View.GONE);
                            //第三方逻辑不变 默认余额不够状态
                            checkbox1.setChecked(false);
                            payment(false);
                        } else {

                            //判断钱包重新排序
                            if (llPay11.getVisibility() == View.VISIBLE) {
                                llPay1.setVisibility(View.GONE);
                                llPay2.setVisibility(View.GONE);
                                llPay3.setVisibility(View.GONE);
                                checkbox1.setChecked(false);
                                checkbox2.setChecked(false);
                                checkbox3.setChecked(false);
                                //只有钱包
                                if (Double.valueOf(wallet) < Double.valueOf(tvAllprice.getText().toString().replace("￥", ""))) {
                                    tvBalance1.setText("（钱包￥" + wallet + ",余额不足）");
                                    ivWallet.setImageResource(R.mipmap.qianbao_un);
                                    //余额不能点击
                                    checkbox11.setChecked(false);
                                    llPay11.setEnabled(false);
                                } else {
                                    tvBalance1.setText("（余额￥" + wallet + "）");
                                    llPay11.setEnabled(true);
                                    ivWallet.setImageResource(R.mipmap.qianbao_is);
                                    checkbox11.setChecked(true);
                                }

                            } else {
                                //只有积分
                                llPaytype.setVisibility(View.GONE);
                                checkbox1.setChecked(false);
                                checkbox2.setChecked(false);
                                checkbox3.setChecked(false);
                            }
                        }
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (
                        Exception e) {
                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    /**
     * 判断第三方支付状态
     *
     * @param isbalance 积分是否可用
     */
    private void payment(boolean isbalance) {
        ApiUtil.getApiService().getTerminalPayment("2").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        boolean iswx = entity.getData().getPaymentMethod().contains("1");
                        boolean isali = entity.getData().getPaymentMethod().contains("2");

                        if (isbalance) {
                            //正常显示隐藏
                            if (!iswx) {
                                //微信隐藏
                                llPay2.setVisibility(View.GONE);
                            }
                            if (!isali) {
                                //支付宝隐藏
                                llPay3.setVisibility(View.GONE);
                            }
                        } else {
                            //显示隐藏和勾选
                            if (iswx && isali) {
                                //微信 勾选
                                //都显示
                                checkbox2.setChecked(true);
                                checkbox3.setChecked(false);
                            } else if (iswx) {
                                //微信 勾选
                                //支付宝隐藏
                                checkbox2.setChecked(true);
                                checkbox3.setChecked(false);
                                llPay3.setVisibility(View.GONE);
                            } else if (isali) {
                                //支付宝 勾选
                                //微信隐藏
                                checkbox3.setChecked(true);
                                checkbox2.setChecked(false);
                                llPay2.setVisibility(View.GONE);
                            } else {
                                //都不勾选
                                //支付宝微信 都隐藏
                                checkbox3.setChecked(false);
                                checkbox2.setChecked(false);
                            }
                        }

                        //判断钱包重新排序
                        if (llPay11.getVisibility() == View.VISIBLE) {
                            if (Double.valueOf(wallet) < Double.valueOf(tvAllprice.getText().toString().replace("￥", ""))) {
                                tvBalance1.setText("（钱包￥" + wallet + ",余额不足）");
                                ivWallet.setImageResource(R.mipmap.qianbao_un);
                                //余额不能点击
                                checkbox11.setChecked(false);
                                llPay11.setEnabled(false);
                            } else {
                                tvBalance1.setText("（余额￥" + wallet + "）");
                                llPay11.setEnabled(true);
                                ivWallet.setImageResource(R.mipmap.qianbao_is);
                                if ((llPay1.getVisibility() == View.VISIBLE && !checkbox1.isChecked()) || llPay1.getVisibility() == View.GONE) {
                                    //去勾选钱包
                                    checkbox11.setChecked(true);
                                    checkbox2.setChecked(false);
                                    checkbox3.setChecked(false);
                                }
                            }

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
    private void goPaybalance(String payPassWor, String type) {
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

        ApiUtil.getApiService().orderorderPay(MyApplication.getToken(), orderId, addressId, type + "", pass, mealId, cid).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                try {
                    MessageForSimple entity = response.body();
                    if (entity.getCode().equals("0")) {
                        EventBus.getDefault().post(new OrderPaySuccess("1"));
                        toSuccessPage("0");
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
                try {
                    MessageForSimple entity = response.body();
                    if (entity.getCode().equals("0")) {
                        //锁定优惠券
                        if (!cid.equals("")) {
                            llYouhuiquan.setEnabled(false);
                            ivYouhuiquan.setVisibility(View.GONE);
                        }
                        wxPay(entity.getData(), "2");
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
    private void goPayforBnak() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderorderPay1(MyApplication.getToken(), orderId, addressId, "5", payPassWor, mealId, cid,bankId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                try {
                    MessageForSimple entity = response.body();
                    if (entity.getCode().equals("0")) {
                        //锁定优惠券
                        if (!cid.equals("")) {
                            llYouhuiquan.setEnabled(false);
                            ivYouhuiquan.setVisibility(View.GONE);
                        }
                        PayA.startActivityInstance(OrderPay.this,bankId,entity.getData().getOrderNo(),tvAllprice.getText().toString(),"2");
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
                        //锁定优惠券
                        if (!cid.equals("")) {
                            llYouhuiquan.setEnabled(false);
                            ivYouhuiquan.setVisibility(View.GONE);
                        }
                        payV2(entity.getData(), "2", mHandler);
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


    /**
     * 是否设置支付密码
     */
    private void isSetPassword() {
        ApiUtil.getApiService().isSetPassword(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                try {
                    EntityForSimpleString entity = response.body();
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
                        Toast.makeText(OrderPay.this, "支付成功", Toast.LENGTH_SHORT).show();
                        //getBlanceAndIntegralAndFans();
                        EventBus.getDefault().post(new OrderPaySuccess("1"));

                        toSuccessPage("0");
                    } else {
                        Toast.makeText(OrderPay.this, "支付失败", Toast.LENGTH_SHORT).show();
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
                EventBus.getDefault().post(new OrderPaySuccess("1"));
                //  toSuccessPage("0");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOorderprePayOrderWX();
                    }
                }, 300);
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
                try {
                    MessageForSimple entity = response.body();
                    if (entity.getCode().equals("0")) {
                        if (entity.getData().getGiveIntegral() == 0) {
                            MyOrderPaySuccess.startActivityInstance(OrderPay.this, "0");
                        } else {
                            MyOrderPaySuccess.startActivityInstance(OrderPay.this, entity.getData().getGiveIntegral() + "");
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
                                MyOrderPaySuccess.startActivityInstance(OrderPay.this, "0");
                            } else {
                                MyOrderPaySuccess.startActivityInstance(OrderPay.this, entity.getData().getGiveIntegral() + "");
                            }
                        } else {
                            showToast("支付正在处理请稍后查看");
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
        initMeal(event);
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
                getBlanceAndIntegralAndFans1(endprice);
            } else {
                tvYouhuiquan.setText("-￥" + allPrice);
                tvYouhuiquan.setTextColor(Color.parseColor("#FF9E0E"));
                tvAllprice.setText("￥0.01");
                tvEndprice.setText("￥0.01");
                getBlanceAndIntegralAndFans1("0.01");
            }

        }

    }

    private void getBlanceAndIntegralAndFans1(String endprice) {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvBalance.setText("（余额￥" + entity.getData().getAmount() + "）");
                        if (Double.valueOf(entity.getData().getAmount()) < Double.valueOf(endprice)) {
                            tvBalance.setText("（余额￥" + entity.getData().getAmount() + ",余额不足）");
                            ivYue.setImageResource(R.mipmap.ye_unenable);
                            //余额不能点击
                            checkbox1.setChecked(false);
                            llPay1.setEnabled(false);
                            if (displayArea.equals("3")) {
                                getTerminalPayment(false);
                            } else {
                                llPay11.setVisibility(View.GONE);
                                payment(false);
                            }
                        } else {
                            ivYue.setImageResource(R.mipmap.zffs_zhye);
                            llPay1.setEnabled(true);
                            if (displayArea.equals("3")) {
                                getTerminalPayment(true);
                            } else {
                                llPay11.setVisibility(View.GONE);
                                payment(true);
                            }
                        }
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

}
