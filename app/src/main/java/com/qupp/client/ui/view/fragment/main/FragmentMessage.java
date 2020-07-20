package com.qupp.client.ui.view.fragment.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.MyImageGetter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 消息
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 13:51
 */


@SuppressLint("all")
public class FragmentMessage extends Fragment {

    View rootView;
    @BindView(R.id.linear)
    View linear;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_longimage)
    TextView tvLongimage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        linear.setPadding(0, MyApplication.getStateBar(getActivity()), 0, 0);
        //黑色字体状态栏
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initView();
        return rootView;
    }

    private void initView() {
        tvTitle.setText(getResources().getString(R.string.main_tab3));
        getData();
    }

    private void getData() {
        ApiUtil.getApiService().ruleByKey("tabRuleKey").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvLongimage.setText(Html.fromHtml(entity.getData().getContent(), new MyImageGetter(getActivity(), tvLongimage), null));
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


    public static FragmentMessage newInstance() {
        FragmentMessage fragment = new FragmentMessage();
        return fragment;
    }


    public void refreshBar() {
        //黑色字体状态栏
        try {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }catch (Exception e){

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

    @Override
    public void onResume() {
        super.onResume();
    }
}
