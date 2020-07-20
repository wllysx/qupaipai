package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 话费充值 dialog
 */


public class BelowPayTelephoneDialog {

    private Context context;
    private Dialog dialog;
    BelowPayTelephoneDialog belowDialog;
    String paymentMethod;

    public BelowPayTelephoneDialog(Context context) {
        super();
        this.context = context;
        belowDialog = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show(String endPirce, String endIntegral, String paymentMethod) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay_telephone, null);

        this.paymentMethod = paymentMethod;
        ViewHolder holder = new ViewHolder(view);
        if (paymentMethod.contains("1")) {
        //显示积分
        holder.llPay0.setVisibility(View.VISIBLE);
        holder.llPay00.setVisibility(View.VISIBLE);
         }
        if (paymentMethod.contains("2")) {
            //显示余额
            holder.llPay1.setVisibility(View.VISIBLE);
        }
        if (paymentMethod.contains("3")) {
            //显示支付宝微信
            holder.llPay2.setVisibility(View.VISIBLE);
            holder.llPay3.setVisibility(View.VISIBLE);
        }
        holder.ivClose.setOnClickListener(v -> dialog.dismiss());
        holder.llPay0.setOnClickListener(v -> {
            holder.checkbox0.setChecked(true);
            holder.checkbox00.setChecked(false);
            holder.checkbox1.setChecked(false);
            holder.checkbox2.setChecked(false);
            holder.checkbox3.setChecked(false);
            holder.tvSubmit.setText("确定付款(" + endIntegral + "积分)");

        });
        holder.llPay00.setOnClickListener(v -> {
            holder.checkbox00.setChecked(true);
            holder.checkbox0.setChecked(false);
            holder.checkbox1.setChecked(false);
            holder.checkbox2.setChecked(false);
            holder.checkbox3.setChecked(false);
            holder.tvSubmit.setText("确定付款(" + endIntegral + "积分)");

        });
        holder.llPay1.setOnClickListener(v -> {
            holder.checkbox1.setChecked(true);
            holder.checkbox00.setChecked(false);
            holder.checkbox2.setChecked(false);
            holder.checkbox3.setChecked(false);
            holder.checkbox0.setChecked(false);
            holder.tvSubmit.setText("确定付款(" + endPirce + "元)");
        });
        holder.llPay2.setOnClickListener(v -> {
            holder.checkbox1.setChecked(false);
            holder.checkbox00.setChecked(false);
            holder.checkbox2.setChecked(true);
            holder.checkbox3.setChecked(false);
            holder.checkbox0.setChecked(false);
            holder.tvSubmit.setText("确定付款(" + endPirce + "元)");
        });
        holder.llPay3.setOnClickListener(v -> {
            holder.checkbox1.setChecked(false);
            holder.checkbox00.setChecked(false);
            holder.checkbox2.setChecked(false);
            holder.checkbox3.setChecked(true);
            holder.checkbox0.setChecked(false);
            holder.tvSubmit.setText("确定付款(" + endPirce + "元)");
        });
        if (!paymentMethod.contains("1") && !paymentMethod.contains("2")) {
            holder.checkbox1.setChecked(false);
            holder.checkbox00.setChecked(false);
            holder.checkbox2.setChecked(true);
            holder.checkbox3.setChecked(false);
            holder.checkbox0.setChecked(false);
            holder.tvSubmit.setText("确定付款(" + endPirce + "元)");
            refreshStates(holder);
        } else {
            if (paymentMethod.contains("1") && paymentMethod.contains("2")) {
                getBlanceAndIntegralAndFans(holder, endPirce, endIntegral);
            } else if (paymentMethod.contains("1")) {
                getBlanceAndIntegralAndFans1(holder, endPirce, endIntegral);
            } else if (paymentMethod.contains("2")) {
                getBlanceAndIntegralAndFans2(holder, endPirce, endIntegral);
            }
        }
        holder.llSubmit.setOnClickListener(v -> {
            dialog.dismiss();
            if (holder.checkbox0.isChecked()) {
                sure.onSure(0);
            }
            if (holder.checkbox00.isChecked()) {
                sure.onSure(4);
            }
            if (holder.checkbox1.isChecked()) {
                sure.onSure(1);
            }
            if (holder.checkbox2.isChecked()) {
                sure.onSure(2);
            }
            if (holder.checkbox3.isChecked()) {
                sure.onSure(3);
            }

        });

        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        dialog.show();

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

    public BelowPayTelephoneDialog setSureListener(Sure sure) {
        this.sure = sure;
        return belowDialog;
    }

    /**
     * 获取用户信息积分等信息
     */
    private void getBlanceAndIntegralAndFans(ViewHolder holder, String endprice, String endIntegral) {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        holder.tvBalance.setText("(剩余：" + entity.getData().getAmount() + "元)");
                        holder.tvIntegral.setText("(剩余：" + entity.getData().getSuperIntegralAmount() + "积分)");
                        holder.tvIntegral0.setText("(剩余：" + entity.getData().getIntegralAmount() + "积分)");


                        if (Double.valueOf(entity.getData().getSuperIntegralAmount()) < Double.valueOf(endIntegral) && Double.valueOf(entity.getData().getIntegralAmount()) < Double.valueOf(endIntegral)) {
                            //积分不能点击
                            holder.checkbox0.setChecked(false);
                            holder.checkbox00.setChecked(false);
                            holder.checkbox1.setChecked(true);
                            holder.checkbox2.setChecked(false);
                            holder.checkbox3.setChecked(false);
                            holder.ivJf.setImageResource(R.mipmap.jf_unenable);
                            holder.llPay0.setOnClickListener(null);

                            holder.tvIntegraltitle.setTextColor(context.getResources().getColor(R.color.textcolor18));
                            holder.tvIntegral.setTextColor(context.getResources().getColor(R.color.textcolor18));
                            //超值积分不能点击
                            holder.checkbox0.setChecked(false);
                            holder.checkbox00.setChecked(false);
                            holder.checkbox1.setChecked(true);
                            holder.checkbox2.setChecked(false);
                            holder.checkbox3.setChecked(false);
                            holder.ivJf0.setImageResource(R.mipmap.jf_unenable0);
                            holder.llPay00.setOnClickListener(null);

                            holder.tvIntegraltitle0.setTextColor(context.getResources().getColor(R.color.textcolor18));
                            holder.tvIntegral0.setTextColor(context.getResources().getColor(R.color.textcolor18));


                            holder.tvSubmit.setText("确定付款(" + endprice + "元)");
                            if (Double.valueOf(entity.getData().getAmount()) < Double.valueOf(endprice)) {
                                //余额不能点击
                                holder.checkbox0.setChecked(false);
                                holder.checkbox00.setChecked(false);
                                holder.checkbox1.setChecked(false);
                                holder.checkbox2.setChecked(true);
                                holder.checkbox3.setChecked(false);
                                holder.llPay1.setOnClickListener(null);
                                holder.ivYe.setImageResource(R.mipmap.ye_unenable);
                                holder.tvBalancetitle.setTextColor(context.getResources().getColor(R.color.textcolor18));
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.textcolor18));
                            }
                        } else {
                            holder.tvSubmit.setText("确定付款(" + endIntegral + "积分)");
                            if (Double.valueOf(entity.getData().getAmount()) < Double.valueOf(endprice)) {
                                //余额不能点击
                                holder.checkbox0.setChecked(true);
                                holder.checkbox1.setChecked(false);
                                holder.checkbox2.setChecked(false);
                                holder.checkbox3.setChecked(false);
                                holder.llPay1.setOnClickListener(null);
                                holder.ivYe.setImageResource(R.mipmap.ye_unenable);
                                holder.tvBalancetitle.setTextColor(context.getResources().getColor(R.color.textcolor18));
                                holder.tvBalance.setTextColor(context.getResources().getColor(R.color.textcolor18));
                            }
                            //选择哪个积分  普通/超值
                            if(Double.valueOf(entity.getData().getSuperIntegralAmount()) >= Double.valueOf(endIntegral)&&Double.valueOf(entity.getData().getIntegralAmount()) >= Double.valueOf(endIntegral)){
                                //普通 超值都够
                                holder.checkbox0.setChecked(true);
                                holder.checkbox00.setChecked(false);

                            }else if(Double.valueOf(entity.getData().getSuperIntegralAmount()) >= Double.valueOf(endIntegral)){
                                //只有普通够
                                holder.checkbox0.setChecked(true);
                                holder.checkbox00.setChecked(false);
                                holder.llPay00.setOnClickListener(null);
                            }else{
                                holder.checkbox0.setChecked(false);
                                holder.checkbox00.setChecked(true);
                                holder.llPay0.setOnClickListener(null);
                            }
                        }

                        refreshStates(holder);
                    } else {
                    }
                    getTerminalPayment(holder);
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
    private void getBlanceAndIntegralAndFans1(ViewHolder holder, String endprice, String endIntegral) {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        holder.tvBalance.setText("(剩余：" + entity.getData().getAmount() + "元)");
                        holder.tvIntegral.setText("(剩余：" + entity.getData().getSuperIntegralAmount() + "积分)");
                        holder.tvIntegral0.setText("(剩余：" + entity.getData().getIntegralAmount() + "积分)");
                        holder.tvSubmit.setText("确定付款(" + endprice + "元)");

                        if(Double.valueOf(entity.getData().getSuperIntegralAmount()) <Double.valueOf(endIntegral)&&Double.valueOf(entity.getData().getIntegralAmount()) < Double.valueOf(endIntegral)){

                            //余额不能点击
                            holder.checkbox0.setChecked(false);
                            holder.checkbox1.setChecked(false);
                            holder.checkbox2.setChecked(true);
                            holder.checkbox3.setChecked(false);
                            holder.ivJf.setImageResource(R.mipmap.jf_unenable);
                            holder.llPay0.setOnClickListener(null);

                            holder.tvIntegraltitle.setTextColor(context.getResources().getColor(R.color.textcolor18));
                            holder.tvIntegral.setTextColor(context.getResources().getColor(R.color.textcolor18));

                            //超值积分不能点击
                            holder.checkbox0.setChecked(false);
                            holder.checkbox00.setChecked(false);
                            holder.checkbox1.setChecked(true);
                            holder.checkbox2.setChecked(false);
                            holder.checkbox3.setChecked(false);
                            holder.ivJf0.setImageResource(R.mipmap.jf_unenable0);
                            holder.llPay00.setOnClickListener(null);

                            holder.tvIntegraltitle0.setTextColor(context.getResources().getColor(R.color.textcolor18));
                            holder.tvIntegral0.setTextColor(context.getResources().getColor(R.color.textcolor18));

                        } else {
                            holder.checkbox0.setChecked(true);
                            holder.checkbox1.setChecked(false);
                            holder.checkbox2.setChecked(false);
                            holder.checkbox3.setChecked(false);

                            //选择哪个积分  普通/超值
                            if(Double.valueOf(entity.getData().getSuperIntegralAmount()) >= Double.valueOf(endIntegral)&&Double.valueOf(entity.getData().getIntegralAmount()) >= Double.valueOf(endIntegral)){
                                //普通 超值都够
                                holder.checkbox0.setChecked(true);
                                holder.checkbox00.setChecked(false);

                            }else if(Double.valueOf(entity.getData().getSuperIntegralAmount()) >= Double.valueOf(endIntegral)){
                                //只有普通够
                                holder.checkbox0.setChecked(true);
                                holder.checkbox00.setChecked(false);
                                holder.llPay00.setOnClickListener(null);
                            }else{
                                holder.checkbox0.setChecked(false);
                                holder.checkbox00.setChecked(true);
                                holder.llPay0.setOnClickListener(null);
                            }
                            holder.tvSubmit.setText("确定付款(" + endIntegral + "积分)");
                        }

                        refreshStates(holder);
                    } else {
                    }
                    getTerminalPayment(holder);
                } catch (
                        Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


    private void getBlanceAndIntegralAndFans2(ViewHolder holder, String endprice, String endIntegral) {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        holder.tvBalance.setText("(剩余：" + entity.getData().getAmount() + "元)");
                        holder.tvIntegral.setText("(剩余：" + entity.getData().getSuperIntegralAmount() + "积分)");
                        holder.tvIntegral0.setText("(剩余：" + entity.getData().getIntegralAmount() + "积分)");

                        holder.tvSubmit.setText("确定付款(" + endprice + "元)");
                        if (Double.valueOf(entity.getData().getAmount()) < Double.valueOf(endprice)) {
                            //余额不能点击
                            holder.checkbox0.setChecked(false);
                            holder.checkbox00.setChecked(false);
                            holder.checkbox1.setChecked(false);
                            holder.checkbox2.setChecked(true);
                            holder.checkbox3.setChecked(false);
                            holder.llPay1.setOnClickListener(null);
                            holder.ivJf.setImageResource(R.mipmap.ye_unenable);
                            holder.tvBalancetitle.setTextColor(context.getResources().getColor(R.color.textcolor18));
                            holder.tvBalance.setTextColor(context.getResources().getColor(R.color.textcolor18));
                        } else {
                            holder.checkbox0.setChecked(false);
                            holder.checkbox00.setChecked(false);
                            holder.checkbox1.setChecked(true);
                            holder.checkbox2.setChecked(false);
                            holder.checkbox3.setChecked(false);
                        }
                        holder.tvSubmit.setText("确定付款(" + endIntegral + "积分)");
                        refreshStates(holder);

                    } else {
                    }
                    getTerminalPayment(holder);

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    private void getTerminalPayment(ViewHolder holder) {

        if (paymentMethod.contains("3")) {
            //有第三方支付
            ApiUtil.getApiService().getTerminalPayment("2").enqueue(new Callback<MessageForSimple>() {
                @Override
                public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                    MessageForSimple entity = response.body();
                    try {
                        if (entity.getCode().equals("0")) {
                            boolean iswx = entity.getData().getPaymentMethod().contains("1");
                            boolean isali = entity.getData().getPaymentMethod().contains("2");
                            boolean isbalance = !holder.checkbox2.isChecked();
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

    }

    private void refreshStates(ViewHolder viewHolder) {
        ArrayList<View> arrayView = new ArrayList<>();
        arrayView.add(viewHolder.llPay0);
        arrayView.add(viewHolder.llPay00);
        arrayView.add(viewHolder.llPay1);
        arrayView.add(viewHolder.llPay2);
        arrayView.add(viewHolder.llPay3);
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(viewHolder.checkbox0);
        checkBoxes.add(viewHolder.checkbox00);
        checkBoxes.add(viewHolder.checkbox1);
        checkBoxes.add(viewHolder.checkbox2);
        checkBoxes.add(viewHolder.checkbox3);
        for (int i = 0; i < 5; i++) {
            if (arrayView.get(i).getVisibility() == View.GONE) {
                checkBoxes.get(i).setChecked(false);
            }
        }
        if (!checkBoxes.get(0).isChecked()&&!checkBoxes.get(1).isChecked() && !checkBoxes.get(2).isChecked() && !checkBoxes.get(3).isChecked() && !checkBoxes.get(4).isChecked()) {
            viewHolder.llSubmit.setBackgroundResource(R.drawable.bg_login_unable);
            viewHolder.tvSubmit.setText("确认付款");
            viewHolder.llSubmit.setEnabled(false);
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_close)
        ImageView ivClose;
        @BindView(R.id.iv_jf)
        ImageView ivJf;
        @BindView(R.id.tv_integraltitle)
        TextView tvIntegraltitle;
        @BindView(R.id.tv_integral)
        TextView tvIntegral;
        @BindView(R.id.checkbox0)
        CheckBox checkbox0;
        @BindView(R.id.ll_pay0)
        LinearLayout llPay0;
        @BindView(R.id.iv_jf0)
        ImageView ivJf0;
        @BindView(R.id.tv_integraltitle0)
        TextView tvIntegraltitle0;
        @BindView(R.id.tv_integral0)
        TextView tvIntegral0;
        @BindView(R.id.checkbox00)
        CheckBox checkbox00;
        @BindView(R.id.ll_pay00)
        LinearLayout llPay00;
        @BindView(R.id.iv_ye)
        ImageView ivYe;
        @BindView(R.id.tv_balancetitle)
        TextView tvBalancetitle;
        @BindView(R.id.tv_balance)
        TextView tvBalance;
        @BindView(R.id.checkbox1)
        CheckBox checkbox1;
        @BindView(R.id.ll_pay1)
        LinearLayout llPay1;
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
        @BindView(R.id.tv_submit)
        TextView tvSubmit;
        @BindView(R.id.ll_submit)
        LinearLayout llSubmit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

