package com.qupp.client.ui.view.activity.mine.balance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.mine.pinganbank.pinganpay.PayA;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.CustomProgressDialog;
import com.qupp.client.utils.dialog.BelowPayDialog1;
import com.qupp.client.utils.dialog.MiddleDialogBank;
import com.qupp.client.utils.event.WxpayEvent;
import com.qupp.client.utils.pay.alipay.PayResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 充值
 */

public class TopUpActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.bt_submit)
    TextView btSubmit;
    private CustomProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_topup);
        pd = CustomProgressDialog.createDialog(TopUpActivity.this);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(TopUpActivity.this), 0, 0);
        setStateColor(false);
        initView();
        getBlanceAndIntegralAndFans();

    }


    private void initView() {
        tvTitle.setText("充值");
        etPrice.addTextChangedListener(textWatcher);

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
                        Toast.makeText(TopUpActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        //getBlanceAndIntegralAndFans();
                        finish();
                    } else {
                        Toast.makeText(TopUpActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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

        ;
    };


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            judgeNumber(s);
            String content = etPrice.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ivClose.setVisibility(View.GONE);
                btSubmit.setClickable(false);
                btSubmit.setEnabled(false);
                btSubmit.setBackgroundResource(R.drawable.bg_login_unable);
            } else {
                ivClose.setVisibility(View.VISIBLE);
                btSubmit.setClickable(true);
                btSubmit.setEnabled(true);
                btSubmit.setBackgroundResource(R.drawable.bg_login_enable);
            }
        }
    };

    /**
     * 金额输入框中的内容限制（最大：小数点前五位，小数点后2位）
     *
     * @param edt
     */
    public void judgeNumber(Editable edt) {

        String temp = edt.toString();

        if (temp.equals(".")) {
            edt.clear();
            return;
        }

        int posDot = temp.indexOf(".");//返回指定字符在此字符串中第一次出现处的索引
        if (posDot <= 0) {//不包含小数点
            if (temp.length() <= 6) {
                return;//小于五位数直接返回
            } else {
                edt.delete(6, 7);//大于五位数就删掉第六位（只会保留五位）
                return;
            }
        }
        if (temp.length() - posDot - 1 > 2)//如果包含小数点
        {
            edt.delete(posDot + 3, posDot + 4);//删除小数点后的第三位
        }
    }

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, TopUpActivity.class));
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


    @OnClick({R.id.back, R.id.iv_close, R.id.bt_submit, R.id.tv_record, R.id.tv_shuoming})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_close:
                etPrice.setText("");
                break;
            case R.id.bt_submit:
                if (etPrice.getText().toString().equals("")) {
                    showToast("请输入金额");
                    return;
                }
                if (etPrice.getText().toString().endsWith(".")) {
                    showToast("请输入正确金额");
                    return;
                }
                getTerminalPayment();
                break;
            case R.id.tv_record:
                TopUpRecordActivity.startActivityInstance(this, tvAmount.getText().toString());
                break;
            case R.id.tv_shuoming:
                new MiddleDialogBank(this).setSureListener(() -> {

                }).show();
                break;
        }
    }

    private void getTerminalPayment() {
        ApiUtil.getApiService().getTerminalPayment("2").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        boolean iswx = entity.getData().getPaymentMethod().contains("1");
                        boolean isali = entity.getData().getPaymentMethod().contains("2");
                        if (!iswx && !isali) {
                            showToast(getResources().getString(R.string.pay_no));
                        } else {
                            new BelowPayDialog1(TopUpActivity.this).setSureListener(item -> {
                                pd.show();
                                recharge(item);
                            }).show(iswx, isali);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    /**
     * 充值
     */
    private void recharge(int type) {
        //payV2("","1",mHandler);
        if (type == 1) {
            //支付宝
            ApiUtil.getApiService().recharge(MyApplication.getToken(), etPrice.getText().toString(), type + "").enqueue(new Callback<EntityForSimpleString>() {
                @Override
                public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                    EntityForSimpleString entity = response.body();
                    try {
                        if (entity.getCode().equals("0")) {
                            payV2(entity.getData(), "1", mHandler);
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

                    t.toString();
                }
            });
        } else {
            ApiUtil.getApiService().recharge(MyApplication.getToken(), etPrice.getText().toString(), type + "", "").enqueue(new Callback<MessageForSimple>() {
                @Override
                public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                    MessageForSimple entity = response.body();
                    try {
                        if (entity.getCode().equals("0")) {
                            wxPay(entity.getData(), "1");
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

    }

    /**
     * 充值银行卡
     */
    private void rechargeBank(String id) {

        ApiUtil.getApiService().recharge1(MyApplication.getToken(), etPrice.getText().toString(),"3", id).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        PayA.startActivityInstance(TopUpActivity.this,id,entity.getData().getOrderNo(),etPrice.getText().toString(),"1");
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
    public void wxpay(WxpayEvent event) {
        if (event != null) {
            if (event.getType().equals("1")) {
                //getBlanceAndIntegralAndFans();
                finish();
            }
        }
    }

}
