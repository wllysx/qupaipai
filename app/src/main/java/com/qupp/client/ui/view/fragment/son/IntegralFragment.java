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
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.IntergralAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.event.RefreshIntegral;
import com.qupp.client.utils.view.springview.DefaultFooter;
import com.liaoinstan.springview.widget.SpringView;

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
 * 我的积分
 * author: MrWang on 2019/8/27
 * email:773630555@qq.com
 * date: on 2019/8/27 16:12
 */

public class IntegralFragment extends Fragment {
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    IntergralAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    Unbinder unbinder;
    int page = 0;
    int current = 1, size = 30;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;

    public static IntegralFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        IntegralFragment checkOrderFragment = new IntegralFragment();
        checkOrderFragment.setArguments(args);
        return checkOrderFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        page = getArguments().getInt("page");
    }

    private void initSpringView() {
       // springview.setHeader(new DefaultHeader(getActivity()));
        springview.setFooter(new DefaultFooter(getActivity()));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                current = 1;
                if (page == 1) {
                    initDataSuper();
                } else {
                    initData();
                }
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                current++;
                if (page == 1) {
                    initDataSuper();
                } else {
                    initData();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initSpringView();
        tvDefault.setText("亲，您还没有积分");
        ivDefault.setImageResource(R.mipmap.icon_default6);
        /*假数据*/
        initAdapter(datas);
        if (page == 1) {
            initDataSuper();
        } else {
            initData();
        }
        return rootView;
    }


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mAdapter = new IntergralAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(), 10);
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
     * 积分详情列表
     */
    private void initData() {
        ApiUtil.getApiService().myIntegrate(MyApplication.getToken(), current + "", size + "").enqueue(new Callback<MessageForSimple>() {
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
                        }
                        datas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        llDefault.setVisibility(View.VISIBLE);
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
     * 超值详情列表
     */
    private void initDataSuper() {
        ApiUtil.getApiService().mySuperIntegrate(MyApplication.getToken(), current + "", size + "").enqueue(new Callback<MessageForSimple>() {
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
                        }
                        datas.addAll(entity.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        llDefault.setVisibility(View.VISIBLE);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshIntegral event) {
        if (event != null) {
            current = 1;
            if (page == 1) {
                initDataSuper();
            } else {
                initData();
            }
        }
    }

}
