package com.qupp.client.ui.view.fragment.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.qupp.client.ui.view.activity.MainActivity;
import com.qupp.client.ui.view.activity.viproom.VipRoomDetails;
import com.qupp.client.ui.view.activity.viproom.VipRoomListSearch;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
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
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 13:51
 */


@SuppressLint("all")
public class FragmentVip extends Fragment {

    View rootView;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vip, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        flLayout.setPadding(0, MyApplication.getStateBar(getActivity()), 0, 0);
        ((MainActivity) getActivity()).setStateColor(false);
        initView();
        initData();
        getRoomList();
        initSpringView();
        return rootView;
    }

    private void initView() {
        tvTitle.setText(getResources().getString(R.string.main_vip));
        initAdapter(data);
    }


    private void initSpringView() {
        springview.setHeader(new DefaultHeader(getActivity()));
        springview.setFooter(new DefaultFooter(getActivity()));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                id = "0";
                getRoomList();
                initData();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                if (data != null && data.size() > 0) {
                    id = data.get(data.size() - 1).getRoomId();
                    getRoomList();
                }

            }
        });
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mAdapter = new VipRoomAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 4; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getActivity(), 10); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //输入密码
            if (MyApplication.getToken().equals("")) {
                MyApplication.toLogin(getActivity());
            } else {
                new PasswordDialog(getActivity()).setSureListener(code -> {
                    checkPassword(data.get(position).getRoomId(),code,data.get(position).getNumber(),data.get(position).getVipNickname(),data.get(position).getVipPhone(),data.get(position).getName());
                }).show(data.get(position).getName());
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
                        VipRoomDetails.startActivityInstance(getActivity(),roomId,number,vipNickname,vipPhone,name);
                    } else {
                        Toast.makeText(getContext(),entity.getMsg(),Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });

    }


    public static FragmentVip newInstance() {
        FragmentVip fragment = new FragmentVip();
        return fragment;
    }


    public void refreshBar() {
        ((MainActivity) getActivity()).setStateColor(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.iv_search})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_search:
                VipRoomListSearch.startActivityInstance(getActivity());
                break;

        }
    }

    /**
     * 店铺个数
     */
    private void initData() {
        ApiUtil.getApiService().vipRoomtotal().enqueue(new Callback<EntityForSimpleString>() {
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
     * 店铺列表
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
                                data.clear();
                                if(entity.getData().size()==0){
                                    llDefault.setVisibility(View.VISIBLE);
                                }else{
                                    llDefault.setVisibility(View.GONE);
                                }
                            }else{
                                if (entity.getData().size() == 0) {
                                    Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_LONG).show();
                                }
                            }
                            data.addAll(entity.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        llDefault.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

}
