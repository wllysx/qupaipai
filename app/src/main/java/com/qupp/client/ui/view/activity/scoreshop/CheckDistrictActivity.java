package com.qupp.client.ui.view.activity.scoreshop;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.fragment.son.CheckDistrictFragment;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CommonTabPagerAdapter;
import com.qupp.client.utils.view.magicIndicator.MyPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 寄拍区 兑换区
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class CheckDistrictActivity extends BaseActivity implements CommonTabPagerAdapter.TabPagerListener {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    CommonTabPagerAdapter adapter;
    String type = "";

    ArrayList<EntityForSimple> typelist;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_mycheck);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        type = getIntent().getStringExtra("type");
        position = getIntent().getIntExtra("position",0);
        initView();
    }

    private void initView() {
        if (type.equals("2")) {
            tvTitle.setText("商城");
        } else if(type.equals("1")) {
            tvTitle.setText("寄拍区");
        }else{
            tvTitle.setText("特卖");
        }

        initCategory();
    }


    /**
     * @param activity
     * @param type     2 换购区 1寄拍区
     */
    public static void startActivityInstance(Activity activity, String type,int position) {
        activity.startActivity(new Intent(activity, CheckDistrictActivity.class)
                .putExtra("type", type)
                .putExtra("position",position)
        );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public Fragment getFragment(int position) {
        String categoryid = "";
        if (position != 0) {
            categoryid = typelist.get(position - 1).getCategoryId();
        }
        return CheckDistrictFragment.newInstance(type, categoryid);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        finish();
    }

    /**
     * 分类
     */
    private void initCategory() {
        String ct = "2";
        if (type.equals("2")) {
            ct = "3";
        } else if(type.equals("1")){
            ct = "2";
        } else{
            ct = "1";
        }
        ApiUtil.getApiService().categorylist("0", ct, "1").enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    typelist = entity.getData();
                    if (entity.getCode().equals("0")) {
                        ArrayList<String> aslist = new ArrayList<>();
                        aslist.add("推荐");
                        for (EntityForSimple entityForSimple : typelist) {
                            aslist.add(entityForSimple.getName());
                        }
                        adapter = new CommonTabPagerAdapter(getSupportFragmentManager(), typelist.size() + 1, aslist, CheckDistrictActivity.this);
                        adapter.setListener(CheckDistrictActivity.this);
                        viewpager.setAdapter(adapter);

                        initMagicIndicator(aslist);
                        if (position != 0&&aslist.size()>position) {
                            viewpager.setCurrentItem(position,false);
                        }
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

    private void initMagicIndicator(List<String> titles) {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                MyPagerTitleView simplePagerTitleView = new MyPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.parseColor("#777777"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#2b2b2b"));
                simplePagerTitleView.setText(titles.get(index));
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setTextSize(15);
                simplePagerTitleView.setOnClickListener(v -> viewpager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(getResources().getColor(R.color.iscur));
                linePagerIndicator.setLineHeight(ScreenAdaptive.dp2px(CheckDistrictActivity.this, 2));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpager);
    }
}
