package com.qupp.client.ui.view.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.viproom.VipRoomDetails;
import com.qupp.client.ui.view.activity.viproom.VipRoomListSearch;
import com.qupp.client.ui.view.fragment.son.MessageFragment;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CommonTabPagerAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.VipRoomAdapter;
import com.qupp.client.utils.dialog.PasswordDialog;
import com.qupp.client.utils.view.springview.DefaultFooter;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * VIP
 */

public class VipActivity extends BaseActivity implements CommonTabPagerAdapter.TabPagerListener {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    List<EntityForSimple> data = new ArrayList<>();
    VipRoomAdapter mAdapter;
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.tv_roomcount)
    TextView tvRoomcount;
    String id = "0";
    String size = "24";
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    boolean vip = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vip);
        ButterKnife.bind(this);
        flLayout.setPadding(0, MyApplication.getStateBar(VipActivity.this), 0, 0);
        setStateColor(false);
        initView();
        initData();
        vipRoomlistCustom();
        initSpringView();
    }


    private void initView() {
        tvTitle.setText(getResources().getString(R.string.main_vip));
        initAdapter(data);
    }


    private void initSpringView() {
        springview.setHeader(new DefaultHeader(VipActivity.this));
        springview.setFooter(new DefaultFooter(VipActivity.this));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                id = "0";
                vip = true;
                vipRoomlistCustom();
                initData();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                if (data != null && data.size() > 0) {
                    id = data.get(data.size() - 1).getRoomId();
                    if(vip) {
                        vipRoomlistCustom();
                    }else {
                        getRoomList();
                    }
                }

            }
        });
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(VipActivity.this, 4));
        mAdapter = new VipRoomAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 4; // 2 columns
        int spacing = ScreenAdaptive.dp2px(VipActivity.this, 10); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            //输入密码
            if (MyApplication.getToken().equals("")) {
                MyApplication.toLogin(VipActivity.this);
            } else {
                if (data.get(position).getOpenPassword().equals("1")) {
                    new PasswordDialog(VipActivity.this).setSureListener(code -> {
                        checkPassword(data.get(position).getRoomId(), code, data.get(position).getNumber(), data.get(position).getVipNickname(), data.get(position).getVipPhone(), data.get(position).getName());
                    }).show(data.get(position).getName());
                } else {
                    VipRoomDetails.startActivityInstance(VipActivity.this, data.get(position).getRoomId(), data.get(position).getNumber(), data.get(position).getVipNickname(), data.get(position).getVipPhone(), data.get(position).getName());
                }
            }
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);
    }

    /**
     * 校验密码
     *
     * @param roomId
     * @param password
     */
    private void checkPassword(String roomId, String password, String number, String vipNickname, String vipPhone, String name) {
        ApiUtil.getApiService().checkPassword(MyApplication.getToken(), roomId, password).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        VipRoomDetails.startActivityInstance(VipActivity.this, roomId, number, vipNickname, vipPhone, name);
                    } else {
                        Toast.makeText(VipActivity.this, entity.getMsg(), Toast.LENGTH_LONG).show();
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
     * 店铺个数
     */
    private void initData() {
        ApiUtil.getApiService().vipRoomtotalOpen().enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvRoomcount.setText("目前VIP房间数为：" + entity.getData() + "个");
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

    /**
     * 店铺列表（普通）
     */
    private void getRoomList() {
        ApiUtil.getApiService().vipRoomlist("", id, size).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData() != null && entity.getData().size() > 0) {
                            if (id.equals("0")) {
                            } else {
                                if (entity.getData().size() == 0) {
                                    Toast.makeText(VipActivity.this, "没有更多了", Toast.LENGTH_LONG).show();
                                }
                            }
                            data.addAll(entity.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
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
     * 店铺列表（vip）
     */
    private void vipRoomlistCustom() {
        ApiUtil.getApiService().vipRoomlistCustom("", id, size).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData() != null && entity.getData().size() > 0) {
                            if (id.equals("0")) {
                                data.clear();
                            }
                            data.addAll(entity.getData());
                            mAdapter.notifyDataSetChanged();

                            if(entity.getData().size()<Integer.valueOf(size)){
                                id="0";
                                vip = false;
                                getRoomList();
                            }
                        }else if(entity.getData() != null && entity.getData().size() == 0){
                            id="0";
                            vip = false;
                            getRoomList();
                        }

                    } else {
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, VipActivity.class));
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

    @Override
    public Fragment getFragment(int position) {
        return MessageFragment.newInstance(position);
    }

    @OnClick({R.id.back, R.id.iv_search})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_search:
                VipRoomListSearch.startActivityInstance(VipActivity.this);
                break;
        }
    }


}
