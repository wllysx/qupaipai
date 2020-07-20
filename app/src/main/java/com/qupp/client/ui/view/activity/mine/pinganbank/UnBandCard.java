package com.qupp.client.ui.view.activity.mine.pinganbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.dialog.UnbandCardDialog;
import com.qupp.client.utils.event.CloseBandPage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 解绑银行卡
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class UnBandCard extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_bankname)
    TextView tvBankname;
    String id="";
    String bankname="";
    String useType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unband_card);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(UnBandCard.this), 0, 0);
        setStateColor(false);
        id = getIntent().getStringExtra("id");
        bankname =getIntent().getStringExtra("bankname");
        useType = getIntent().getStringExtra("useType");
        initView();
    }


    private void initView() {
        tvTitle.setText("解绑银行卡");
        tvName.setText(getIntent().getStringExtra("name"));
        tvBankname.setText(getIntent().getStringExtra("bankname"));
    }


    /**
     * 启动页面
     *
     * @param activity
     */
    public static void startActivityInstance(Activity activity,String name,String bankName,String id,String useType) {
        activity.startActivity(new Intent(activity, UnBandCard.class).putExtra("name",name).putExtra("bankname",bankName).putExtra("id",id).putExtra("useType",useType)
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


    @OnClick({R.id.bt_login, R.id.back, R.id.tv_wenti})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.bt_login:
                new UnbandCardDialog(this).setSureListener(() ->unBind()).show("确定解绑","确定解除绑定招商银行（"+bankname+"）账户，解除绑定后若改卡有正在提现的金额，则提现撤销，返余额。");
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_wenti:
                showToast("问题");
        }
    }

    /**
     * 提交
     */
    private void unBind() {
        if(useType.equals("1")) {
            ApiUtil.getApiService().unBind(MyApplication.getToken(), id).enqueue(new Callback<MessageForSimple>() {
                @Override
                public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                    MessageForSimple entity = response.body();
                    try {
                        if (entity.getCode().equals("0")) {
                            //成功到列表页面
                            EventBus.getDefault().post(new CloseBandPage());
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
        }else{
            if (MyApplication.getToken().equals("")) {
                MyApplication.toLogin(UnBandCard.this);
            } else {
                AddBankC.startActivityInstance(UnBandCard.this, "https://optapph5.jiayikou.com/#/unbindCard/"+ MyApplication.getToken()+"/"+id);
            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void close(CloseBandPage event) {
        if (event != null) {
            finish();
        }
    }



}
