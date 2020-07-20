package com.qupp.client.ui.view.fragment.son;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.main.NewCommodityDetailsActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MainItemAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.event.MainPageRefresh;
import com.qupp.client.utils.view.headerviewpager.HeaderViewPagerFragment;

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


public class MainItemFragment extends HeaderViewPagerFragment {
  /*  @BindView(R.id.springview)
    SpringView springview;*/
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private Context context;
    private int mPage;
    public static final String MERCHANT_DETAILS_PAGE = "MERCHANT_DETAILS_PAGE";
    private MainItemAdapter mAdapter;

    List<EntityForSimple> mDatas = new ArrayList<>();
    Unbinder unbinder;
    int current = 1, size = 30;

    public static MainItemFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(MERCHANT_DETAILS_PAGE, page);
        MainItemFragment tripFragment = new MainItemFragment();
        tripFragment.setArguments(args);
        return tripFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(MERCHANT_DETAILS_PAGE);
        context = getActivity().getApplicationContext();
    }

    private void initSpringView() {
       // springview.setHeader(new DefaultHeader(getActivity()));
       /* springview.setFooter(new DefaultFooter(getActivity()));
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
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mainitem, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        initAdapter(mDatas);
        initSpringView();
        initData();
        return rootView;
    }


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mAdapter = new MainItemAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            NewCommodityDetailsActivity.startActivityInstance(getActivity(),data.get(position).getStatus(),data.get(position).getAuctionId(),data.get(position).getTopPrice(),data.get(position).getGoodsName());
        });
        int spanCount = 2; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(),10); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }


    /**
     * 获取商品列表
     */
    private void initData(){
        ApiUtil.getApiService().listToday(current+"",size+"").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if(current==1){
                            mDatas.clear();
                        }
                        mDatas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    /**
     * socket数据刷新
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(MainPageRefresh event) {
        current = 1;
        initData();
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }
}
