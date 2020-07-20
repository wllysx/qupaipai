package com.qupp.client.ui.view.activity.mine.integral;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.main.ShopActivity;
import com.qupp.client.ui.view.fragment.son.IntegralFragment;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.adapter.CommonTabPagerAdapter;
import com.qupp.client.utils.event.RefreshIntegral;
import com.qupp.client.utils.view.magicIndicator.MyPagerTitleView;
import com.qupp.client.utils.view.springview.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IntegralActivity extends BaseActivity implements CommonTabPagerAdapter.TabPagerListener {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    String integral = "";
    @BindView(R.id.springviewtop)
    SpringView springviewtop;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    ArrayList<String> titles = new ArrayList<>();
    CommonTabPagerAdapter adapter;
    @BindView(R.id.tv_totalintegral)
    TextView tvTotalintegral;
    @BindView(R.id.tv_superintegral)
    TextView tvSuperintegral;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(IntegralActivity.this), 0, 0);
        integral = getIntent().getStringExtra("integral");
        setStateColor(true);
        initView();
        initSpringView();
        initData();

    }

    private void initData() {
        ApiUtil.getApiService().myAllIntegrate(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvIntegral.setText(entity.getData().getIntegralAmount());
                        tvTotalintegral.setText(entity.getData().getTotalIntegralAmount());
                        tvSuperintegral.setText(entity.getData().getSuperIntegralAmount());
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

    private void initSpringView() {
        springviewtop.setHeader(new DefaultHeader(this));
        springviewtop.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                initData();
                EventBus.getDefault().post(new RefreshIntegral());
                springviewtop.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                springviewtop.onFinishFreshAndLoad();
            }
        });
        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                springviewtop.setEnable(true);
            } else {
                springviewtop.setEnable(false);
            }
        });


    }


    private void initView() {
        tvTitle.setText("我的积分");
        titles.add("超值积分");
        titles.add("积分");
        adapter = new CommonTabPagerAdapter(getSupportFragmentManager(), 2, titles, this);
        adapter.setListener(this);
        viewpager.setAdapter(adapter);
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        //magicIndicator.setBackgroundColor(Color.BLACK);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                MyPagerTitleView simplePagerTitleView = new MyPagerTitleView(context);
                //magicIndicator.setBackgroundColor(Color.parseColor("#ffffff"));
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
                linePagerIndicator.setLineHeight(ScreenAdaptive.dp2px(IntegralActivity.this, 2));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpager);
    }


    public static void startActivityInstance(Activity activity, String integral) {
        activity.startActivity(new Intent(activity, IntegralActivity.class)
                .putExtra("integral", integral)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.tv_guize, R.id.tv_duihuan, R.id.back})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_guize:
                IntegralGuizeActivity.startActivityInstance(IntegralActivity.this);
                break;
            case R.id.tv_duihuan:
                ShopActivity.startActivityInstance(IntegralActivity.this);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public Fragment getFragment(int position) {
        return IntegralFragment.newInstance(position);
    }
}
