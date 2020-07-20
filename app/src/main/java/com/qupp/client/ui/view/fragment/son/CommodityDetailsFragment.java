package com.qupp.client.ui.view.fragment.son;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.MyImageGetter;
import com.qupp.client.utils.event.GoodsDetails;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商品详情
 * author: MrWang on 2019/8/27
 * email:773630555@qq.com
 * date: on 2019/8/27 16:12
 */

public class CommodityDetailsFragment extends Fragment {

    Unbinder unbinder;
    String type = "";
    //@BindView(R.id.ll_title)
    //LinearLayout llTitle;
    @BindView(R.id.tv_longimage)
    TextView tvLongimage;
    String auctionId = "";

    public static CommodityDetailsFragment newInstance(String type,String auctionId) {
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("auctionId", auctionId);
        CommodityDetailsFragment checkOrderFragment = new CommodityDetailsFragment();
        checkOrderFragment.setArguments(args);
        return checkOrderFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_commodity_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        type = getArguments().getString("type");
        auctionId = getArguments().getString("auctionId");
      /*  if (type.equals("2") || type.equals("1")) {
            llTitle.setVisibility(View.VISIBLE);
        } else {
            llTitle.setVisibility(View.GONE);
        }*/
        //initData();
        return rootView;
    }


    @Override
    public void onDestroyView() {
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void initData() {
        ApiUtil.getApiService().auctionRecordInfodetail(auctionId).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {

                    }
                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void sendprice(GoodsDetails event) {
        tvLongimage.setText(Html.fromHtml(event.getGoodsDetail(), new MyImageGetter(getContext(), tvLongimage), null));
        if(event!=null&&event.getShowTitle().equals("1")){
            //显示标题
            //llTitle.setVisibility(View.VISIBLE);
            //Toast.makeText(getContext(),"111",Toast.LENGTH_LONG).show();
        }
    }
}
