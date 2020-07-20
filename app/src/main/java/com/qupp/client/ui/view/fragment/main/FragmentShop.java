package com.qupp.client.ui.view.fragment.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.MainActivity;
import com.qupp.client.ui.view.activity.scoreshop.ShopCommodityTypeActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.TypeAdapter;
import com.qupp.client.utils.adapter.TypeRightAdapter;
import com.qupp.client.utils.event.NetRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 分类
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 13:51
 */


@SuppressLint("all")
public class FragmentShop extends Fragment {

    View rootView;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mRecyclerView_left)
    RecyclerView mRecyclerViewLeft;
    @BindView(R.id.ll_linear)
    LinearLayout llLinear;
    TypeRightAdapter mAdapter;
    TypeAdapter mTypeAdapter;

    ArrayList<EntityForSimple> data = new ArrayList<>();
    ArrayList<EntityForSimple> typedata = new ArrayList<>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        llLinear.setPadding(0, MyApplication.getStateBar(getActivity()), 0, 0);
        ((MainActivity) getActivity()).setStateColor(false);
        initView();
        initData();
        initAdapter(data);
        initTypeAdapter(typedata);

        return rootView;
    }


    private void initView() {
        tvTitle.setText("分类");
    }


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new TypeRightAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 3; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(), 35); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ShopCommodityTypeActivity.startActivityInstance(getActivity(), ((TextView) view.findViewById(R.id.tv_name)).getText().toString(), data.get(position).getCategoryId(),"","");
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    /**
     * 设置RecyclerView属性
     */
    private void initTypeAdapter(List<EntityForSimple> data) {

        mRecyclerViewLeft.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mTypeAdapter = new TypeAdapter(data);
        mTypeAdapter.bindToRecyclerView(mRecyclerViewLeft);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(), 0); // 50px
        boolean includeEdge = false;
        mRecyclerViewLeft.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            for(EntityForSimple type:typedata){
                type.setChecked(false);
            }
            typedata.get(position).setChecked(true);
            mTypeAdapter.notifyDataSetChanged();
            initData2(typedata.get(position).getCategoryId());
        });
        MyApplication.setMaxFlingVelocity(mRecyclerViewLeft);
    }

    public static FragmentShop newInstance() {
        FragmentShop fragment = new FragmentShop();
        return fragment;
    }


    public void refreshBar() {
        try {
            ((MainActivity) getActivity()).setStateColor(false);
        }catch (Exception e){

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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


    /**
     * 获取商品列表
     */
    private void initData() {
        ApiUtil.getApiService().categorylist("0",  "1",null).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        typedata.clear();
                        typedata.addAll(entity.getData());
                        if(typedata.size()>0){
                            typedata.get(0).setChecked(true);
                        }
                        mTypeAdapter.notifyDataSetChanged();
                        initData2(typedata.get(0).getCategoryId());
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

    private void initData2(String parentId) {
        ApiUtil.getApiService().categorylist(parentId,  "1",null).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        data.clear();
                        data.addAll(entity.getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
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
    public void toLogin(NetRefreshEvent event) {
        if (event != null) {
            initData();
        }
    }

}
