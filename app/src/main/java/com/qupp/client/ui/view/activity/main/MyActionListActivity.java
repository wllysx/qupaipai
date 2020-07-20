package com.qupp.client.ui.view.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.ui.view.activity.scoreshop.ShopCommodityTypeActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MyAcuctionAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.dialog.MiddleDialog;
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
 * 我的活动
 */

public class MyActionListActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    MyAcuctionAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    int current = 1;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    String position="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_list);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(MyActionListActivity.this), 0, 0);
        setStateColor(false);
        position = getIntent().getStringExtra("position");
        initView();
        initSpringView();
        initData();

    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(this));
        // springview.setFooter(new DefaultFooter(this));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                current = 1;
                initData();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                current++;
                initData();
            }
        });
    }


    private void initView() {
        if(position.equals("4")) {
            tvTitle.setText("活动列表");
        }else{
            tvTitle.setText("游戏");
        }
        ivDefault.setImageResource(R.mipmap.icon_default13);
        tvDefault.setText("亲，暂时没有活动～");
        initAdapter(datas);
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new MyAcuctionAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 10);
        ; // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));

        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            switch (datas.get(position).getType()) {
                case 1:
                    H5Web.startActivityInstance(MyActionListActivity.this, datas.get(position).getH5Id());
                    break;
                case 2:
                    CommodityList.startActivityInstance(MyActionListActivity.this, datas.get(position).getName(), datas.get(position).getLinkId());
                    break;
                case 3:
                    ShopCommodityTypeActivity.startActivityInstance(MyActionListActivity.this, datas.get(position).getName(), datas.get(position).getLinkId(), "", "");
                    break;
                case 4:
                    //小程序
                    break;
                case 5:
                    //拍卖区域
                    if (datas.get(position).getLinkId().equals("9")) {
                        if (MyApplication.getToken().equals("")) {
                            LoginActivity.startActivityInstance(MyActionListActivity.this);
                        } else {
                            isNewPersion();
                        }
                    } else {
                        CommodityTypeActivity.startActivityInstance(MyActionListActivity.this, datas.get(position).getName(), datas.get(position).getLinkId(), "", "");
                    }
                    break;
                case 6:
                    break;
                case 7:
                    String url = datas.get(position).getLinkId() + MyApplication.getToken();
                    ActionH5Web.startActivityInstance(MyActionListActivity.this, url);
                    break;
                case 8:
                    CommodityDetailsActivity.startActivityInstance(MyActionListActivity.this, datas.get(position).getLinkId());
                    break;
            }
        });

        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }


    public static void startActivityInstance(Activity activity,String position) {
        activity.startActivity(new Intent(activity, MyActionListActivity.class).putExtra("position",position));

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


    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    public void initData() {
        ApiUtil.getApiService().pageList(position).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        datas.clear();
                        datas.addAll(entity.getData());
                        if(datas.size()==0){
                            llDefault.setVisibility(View.VISIBLE);
                        }else{
                            llDefault.setVisibility(View.INVISIBLE);
                        }
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

    private void isNewPersion() {
        ApiUtil.getApiService().isNewPeople(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {

                        if (entity.getData().equals("false")) {
                            //非新人
                            MyApplication.isNewPeople = false;
                            new MiddleDialog(MyActionListActivity.this).setSureListener(() -> {
                                CommodityTypeActivity.startActivityInstance(MyActionListActivity.this, "新人区", "9", "", "");
                            }).show("温馨提示", "您已不是新人，确认要进入新人区吗", "取消", "确定", false);
                        } else {
                            //新人
                            MyApplication.isNewPeople = true;
                            CommodityTypeActivity.startActivityInstance(MyActionListActivity.this, "新人区", "9", "", "");
                        }
                    } else {
                        Toast.makeText(MyActionListActivity.this, entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }


}
