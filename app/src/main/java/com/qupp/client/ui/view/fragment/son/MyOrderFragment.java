package com.qupp.client.ui.view.fragment.son;

import android.content.Intent;
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
import com.qupp.client.ui.view.activity.customerservice.ContractWebCenter;
import com.qupp.client.ui.view.activity.scoreshop.CommodityPay;
import com.qupp.client.ui.view.activity.scoreshop.MyOrderDetails;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CustomProgressDialog;
import com.qupp.client.utils.adapter.MyOrderAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.dialog.MiddleDialog;
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

import static com.qupp.client.ui.view.activity.scoreshop.Authentication.*;


public class MyOrderFragment extends Fragment {
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    MyOrderAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    Unbinder unbinder;
    int current = 1, size = 30, page = 0;
    String orderStatusQuery = "4";
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    private CustomProgressDialog pd;

    public static MyOrderFragment newInstance(int page) {
        Bundle args = new Bundle();
        MyOrderFragment checkOrderFragment = new MyOrderFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_myorder, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        pd = CustomProgressDialog.createDialog(getActivity());
        EventBus.getDefault().register(this);
        page = getArguments().getInt("page");
        switch (page) {
            case 0:
                orderStatusQuery = "4";
                break;
            case 1:
                orderStatusQuery = "0";
                break;
            case 2:
                orderStatusQuery = "1";
                break;
            case 3:
                orderStatusQuery = "2";
                break;
            case 4:
                orderStatusQuery = "3";
                break;
        }
        initSpringView();
        /*假数据*/
        initAdapter(datas);
        if(current==1){
            getData();
        }
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mAdapter = new MyOrderAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //Toast.makeText(getContext(), position + "", Toast.LENGTH_LONG).show();
        });
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(), 10);
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> MyOrderDetails.startActivityInstance(getActivity(),datas.get(position).getOrderId()));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            String textString = ((TextView) view).getText().toString();

            if (DoubleClick.isFastDoubleClick()) {
                return;
            }
            if (textString.equals("申请寄拍")) {
                //StatService.onEvent(getActivity(), "shenqingjipai", "申请寄拍");ddddd
                pd.show();
                checkRealPerson(datas.get(position).getOrderId());
            } else if (textString.equals("去支付")) {
                CommodityPay.startActivityInstance(getActivity(), "1",datas.get(position).getOrderId(),null);
            }else if(textString.equals("确认收货")){
                orderreceipt(datas.get(position).getOrderId());
            }
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    /**
     * 获取实名认证状态
     */
    private void checkRealPerson(String orderId) {
        ApiUtil.getApiService().checkRealPersonNum(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData().equals("1")) {
                            applyMailing(orderId);
                        } else if(entity.getData().equals("0")){
                            new MiddleDialog(getActivity()).setSureListener(() -> startActivityInstance(getActivity())).show("温馨提示", "为了保障您的账户安全，请完成实名认证后在进行提现！", "取消", "去认证", false);


                        }else if(entity.getData().equals("2")){
                            new MiddleDialog(getActivity()).setSureListener(() -> startActivityInstance(getActivity())).show("温馨提示", "一个身份证只能绑定一个账号，请更换身份信息！", "取消", "去更换", false);
                        }
                    } else {
                        Toast.makeText(getActivity(),entity.getMsg(),Toast.LENGTH_LONG).show();
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                } catch (Exception e) {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }

                }
            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }

    /**
     * 申请寄拍
     *
     * @param orderId
     */
    private void applyMailing(String orderId) {
        ApiUtil.getApiService().applyMailing(MyApplication.getToken(),orderId).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        EventBus.getDefault().post(new OrderPaySuccess("1"));
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        startActivity(new Intent(getActivity(), ContractWebCenter.class).putExtra("OpenUrl",entity.getData()));
                    } else {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(getActivity(),entity.getMsg(),Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }

                }
            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }


    /**
     * 确认收货
     */
    private void orderreceipt(String orderId) {
        ApiUtil.getApiService().orderreceipt(MyApplication.getToken(),orderId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        EventBus.getDefault().post(new OrderPaySuccess("1"));
                    } else {
                        Toast.makeText(getActivity(),entity.getMsg(),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){

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
        ApiUtil.getApiService().mallorderlist(MyApplication.getToken(),orderStatusQuery, null, size + "", current + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if(entity.getData().getRecords().size()==0){
                                llDefault.setVisibility(View.VISIBLE);
                            }else{
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
                }catch (Exception e){

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
            current=1;
            getData();
        }
    }


}
