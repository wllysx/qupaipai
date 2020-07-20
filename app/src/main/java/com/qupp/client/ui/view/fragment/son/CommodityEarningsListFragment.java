package com.qupp.client.ui.view.fragment.son;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.EarningsAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收益排行榜
 */

public class CommodityEarningsListFragment extends Fragment {
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    EarningsAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    Unbinder unbinder;
    String auctionId="";

    public static CommodityEarningsListFragment newInstance(String auctionId) {
        Bundle args = new Bundle();
        args.putString("auctionId",auctionId);
        CommodityEarningsListFragment checkOrderFragment = new CommodityEarningsListFragment();
        checkOrderFragment.setArguments(args);
        return checkOrderFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initSpringView() {
       // springview.setHeader(new DefaultHeader(getActivity()));
        //springview.setFooter(new DefaultFooter(getActivity()));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_commondity_earningslist, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        auctionId = getArguments().getString("auctionId");
        initSpringView();
        /*假数据*/
        initAdapter(datas);
        initData();
        return rootView;
    }


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        mAdapter = new EarningsAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Toast.makeText(getContext(), position + "", Toast.LENGTH_LONG).show();
        });
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(),10);; // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    private void initData() {
        ApiUtil.getApiService().rankingList(auctionId).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        datas.addAll(entity.getData());
                        mAdapter.notifyDataSetChanged();
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
