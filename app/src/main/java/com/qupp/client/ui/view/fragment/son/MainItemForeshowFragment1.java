package com.qupp.client.ui.view.fragment.son;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.adapter.MainItemAdapter1;
import com.qupp.client.utils.event.MainPageRefresh;
import com.qupp.client.utils.event.ToTopEvent;
import com.qupp.client.utils.view.headerviewpager.HeaderViewPagerFragment;
import com.qupp.client.utils.waterfall.StaggeredGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * author: MrWang on 2019/10/23
 * email:773630555@qq.com
 * date: on 2019/10/23 17:41
 */


public class MainItemForeshowFragment1 extends HeaderViewPagerFragment implements AbsListView.OnScrollListener{
    @BindView(R.id.tv_tab1)
    TextView tvTab1;
    @BindView(R.id.tv_tab2)
    TextView tvTab2;
    @BindView(R.id.tv_tab3)
    TextView tvTab3;
    @BindView(R.id.mRecyclerView)
    StaggeredGridView mRecyclerView;
    private MainItemAdapter1 mAdapter;

    List<EntityForSimple> mDatas = new ArrayList<>();
    Unbinder unbinder;
    int current = 1, size = 20, type = 1;
    private boolean mHasRequestedMore = true;

    public static MainItemForeshowFragment1 newInstance() {
        Bundle args = new Bundle();
        MainItemForeshowFragment1 tripFragment = new MainItemForeshowFragment1();
        tripFragment.setArguments(args);
        return tripFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mainitem_foreshow1, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        initAdapter(mDatas);
        initData();
        return rootView;
    }


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mAdapter = new MainItemAdapter1(getActivity(),data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(this);
       /* mRecyclerView.setOnItemClickListener((parent, view, position, id) ->
        {
            if (DoubleClick.isFastDoubleClick()) {
                return;
            }
            NewCommodityDetailsActivity.startActivityInstance(getActivity(), data.get(position).getStatus(), data.get(position).getAuctionId(), data.get(position).getTopPrice(), data.get(position).getGoodsName());

        });*/


    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @OnClick({R.id.tv_tab1, R.id.tv_tab2, R.id.tv_tab3})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_tab1:
                tvTab1.setTextColor(getResources().getColor(R.color.white));
                tvTab2.setTextColor(Color.parseColor("#e0b665"));
                tvTab3.setTextColor(Color.parseColor("#e0b665"));
                tvTab1.setBackgroundResource(R.drawable.bg_main_tab_selected);
                tvTab2.setBackgroundResource(R.drawable.bg_main_tab_selected_un);
                tvTab3.setBackgroundResource(R.drawable.bg_main_tab_selected_un);
                type = 1;
                break;
            case R.id.tv_tab2:
                tvTab1.setTextColor(Color.parseColor("#e0b665"));
                tvTab2.setTextColor(getResources().getColor(R.color.white));
                tvTab3.setTextColor(Color.parseColor("#e0b665"));
                tvTab1.setBackgroundResource(R.drawable.bg_main_tab_selected_un);
                tvTab2.setBackgroundResource(R.drawable.bg_main_tab_selected);
                tvTab3.setBackgroundResource(R.drawable.bg_main_tab_selected_un);
                type = 2;
                break;
            case R.id.tv_tab3:
                tvTab1.setTextColor(Color.parseColor("#e0b665"));
                tvTab2.setTextColor(Color.parseColor("#e0b665"));
                tvTab3.setTextColor(getResources().getColor(R.color.white));
                tvTab1.setBackgroundResource(R.drawable.bg_main_tab_selected_un);
                tvTab2.setBackgroundResource(R.drawable.bg_main_tab_selected_un);
                tvTab3.setBackgroundResource(R.drawable.bg_main_tab_selected);
                type = 3;
                break;
        }
        current = 1;
        initData();
    }

    /**
     * 获取商品列表
     */
    private void initData() {
        ApiUtil.getApiService().listDate(current + "", size + "", type + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            mDatas.clear();
                            if(entity.getData().getRecords().size()<size) {
                                mHasRequestedMore = true;
                            }else{
                                mHasRequestedMore = false;
                            }
                        }else{
                            if (entity.getData().getRecords().size() == 0) {
                                Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_LONG).show();
                                mHasRequestedMore = true;
                            }else{
                                mHasRequestedMore = false;
                            }
                        }

                        mDatas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
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
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(MainPageRefresh event) {
        current = 1;
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void totop(ToTopEvent event) {
        if(event.getPosition()==0) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
/* Log.d("11111", "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);*/
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d("11111", "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                current++;
                initData();
            }
        }
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

}
