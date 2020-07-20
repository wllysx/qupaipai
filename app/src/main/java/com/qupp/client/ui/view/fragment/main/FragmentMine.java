package com.qupp.client.ui.view.fragment.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.MainActivity;
import com.qupp.client.ui.view.activity.customerservice.MoorWebCenter;
import com.qupp.client.ui.view.activity.main.MessageActivity;
import com.qupp.client.ui.view.activity.mine.KnowActivity;
import com.qupp.client.ui.view.activity.mine.MyAddressActivity;
import com.qupp.client.ui.view.activity.mine.SettingActivity;
import com.qupp.client.ui.view.activity.mine.ShouCangActivity;
import com.qupp.client.ui.view.activity.mine.balance.BalanceActivity;
import com.qupp.client.ui.view.activity.mine.coupon.MyCoupon;
import com.qupp.client.ui.view.activity.mine.friend.MyFriendActivity;
import com.qupp.client.ui.view.activity.mine.history.CommodityHistoryActivity;
import com.qupp.client.ui.view.activity.mine.integral.IntegralActivity;
import com.qupp.client.ui.view.activity.mine.myaction.MyActionActivity;
import com.qupp.client.ui.view.activity.mine.order.OrderListActivity;
import com.qupp.client.ui.view.activity.mine.setting.MyDataActivity;
import com.qupp.client.ui.view.activity.mine.setting.ShardMineActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleB;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.dialog.SignInDialog;
import com.qupp.client.utils.dialog.SignInGzDialog;
import com.qupp.client.utils.view.ResizableImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 我的
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 13:51
 */


@SuppressLint("all")
public class FragmentMine extends Fragment {

    View rootView;
    @BindView(R.id.ll_linear)
    View llLinear;
    Unbinder unbinder;

    public static String url = "https://www.baidu.com/";
    public static String text = "友盟微社区sdk，多终端一社区，为您的app添加社区就是这么简单";
    public static String title = "友盟微社区";
    public static String imageurl = "http://dev.umeng.com/images/tab2_1.png";
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_invitationCode)
    TextView tvInvitationCode;
    @BindView(R.id.tv_integralAmount)
    TextView tvIntegralAmount;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_fanNums)
    TextView tvFanNums;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.ll_order1)
    LinearLayout llOrder1;
    @BindView(R.id.ll_order2)
    LinearLayout llOrder2;
    @BindView(R.id.ll_order3)
    LinearLayout llOrder3;
    @BindView(R.id.ll_order4)
    LinearLayout llOrder4;
    @BindView(R.id.ll_more2)
    LinearLayout llMore2;
    @BindView(R.id.ll_more3)
    LinearLayout llMore3;
    @BindView(R.id.ll_more4)
    LinearLayout llMore4;
    @BindView(R.id.ll_more5)
    LinearLayout llMore5;
    EntityForSimple entityForSimple;
    @BindView(R.id.tv_point1)
    TextView tvPoint1;
    @BindView(R.id.tv_point2)
    TextView tvPoint2;
    @BindView(R.id.tv_point3)
    TextView tvPoint3;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.iv_qiandao)
    ResizableImageView ivQiandao;
    boolean isQiandao = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        llLinear.setPadding(0, MyApplication.getStateBar(getActivity()), 0, 0);
        ((MainActivity) getActivity()).setStateColor(true);

        //LoginActivity.startActivityInstance(getActivity());
        //startActivity(new Intent(getActivity(),TestImActivity.class));
        initView();
        return rootView;
    }

    private void initView() {
        Log.d("MyApplication", MyApplication.getUserId());
        entityForSimple = Paper.book("jyk").read("userdata", new EntityForSimple());
        Glide.with(getActivity()).load(entityForSimple.getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into(ivPhoto);
        tvNickname.setText(entityForSimple.getNickname());
        tvInvitationCode.setText("我的邀请码：" + entityForSimple.getInvitationCode());
        tvAmount.setText(entityForSimple.getAmount());
        tvFanNums.setText(entityForSimple.getFanNums());
        tvIntegralAmount.setText(entityForSimple.getIntegralAmount());
    }

    public static FragmentMine newInstance() {
        FragmentMine fragment = new FragmentMine();
        return fragment;
    }


    public void refreshBar() {
        try {
            ((MainActivity) getActivity()).setStateColor(true);
            orderstatistics();
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @OnClick({R.id.iv_set, R.id.ll_allorder, R.id.ll_order1, R.id.ll_order2, R.id.ll_order3, R.id.ll_order4, R.id.ll_more2, R.id.ll_more3, R.id.ll_more4, R.id.ll_more5, R.id.ll_more6, R.id.iv_photo, R.id.iv_message,R.id.ll_more7,R.id.ll_more8,R.id.ll_more9})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_photo:
                MyDataActivity.startActivityInstance(getActivity());
                break;
            case R.id.iv_set:
                SettingActivity.startActivityInstance(getActivity());
                break;
            case R.id.ll_order1:
                OrderListActivity.startActivityInstance(getActivity(), 1);
                break;
            case R.id.ll_order2:
                OrderListActivity.startActivityInstance(getActivity(), 2);
                break;
            case R.id.ll_order3:
                OrderListActivity.startActivityInstance(getActivity(), 3);
                break;
            case R.id.ll_order4:
                OrderListActivity.startActivityInstance(getActivity(), 4);
                break;
            case R.id.ll_allorder:
                OrderListActivity.startActivityInstance(getActivity(), 0);
                break;
            case R.id.ll_more2:
                String nick = "{\"nickName\":\"" + entityForSimple.getNickname() + "(" + entityForSimple.getPhone() + ")" + "\"}";
                String nickname = "{'nickName':'访客昵称'}";
                // String nickname = "{"+"\""+"nickName"+"\""+":"+"111111"+"}";
                // startActivity(new Intent(getActivity(), MoorWebCenter.class).putExtra("OpenUrl", "https://ykf-webchat.7moor.com/wapchat.html?accessId=612e30a0-d2c8-11e9-90b1-57e72220947b&fromUrl=&urlTitle=nickname&clientId=8888&otherParams=" + nick));
                startActivity(new Intent(getActivity(), MoorWebCenter.class).putExtra("OpenUrl", "https://ykf-webchat.7moor.com/wapchat.html?accessId=612e30a0-d2c8-11e9-90b1-57e72220947b&fromUrl=&urlTitle=nickname&clientId=" + MyApplication.getUserId() + "&otherParams=" + nick));
                Log.d("h5url", "https://ykf-webchat.7moor.com/wapchat.html?accessId=612e30a0-d2c8-11e9-90b1-57e72220947b&fromUrl=&urlTitle=&clientId=" + MyApplication.getUserId() + "&otherParams=" + nick);
              /*  final KfStartHelper helper = new KfStartHelper(getActivity());
                helper.initSdkChat("ec4ce490-cfa0-11e9-8d90-ff6a9d01acc9", entityForSimple.getNickname()+ "(" + entityForSimple.getPhone() + ")", MyApplication.getUserId());*///腾讯云正式
                break;
            case R.id.ll_more3:
                MyAddressActivity.startActivityInstance(getActivity(), "0");
                //PhysicalActivity.startActivityInstance(getActivity(),"624606152570793988");
                break;
            case R.id.ll_more4:
                ShardMineActivity.startActivityInstance(getActivity());
                break;
            case R.id.ll_more5:
              /*  Toast.makeText(getActivity(), "用户须知", Toast.LENGTH_LONG).show();
                String nick1 = encode("(一辉)15669967117");
                startActivity(new Intent(getActivity(), MoorWebCenter.class).putExtra("OpenUrl", "https://ykf-webchat.7moor.com/wapchat.html?accessId=612e30a0-d2c8-11e9-90b1-57e72220947b&fromUrl=&urlTitle=&clientId=" + nick1));*/
                KnowActivity.startActivityInstance(getActivity());
                break;
            case R.id.ll_more6:
                ShouCangActivity.startActivityInstance(getActivity());
                break;
            case R.id.ll_more7:
                MyActionActivity.startActivityInstance(getActivity());
                break;
            case R.id.iv_message:
                if (MyApplication.getToken().equals("")) {
                    MyApplication.toLogin(getActivity());
                } else {
                    MessageActivity.startActivityInstance(getActivity());
                    ivMessage.setImageResource(R.mipmap.icon_main_message);
                }
                break;
            case R.id.ll_more8:
                CommodityHistoryActivity.startActivityInstance(getActivity());
                break;
            case R.id.ll_more9:
                MyCoupon.startActivityInstance(getActivity());
                //SelectCoupon.startActivityInstance(getActivity(),"");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MyApplication.getToken().equals("")) {
            getBlanceAndIntegralAndFans();
            orderstatistics();
            pushMessageisRead();
            checkSign();
        }
    }


    /**
     * 判断是否签到
     */
    private void checkSign() {
        ApiUtil.getApiService().checkSign(MyApplication.getToken()).enqueue(new Callback<EntityForSimpleB>() {
            @Override
            public void onResponse(Call<EntityForSimpleB> call, Response<EntityForSimpleB> response) {
                EntityForSimpleB entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        isQiandao = entity.isData();
                        if (entity.isData()) {
                            ivQiandao.setImageResource(R.mipmap.wd_yqd);
                        } else {
                            ivQiandao.setImageResource(R.mipmap.wd_wqd);
                        }
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleB> call, Throwable t) {
            }
        });
    }

    /**
     * 判断是否签到
     */
    private void signInAdd() {
        ApiUtil.getApiService().signInAdd(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        new SignInDialog(getActivity()).setSureListener(() -> {
                            checkSign();
                            getBlanceAndIntegralAndFans();
                        }).show(entity.getData());
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


    private void pushMessageisRead() {
        ApiUtil.getApiService().pushMessageisRead(MyApplication.getToken(), MyApplication.getMessageTime()).enqueue(new Callback<EntityForSimpleB>() {
            @Override
            public void onResponse(Call<EntityForSimpleB> call, Response<EntityForSimpleB> response) {
                EntityForSimpleB entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.isData()) {
                            ivMessage.setImageResource(R.mipmap.icon_xiaoxioint);
                        } else {
                            ivMessage.setImageResource(R.mipmap.icon_main_message);
                        }
                    } else {
                        Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleB> call, Throwable t) {
            }
        });
    }

    //编码
    public static String encode(String s) {
        try {
            String encode = URLEncoder.encode(s, "gb2312");
            return encode;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @OnClick({R.id.ll_integral, R.id.ll_balance, R.id.ll_friend})
    public void onTopViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_integral:
                IntegralActivity.startActivityInstance(getActivity(), tvIntegralAmount.getText().toString());
                break;
            case R.id.ll_balance:
                BalanceActivity.startActivityInstance(getActivity());
                break;
            case R.id.ll_friend:
                MyFriendActivity.startActivityInstance(getActivity(), tvFanNums.getText().toString());
                break;
        }
    }


    /**
     * 获取用户信息积分等信息
     */
    private void getBlanceAndIntegralAndFans() {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        Paper.book("jyk").write("userdata", entity.getData());
                        Glide.with(getActivity()).load(entity.getData().getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into(ivPhoto);
                        tvNickname.setText(entity.getData().getNickname());
                        tvInvitationCode.setText("邀请码：" + entity.getData().getInvitationCode());
                        MyApplication.setCode(entity.getData().getInvitationCode());
                        tvAmount.setText(entity.getData().getAmount());
                        tvFanNums.setText(entity.getData().getFanNums());
                        tvIntegralAmount.setText(entity.getData().getTotalIntegralAmount());
                        MyApplication.setPhone(entity.getData().getPhone());
                    } else {
                        //Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


    private void orderstatistics() {
        ApiUtil.getApiService().orderstatistics(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData().getWaitPay().equals("0")) {
                            tvPoint1.setVisibility(View.INVISIBLE);
                        } else {
                            tvPoint1.setText(entity.getData().getWaitPay());
                            tvPoint1.setVisibility(View.VISIBLE);
                        }

                        if (entity.getData().getWaitDeliverGoods().equals("0")) {
                            tvPoint2.setVisibility(View.INVISIBLE);
                        } else {
                            tvPoint2.setText(entity.getData().getWaitDeliverGoods());
                            tvPoint2.setVisibility(View.VISIBLE);
                        }

                        if (entity.getData().getWaitReceivingGoods().equals("0")) {
                            tvPoint3.setVisibility(View.INVISIBLE);
                        } else {
                            tvPoint3.setText(entity.getData().getWaitReceivingGoods());
                            tvPoint3.setVisibility(View.VISIBLE);
                        }

                    } else {
                        //Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


    @OnClick({R.id.v_about, R.id.v_qiandao})
    public void onViewClickedSignin(View view) {
        switch (view.getId()) {
            case R.id.v_about:
                new SignInGzDialog(getActivity()).show();
                break;
            case R.id.v_qiandao:
                signInAdd();
                break;
        }
    }
}
