package com.qupp.client.ui.view.activity.mine.balance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
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
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.BalanceDetailAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.view.springview.DefaultFooter;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 余额明细
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class BalanceDetailMoreActivity extends BaseActivity {

    @BindView(R.id.linear)
    View linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    BalanceDetailAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;


    int size = 10;
    String id = "0";
    String tradeType = "0";
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    String amount = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_detail);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(BalanceDetailMoreActivity.this), 0, 0);
        amount = getIntent().getStringExtra("amount");
        setStateColor(false);
        initView();
        initSpringView();
        getMyAmount();

    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
        springview.setFooter(new DefaultFooter(this));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                id = "0";
                getMyAmount();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                if (datas.size() > 0) {
                    id = datas.get(datas.size() - 1).getId();
                    getMyAmount();
                }
            }
        });
    }


    private void initView() {
        tvTitle.setText("余额明细");
        ivDefault.setImageResource(R.mipmap.icon_default10);
        tvDefault.setText("亲，您还暂无余额明细");
        initAdapter(datas);
        tvBalance.setText(amount);
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new BalanceDetailAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 10); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }


    public static void startActivityInstance(Activity activity,String amount) {
        activity.startActivity(new Intent(activity, BalanceDetailMoreActivity.class)
            .putExtra("amount",amount)
        );
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


    @OnClick({R.id.back, R.id.ll_select, R.id.ll_menu})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_select:
                if (llMenu.getVisibility() == View.VISIBLE) {
                    hideMenu(llMenu);
                } else {
                    showMenu(llMenu);
                }
                break;
            case R.id.ll_menu:
                hideMenu(llMenu);
                break;
        }
    }

    private void showMenu(View v) {
        v.setVisibility(View.VISIBLE);
        int height = v.getHeight();
        TranslateAnimation anim = new TranslateAnimation(0, 0, -height, 0);
        anim.setDuration(200);
        v.startAnimation(anim);
    }

    private void hideMenu(View v) {
        v.setVisibility(View.GONE);
        int height = v.getHeight();
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -height);
        anim.setDuration(200);
        v.startAnimation(anim);

    }


    @OnClick({R.id.tv_type1, R.id.tv_type2, R.id.tv_type3, R.id.tv_type4, R.id.tv_type5, R.id.tv_type6, R.id.tv_type7})
    public void onViewTypeClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_type1:
                tradeType = "0";
                showToast(((TextView) view).getText().toString());
                break;
            case R.id.tv_type2:
                tradeType = "1";
                showToast(((TextView) view).getText().toString());
                break;
            case R.id.tv_type3:
                tradeType = "2";
                showToast(((TextView) view).getText().toString());
                break;
            case R.id.tv_type4:
                tradeType = "3";
                showToast(((TextView) view).getText().toString());
                break;
            case R.id.tv_type5:
                tradeType = "4";
                showToast(((TextView) view).getText().toString());
                break;
            case R.id.tv_type6:
                tradeType = "5";
                showToast(((TextView) view).getText().toString());
            case R.id.tv_type7:
                tradeType = "6";
                showToast(((TextView) view).getText().toString());
                break;
        }
        hideMenu(llMenu);
        id  = "0";
        getMyAmount();
    }

    /**
     * 我的余额明细列表
     */
    private void getMyAmount() {
        ApiUtil.getApiService().myOldAmountList(MyApplication.getToken(), id, size + "", null).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (id.equals("0")) {
                            datas.clear();
                            if (entity.getData().size() == 0) {
                                llDefault.setVisibility(View.VISIBLE);
                            } else {
                                llDefault.setVisibility(View.GONE);
                            }
                        }else{
                            if(entity.getData().size()==0){
                                //已加载全部
                               showToast("没有更多了");
                            }
                        }
                        datas.addAll(entity.getData());
                        mAdapter.notifyDataSetChanged();


                    } else {
                        showToast(entity.getMsg());
                        llDefault.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {


                }
            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
                llDefault.setVisibility(View.VISIBLE);
            }
        });
    }
}
