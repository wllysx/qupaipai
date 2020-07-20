package com.qupp.client.ui.view.activity.mine.pinganbank.pinganpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.TimeCount2;
import com.qupp.client.utils.dialog.UnbandCardDialog;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 填写支付信息（验证码）
 */

public class PayA extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    String payScene = "1";
    String orderNo = "";
    String bankCardId = "";
    String payDate = "";
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_amount1)
    TextView tvAmount1;
    @BindView(R.id.tv_bankName)
    TextView tvBankName;
    @BindView(R.id.tv_cardtype1)
    TextView tvCardtype1;
    @BindView(R.id.tv_cardtype2)
    TextView tvCardtype2;
    String amount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_banka);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(PayA.this), 0, 0);
        bankCardId = getIntent().getStringExtra("bankCardId");
        orderNo = getIntent().getStringExtra("orderNo");
        amount = getIntent().getStringExtra("amount");
        payScene = getIntent().getStringExtra("payScene");
        setStateColor(false);
        initView();
        getdata();
    }


    private void initView() {
        tvTitle.setText("银行卡支付");
        tvAmount.setText(amount);
        tvAmount1.setText(amount);
    }


    /**
     * 启动页面
     *
     * @param activity
     */
    public static void startActivityInstance(Activity activity, String bankCardId, String orderNo,String amount,String payScene) {
        activity.startActivity(new Intent(activity, PayA.class).putExtra("bankCardId", bankCardId).putExtra("orderNo", orderNo).putExtra("amount",amount).putExtra("payScene",payScene)
        );
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


    @OnClick({R.id.bt_login, R.id.back, R.id.tv_wenti, R.id.tv_code})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.bt_login:
                submit();

                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_code:
                getcode();
                break;
            case R.id.tv_wenti:
                showToast("问题");
        }
    }


    /**
     * 获取验证码
     */
    private void getdata() {
        ApiUtil.getApiService().bankCards(MyApplication.getToken(), bankCardId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {

                        tvBankName.setText(entity.getData().getBankName()+"("+entity.getData().getShortCardCode()+")");
                        if(entity.getData().getBankCardType().equals("1")){
                            tvCardtype1.setVisibility(View.VISIBLE);
                            tvCardtype2.setVisibility(View.GONE);
                        }else{
                            tvCardtype2.setVisibility(View.VISIBLE);
                            tvCardtype1.setVisibility(View.GONE);
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
     * 获取验证码
     */
    private void getcode() {

        HashMap map = new HashMap();
        map.put("payScene", payScene);
        map.put("orderNo",  orderNo);
        map.put("bankCardId", bankCardId);
        JSONObject jasonObject = new JSONObject(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jasonObject.toString());

        ApiUtil.getApiService().messageCode(MyApplication.getToken(),body).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        payDate = entity.getData().getPayDate();
                        TimeCount2 time = new TimeCount2(60000, 1000, tvCode, "获取");
                        time.start();
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
     * 提交subit
     */
    private void submit() {
        HashMap map = new HashMap();
        map.put("orderNo", orderNo);
        map.put("bankCardId",  bankCardId);
        map.put("messageCode", etCode.getText().toString());
        map.put("payScene", payScene);
        map.put("payTime", payDate);
        JSONObject jasonObject = new JSONObject(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jasonObject.toString());

        ApiUtil.getApiService().paymentconfirm(MyApplication.getToken(),body).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        PayB.startActivityInstance(PayA.this,amount,tvBankName.getText().toString());
                        finish();
                    } else {
                        showToast(entity.getMsg());
                        new UnbandCardDialog(PayA.this).setSureListener(() -> {
                        }).show("提交订单失败", "支付订单提交失败，失败原因" + entity.getMsg());
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
