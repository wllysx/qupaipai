package com.qupp.client.ui.view.fragment.son;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CheckOrderAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.dialog.CheckMoreDialog;
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
 * author: MrWang on 2019/8/27
 * email:773630555@qq.com
 * date: on 2019/8/27 16:12
 */

public class CheckOrderFragment extends Fragment {
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    CheckOrderAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    Unbinder unbinder;
    int current = 1, size = 30, page = 0;
    String orderTypeQuery = "4";
    @BindView(R.id.ll_default)
    LinearLayout llDefault;

    public static CheckOrderFragment newInstance(int page) {
        Bundle args = new Bundle();
        CheckOrderFragment checkOrderFragment = new CheckOrderFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_checkorder, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        page = getArguments().getInt("page");
        switch (page) {
            case 0:
                orderTypeQuery = "4";
                break;
            case 1:
                orderTypeQuery = "2";
                break;
            case 2:
                orderTypeQuery = "3";
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
        mAdapter = new CheckOrderAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Toast.makeText(getContext(), position + "", Toast.LENGTH_LONG).show();
        });
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(), 10); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        // mAdapter.setOnItemClickListener((adapter, view, position) -> MyCheckDetails.startActivityInstance(getActivity()));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Log.d("urlsss",datas.get(position).getContractUrl());
            if (DoubleClick.isFastDoubleClick()) {
                return;
            }
            if(view.getId()==R.id.tv_mingxi){
                new CheckMoreDialog(getActivity()).show(datas.get(position).getLogo(),datas.get(position).getGoodsName(),datas.get(position).getOrderId());
            }else{
                openBrower(datas.get(position).getContractUrl());
            }
            // startActivity(new Intent(getActivity(), PdfWebCenter.class).putExtra("OpenUrl",datas.get(position).getContractUrl()).putExtra("id",datas.get(position).getId()));
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);
    }


    /**
     * 打开升级页面
     *
     * @param str
     */
    protected void openBrower(String str) {
        if(str!=null&&!str.equals("")) {
           /* Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse(str));
            it = Intent.createChooser(it, null);
            startActivity(it);*/
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(str);
            intent.setData(content_url);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    /**
     * 获取订单列表
     */
    private void getData() {
        ApiUtil.getApiService().mallorderlist(MyApplication.getToken(),null, orderTypeQuery, size + "", current + "").enqueue(new Callback<MessageForSimple>() {
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
                        llDefault.setVisibility(View.VISIBLE);
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

}
