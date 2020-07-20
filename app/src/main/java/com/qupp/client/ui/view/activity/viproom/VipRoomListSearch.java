package com.qupp.client.ui.view.activity.viproom;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.VipRoomAdapter;
import com.qupp.client.utils.dialog.PasswordDialog;

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
 * vip店铺搜索
 */


@SuppressLint("all")
public class VipRoomListSearch extends BaseActivity {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    List<EntityForSimple> datas = new ArrayList<>();
    VipRoomAdapter mAdapter;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viproom_listsearch);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        tvDefault.setText("该房间暂未开通~");
        ivDefault.setImageResource(R.mipmap.icon_default9);
        initView();
        initSearch();
    }

    private void initView() {
        /*假数据*/
        initAdapter(datas);
        llDefault.setVisibility(View.VISIBLE);
    }



    private void initSearch() {
        etSearch.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
              /*  datas.add(new EntityForSimple());
                mAdapter.notifyDataSetChanged();*/
                getRoomList(etSearch.getText().toString());
            }
            return false;
        });
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new VipRoomAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 4; // 2 columns
        int spacing = ScreenAdaptive.dp2px(this, 10); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //输入密码
            if (MyApplication.getToken().equals("")) {
                MyApplication.toLogin(VipRoomListSearch.this);
            } else {
                if(data.get(position).getOpenPassword().equals("1")) {
                    new PasswordDialog(VipRoomListSearch.this).setSureListener(code -> {
                        checkPassword(data.get(position).getRoomId(), code, data.get(position).getNumber(), data.get(position).getVipNickname(), data.get(position).getVipPhone(), data.get(position).getName());
                    }).show(data.get(position).getName());
                }else{
                    VipRoomDetails.startActivityInstance(VipRoomListSearch.this,data.get(position).getRoomId(),data.get(position).getNumber(),data.get(position).getVipNickname(),data.get(position).getVipPhone(),data.get(position).getName());
                }
            }

        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    /**
     * 校验密码
     * @param roomId
     * @param password
     */
    private void checkPassword(String roomId,String password,String number,String vipNickname,String vipPhone,String name){
        ApiUtil.getApiService().checkPassword(MyApplication.getToken(),roomId,password).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        VipRoomDetails.startActivityInstance(VipRoomListSearch.this,roomId,number,vipNickname,vipPhone,name);
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

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, VipRoomListSearch.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @OnClick({R.id.tv_cancel})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    /**
     * 店铺列表
     */
    private void getRoomList(String number) {
        ApiUtil.getApiService().getOneByNumbe(number).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData() != null) {
                            datas.clear();
                            llDefault.setVisibility(View.GONE);
                            datas.add(entity.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        showToast(entity.getMsg());
                        datas.clear();
                        mAdapter.notifyDataSetChanged();
                        llDefault.setVisibility(View.VISIBLE);
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
