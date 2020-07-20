package com.qupp.client.ui.view.activity.mine.balance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.mine.pinganbank.MyBankCard;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.dialog.MiddleDialogWithoutTitle;
import com.qupp.client.utils.event.AmountEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 余额
 */

public class BalanceActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_lockAmount)
    TextView tvLockAmount;
    @BindView(R.id.tv_todayIncome)
    TextView tvTodayIncome;
    @BindView(R.id.tv_totalIncome)
    TextView tvTotalIncome;
    @BindView(R.id.tv_setsafe)
    TextView tvSetsafe;
    boolean alreadBand = false;
    @BindView(R.id.tv_band)
    TextView tvBand;
    @BindView(R.id.iv_todayIncome)
    ImageView ivTodayIncome;
    @BindView(R.id.iv_totalIncome)
    ImageView ivTotalIncome;
    boolean isTodayShow = false;
    boolean isTotalShow = false;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    String wallet = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_mybalance);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(BalanceActivity.this), 0, 0);
        setStateColor(true);
        initView();
    }

    private void initView() {
        tvTitle.setText("我的余额");
    }

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, BalanceActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSetPassword();
        withdrawAccountlist();
        accountDetail();
        memberWalletdetail();
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


    @OnClick({R.id.back, R.id.ll_topup, R.id.ll_withdraw, R.id.ll_bind, R.id.ll_save, R.id.ll_more, R.id.rl_todayIncome, R.id.rl_totalIncome, R.id.tv_wallet,R.id.ll_bank})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_topup:
                TopUpActivity.startActivityInstance(this);
                break;
            case R.id.ll_withdraw:
                if (alreadBand) {
                    WithDrawActivity.startActivityInstance(this);
                } else {
                    new MiddleDialogWithoutTitle(this).setSureListener(() -> {
                        BandPayAccountActivity.startActivityInstance(this);
                    }).show("您暂无绑定账号", "取消", "去绑定", false);
                }
                break;
            case R.id.ll_bind:
                BandPayAccountActivity.startActivityInstance(this);
                break;
            case R.id.ll_save:
                SetPasswordAActivity.startActivityInstance(this);
                break;
            case R.id.ll_more:
                BalanceDetailActivity.startActivityInstance(this, tvAmount.getText().toString());
                break;
            case R.id.rl_todayIncome:
                if (isTodayShow) {
                    tvTodayIncome.setVisibility(View.INVISIBLE);
                    ivTodayIncome.setVisibility(View.VISIBLE);
                } else {
                    tvTodayIncome.setVisibility(View.VISIBLE);
                    ivTodayIncome.setVisibility(View.INVISIBLE);
                }
                isTodayShow = !isTodayShow;
                break;
            case R.id.rl_totalIncome:
                if (isTotalShow) {
                    tvTotalIncome.setVisibility(View.INVISIBLE);
                    ivTotalIncome.setVisibility(View.VISIBLE);
                } else {
                    tvTotalIncome.setVisibility(View.VISIBLE);
                    ivTotalIncome.setVisibility(View.INVISIBLE);
                }
                isTotalShow = !isTotalShow;
                break;
            case R.id.tv_wallet:
                WalletActivity.startActivityInstance(this, wallet);
                break;
            case R.id.ll_bank:
                MyBankCard.startActivityInstance(this);
                break;
        }
    }

    /**
     * 获取账户信息
     */
    private void accountDetail() {
        ApiUtil.getApiService().accountdetail(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvAmount.setText(entity.getData().getAmount());
                        tvLockAmount.setText("冻结：" + (entity.getData().getLockAmount().equals("0") ? "0.00" : entity.getData().getLockAmount()));
                        tvTodayIncome.setText(entity.getData().getYesterdayProfit());
                        tvTotalIncome.setText(entity.getData().getTotalIncome());
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
                        tvWallet.setText("钱包："+wallet);
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
                        if (entity.getData().equals("true")) {
                            tvSetsafe.setText("已设置");
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
     * 账号是否绑定
     */
    private void withdrawAccountlist() {
        ApiUtil.getApiService().withdrawAccountlist(MyApplication.getToken()).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData() == null || entity.getData().size() == 0) {
                            alreadBand = false;
                            tvBand.setText("未绑定");
                        } else {
                            alreadBand = true;
                            tvBand.setText("已绑定");
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


    @OnClick(R.id.iv_know)
    public void onViewClicked() {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        showToast("冻结资金指的是您的提现金额还在处理中");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toLogin(AmountEvent event) {
        accountDetail();
    }
}
