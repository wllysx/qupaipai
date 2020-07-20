package com.qupp.client.ui.view.fragment.son;

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
import com.qupp.client.ui.view.activity.mine.order.OrderDetails;
import com.qupp.client.ui.view.activity.mine.order.OrderPay;
import com.qupp.client.ui.view.activity.mine.order.SelectMealActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.adapter.OrderAdapter;
import com.qupp.client.utils.event.OrderPaySuccess;
import com.qupp.client.utils.view.springview.DefaultFooter;
import com.qupp.client.utils.view.springview.DefaultHeader;
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
 * author: MrWang on 2019/8/27
 * email:773630555@qq.com
 * date: on 2019/8/27 16:12
 */

public class OrderListFragment extends Fragment {
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    OrderAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    ;
    Unbinder unbinder;
    String orderStatus = "";
    int current = 1, size = 30;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;

    public static OrderListFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        OrderListFragment checkOrderFragment = new OrderListFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_order_orderlist, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        int position = getArguments().getInt("position");
        switch (position) {
            case 0:
                break;
            case 1:
                orderStatus = "0";
                break;
            case 2:
                orderStatus = "1";
                break;
            case 3:
                orderStatus = "2";
                break;
            case 4:
                orderStatus = "3";
                break;
        }
        initSpringView();
        initAdapter(datas);
        getData();
        return rootView;
    }


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mAdapter = new OrderAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Toast.makeText(getContext(), position + "", Toast.LENGTH_LONG).show();
        });
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(), 10);
        ; // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> OrderDetails.startActivityInstance(getActivity(), data.get(position).getOrderId(), datas.get(position).getOrderStatus()));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (DoubleClick.isFastDoubleClick()) {
                return;
            }
            String textString = ((TextView) view).getText().toString();
            if (textString.equals("去支付")) {
                //支付分支
                if (data.get(position).getIntegralType() != null && data.get(position).getIntegralType().equals("2")) {
                    if(data.get(position).getAuctionMealId().equals("0")){
                        SelectMealActivity.startActivityInstance(getActivity(), "1", data.get(position).getOrderId(), data.get(position).getAuctionId(), -1);
                    }else{
                        OrderDetails.startActivityInstance(getActivity(), data.get(position).getOrderId(), datas.get(position).getOrderStatus());
                    }
                } else {
                    OrderPay.startActivityInstance(getActivity(), data.get(position).getOrderId(),null);
                }
            } else if (textString.equals("确认收货")) {
                receipt(data.get(position).getOrderId());
            }
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    /**
     * 确认收货
     */
    private void receipt(String orderId) {
        ApiUtil.getApiService().receipt(MyApplication.getToken(), orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        EventBus.getDefault().post(new OrderPaySuccess("1"));
                    } else {
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
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    /**
     * 获取订单列表
     */
    private void getData() {
        ApiUtil.getApiService().orderlist(MyApplication.getToken(), size + "", current + "", orderStatus).enqueue(new Callback<MessageForSimple>() {
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
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void order(OrderPaySuccess event) {
        if (event != null) {
            if (event.getType().equals("1")) {
                current = 1;
                getData();
            }
        }
    }


}
