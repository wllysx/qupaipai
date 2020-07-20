package com.qupp.client.ui.view.fragment.son;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.mine.coupon.CommodityCoupon;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CouponAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.view.springview.DefaultFooter;
import com.qupp.client.utils.view.springview.DefaultHeader;
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
 * 我的优惠圈
 * author: MrWang on 2019/8/27
 * email:773630555@qq.com
 * date: on 2019/8/27 16:12
 */

public class CouponFragment extends Fragment {
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    CouponAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    Unbinder unbinder;
    int current = 1, size = 20, page = 0;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    int type = 1;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;


    public static CouponFragment newInstance(int page) {
        Bundle args = new Bundle();
        CouponFragment checkOrderFragment = new CouponFragment();
        args.putInt("page", page);
        checkOrderFragment.setArguments(args);
        return checkOrderFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initSpringView() {
        springview.setHeader(new DefaultHeader(getActivity()));
        springview.setFooter(new DefaultFooter(getActivity()));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                current = 1;
                getData();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                current++;
                getData();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_counon, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        ivDefault.setImageResource(R.mipmap.icon_default11);
        tvDefault.setText("亲，暂时没有可用优惠券哦～");
        page = getArguments().getInt("page");
        type = page;
        initSpringView();
        initAdapter(datas);
        if (current == 1) {
            getData();
        }
        return rootView;
    }


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mAdapter = new CouponAdapter(data, page);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if(datas.get(position).getUseLimitType().equals("2")) {
                String ids = "";
                for(String str: datas.get(position).getCategoryIdList()){
                    ids = ids+str+",";
                }
                CommodityCoupon.startActivityInstance(getActivity(), datas.get(position).getCouponShowName(), ids);
            }else{
                //首页
                MyApplication.toMain = true;
                getActivity().finish();
            }
        });
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(), 10); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);
    }


    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    /**
     * 优惠券列表
     */
    private void getData() {
        ApiUtil.getApiService().getMyPage(MyApplication.getToken(), 1 + "", size + "", current + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if (entity.getData().getRecords().size() == 0) {
                                llDefault.setVisibility(View.VISIBLE);
                            } else {
                                llDefault.setVisibility(View.GONE);
                            }
                        } else {
                            if (entity.getData().getRecords().size() == 0) {
                                Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_LONG).show();
                            }
                        }
                        datas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        llDefault.setVisibility(View.VISIBLE);
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

}
