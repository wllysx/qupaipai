package com.qupp.client.ui.view.activity.scoreshop;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.qupp.client.ui.view.activity.mine.pinganbank.pinganpay.PayA;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.CustomProgressDialog;
import com.qupp.client.utils.adapter.pingan.PayBankAdapter;
import com.qupp.client.utils.dialog.MiddleDialogWithoutTitle;
import com.qupp.client.utils.dialog.PayPasswordDialog;
import com.qupp.client.utils.event.AddressEvent;
import com.qupp.client.utils.event.OrderPaySuccess;
import com.qupp.client.utils.event.WxpayEvent;
import com.qupp.client.utils.glide.GlideRoundTransform;
import com.qupp.client.utils.pay.alipay.PayResult;
import com.qupp.client.utils.secretUtils.RsaCipherUtil;
import com.qupp.client.utils.view.FullListview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
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
 * 积分商城 商品支付（type==1 积分加现金 type==2 纯积分）
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class CommodityPay extends BaseActivity {

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

    String type = "", goodsId = "", goodsType = "";
    @BindView(R.id.ll_paytype)
    LinearLayout llPaytype;
    @BindView(R.id.ll_submit)
    LinearLayout llSubmit;
    @BindView(R.id.ll_conversion)
    LinearLayout llConversion;
    @BindView(R.id.tv_commoditytitle)
    TextView tvCommoditytitle;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    String addressId = "";
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.ll_address_add)
    LinearLayout llAddressAdd;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    EntityForSimple entitypay;
    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.ll_pay1)
    LinearLayout llPay1;
    @BindView(R.id.tv_endprice)
    TextView tvEndprice;
    @BindView(R.id.tv_jian)
    TextView tvJian;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_jia)
    TextView tvJia;
    @BindView(R.id.tv_jiner_title)
    TextView tvJinerTitle;
    @BindView(R.id.tv_jiner_vaule)
    TextView tvJinerVaule;
    @BindView(R.id.tv_middle_add)
    TextView tvMiddleAdd;
    @BindView(R.id.tv_jifen_title)
    TextView tvJifenTitle;
    @BindView(R.id.tv_jifen_vaule)
    TextView tvJifenVaule;
    @BindView(R.id.ll_count)
    LinearLayout llCount;
    @BindView(R.id.ll_pay2)
    LinearLayout llPay2;
    @BindView(R.id.ll_pay3)
    LinearLayout llPay3;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.checkbox1_jf)
    CheckBox checkbox1Jf;
    @BindView(R.id.ll_jifenpay1)
    LinearLayout llJifenpay1;
    @BindView(R.id.checkbox2_jf)
    CheckBox checkbox2Jf;
    @BindView(R.id.ll_jifenpay2)
    LinearLayout llJifenpay2;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.checkbox_wallet)
    CheckBox checkboxWallet;
    @BindView(R.id.tv_wallet_title)
    TextView tvWalletTitle;
    @BindView(R.id.tv_wallet_value)
    TextView tvWalletValue;
    @BindView(R.id.ll_wallet)
    LinearLayout llWallet;
    @BindView(R.id.listview)
    FullListview listview;
    private boolean issetPassword = true;
    String orderId = "";
    String endprice = "";
    private CustomProgressDialog pd;
    PayPasswordDialog payPasswordDialog;
    String jifen = "0";
    String xianjin = "0";
    String priceType = "";
    int nowCount = 1;
    @BindView(R.id.ll_jifenview)
    LinearLayout llJifenview;
    String integral = "";
    @BindView(R.id.tv_ptjftitle)
    TextView tvPtjftitle;
    @BindView(R.id.tv_ptjf)
    TextView tvPtjf;
    @BindView(R.id.tv_czjftitle)
    TextView tvCzjftitle;
    @BindView(R.id.tv_czjf)
    TextView tvCzjf;

    String uperIntegralAmount = "0";
    String integralAmount = "0";
    String myallprice = "";
    String paytype = "1";
    String wallet = "0";
    String bankId = "";

    ArrayList<EntityForSimple> datas = new ArrayList<>();
    PayBankAdapter blowBankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_pay);
        unbinder = ButterKnife.bind(this);
        pd = CustomProgressDialog.createDialog(CommodityPay.this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        type = getIntent().getStringExtra("type");
        goodsId = getIntent().getStringExtra("goodsId");
        entitypay = ((EntityForSimple) getIntent().getSerializableExtra("entitypay"));
        initView();
        if (entitypay == null) {
            //来自订单列表 预支付
            getData(goodsId);
            //预支付不显示积分
            llJifenview.setVisibility(View.GONE);
            try {
                etRemark.setEnabled(false);
            } catch (Exception e) {

            }
        } else {
            if (entitypay.getMemberAddress() == null) {
                //无默认地址
                llEdit.setVisibility(View.GONE);
                llAddressAdd.setVisibility(View.VISIBLE);
            } else {
                //有地址
                llEdit.setVisibility(View.VISIBLE);
                llAddressAdd.setVisibility(View.GONE);
                tvName.setText("收货人：" + entitypay.getMemberAddress().getConsignee());
                tvAddress.setText("地址：" + entitypay.getMemberAddress().getProvinceText() + entitypay.getMemberAddress().getCityText() + entitypay.getMemberAddress().getDistrictText() + entitypay.getMemberAddress().getAddress());
                tvPhone.setText(entitypay.getMemberAddress().getMobile());
                addressId = entitypay.getMemberAddress().getAddressId();
            }
            //商品详情
            // Glide.with(CommodityPay.this).load(entitypay.getGoodsInfo().getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into(ivLogo);

            RequestOptions options1 = new RequestOptions()
                    .centerCrop()
                    .apply(new RequestOptions())
                    .priority(Priority.NORMAL)//优先级
                    .transform(new GlideRoundTransform(7));
            Glide.with(CommodityPay.this).load(entitypay.getGoodsInfo().getLogo()).apply(options1).into(ivLogo);

            if (entitypay.getGoodsInfo().getFirstCategoryId().equals(MyApplication.firstCategoryId)) {
                paytype = "1";
            } else {
                paytype = "2";
            }

            integral = entitypay.getGoodsInfo().getIntegral();
            tvIntegral.setText(entitypay.getGoodsInfo().getIntegral() + "积分");
            tvScore.setText("(" + entitypay.getGoodsInfo().getIntegral() + "积分）");
            goodsType = entitypay.getGoodsInfo().getGoodsType();
            tvCommoditytitle.setText(entitypay.getGoodsInfo().getGoodsName());
            if (entitypay.getGoodsInfo().getPriceType().equals("1")) {
                tvPrice.setText(entitypay.getGoodsInfo().getIntegral().replace(".00", "") + "积分");
                tvIntegral.setVisibility(View.GONE);
            } else if (entitypay.getGoodsInfo().getPriceType().equals("2")) {
                tvIntegral.setText("+" + entitypay.getGoodsInfo().getIntegral().replace(".00", "") + "积分");
                tvPrice.setText("￥" + entitypay.getGoodsInfo().getPrice().replace(".00", ""));
                tvPrice.setVisibility(View.VISIBLE);
            } else if (entitypay.getGoodsInfo().getPriceType().equals("3")) {
                tvPrice.setText("￥" + entitypay.getGoodsInfo().getPrice().replace(".00", ""));
                tvIntegral.setVisibility(View.GONE);
            } else {
                tvIntegral.setText("+" + entitypay.getGoodsInfo().getIntegral().replace(".00", "") + "钱包");
                tvPrice.setText("￥" + entitypay.getGoodsInfo().getPrice().replace(".00", ""));
                tvPrice.setVisibility(View.VISIBLE);
                tvJifenTitle.setText("钱包：");
                tvScore.setText("(" + entitypay.getGoodsInfo().getIntegral() + "钱包）");
            }
            tvEndprice.setText("(￥" + entitypay.getGoodsInfo().getPrice().replace(".00", "") + ")");
            endprice = entitypay.getGoodsInfo().getPrice();


            jifen = entitypay.getGoodsInfo().getIntegral().replace(".00", "");
            xianjin = entitypay.getGoodsInfo().getPrice().replace(".00", "");
            priceType = entitypay.getGoodsInfo().getPriceType();
            addPrice();
            memberWalletdetail();
        }

        blowBankAdapter = new PayBankAdapter(CommodityPay.this,datas);
        blowBankAdapter.setSureListener(position -> {
            bankId = datas.get(position).getId();
            checkbox1.setChecked(false);
            checkbox2.setChecked(false);
            checkbox3.setChecked(false);
            for(EntityForSimple data:datas){
                data.setChecked(false);
            }
            datas.get(position).setChecked(true);
            blowBankAdapter.notifyDataSetChanged();
        });
        listview.setAdapter(blowBankAdapter);
        getData();


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
                        tvWalletValue.setText("(余额￥" + wallet + ")");
                        getBlanceAndIntegralAndFans();
                    } else {
                        // showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    private void addPrice() {
        tvCount.setText(nowCount + "");
        if (goodsType.equals("2")) {
            llCount.setVisibility(View.VISIBLE);

            if (priceType.equals("1")) {
                //只积分
                tvMiddleAdd.setVisibility(View.GONE);
            } else if (priceType.equals("2")) {
                //现金加积分
            } else {
                //只现金
                tvMiddleAdd.setVisibility(View.GONE);
            }

        } else {
            llCount.setVisibility(View.GONE);
        }

        if (priceType.equals("1")) {
            //只积分
            tvJinerTitle.setVisibility(View.GONE);
            tvJinerVaule.setVisibility(View.GONE);
            llJifenview.setVisibility(View.VISIBLE);
        } else if (priceType.equals("2")) {
            //现金加积分
            llJifenview.setVisibility(View.VISIBLE);
        } else if (priceType.equals("3")) {
            //只现金
            tvJifenTitle.setVisibility(View.GONE);
            tvJifenVaule.setVisibility(View.GONE);
            llJifenview.setVisibility(View.GONE);
        } else {
            llJifenview.setVisibility(View.GONE);
            //显示钱包
            llWallet.setVisibility(View.VISIBLE);
            tvMiddleAdd.setVisibility(View.VISIBLE);
        }


        DecimalFormat df = new DecimalFormat("0.00");
        String str = df.format(nowCount * Double.valueOf(xianjin));
        tvJinerVaule.setText(str.replace(".00", ""));
        tvJifenVaule.setText((nowCount * Double.valueOf(jifen) + "").replace(".00", ""));

        tvEndprice.setText("(￥" + (nowCount * Double.valueOf(xianjin) + "").replace(".00", "") + ")");


        if (priceType.equals("4")) {
            tvScore.setText("(" + nowCount * Double.valueOf(jifen) + "钱包）");
            if (Double.valueOf(wallet) < Double.valueOf(tvJifenVaule.getText().toString())) {
                //钱包不够
                checkboxWallet.setChecked(false);
                tvWalletTitle.setTextColor(getResources().getColor(R.color.textcolor32));
                tvWalletValue.setTextColor(getResources().getColor(R.color.textcolor32));
            } else {
                checkboxWallet.setChecked(true);
                tvWalletTitle.setTextColor(getResources().getColor(R.color.textcolor7));
                tvWalletValue.setTextColor(getResources().getColor(R.color.textcolor11));
            }
        } else {
            //计算积分是否够用
            tvScore.setText("(" + nowCount * Double.valueOf(jifen) + "积分）");
            if (!uperIntegralAmount.equals("0") || !integralAmount.equals("0")) {
                if (Double.valueOf(uperIntegralAmount) < Double.valueOf(tvJifenVaule.getText().toString())) {

                } else {
                    llJifenpay2.setEnabled(true);
                    tvCzjftitle.setTextColor(getResources().getColor(R.color.textcolor7));
                    tvCzjf.setTextColor(getResources().getColor(R.color.textcolor11));
                }
                if (Double.valueOf(integralAmount) < Double.valueOf(tvJifenVaule.getText().toString())) {
                    //超值不够
                    llJifenpay1.setEnabled(false);
                    checkbox1Jf.setChecked(false);
                    tvPtjftitle.setTextColor(getResources().getColor(R.color.textcolor32));
                    tvPtjf.setTextColor(getResources().getColor(R.color.textcolor32));
                } else {
                    llJifenpay1.setEnabled(true);
                    tvPtjftitle.setTextColor(getResources().getColor(R.color.textcolor7));
                    tvPtjf.setTextColor(getResources().getColor(R.color.textcolor11));
                }

                if (Double.valueOf(uperIntegralAmount) < Double.valueOf(tvJifenVaule.getText().toString()) && Double.valueOf(integralAmount) < Double.valueOf(tvJifenVaule.getText().toString())) {
                    //都不够
                    llJifenpay1.setEnabled(false);
                    checkbox1Jf.setChecked(false);
                    tvPtjftitle.setTextColor(getResources().getColor(R.color.textcolor32));
                    tvPtjf.setTextColor(getResources().getColor(R.color.textcolor32));

                    llJifenpay2.setEnabled(false);
                    checkbox2Jf.setChecked(false);
                    tvCzjftitle.setTextColor(getResources().getColor(R.color.textcolor32));
                    tvCzjf.setTextColor(getResources().getColor(R.color.textcolor32));
                } else if (Double.valueOf(uperIntegralAmount) < Double.valueOf(tvJifenVaule.getText().toString())) {
                    //超值不够（普通够）
                    llJifenpay2.setEnabled(false);
                    checkbox2Jf.setChecked(false);
                    tvCzjftitle.setTextColor(getResources().getColor(R.color.textcolor32));
                    tvCzjf.setTextColor(getResources().getColor(R.color.textcolor32));

                    llJifenpay1.setEnabled(true);
                    checkbox1Jf.setChecked(true);
                    tvPtjftitle.setTextColor(getResources().getColor(R.color.textcolor7));
                    tvPtjf.setTextColor(getResources().getColor(R.color.textcolor11));
                } else if (Double.valueOf(integralAmount) < Double.valueOf(tvJifenVaule.getText().toString())) {
                    //普通不够（超值够）
                    llJifenpay1.setEnabled(false);
                    checkbox1Jf.setChecked(false);
                    tvPtjftitle.setTextColor(getResources().getColor(R.color.textcolor32));
                    tvPtjf.setTextColor(getResources().getColor(R.color.textcolor32));

                    llJifenpay2.setEnabled(true);
                    checkbox2Jf.setChecked(true);
                    tvCzjftitle.setTextColor(getResources().getColor(R.color.textcolor7));
                    tvCzjf.setTextColor(getResources().getColor(R.color.textcolor11));
                } else {
                    //都够

                    if (checkbox1Jf.isChecked()) {
                        checkbox1Jf.setChecked(true);
                        checkbox2Jf.setChecked(false);
                    } else {
                        checkbox1Jf.setChecked(false);
                        checkbox2Jf.setChecked(true);
                    }
                    llJifenpay1.setEnabled(true);
                    tvPtjftitle.setTextColor(getResources().getColor(R.color.textcolor7));
                    tvPtjf.setTextColor(getResources().getColor(R.color.textcolor11));

                    llJifenpay2.setEnabled(true);
                    tvCzjftitle.setTextColor(getResources().getColor(R.color.textcolor7));
                    tvCzjf.setTextColor(getResources().getColor(R.color.textcolor11));

                }

            }
        }

        if (llPay1.getVisibility() == View.VISIBLE) {
            if (!myallprice.equals("")) {
                if (Double.valueOf(str) > Double.valueOf(myallprice)) {
                    //余额不能点击
                    checkbox1.setChecked(false);
                    llPay1.setEnabled(false);
                } else {
                    //checkbox1.setChecked(true);
                    llPay1.setEnabled(true);
                }
            }

        }

        //只有寄拍不显示
        if (goodsType.equals("1")) {
            llJifenview.setVisibility(View.GONE);
            llCount.setVisibility(View.GONE);
        }


    }

    private void initView() {
        tvTitle.setText("支付订单");
        if (type.equals("1")) {
            llSubmit.setVisibility(View.VISIBLE);
            llPaytype.setVisibility(View.VISIBLE);
            tvPrice.setVisibility(View.VISIBLE);
            llConversion.setVisibility(View.GONE);
        } else {
            llSubmit.setVisibility(View.GONE);
            llPaytype.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            llConversion.setVisibility(View.VISIBLE);
        }
    }

    public static void startActivityInstance(Activity activity, String type, String goodsId, EntityForSimple entitypay) {
        activity.startActivity(new Intent(activity, CommodityPay.class)
                .putExtra("type", type)
                .putExtra("goodsId", goodsId)
                .putExtra("entitypay", entitypay)
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
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }


    @OnClick({R.id.back, R.id.ll_edit, R.id.ll_pay1, R.id.ll_pay2, R.id.ll_pay3, R.id.ll_submit, R.id.ll_conversion, R.id.ll_address_add, R.id.tv_jia, R.id.tv_jian, R.id.ll_jifenpay1, R.id.ll_jifenpay2})
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
                bankId = "";
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
                checkbox1.setChecked(true);
                for(EntityForSimple data:datas){
                    data.setChecked(false);
                }
                blowBankAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_pay2:
                bankId = "";
                checkbox2.setChecked(true);
                checkbox3.setChecked(false);
                checkbox1.setChecked(false);
                for(EntityForSimple data:datas){
                    data.setChecked(false);
                }
                blowBankAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_pay3:
                bankId = "";
                checkbox2.setChecked(false);
                checkbox3.setChecked(true);
                checkbox1.setChecked(false);
                for(EntityForSimple data:datas){
                    data.setChecked(false);
                }
                blowBankAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_submit:
                if (DoubleClick.isFastDoubleClick1()) {
                    showToast("正在唤起支付请稍后...");
                    return;
                }
                if (entitypay == null) {
                    //已有订单
                    if (checkbox1.isChecked()) {
                        //余额
                        if (issetPassword) {
                            payPasswordDialog = new PayPasswordDialog(CommodityPay.this);
                            payPasswordDialog.setSureListener(code -> {
                                goPaybalanceOrder(code);
                            }).show();

                        } else {
                            new MiddleDialogWithoutTitle(CommodityPay.this).setSureListener(() -> {
                                SetPasswordAActivity.startActivityInstance(CommodityPay.this);
                            }).setCancelListener(() -> finish()).show("设置安全密码才可以提现，去设置？", "关闭", "去设置", false);
                        }
                    }
                    if (checkbox2.isChecked()) {
                        pd.show();
                        //微信
                        goPayforWxOrder();
                    }
                    if (checkbox3.isChecked()) {
                        pd.show();
                        //支付宝
                        gopayforAliOrder();
                    }
                    //没有可支付的方式
                   /* if (!checkbox1.isChecked() && !checkbox2.isChecked() && !checkbox3.isChecked()) {
                        showToast("请选择支付方式");
                    }*/
                   if(bankId!=null&&!bankId.equals("")){
                       pd.show();
                       //支付宝
                       goPayBankOrder();
                   }
                } else {
                    //还未生成订单
                    if (checkbox1.isChecked()) {
                        //余额
                        if (issetPassword) {
                            new PayPasswordDialog(CommodityPay.this).setSureListener(new PayPasswordDialog.Sure() {
                                @Override
                                public void onSure(String code) {
                                    goPaybalance(code);
                                    // showToast(code);
                                }
                            }).show();

                        } else {
                            new MiddleDialogWithoutTitle(CommodityPay.this).setSureListener(() -> {
                                SetPasswordAActivity.startActivityInstance(CommodityPay.this);
                            }).setCancelListener(() -> finish()).show("设置安全密码才可以提现，去设置？", "关闭", "去设置", false);
                        }
                    }
                    if (checkbox2.isChecked()) {
                        //微信
                        pd.show();
                        goPayforWx();
                    }
                    if (checkbox3.isChecked()) {
                        //支付宝
                        pd.show();
                        gopayforAli();
                    }
                   /* //没有可支付的方式
                    if (!checkbox1.isChecked() && !checkbox2.isChecked() && !checkbox3.isChecked()) {
                        showToast("请选择支付方式");
                    }*/
                    if(bankId!=null&&!bankId.equals("")){
                        pd.show();
                        //支付宝
                        goPayBank();
                    }
                }
                //CommodityPaySuccess.startActivityInstance(this);
                break;
            case R.id.ll_conversion:
                orderPay();
                break;
            case R.id.tv_jia:
                nowCount++;
                addPrice();

                break;
            case R.id.tv_jian:
                if (nowCount <= 1) {
                    return;
                }
                nowCount--;
                addPrice();
                break;
            case R.id.ll_jifenpay1:
                checkbox1Jf.setChecked(true);
                checkbox2Jf.setChecked(false);
                break;
            case R.id.ll_jifenpay2:
                checkbox2Jf.setChecked(true);
                checkbox1Jf.setChecked(false);
                break;
        }
    }

    /**
     * 余额支付
     *
     * @param payPassWor
     */
    private void goPaybalance(String payPassWor) {
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

        ApiUtil.getApiService().orderPay2(MyApplication.getToken(), goodsId, addressId, "1", pass, nowCount + "", getCheck(), etRemark.getText().toString()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        EventBus.getDefault().post(new OrderPaySuccess("1"));
                        CommodityPaySuccess.startActivityInstance(CommodityPay.this, goodsType, entity.getData());
                        finish();
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
     * 余额支付(order)
     *
     * @param payPassWor
     */
    private void goPaybalanceOrder(String payPassWor) {
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

        ApiUtil.getApiService().orderPay2order(MyApplication.getToken(), goodsId, addressId, "1", pass).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        payPasswordDialog.dismiss();
                        EventBus.getDefault().post(new OrderPaySuccess("1"));
                        if (entitypay != null) {
                            CommodityPaySuccess.startActivityInstance(CommodityPay.this, goodsType, entity.getData());
                        }
                        finish();
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
     * 微信支付
     */
    private void goPayforWx() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderPay1(MyApplication.getToken(), goodsId, addressId, "2", payPassWor, nowCount + "", getCheck(), etRemark.getText().toString(),null).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        wxPay(entity.getData(), "2");
                        orderId = entity.getData().getOutTradeNo();
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
     * 微信支付(order)
     */
    private void goPayforWxOrder() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderPay1order(MyApplication.getToken(), goodsId, addressId, "2", payPassWor,null).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        wxPay(entity.getData(), "2");
                        orderId = entity.getData().getOutTradeNo();
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
     * 银行卡支付
     */
    private void goPayBank() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderPay1(MyApplication.getToken(), goodsId, addressId, "5", payPassWor, nowCount + "", getCheck(), etRemark.getText().toString(),bankId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        PayA.startActivityInstance(CommodityPay.this,bankId,entity.getData().getOrderNo(),tvEndprice.getText().toString(),"3");
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
     * 银行卡支付(order)
     */
    private void goPayBankOrder() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        String payPassWor = "";
        ApiUtil.getApiService().orderPay1order(MyApplication.getToken(), goodsId, addressId, "5", payPassWor,bankId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        wxPay(entity.getData(), "2");
                        orderId = entity.getData().getOutTradeNo();
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
     * 支付宝订单
     */
    private void gopayforAli() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        ApiUtil.getApiService().orderPay1(MyApplication.getToken(), goodsId, addressId, "3", null, nowCount + "", getCheck(), etRemark.getText().toString(),null).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        orderId = entity.getData().getOrderId();
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
     * 支付宝订单（orderid）
     */
    private void gopayforAliOrder() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        ApiUtil.getApiService().orderPay1order(MyApplication.getToken(), goodsId, addressId, "3", null,null).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        orderId = entity.getData().getOrderId();
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
     * 积分兑换
     */
    private void orderPay() {
        if (addressId.equals("")) {
            showToast("请选择地址");
            return;
        }
        ApiUtil.getApiService().orderPay(MyApplication.getToken(), goodsId, addressId, "4", nowCount + "", getCheck(), etRemark.getText().toString()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        CommodityPaySuccess.startActivityInstance(CommodityPay.this, goodsType, entity.getData());
                        finish();
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
     * 预支付订单
     */
    private void getData(String goodsId) {
        ApiUtil.getApiService().prePayOrderByOrderId(MyApplication.getToken(), goodsId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {

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
                            addressId = entity.getData().getMemberAddress().getAddressId();
                        }
                        //商品详情
                        Glide.with(CommodityPay.this).load(entity.getData().getGoodsInfo().getLogo()).apply(new RequestOptions().placeholder(R.color.line).error(R.color.line)).into(ivLogo);
                        tvIntegral.setText(entity.getData().getGoodsInfo().getIntegral() + "积分");
                        tvScore.setText("(" + entity.getData().getGoodsInfo().getIntegral() + "积分）");
                        goodsType = entity.getData().getGoodsInfo().getGoodsType();
                        tvCommoditytitle.setText(entity.getData().getGoodsInfo().getGoodsName());
                        if (entity.getData().getGoodsInfo().getPriceType().equals("1")) {
                            tvIntegral.setText(entity.getData().getGoodsInfo().getIntegral().replace(".00", "") + "积分");
                            tvPrice.setVisibility(View.GONE);
                        } else if (entity.getData().getGoodsInfo().getPriceType().equals("2")) {
                            tvIntegral.setText(entity.getData().getGoodsInfo().getIntegral().replace(".00", "") + "积分");
                            tvPrice.setText("+￥" + entity.getData().getGoodsInfo().getPrice().replace(".00", ""));
                            tvPrice.setVisibility(View.VISIBLE);
                        } else if (entity.getData().getGoodsInfo().getPriceType().equals("3")) {
                            tvIntegral.setText("￥" + entity.getData().getGoodsInfo().getPrice().replace(".00", ""));
                            tvPrice.setVisibility(View.GONE);
                        } else {
                            tvIntegral.setText(entity.getData().getGoodsInfo().getIntegral().replace(".00", "") + "钱包");
                            tvPrice.setText("+￥" + entity.getData().getGoodsInfo().getPrice().replace(".00", ""));
                            tvPrice.setVisibility(View.VISIBLE);
                            tvJifenTitle.setText("钱包：");
                        }
                        tvNum.setText("×" + entity.getData().getOrder().getNum());
                        tvNum.setVisibility(View.VISIBLE);
                        tvEndprice.setText("(￥" + (Double.valueOf(entity.getData().getGoodsInfo().getPrice()) * Double.valueOf(entity.getData().getOrder().getNum())) + ")");
                        integral = entity.getData().getGoodsInfo().getIntegral();
                        endprice = entity.getData().getGoodsInfo().getPrice();

                        if (entitypay.getGoodsInfo().getFirstCategoryId().equals(MyApplication.firstCategoryId)) {
                            paytype = "1";
                        } else {
                            paytype = "2";
                        }
                        memberWalletdetail();
                        if (entity.getData().getOrder().getRemark() == null || entity.getData().getOrder().equals("")) {
                            etRemark.setVisibility(View.GONE);
                        } else {
                            etRemark.setText("订单备注：" + entity.getData().getOrder().getRemark());
                        }
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {
                    Log.d("e", e.toString());

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    /**
     * 获取余额
     */
    private void getBlanceAndIntegralAndFans() {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        myallprice = entity.getData().getAmount();
                        tvBalance.setText("（余额￥" + entity.getData().getAmount() + "）");
                        addPrice();
                        if (Double.valueOf(entity.getData().getAmount()) < Double.valueOf(endprice)) {
                            tvBalance.setText("（余额￥" + entity.getData().getAmount() + ",余额不足）");
                            //余额不能点击
                            checkbox1.setChecked(false);
                            llPay1.setOnClickListener(null);
                            getTerminalPayment(false);
                        } else {
                            getTerminalPayment(true);
                        }


                        uperIntegralAmount = entity.getData().getSuperIntegralAmount();
                        integralAmount = entity.getData().getIntegralAmount();

                        if (!goodsType.equals("1")) {

                            tvCzjf.setText("(剩余：" + entity.getData().getSuperIntegralAmount() + "分)");
                            tvPtjf.setText("(剩余：" + entity.getData().getIntegralAmount() + "分)");
                            //判断积分
                            if (Double.valueOf(entity.getData().getSuperIntegralAmount()) < Double.valueOf(integral)) {
                                //超值不够
                                llJifenpay2.setEnabled(false);
                                checkbox2Jf.setChecked(false);
                                tvCzjftitle.setTextColor(getResources().getColor(R.color.textcolor32));
                                tvCzjf.setTextColor(getResources().getColor(R.color.textcolor32));
                                //普通积分一定够
                                checkbox1Jf.setChecked(true);
                            } else {
                                checkbox2Jf.setChecked(true);
                            }
                            if (Double.valueOf(entity.getData().getIntegralAmount()) < Double.valueOf(integral)) {
                                //普通不够
                                llJifenpay1.setEnabled(false);
                                checkbox1Jf.setChecked(false);
                                tvPtjftitle.setTextColor(getResources().getColor(R.color.textcolor32));
                                tvPtjf.setTextColor(getResources().getColor(R.color.textcolor32));
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Log.d("111", e.toString());

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
    private void getTerminalPayment(boolean isbalance) {
        //网红爆款
        /*if (isFaddish) {*/
        ApiUtil.getApiService().getPaymentSettings(MyApplication.getToken(), paytype).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        String paymentMethod = entity.getData().getPaymentMethod();
                        if (!paymentMethod.contains("1")) {
                            //没有积分支付
                            if (paymentMethod.contains("2") && paymentMethod.contains("3")) {
                                //可用余额和现金（和原来逻辑不变）
                                payment(isbalance);
                            } else if (paymentMethod.contains("2")) {
                                //只余额
                                llPay2.setVisibility(View.GONE);
                                llPay3.setVisibility(View.GONE);
                            } else {
                                //只现金
                                llPay1.setVisibility(View.GONE);
                                //第三方逻辑不变 默认余额不够状态
                                checkbox1.setChecked(false);
                                payment(false);
                            }
                        } else {
                            //有积分支付
                            paymentMethod = paymentMethod.replace("1", "");
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
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
       /* } else {
            payment(isbalance);
        }*/

    }

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
                                llPay2.setVisibility(View.GONE);
                                llPay3.setVisibility(View.GONE);
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
                        Toast.makeText(CommodityPay.this, "支付成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new OrderPaySuccess("1"));
                        if (entitypay != null) {
                            CommodityPaySuccess.startActivityInstance(CommodityPay.this, goodsType, orderId);
                        }
                        finish();
                    } else {
                        Toast.makeText(CommodityPay.this, "支付失败", Toast.LENGTH_SHORT).show();
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
                EventBus.getDefault().post(new OrderPaySuccess("1"));
                if (entitypay != null) {
                    //来自详情
                    CommodityPaySuccess.startActivityInstance(CommodityPay.this, goodsType, orderId);
                }
                finish();
            }
        }
    }

    private String getCheck() {
        String states = null;
        if (checkbox2Jf.isChecked()) {
            states = "2";
        }
        if (checkbox1Jf.isChecked()) {
            states = "1";
        }
        return states;
    }

    private void getData() {
        ApiUtil.getApiService().bankCards(MyApplication.getToken()).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        datas.clear();
                        datas.addAll(entity.getData());
                        blowBankAdapter.notifyDataSetChanged();
                    } else {
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
