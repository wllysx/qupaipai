package com.qupp.client.ui.view.fragment.son;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
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
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainItemFragment1 extends HeaderViewPagerFragment implements AbsListView.OnScrollListener {

    Unbinder unbinder;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    private StaggeredGridView gridView;
    MainItemAdapter1 adapter;
    int current = 1, size = 20;
    String startTime = "", endTime = "";
    ArrayList<EntityForSimple> mdatas = new ArrayList<>();

    public static MainItemFragment1 newInstance() {
        return new MainItemFragment1();
    }

    private boolean mHasRequestedMore = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainitem1, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        gridView = (StaggeredGridView) view.findViewById(R.id.mRecyclerView);
        tvDefault.setText("亲，暂无数据..");
        ivDefault.setImageResource(R.mipmap.icon_default3);
        initAdapter(mdatas);
        //initData();
        return view;
    }


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        adapter = new MainItemAdapter1(getActivity(), data);
        gridView.setAdapter(adapter);
        gridView.setOnScrollListener(this);
      /*  gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (DoubleClick.isFastDoubleClick()) {
                return;
            }
            NewCommodityDetailsActivity.startActivityInstance(getActivity(), data.get(position).getStatus(), data.get(position).getAuctionId(), data.get(position).getTopPrice(), data.get(position).getGoodsName());
        });*/

    }

    @Override
    public View getScrollableView() {
        return ((AbsListView) gridView);
    }

    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(MainPageRefresh event) {
        mHasRequestedMore = true;
        current = 1;
        startTime = event.getStartTime();
        endTime = event.getEndTime();
        if(startTime.equals("2")||startTime.equals("3")){
            initData1();
        }else {
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void totop(ToTopEvent event) {
        if (event.getPosition() == 1) {
            gridView.setAdapter(adapter);
        }
    }


    /**
     * 获取商品列表
     */
    private void initData() {
        Log.d("starttitmenditim",current+","+size+","+startTime+","+endTime+mHasRequestedMore);
        if(current==1) {
            mdatas.clear();
            adapter.notifyDataSetChanged();
        }
        ApiUtil.getApiService().listTodayHours(current + "", size + "", startTime, endTime).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            mdatas.clear();
                            if (entity.getData().getRecords().size() < size) {
                                mHasRequestedMore = true;
                            } else {
                                mHasRequestedMore = false;
                            }
                            if(entity.getData().getRecords().size()==0){
                                llDefault.setVisibility(View.VISIBLE);
                            }else{
                                llDefault.setVisibility(View.GONE);
                            }
                        } else {
                            if (entity.getData().getRecords().size() == 0) {
                                Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_LONG).show();
                                mHasRequestedMore = true;
                            } else {
                                mHasRequestedMore = false;
                            }
                        }
                        Log.d("starttitmenditim",current+","+size+","+startTime+","+endTime+mHasRequestedMore);
                        mdatas.addAll(entity.getData().getRecords());
                        if (current == 1) {
                            adapter.notifyDataSetChanged();
                            gridView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
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
     * 获取商品列表（明日后日）
     */
    private void initData1() {
        Log.d("starttitmenditim",current+","+size+","+startTime+","+endTime+mHasRequestedMore);
        if(current==1) {
            mdatas.clear();
            adapter.notifyDataSetChanged();
        }
        ApiUtil.getApiService().listDate(current + "", size + "", startTime).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            mdatas.clear();
                            if (entity.getData().getRecords().size() < size) {
                                mHasRequestedMore = true;
                            } else {
                                mHasRequestedMore = false;
                            }
                            if(entity.getData().getRecords().size()==0){
                                llDefault.setVisibility(View.VISIBLE);
                            }else{
                                llDefault.setVisibility(View.GONE);
                            }
                        } else {
                            if (entity.getData().getRecords().size() == 0) {
                                Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_LONG).show();
                                mHasRequestedMore = true;
                            } else {
                                mHasRequestedMore = false;
                            }
                        }
                        Log.d("starttitmenditim",current+","+size+","+startTime+","+endTime+mHasRequestedMore);
                        mdatas.addAll(entity.getData().getRecords());
                        if (current == 1) {
                            adapter.notifyDataSetChanged();
                            gridView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
       /* Log.d("22222222", "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);*/
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d("11111", "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                current++;
                if (startTime != null && !startTime.equals("")) {
                    if(startTime.equals("2")||startTime.equals("3")){
                        initData1();
                    }else {
                        initData();
                    }
                }
            }
        }
    }

}
