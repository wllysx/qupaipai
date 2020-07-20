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

import androidx.fragment.app.Fragment;

import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.adapter.ShopAdapter1;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.qupp.client.utils.waterfall.StaggeredGridView;
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
 * author: MrWang on 2019/8/27
 * email:773630555@qq.com
 * date: on 2019/8/27 16:12
 */

public class CheckDistrictFragment extends Fragment implements AbsListView.OnScrollListener{
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    StaggeredGridView mRecyclerView;
    ShopAdapter1 mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    Unbinder unbinder;
    int current = 1, size = 30;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    String type = "", categoryId = "";
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    private boolean mHasRequestedMore = true;

    public static CheckDistrictFragment newInstance(String type, String categoryId) {
        Bundle args = new Bundle();
        CheckDistrictFragment checkOrderFragment = new CheckDistrictFragment();
        args.putString("type", type);
        args.putString("categoryId", categoryId);
        checkOrderFragment.setArguments(args);
        return checkOrderFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(getActivity()));
        // springview.setFooter(new DefaultFooter(getActivity()));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

                springview.onFinishFreshAndLoad();
                current = 1;
                //   initData();
                if(type.equals("2")&&categoryId.equals("")){
                    //换购区 显示寄拍区的前20条
                    initData();
                }else{
                    initData1();
                }
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                current++;
                initData();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkdistrict, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        type = getArguments().getString("type");

        categoryId = getArguments().getString("categoryId");
        initSpringView();
        /*假数据*/
        initAdapter(datas);
        initView();
        if(current==1) {
            if(type.equals("2")&&categoryId.equals("")){
                //换购区 显示寄拍区的前20条
                initData();
            }else{
                initData1();
            }
        }
        return rootView;
    }


    private void initView(){
        ivDefault.setImageResource(R.mipmap.icon_default3);
        tvDefault.setText("亲，暂无数据");
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {

        mAdapter = new ShopAdapter1(getActivity(),data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(this);
      /*  mRecyclerView.setOnItemClickListener((parent, view, position, id) ->
                CommodityDetailsActivity.startActivityInstance(getActivity(), data.get(position).getGoodsId()));*/

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }


    /**
     * 获取商品列表
     */
    private void initData() {
        ApiUtil.getApiService().goodsInfolist(null, null, null, size + "", current + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if (entity.getData().getRecords().size() < 30) {
                                mHasRequestedMore = true;
                            } else {
                                mHasRequestedMore = false;
                            }
                        } else {
                            if (entity.getData().getRecords().size() == 0) {
                                Toast.makeText(getActivity(),"没有更多了",Toast.LENGTH_SHORT).show();
                                mHasRequestedMore = true;
                            } else {
                                mHasRequestedMore = false;
                            }
                        }
                        datas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(),entity.getMsg(),Toast.LENGTH_SHORT).show();
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
     * 获取商品列表
     */
    private void initData1() {
        ApiUtil.getApiService().goodsInfolist(type, categoryId, null, size + "", current + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if(entity.getData().getRecords().size()>0){
                                llDefault.setVisibility(View.GONE);
                            }else{
                                llDefault.setVisibility(View.VISIBLE);
                            }
                            if(entity.getData().getRecords().size()<30) {
                                mHasRequestedMore = true;
                            }else{
                                mHasRequestedMore = false;
                            }
                        }
                        datas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mHasRequestedMore = false;
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d("11111", "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d("11111", "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                current++;
                if(type.equals("2")&&categoryId.equals("")){
                    //换购区 显示寄拍区的前20条
                    initData();
                }else{
                    initData1();
                }
            }
        }
        if(firstVisibleItem<4){
            springview.setEnableHeader(true);
        }else{
            springview.setEnableHeader(false);
        }
    }
}
