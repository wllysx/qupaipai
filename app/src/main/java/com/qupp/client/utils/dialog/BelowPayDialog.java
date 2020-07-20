package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 下面弹出支付dialog
 * author: MrWang on 2019/8/21
 * email:773630555@qq.com
 * date: on 2019/8/21 13:20
 */


public class BelowPayDialog {

    private Context context;
    private Dialog dialog;
    BelowPayDialog belowDialog;
    boolean isFaddish = false;
    private String type;
    private String endprice;
    private String wallet;
    private String firstCategoryId;
    String bankId = "";

    public BelowPayDialog(Context context) {
        super();
        this.context = context;
        belowDialog = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show(String endPirce, String firstCategoryId, String type, String wallet) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay, null);
        this.endprice = endPirce;
        this.wallet = wallet;
        this.firstCategoryId = firstCategoryId;
        if (firstCategoryId.equals(MyApplication.firstCategoryId) || type.equals("2") || type.equals("3") || type.equals("4")) {
            isFaddish = true;
        } else {
            isFaddish = false;
        }
        this.type = type;
        ViewHolder holder = new ViewHolder(view);

        holder.tvEndprice.setText(endPirce);

        holder.ivClose.setOnClickListener(v -> dialog.dismiss());
        holder.llPay1.setOnClickListener(v -> {
            bankId = "";
            holder.checkbox1.setChecked(true);
            holder.checkbox2.setChecked(false);
            holder.checkbox3.setChecked(false);
            holder.checkbox11.setChecked(false);
        });
        holder.llPay11.setOnClickListener(v -> {
            holder.checkbox1.setChecked(false);
            holder.checkbox2.setChecked(false);
            holder.checkbox3.setChecked(false);
            holder.checkbox11.setChecked(true);
            bankId = "";
        });
        holder.llPay2.setOnClickListener(v -> {
            holder.checkbox1.setChecked(false);
            holder.checkbox2.setChecked(true);
            holder.checkbox3.setChecked(false);
            holder.checkbox11.setChecked(false);
            bankId = "";
        });
        holder.llPay3.setOnClickListener(v -> {
            holder.checkbox1.setChecked(false);
            holder.checkbox2.setChecked(false);
            holder.checkbox3.setChecked(true);
            holder.checkbox11.setChecked(false);
            bankId = "";
        });
        getBlanceAndIntegralAndFans(holder, endPirce);
        holder.llSubmit.setOnClickListener(v -> {
            dialog.dismiss();
            if (holder.checkbox1.isChecked()) {
                sure.onSure(1);
            }
            if (holder.checkbox11.isChecked()) {
                sure.onSure(6);
            }
            if (holder.checkbox2.isChecked()) {
                sure.onSure(2);
            }
            if (holder.checkbox3.isChecked()) {
                sure.onSure(3);
            }
            if(bankId!=null&&!bankId.equals("")){
                sure1.onSure1("5",bankId);
            }

        });

        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        new Handler().postDelayed(() -> {
            dialog.show();
        }, 350);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);

    }

    public static interface Sure {
        void onSure(int item);
    }

    public Sure sure;

    public BelowPayDialog setSureListener(Sure sure) {
        this.sure = sure;
        return belowDialog;
    }

    public static interface Sure1 {
        void onSure1(String type,String bankid);
    }

    public Sure1 sure1;

    public BelowPayDialog setSureListener1(Sure1 sure1) {
        this.sure1 = sure1;
        return belowDialog;
    }


    /**
     * 获取用户信息积分等信息
     */
    private void getBlanceAndIntegralAndFans(ViewHolder holder, String endprice) {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        holder.tvBalance.setText("（余额￥" + entity.getData().getAmount() + "）");
                        if (Double.valueOf(entity.getData().getAmount()) < Double.valueOf(endprice)) {
                            holder.tvBalance.setText("（余额￥" + entity.getData().getAmount() + ",余额不足）");
                            holder.ivYue.setImageResource(R.mipmap.ye_unenable);
                            //余额不能点击
                            holder.checkbox1.setChecked(false);
                            holder.llPay1.setOnClickListener(null);
                            getTerminalPayment(false, holder);
                        } else {
                            getTerminalPayment(true, holder);
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
     * 判断第三方支付状态
     *
     * @param isbalance 积分是否可用
     */
    private void getTerminalPayment(boolean isbalance, ViewHolder holder) {
        //网红爆款
        if (isFaddish) {
            ApiUtil.getApiService().getPaymentSettings(MyApplication.getToken(), type).enqueue(new Callback<MessageForSimple>() {
                @Override
                public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                    MessageForSimple entity = response.body();
                    try {
                        if (entity.getCode().equals("0")) {
                            String paymentMethod = entity.getData().getPaymentMethod();
                            //有积分支付
                            paymentMethod = paymentMethod.replace("1", "");
                            if (!type.equals("4")) {
                                paymentMethod = paymentMethod.replace("4", "");
                            }
                            if (paymentMethod.contains("4")) {
                                //有钱包支付
                                holder.llPay11.setVisibility(View.VISIBLE);
                            } else {
                                holder.llPay11.setVisibility(View.GONE);
                            }
                            paymentMethod = paymentMethod.replace("4", "");

                            if (paymentMethod.contains("2") && paymentMethod.contains("3")) {
                                //可用余额和现金（和原来逻辑不变）
                                payment(isbalance, holder);
                            } else if (paymentMethod.contains("2")) {
                                //只余额
                                holder.llPay2.setVisibility(View.GONE);
                                holder.llPay3.setVisibility(View.GONE);
                            } else if (paymentMethod.contains("3")) {
                                //只现金
                                holder.llPay1.setVisibility(View.GONE);
                                //第三方逻辑不变 默认余额不够状态
                                holder.checkbox1.setChecked(false);
                                payment(false, holder);
                            } else {
                                holder.checkbox1.setChecked(false);
                                holder.checkbox2.setChecked(false);
                                holder.checkbox3.setChecked(false);
                                holder.llPay1.setVisibility(View.GONE);
                                holder.llPay2.setVisibility(View.GONE);
                                holder.llPay3.setVisibility(View.GONE);
                            }

                        } else {
                            //showToast(entity.getMsg());
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<MessageForSimple> call, Throwable t) {
                }
            });
        } else {
            holder.llPay11.setVisibility(View.GONE);
            payment(isbalance, holder);
        }

    }

    private void payment(boolean isbalance, ViewHolder holder) {
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
                                holder.llPay2.setVisibility(View.GONE);
                            }
                            if (!isali) {
                                //支付宝隐藏
                                holder.llPay3.setVisibility(View.GONE);
                            }
                        } else {
                            //显示隐藏和勾选
                            if (iswx && isali) {
                                //微信 勾选
                                //都显示
                                holder.checkbox2.setChecked(true);
                                holder.checkbox3.setChecked(false);
                            } else if (iswx) {
                                //微信 勾选
                                //支付宝隐藏
                                holder.checkbox2.setChecked(true);
                                holder.checkbox3.setChecked(false);
                                holder.llPay3.setVisibility(View.GONE);
                            } else if (isali) {
                                //支付宝 勾选
                                //微信隐藏
                                holder.checkbox3.setChecked(true);
                                holder.checkbox2.setChecked(false);
                                holder.llPay2.setVisibility(View.GONE);
                            } else {
                                //都不勾选
                                //支付宝微信 都隐藏
                                holder.checkbox3.setChecked(false);
                                holder.checkbox2.setChecked(false);
                                holder.llPay3.setVisibility(View.GONE);
                                holder.llPay2.setVisibility(View.GONE);
                            }
                        }

                        //判断钱包重新排序
                        if (holder.llPay11.getVisibility() == View.VISIBLE) {
                            if (Double.valueOf(wallet) < Double.valueOf(endprice)) {
                                holder.tvBalance1.setText("（钱包￥" + wallet + ",余额不足）");
                                holder.ivWallet.setImageResource(R.mipmap.qianbao_un);
                                //余额不能点击
                                holder.checkbox11.setChecked(false);
                                holder.llPay11.setOnClickListener(null);
                            } else {
                                holder.tvBalance1.setText("（余额￥" + wallet + "）");

                                if ((holder.llPay1.getVisibility() == View.VISIBLE && !holder.checkbox1.isChecked()) || holder.llPay1.getVisibility() == View.GONE) {
                                    //去勾选钱包
                                    holder.checkbox11.setChecked(true);
                                    holder.checkbox2.setChecked(false);
                                    holder.checkbox3.setChecked(false);
                                }
                            }

                        }

                    } else {
                        Toast.makeText(context, entity.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


    static class ViewHolder {
        @BindView(R.id.iv_close)
        ImageView ivClose;
        @BindView(R.id.tv_endprice)
        TextView tvEndprice;
        @BindView(R.id.checkbox1)
        CheckBox checkbox1;
        @BindView(R.id.iv_yue)
        ImageView ivYue;
        @BindView(R.id.tv_balance)
        TextView tvBalance;
        @BindView(R.id.ll_pay1)
        LinearLayout llPay1;
        @BindView(R.id.checkbox1_1)
        CheckBox checkbox11;
        @BindView(R.id.iv_wallet)
        ImageView ivWallet;
        @BindView(R.id.tv_balance_1)
        TextView tvBalance1;
        @BindView(R.id.ll_pay1_1)
        LinearLayout llPay11;
        @BindView(R.id.checkbox2)
        CheckBox checkbox2;
        @BindView(R.id.ll_pay2)
        LinearLayout llPay2;
        @BindView(R.id.checkbox3)
        CheckBox checkbox3;
        @BindView(R.id.ll_pay3)
        LinearLayout llPay3;
        @BindView(R.id.ll_paytype)
        LinearLayout llPaytype;
        @BindView(R.id.ll_submit)
        LinearLayout llSubmit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

