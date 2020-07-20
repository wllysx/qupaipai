package com.qupp.client.ui.view.fragment.son;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.main.CommodityList;
import com.qupp.client.ui.view.activity.main.H5Web;
import com.qupp.client.ui.view.activity.main.NewCommodityDetailsActivity;
import com.qupp.client.ui.view.activity.mine.order.OrderDetails;
import com.qupp.client.ui.view.activity.scoreshop.CommodityDetailsActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.MessageAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.event.RefreshMessage;
import com.qupp.client.utils.view.springview.DefaultFooter;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

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
 * 消息
 */

public class MessageFragment extends Fragment {
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    MessageAdapter mAdapter;
    List<EntityForSimple> datas = new ArrayList<>();
    Unbinder unbinder;
    int position = 0;
    String type = "";
    int current = 1, size = 30;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    String pushId="";

    public static MessageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        MessageFragment checkOrderFragment = new MessageFragment();
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
                pushId = "";
                initData();
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();
                if(datas.size()>0){
                    pushId =  datas.get(datas.size()-1).getPushId();
                }
                current++;
                initData();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_msg, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        tvDefault.setText("亲，您还暂无消息");
        ivDefault.setImageResource(R.mipmap.icon_default4);
        initSpringView();
        position = getArguments().getInt("position");
        type = position == 0 ? "1" : "2";
        initAdapter(datas);
        initData();
        return rootView;
    }


    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mAdapter = new MessageAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(), 10);
        // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(data.get(position).getMsgType().equals("1")){
                if(data.get(position).getType()==1) {
                    //h5
                    H5Web.startActivityInstance(getActivity(), data.get(position).getH5Id(),data.get(position).getLinkId());
                }else if(data.get(position).getType()==2){
                    //拍卖商品
                    CommodityList.startActivityInstance1(getActivity(),data.get(position).getTitle(), data.get(position).getLinkId());
                }else if(data.get(position).getType()==3){
                    //积分商城详情
                    CommodityDetailsActivity.startActivityInstance1(getActivity(), data.get(position).getLinkId());
                }else {
                    startShakeByViewAnim(view, 0.9f, 1.1f, 0.5f, 300);
                }
            }else{
                if(data.get(position).getType()==11) {
                    OrderDetails.startActivityInstance(getActivity(), data.get(position).getLinkId(), "0");
                }else if(data.get(position).getType()==12){
                    NewCommodityDetailsActivity.startActivityInstance(getActivity(),"",data.get(position).getLinkId(),"","");
                }else if(data.get(position).getType()==1){
                    H5Web.startActivityInstance(getActivity(), data.get(position).getH5Id(),data.get(position).getLinkId());
                }
            }
        });
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    private void startShakeByViewAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //由小变大
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(10);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);

        view.startAnimation(smallAnimationSet);
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
        ApiUtil.getApiService().pushMessagelist(MyApplication.getToken(),size + "",type,pushId).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (current == 1) {
                            datas.clear();
                            if(entity.getData().size()==0){
                                llDefault.setVisibility(View.VISIBLE);
                            }else{
                                llDefault.setVisibility(View.GONE);
                            }
                        }
                        datas.addAll(entity.getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                        llDefault.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toLogin(RefreshMessage event) {
        current = 1;
        initData();
    }

}
