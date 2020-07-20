package com.qupp.client.ui.view.fragment.son;

import android.app.Service;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcssloop.widget.RCImageView;
import com.google.gson.Gson;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.mine.balance.TopUpActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.AddPriceAdapter;
import com.qupp.client.utils.adapter.MyitemDecoration;
import com.qupp.client.utils.dialog.MiddleDialog;
import com.qupp.client.utils.event.CloseDrawlayoutEvent;
import com.qupp.client.utils.event.DetailStatesEvent;
import com.qupp.client.utils.event.SendPrice;
import com.qupp.client.utils.view.springview.PaiHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.rabtman.wsmanager.WsManager;
import com.rabtman.wsmanager.listener.WsStatusListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddPriceFragment extends Fragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    AddPriceAdapter mAdapter;
    ArrayList<EntityForSimple> datas = new ArrayList<>();
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_layout)
    View flLayout;
    @BindView(R.id.ll_blow_state1)
    LinearLayout llBlowState1;
    @BindView(R.id.tv_topprice)
    TextView tvTopprice;
    @BindView(R.id.tv_nextprice)
    TextView tvNextprice;
    @BindView(R.id.tv_nextCashDeposit)
    TextView tvNextCashDeposit;
    @BindView(R.id.springview)
    SpringView springview;
    @BindView(R.id.iv_photo)
    RCImageView ivPhoto;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_onlinecount)
    TextView tvOnlinecount;
    @BindView(R.id.iv_lingxian)
    ImageView ivLingxian;
    @BindView(R.id.tv_nowpricetitle)
    TextView tvNowpricetitle;
    @BindView(R.id.rl_chujia)
    RelativeLayout rlChujia;
    @BindView(R.id.iv_hg)
    ImageView ivHg;
    boolean isNewPeopel;

    private OkHttpClient okHttpClient;
    private WsManager wsManager;

    int a = 0;
    double nextCashDeposit = 0;
    String topPrice = "", markupPrice = "";
    String type = "", from = "", auctionId = "", starttime = "", goodsname = "", number = "",displayArea="";
    String logourl = "", viewCount = "";
    int current = 1, size = 20;

    boolean canChuJia = true;
    private Vibrator mVibrator;

    Toast toast;


    /**
     * @param from 0vip 1主页
     * @param type
     * @return
     */
    public static AddPriceFragment newInstance(String from, String type, String auctionId, String topPrice, String markupPrice, double nextCashDeposit, String goodsname, String starttime, String logourl, String number, String viewCount,String displayArea) {
        AddPriceFragment checkOrderFragment = new AddPriceFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("from", from);
        args.putString("auctionId", auctionId);
        args.putString("topPrice", topPrice);
        args.putString("markupPrice", markupPrice);
        args.putDouble("nextCashDeposit", nextCashDeposit);
        args.putString("goodsname", goodsname);
        args.putString("starttime", starttime);
        args.putString("logourl", logourl);
        args.putString("number", number);
        args.putString("viewCount", viewCount);
        args.putString("displayArea",displayArea);
        checkOrderFragment.setArguments(args);
        return checkOrderFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_addprice, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        int stateBarHeight = MyApplication.getStateBar(getActivity());
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        type = getArguments().getString("type");
        from = getArguments().getString("from");
        auctionId = getArguments().getString("auctionId");
        topPrice = getArguments().getString("topPrice");
        markupPrice = getArguments().getString("markupPrice");
        nextCashDeposit = getArguments().getDouble("nextCashDeposit");
        starttime = getArguments().getString("starttime");
        goodsname = getArguments().getString("goodsname");
        number = getArguments().getString("number");
        viewCount = getArguments().getString("viewCount");
        displayArea = getArguments().getString("displayArea");


        if(!MyApplication.isNewPeople&&displayArea.equals("9")){
            //不能点击
            rlChujia.setBackgroundColor(Color.parseColor("#AEAEAE"));
            isNewPeopel = true;
        }

        canChuJia = true;
        mVibrator = (Vibrator) getContext().getSystemService(Service.VIBRATOR_SERVICE);
        llBlowState1.setOnClickListener(null);
        initView();
        return rootView;
    }

    private void initView() {
        flLayout.setOnClickListener(null);
        if (from.equals("0")) {
            //无分享
            //tvTitle.setText(number);
            tvTitle.setText("拍卖现场");
            tvOnlinecount.setText("在线人数:" + viewCount);
        } else {
            tvTitle.setText("拍卖现场");
            tvOnlinecount.setText("趣拍拍：￥" + markupPrice);
        }
        initStates();

        setNextPirce(topPrice);
        tvNextCashDeposit.setText("（保证金：￥" + MyApplication.fourAndFive(nextCashDeposit + "") + "）");

        initAdapter(datas);
        //结束才加载列表
        getListData(false);
        initSpringView();
    }


    private void initSpringView() {
        //结束时才可下拉
        if (!type.equals("2") && !type.equals("3")) {
            springview.setHeader(new PaiHeader(getActivity()));
        }
        // springview.setFooter(new DefaultFooter(getActivity()));
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springview.onFinishFreshAndLoad();
                current++;
                getListData(true);
            }

            @Override
            public void onLoadmore() {
                springview.onFinishFreshAndLoad();

            }
        });
    }

    /**
     * 设置RecyclerView属性
     */
    private void initAdapter(List<EntityForSimple> data) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mAdapter = new AddPriceAdapter(data);
        mAdapter.bindToRecyclerView(mRecyclerView);
        int spanCount = 1; // 2 columns
        int spacing = ScreenAdaptive.dp2px(getContext(), 15); // 50px
        boolean includeEdge = false;
        mRecyclerView.addItemDecoration(new MyitemDecoration(spanCount, spacing, includeEdge));
        MyApplication.setMaxFlingVelocity(mRecyclerView);

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }


    @OnClick({R.id.back, R.id.rl_chujia})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                EventBus.getDefault().post(new CloseDrawlayoutEvent());
                break;
            case R.id.rl_chujia:
                if(isNewPeopel){
                    Toast.makeText(getActivity(),"你已不是新用户，不能出价该商品！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (MyApplication.getToken().equals("")) {
                    MyApplication.toLogin(getActivity());
                } else {
                    initData();
                }

                break;
        }
    }


    /**
     * 出价接口
     */
    private void initData() {

        if (!canChuJia) {
            return;
        }
        canChuJia = false;
        if (auctionId == null || auctionId.equals("")) {
            return;
        }
        ApiUtil.getApiService().auctionRecordDetailadd(MyApplication.getToken(), auctionId, topPrice).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                canChuJia = true;
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0") || entity.getCode().equals("1")) {
                        showToast(entity.getMsg());
                    } else {
                        if (entity.getCode().equals("2") && entity.getMsg().equals("1")) {
                            new MiddleDialog(getActivity()).setSureListener(() -> {
                                TopUpActivity.startActivityInstance(getActivity());
                            }).show("提示", "您的余额不足，请去充值！", "取消", "确定", false);
                        }

                    }
                    if (entity.getCode().equals("0")) {
                        try {
                            mVibrator.vibrate(new long[]{100, 300}, -1);
                        } catch (Exception E) {

                        }
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
                canChuJia = true;
            }
        });
    }

    /**
     * 获取出价历史列表
     */
    private void getListData(boolean isRefresh) {
        ApiUtil.getApiService().listByAuctionIdWx(current + "", size + "", auctionId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        ArrayList<EntityForSimple> mlist = entity.getData().getRecords();
                        Collections.reverse(mlist);
                        if (mlist != null) {
                            for (EntityForSimple entityForSimple : mlist) {
                                if (entityForSimple.getUserId().equals(MyApplication.getUserId())) {
                                    entityForSimple.setType(EntityForSimple.TYPE_SELF);
                                } else {
                                    entityForSimple.setType(EntityForSimple.TYPE_OTHER);
                                }
                            }
                            if (current == 0) {
                                datas.clear();
                            }

                            if (!type.equals("2") && !type.equals("3")) {
                                //已结束
                                if (mlist.size() < 20) {
                                    EntityForSimple topEntity = new EntityForSimple();
                                    topEntity.setType(EntityForSimple.TYPE_TOP);
                                    topEntity.setTime(starttime);
                                    topEntity.setGoodsName(goodsname);
                                    //加载第一调数据
                                    mlist.add(0, topEntity);
                                    springview.setEnableHeader(false);
                                }
                            } else {
                                EntityForSimple topEntity = new EntityForSimple();
                                topEntity.setType(EntityForSimple.TYPE_TOP);
                                topEntity.setTime(starttime);
                                topEntity.setGoodsName(goodsname + "开始拍卖");
                                //加载第一调数据
                                mlist.add(0, topEntity);
                            }
                            datas.addAll(0, mlist);
                            // mRecyclerView.scrollToPosition(mlist.size());
                            mAdapter.notifyDataSetChanged();
                            try {
                                if (!isRefresh) {
                                    mRecyclerView.smoothScrollToPosition(datas.size() - 1);
                                }
                            } catch (Exception e) {

                            }

                            if (datas != null && datas.size() > 0) {
                                Glide.with(getActivity()).load(datas.get(datas.size() - 1).getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_head_default).error(R.mipmap.icon_head_default)).into(ivPhoto);
                                tvNickname.setText(datas.get(datas.size() - 1).getUserName());
                            }
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

    private void setNextPirce(String topPrice) {
        tvNextprice.setText(("￥" + MyApplication.rvZeroAndDot(MyApplication.fourAndFive((Double.valueOf(markupPrice) + Double.valueOf(topPrice)) + ""))));
    }

    private void initStates() {
        if (type.equals("2")) {
            //未开始
            tvTopprice.setText("￥" + topPrice);
            llBlowState1.setVisibility(View.GONE);
            tvNowpricetitle.setVisibility(View.VISIBLE);
            initWebSocket();
            //倒计时
        } else if (type.equals("3")) {
            //竞拍中
            tvTopprice.setText("￥" + topPrice);
            initWebSocket();
            llBlowState1.setVisibility(View.VISIBLE);
            ivLingxian.setImageResource(R.mipmap.pc_lx);
            tvNowpricetitle.setVisibility(View.VISIBLE);
        } else {
            //已结束
            tvTopprice.setText("￥" + topPrice);
            llBlowState1.setVisibility(View.GONE);
            //结束时的UI
            tvOnlinecount.setText("拍卖已结束，最终成交价");
            tvNowpricetitle.setVisibility(View.GONE);
            ivLingxian.setImageResource(R.mipmap.icon_jpsuccess);
            closeWebCurrSocket();
        }
        Log.d("states", type + "add");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void states(DetailStatesEvent event) {
        if (event != null) {
            type = event.getStates();
            Log.d("states", type + "event");
            initStates();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void sendprice(SendPrice event) {
        initData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        closeWebCurrSocket();
    }

    /**
     * 链接websocket
     */
    private void initWebSocket() {
        try {
            if (null == okHttpClient) {
                okHttpClient = new OkHttpClient().newBuilder()
                        .retryOnConnectionFailure(true)//允许失败重试
                        .connectTimeout(8, TimeUnit.SECONDS)
                        .readTimeout(8, TimeUnit.SECONDS)
                        .writeTimeout(8, TimeUnit.SECONDS)
                        .build();
            }
            if (null == wsManager) {
                wsManager = new WsManager.Builder(getActivity())
                        .wsUrl(MyApplication.baseSocketUrl)
                        .needReconnect(true)
                        .client(okHttpClient)
                        .build();
                wsManager.setWsStatusListener(new WsStatusListener() {
                    @Override
                    public void onOpen(okhttp3.Response response) {
                        super.onOpen(response);
                        Log.d("socketresult", "open");
                        if (from.equals("0")) {
                            //vip
                            Log.d("socketresult", "send:" + "auctionCount_" + auctionId);
                            try {
                                wsManager.sendMessage("auctionCount_" + auctionId);
                                wsManager.sendMessage(auctionId);
                            } catch (Exception e) {

                            }

                        } else {
                            Log.d("socketresult", "send:" + auctionId);
                            try {
                                wsManager.sendMessage(auctionId);

                            } catch (Exception e) {

                            }
                        }
                    }

                    @Override
                    public void onMessage(String text) {
                        super.onMessage(text);
                        Log.d("socketresult", text + "111");
                        //接收socket的数据加载到列表
                        if (text != null && !text.equals("OK")) {
                            try {
                                EntityForSimple socketEvent = new Gson().fromJson(text, EntityForSimple.class);
                                socketRefresh(socketEvent);
                            } catch (Exception e) {

                            }
                        }

                    }

                    @Override
                    public void onMessage(ByteString bytes) {
                        super.onMessage(bytes);
                        Log.d("socketresult", "bytes");
                    }

                    @Override
                    public void onReconnect() {
                        super.onReconnect();
                    }

                    @Override
                    public void onClosing(int code, String reason) {
                        super.onClosing(code, reason);
                        Log.d("socketresult", "closeing");
                    }

                    @Override
                    public void onClosed(int code, String reason) {
                        super.onClosed(code, reason);
                        Log.d("socketresult", "closeed");
                    }

                    @Override
                    public void onFailure(Throwable t, okhttp3.Response response) {
                        super.onFailure(t, response);
                        try {
                            if (null != t) {
                                if (t instanceof SocketTimeoutException) {//连接超时
                                    Log.i("socketresult", t.getMessage());
                                } else if (t instanceof UnknownHostException) {//服务器主机未找到
                                    Log.i("socketresult", t.getMessage());
                                } else {//其他错误
                                    t.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });
                wsManager.startConnect();
            } else {
                if (null != wsManager) {
                    if (!wsManager.isWsConnected()) {
                        wsManager.startConnect();
                    }
                    if (from.equals("0")) {
                        //vip
                        Log.d("socketresult", "send:" + "auctionCount_" + auctionId);
                        try {
                            wsManager.sendMessage("auctionCount_" + auctionId);
                            wsManager.sendMessage(auctionId);
                        } catch (Exception e) {

                        }

                    } else {
                        Log.d("socketresult", "send:" + auctionId);
                        try {
                            wsManager.sendMessage(auctionId);
                        } catch (Exception e) {

                        }


                    }

                }
            }
        } catch (Exception e) {

        }

    }

    public void closeWebCurrSocket() {
        if (null != wsManager) {
            Log.v("wsManager", "BourseBiFrag + stopConnect");
            try {
                if (wsManager.isWsConnected()) {
                    wsManager.stopConnect();
                }
                wsManager = null;
            } catch (Exception e) {

            }
        }
    }

    private void socketRefresh(EntityForSimple socketEvent) {
        //在线人数
        //tvOnlinecount

        if (auctionId.equals(socketEvent.getAuctionId()) || ("auctionCount_" + auctionId).equals(socketEvent.getAuctionId())) {


            if (from.equals("0")) {
                //vip
                if (socketEvent.getCount() != null) {
                    tvOnlinecount.setText("在线人数:" + socketEvent.getCount());
                }
            }
            if (auctionId.equals(socketEvent.getAuctionId())) {
                if (socketEvent.getUserId().equals(MyApplication.getUserId())) {
                    socketEvent.setType(EntityForSimple.TYPE_SELF);
                } else {
                    socketEvent.setType(EntityForSimple.TYPE_OTHER);
                }
                topPrice = socketEvent.getPayPrice();
                datas.add(socketEvent);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(datas.size() - 1);

                Glide.with(getActivity()).load(socketEvent.getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_head_default).error(R.mipmap.icon_head_default)).into(ivPhoto);
                tvNickname.setText(socketEvent.getUserName());
                tvNextCashDeposit.setText("（保证金：￥" + MyApplication.fourAndFive(socketEvent.getNextCashDeposit() + "") + "）");
                tvTopprice.setText("￥" + socketEvent.getPayPrice());
                tvNextprice.setText(("￥" + MyApplication.rvZeroAndDot(MyApplication.fourAndFive((Double.valueOf(markupPrice) + Double.valueOf(socketEvent.getPayPrice()) + "")))));
                EventBus.getDefault().post(socketEvent);
                if (socketEvent.isCustomMarkUp()){
                    ivHg.setVisibility(View.VISIBLE);
                }else{
                    ivHg.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    public void showToast(String msg) {
        try {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {

        }
    }

}
