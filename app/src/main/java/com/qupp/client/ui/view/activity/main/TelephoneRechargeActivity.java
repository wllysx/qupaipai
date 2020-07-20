package com.qupp.client.ui.view.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
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

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.mine.balance.SetPasswordAActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CustomProgressDialog;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.TelephoneAdapter;
import com.qupp.client.utils.dialog.BelowPayTelephoneDialog;
import com.qupp.client.utils.dialog.MiddleDialogWithoutTitle;
import com.qupp.client.utils.dialog.PayPasswordDialog;
import com.qupp.client.utils.event.WxpayEvent;
import com.qupp.client.utils.pay.alipay.PayResult;
import com.qupp.client.utils.secretUtils.RsaCipherUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 话费充值
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class TelephoneRechargeActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    TelephoneAdapter mAdapter;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    ArrayList<EntityForSimple> entityForSimples = new ArrayList<>();
    private static final int SDK_PAY_FLAG = 1;
    /**
     * 1 只积分  2 积分加现金  3只现金
     */
    String  paytype="", paymentMethod="";
    private CustomProgressDialog pd;
    private boolean issetPassword = true;
    PayPasswordDialog payPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(TelephoneRechargeActivity.this), 0, 0);
        setStateColor(false);
        pd = CustomProgressDialog.createDialog(TelephoneRechargeActivity.this);
        initView();
    }


    private void initView() {
        tvTitle.setText("话费充值");
        etPhone.addTextChangedListener(textWatcher);
        initPayType();

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if (charSequence == null || charSequence.length() == 0) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0;i<charSequence.length();i++) {
                if (i != 3 && i != 8 && charSequence.charAt(i) == ' ') {
                    continue;
                } else {
                    stringBuilder.append(charSequence.charAt(i));
                    if ((stringBuilder.length() == 4 || stringBuilder.length() == 9)
                            && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
                        stringBuilder.insert(stringBuilder.length() - 1, ' ');
                    }
                }
            }
            if (!stringBuilder.toString().equals(charSequence.toString())) {
                int index = start + 1;
                if (stringBuilder.charAt(start) == ' ') {
                    if (before == 0) {
                        index++;
                    } else {
                        index--;
                    }
                } else {
                    if (before == 1) {
                        index--;
                    }
                }
                etPhone.setText(stringBuilder.toString());
                etPhone.setSelection(index);

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ivClose.setVisibility(View.GONE);
            } else {
                ivClose.setVisibility(View.VISIBLE);
            }

            if (etPhone.getText().toString().length()==13) {
                //可以点击
                etPhone.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                for(EntityForSimple entity:entityForSimples){
                    entity.setChecked(true);
                    if(mAdapter!=null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            } else if(etPhone.getText().toString().equals("")){
                //不加粗
                etPhone.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }else{
                etPhone.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //不可点击
                for(EntityForSimple entity:entityForSimples){
                    entity.setChecked(false);
                    entity.setSelected(false);
                    if(mAdapter!=null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new TelephoneAdapter(data,paytype);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 3; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 10);
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(data.get(position).isChecked()) {
                for(EntityForSimple entity:entityForSimples){
                    entity.setSelected(false);
                }
                data.get(position).setSelected(true);
                mAdapter.notifyDataSetChanged();
                MyApplication.closeKeyboard(TelephoneRechargeActivity.this);
                new BelowPayTelephoneDialog(TelephoneRechargeActivity.this).setSureListener(item -> {
                    pay(item + "",data.get(position).getId());
                }).show(data.get(position).getPanelAmount(),data.get(position).getIntegral(),paymentMethod);
            }
        });


    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, TelephoneRechargeActivity.class)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSetPassword();
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


    @OnClick({R.id.back, R.id.iv_close, R.id.tv_record, R.id.tv_menu})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_close:
                etPhone.setText("");
                for(EntityForSimple entity:entityForSimples){
                    entity.setChecked(false);
                    entity.setSelected(false);
                    if(mAdapter!=null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.tv_record:
                TelephoneRecordActivity.startActivityInstance(TelephoneRechargeActivity.this);
                break;
            case R.id.tv_menu:
                TelephoneGuizeActivity.startActivityInstance(this);
                break;
        }
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


    private void initPayType() {
        ApiUtil.getApiService().getPaymentSettings(MyApplication.getToken(),"0").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        paymentMethod = entity.getData().getPaymentMethod();
                        if(paymentMethod.contains("1")){
                            if(paymentMethod.contains("2")||paymentMethod.contains("3")){
                                //积分加现金
                                paytype = "2";
                            }else{
                                //只积分
                                paytype = "1";
                            }

                        }else{
                            //只现金
                            paytype = "3";
                        }
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

    private void getData() {
        ApiUtil.getApiService().oilCardPricelist(MyApplication.getToken(),"0").enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        entityForSimples.clear();
                        entityForSimples.addAll(entity.getData());
                        initAdapter(entityForSimples);
                        etPhone.setText(MyApplication.getPhone());
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

    private void pay(String type,String id){
        if(type.equals("0")||type.equals("4")){
            String integralType = "";
            if(type.equals("0")){
                integralType = "2";
            }else{
                integralType = "1";
            }
            //积分支付
            ApiUtil.getApiService().juheRecharge(MyApplication.getToken(),etPhone.getText().toString().replace(" ",""),"1",id,null,integralType ).enqueue(new Callback<EntityForSimpleString>() {
                @Override
                public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                    EntityForSimpleString entity = response.body();
                    try {
                        if (entity.getCode().equals("0")) {
                            //showToast("支付成功");
                            TelephoneRechargeSuccess.startActivityInstance(TelephoneRechargeActivity.this);
                            MyApplication.closeKeyboard(TelephoneRechargeActivity.this);
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
        }else if(type.equals("1")){
            //余额支付
            if (issetPassword) {
                payPasswordDialog = new PayPasswordDialog(TelephoneRechargeActivity.this);
                payPasswordDialog.setSureListener(code -> {

                    String pass = "";
                    try {
                        pass = RsaCipherUtil.encrypt(code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ApiUtil.getApiService().juheRecharge(MyApplication.getToken(),etPhone.getText().toString().replace(" ",""),"2",id,pass,null).enqueue(new Callback<EntityForSimpleString>() {
                        @Override
                        public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                            EntityForSimpleString entity = response.body();
                            try {
                                if (entity.getCode().equals("0")) {
                                   // showToast("支付成功");
                                    TelephoneRechargeSuccess.startActivityInstance(TelephoneRechargeActivity.this);
                                    payPasswordDialog.dismiss();
                                    MyApplication.closeKeyboard(TelephoneRechargeActivity.this);
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
                }).show();

            } else {
                new MiddleDialogWithoutTitle(TelephoneRechargeActivity.this).setSureListener(() -> {
                    SetPasswordAActivity.startActivityInstance(TelephoneRechargeActivity.this);
                }).setCancelListener(() -> finish()).show("设置安全密码才可以话费充值，去设置？", "关闭", "去设置", false);
            }
        }else if(type.equals("2")){
            //微信
            ApiUtil.getApiService().juheRecharge(MyApplication.getToken(),etPhone.getText().toString().replace(" ",""),"3",id).enqueue(new Callback<MessageForSimple>() {
                @Override
                public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                    MessageForSimple entity = response.body();
                    try {
                        if (entity.getCode().equals("0")) {
                            wxPay(entity.getData(), "2");
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
        }else if(type.equals("3")){
            //支付宝
            ApiUtil.getApiService().juheRecharge(MyApplication.getToken(),etPhone.getText().toString().replace(" ",""),"4",id).enqueue(new Callback<MessageForSimple>() {
                @Override
                public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                    MessageForSimple entity = response.body();
                    try {
                        if (entity.getCode().equals("0")) {
                            pd.show();
                            payV2(entity.getData().getOrderStr(), "3", mHandler);
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
                      //  Toast.makeText(TelephoneRechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        TelephoneRechargeSuccess.startActivityInstance(TelephoneRechargeActivity.this);
                    } else {
                        Toast.makeText(TelephoneRechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
    public void wxpay(WxpayEvent event) {
        if (event != null) {
            if (event.getType().equals("2")) {
               // showToast("支付成功");
                TelephoneRechargeSuccess.startActivityInstance(TelephoneRechargeActivity.this);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
}
