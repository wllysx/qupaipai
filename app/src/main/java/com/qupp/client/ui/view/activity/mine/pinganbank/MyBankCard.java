package com.qupp.client.ui.view.activity.mine.pinganbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.pingan.BankCardAdapter;
import com.qupp.client.utils.dialog.BankAddDialog;
import com.qupp.client.utils.dialog.PayPasswordDialog;
import com.qupp.client.utils.event.CloseBandPage;
import com.qupp.client.utils.secretUtils.RsaCipherUtil;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的银行卡列表
 */

public class MyBankCard extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    BankCardAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    int current = 1;
    PayPasswordDialog payPasswordDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(MyBankCard.this), 0, 0);
        setStateColor(false);
        initView();
        initSpringView();
        getData();


    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
     //   springview.setFooter(new DefaultFooter(this));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                current = 1;
                getData();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                current++;
                getData();
            }
        });
    }


    private void initView() {
        tvTitle.setText("银行卡管理");
        ivDefault.setImageResource(R.mipmap.icon_default10);
        tvDefault.setText("亲，您还暂无银行卡");
        initAdapter(datas);
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new BankCardAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyBankCardDetails.startActivityInstance(this,datas.get(position));
        });
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 15);
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);
    }

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, MyBankCard.class));
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


    @OnClick({R.id.back, R.id.tv_wenti, R.id.ll_addbank})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_wenti:
                showToast("问题");
                break;
            case R.id.ll_addbank:
                new BankAddDialog(MyBankCard.this).setSureListener(item -> {
                    payPasswordDialog = new PayPasswordDialog(MyBankCard.this);
                    payPasswordDialog.setSureListener(code -> {
                        checkPass(code,item);
                    }).show();
                }).show();
                break;
        }
    }


    /**
     * 验证密码
     */
    private void checkPass(String pass,int item) {

        String password = "";
        try {
            password = RsaCipherUtil.encrypt(pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiUtil.getApiService().verification(MyApplication.getToken(),password).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        payPasswordDialog.dismiss();
                        //到添加银行卡页面
                        if(item==2) {
                            AddBankA.startActivityInstance(MyBankCard.this);
                        }else{

                            if (MyApplication.getToken().equals("")) {
                                MyApplication.toLogin(MyBankCard.this);
                            } else {
                                AddBankC.startActivityInstance(MyBankCard.this, "https://optapph5.jiayikou.com/#/bindCard/"+ MyApplication.getToken());
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
     * 获取银行卡列表
     */
    private void getData() {
        ApiUtil.getApiService().bankCards(MyApplication.getToken()).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if (entity.getData().size() == 0) {
                                llDefault.setVisibility(View.VISIBLE);
                            } else {
                                llDefault.setVisibility(View.GONE);
                            }
                        } else {
                            if (entity.getData().size() == 0) {
                                showToast("没有更多了");
                            }
                        }
                        datas.addAll(entity.getData());
                        mAdapter.notifyDataSetChanged();
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void close(CloseBandPage event) {
        if (event != null) {
            current = 1;
            getData();
        }
    }

}
