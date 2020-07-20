package com.qupp.client.ui.view.activity.mine.balance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.main.DefaultWeb;
import com.qupp.client.ui.view.activity.scoreshop.Authentication;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.dialog.BelowPayTypeDialog;
import com.qupp.client.utils.dialog.MiddleDialog;
import com.qupp.client.utils.dialog.MiddleDialogWithoutTitle;
import com.qupp.client.utils.dialog.PayPasswordDialog;
import com.qupp.client.utils.dialog.WithdrawDialog;
import com.qupp.client.utils.event.AmountEvent;
import com.qupp.client.utils.secretUtils.RsaCipherUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 提现
 */

public class WithDrawActivity extends BaseActivity {

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
    @BindView(R.id.tv_paytype)
    TextView tvPaytype;
    @BindView(R.id.ll_to)
    LinearLayout llTo;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_allprice)
    TextView tvAllprice;
    @BindView(R.id.bt_submit)
    TextView btSubmit;
    @BindView(R.id.ll_allprice)
    LinearLayout llAllprice;
    String type = "1";
    String wxname = "", aliname = "";
    PayPasswordDialog payPasswordDialog;
    String bankCardId=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_withdraw);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(WithDrawActivity.this), 0, 0);
        setStateColor(false);
        initView();
        getBlanceAndIntegralAndFans();
        withdrawAccountlist();
        isSetPassword();
    }

    private void initView() {
        tvTitle.setText("提现");
        etPrice.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
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

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, WithDrawActivity.class));
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


    @OnClick({R.id.back, R.id.iv_close, R.id.bt_submit, R.id.tv_record, R.id.ll_to, R.id.tv_alltixian, R.id.tv_about})
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
                if (etPrice.getText().toString().isEmpty()) {
                    showToast("请输入提现金额");
                    return;
                }
                //是否实名弹窗
                checkRealPerson();
                break;
            case R.id.tv_record:
                WithDrawRecordActivity.startActivityInstance(this, tvAmount.getText().toString());
                break;
            case R.id.ll_to:
                BelowPayTypeDialog belowPayTypeDialog = new BelowPayTypeDialog(WithDrawActivity.this);
                belowPayTypeDialog.setSureListener(item -> {
                    bankCardId = null;
                    type = item + "";
                    if (type.equals("0")) {
                        //微信
                        tvPaytype.setText("微信 " + wxname);
                    } else {
                        //支付宝
                        tvPaytype.setText("支付宝 " + aliname);
                    }
                }).show(type, wxname, aliname);
                break;
            case R.id.tv_alltixian:
                etPrice.setText(tvAllprice.getText().toString().replace("￥", ""));
                etPrice.setSelection(etPrice.getText().toString().length());//将光标移至文字末尾
                break;
            case R.id.tv_about:
                geturl();
                break;
        }
    }

    /**
     * 获取实名认证状态
     */
    private void checkRealPerson() {
        ApiUtil.getApiService().checkRealPersonNum(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData().equals("1")) {
                            getData(etPrice.getText().toString());
                        } else if(entity.getData().equals("0")){
                            new MiddleDialog(WithDrawActivity.this).setSureListener(() -> {
                                Authentication.startActivityInstance(WithDrawActivity.this);
                            }).show("温馨提示", "为了保障您的账户安全，请完成实名认证后在进行提现！", "取消", "去认证", false);


                        }else if(entity.getData().equals("2")){
                            new MiddleDialog(WithDrawActivity.this).setSureListener(() -> {
                                Authentication.startActivityInstance(WithDrawActivity.this);
                            }).show("温馨提示", "一个身份证只能绑定一个账号，请更换身份信息！", "取消", "去更换", false);
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
                        tvAllprice.setText("￥" + entity.getData().getAmount());
                        if (Double.valueOf(entity.getData().getAmount()) >= 100) {
                            llAllprice.setVisibility(View.VISIBLE);
                        } else {
                            llAllprice.setVisibility(View.GONE);
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
     * 账号是否绑定
     */
    private void withdrawAccountlist() {
        ApiUtil.getApiService().withdrawAccountlist(MyApplication.getToken()).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {

                        if (entity.getData().size() == 1) {
                            if (entity.getData().get(0).getAccountType().equals("1")) {
                                tvPaytype.setText("支付宝 " + MyApplication.getPhonePass(entity.getData().get(0).getName()));
                                type = "1";
                            } else {
                                tvPaytype.setText("微信 " + entity.getData().get(0).getName());
                                type = "0";
                            }
                          //  llTo.setOnClickListener(null);
                            ivRight.setVisibility(View.GONE);

                        } else {
                            for (EntityForSimple entity1 : entity.getData()) {
                                if (entity1.getAccountType().equals("1")) {
                                    //支付宝
                                    aliname = entity1.getName();
                                    tvPaytype.setText("支付宝 " + aliname);
                                } else {
                                    wxname = entity1.getName();
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
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

    /**
     * 提现
     */
    private void withdrawadd(String pass,String isIntegral,String integralType) {

        String password = "";
        try {
            password = RsaCipherUtil.encrypt(pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiUtil.getApiService().withdrawadd(MyApplication.getToken(), password, etPrice.getText().toString(), type,"1",isIntegral,integralType,bankCardId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        showToast("提现申请已提交");
                        if (payPasswordDialog != null) {
                            payPasswordDialog.dismiss();
                        }
                        getBlanceAndIntegralAndFans();
                        new Handler().postDelayed(() -> {
                            finish();
                        }, 1000);
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
     * 获取积分兑换数据
     *
     * @param money
     */
    private void getData(String money) {
        ApiUtil.getApiService().withdrawNeedIntegral(MyApplication.getToken(), money).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        new WithdrawDialog(WithDrawActivity.this).setSureListener((item, integralType) -> {
                            payPasswordDialog = new PayPasswordDialog(WithDrawActivity.this);
                            payPasswordDialog.setSureListener(code -> withdrawadd(code,item+"",integralType)).show();
                        }).show(money, entity.getData().getServiceCharge(), entity.getData().getNeedIntegral(), entity.getData().isIntegral());
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
                            new MiddleDialogWithoutTitle(WithDrawActivity.this).setSureListener(() -> {
                                SetPasswordAActivity.startActivityInstance(WithDrawActivity.this);
                            }).setCancelListener(() -> finish()).show("设置安全密码才可以提现，去设置？", "关闭", "去设置", false);
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

    private void geturl() {
        ApiUtil.getApiService().syskey("withDraw_money_h5").enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        String url = entity.getData();
                        DefaultWeb.startActivityInstance(WithDrawActivity.this, url,"提现说明");
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toLogin(AmountEvent event) {
        getBlanceAndIntegralAndFans();
    }

}
