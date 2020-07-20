package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
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

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.view.SwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 提现服务费dialog
 */


public class WithdrawDialog {

    private Context context;
    private Dialog dialog;
    WithdrawDialog belowDialog;


    public WithdrawDialog(Context context) {
        super();
        this.context = context;
        belowDialog = this;
        // TODO Auto-generated constructor stub
    }

    /**
     * dialog的提示信息
     */
    public void show(String money, String service, String needIntegral, boolean isIntegral) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_withdraw, null);

        ViewHolder holder = new ViewHolder(view);
        holder.ivClose.setOnClickListener(v -> dialog.dismiss());
        holder.tvSure.setOnClickListener(v -> {
            dialog.dismiss();
            String integralType = "";
            if(holder.swithview.isOpened()){
                if(!holder.checkbox1Jf.isChecked()&&!holder.checkbox2Jf.isChecked()){
                    integralType = null;
                }else {
                    if (holder.checkbox1Jf.isChecked()) {
                        integralType = "2";
                    } else {
                        integralType = "1";
                    }
                }
            }else{
                integralType = null;
            }

            sure.onSure(holder.swithview.isOpened() ? 1 : 0,integralType);
        });

        holder.tvMoney.setText(money + "元");
        holder.tvService.setText(service + "元");
        holder.tvHint.setText("用" + needIntegral + "积分兑换" + service + "元免费额度");
        if (isIntegral) {
            holder.llHint.setVisibility(View.VISIBLE);
            holder.swithview.setOpened(false);
            holder.swithview.setChangeListener((v, ischecked) -> {
                if (ischecked) {
                    holder.tvService.setText("0.00" + "元");
                    holder.llJifenpay1.setVisibility(View.VISIBLE);
                    holder.llJifenpay2.setVisibility(View.VISIBLE);
                } else {
                    holder.tvService.setText(service + "元");
                    holder.llJifenpay1.setVisibility(View.GONE);
                    holder.llJifenpay2.setVisibility(View.GONE);
                }
            });
        } else {
            holder.llHint.setVisibility(View.VISIBLE);
            holder.swithview.setOpened(false);
            holder.swithview.setClickable(false);
            holder.swithview.setFocusable(false);
            holder.swithview.setChangeListener((v, ischecked) -> {
            });
            holder.v.setOnClickListener(v -> {

            });
        }
        holder.llJifenpay1.setOnClickListener(v->{
          holder.checkbox1Jf.setChecked(true);
          holder.checkbox2Jf.setChecked(false);
        });
        holder.llJifenpay2.setOnClickListener(v->{
            holder.checkbox1Jf.setChecked(false);
            holder.checkbox2Jf.setChecked(true);
        });

        getBlanceAndIntegralAndFans(holder,needIntegral);

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
        void onSure(int item, String integralType);
    }

    public Sure sure;

    public WithdrawDialog setSureListener(Sure sure) {
        this.sure = sure;
        return belowDialog;
    }


    private void getBlanceAndIntegralAndFans(ViewHolder viewHolder, String integral) {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {

                        //积分
                        String uperIntegralAmount = entity.getData().getSuperIntegralAmount();
                        //超值积分
                        String integralAmount = entity.getData().getIntegralAmount();


                        viewHolder.tvJifen1.setText("(剩余：" + uperIntegralAmount + "分)");
                        viewHolder.tvJifen2.setText("(剩余：" + integralAmount + "分)");
                        //判断积分
                        if (Double.valueOf(uperIntegralAmount) < Double.valueOf(integral)) {
                            //积分不够
                            viewHolder.llJifenpay1.setEnabled(false);
                            viewHolder.checkbox1Jf.setChecked(false);
                            //viewHolder.tvCzjftitle.setTextColor(context.getResources().getColor(R.color.textcolor32));
                            viewHolder.tvJifen1.setTextColor(context.getResources().getColor(R.color.textcolor32));
                        }
                        if (Double.valueOf(integralAmount) < Double.valueOf(integral)) {
                            //超值积分不够
                            viewHolder.llJifenpay2.setEnabled(false);
                            viewHolder.checkbox2Jf.setChecked(false);
                          //  viewHolder.tvPtjftitle.setTextColor(context.getResources().getColor(R.color.textcolor32));
                            viewHolder.tvJifen2.setTextColor(context.getResources().getColor(R.color.textcolor32));
                        }
                        if(Double.valueOf(uperIntegralAmount) >= Double.valueOf(integral)&&Double.valueOf(integralAmount) > Double.valueOf(integral)){
                            //都够
                            viewHolder.checkbox1Jf.setChecked(true);
                            viewHolder.checkbox2Jf.setChecked(false);
                        }else if(Double.valueOf(uperIntegralAmount) >= Double.valueOf(integral)){
                            //积分够
                            viewHolder.checkbox1Jf.setChecked(true);
                            viewHolder.checkbox2Jf.setChecked(false);
                        }else if(Double.valueOf(integralAmount) > Double.valueOf(integral)){
                            //超值积分够
                            viewHolder.checkbox1Jf.setChecked(false);
                            viewHolder.checkbox2Jf.setChecked(true);
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

    static class ViewHolder {
        @BindView(R.id.iv_close)
        ImageView ivClose;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_service)
        TextView tvService;
        @BindView(R.id.tv_hint)
        TextView tvHint;
        @BindView(R.id.swithview)
        SwitchView swithview;
        @BindView(R.id.v)
        View v;
        @BindView(R.id.ll_hint)
        LinearLayout llHint;
        @BindView(R.id.tv_ptjftitle)
        TextView tvPtjftitle;
        @BindView(R.id.tv_jifen1)
        TextView tvJifen1;
        @BindView(R.id.checkbox1_jf)
        CheckBox checkbox1Jf;
        @BindView(R.id.ll_jifenpay1)
        LinearLayout llJifenpay1;
        @BindView(R.id.tv_czjftitle)
        TextView tvCzjftitle;
        @BindView(R.id.tv_jifen2)
        TextView tvJifen2;
        @BindView(R.id.checkbox2_jf)
        CheckBox checkbox2Jf;
        @BindView(R.id.ll_jifenpay2)
        LinearLayout llJifenpay2;
        @BindView(R.id.tv_sure)
        TextView tvSure;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

