package com.qupp.client.ui.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.qupp.client.R;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.MySharePerference;
import com.qupp.client.utils.adapter.GuidePageAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    @BindView(R.id.guide_vp)
    ViewPager guide_vp;
    Unbinder unbinder;
    @BindView(R.id.tv_jump)
    TextView tvJump;
    private List<View> viewList;//View资源的集合
    private LinearLayout vg;//放置圆点

    //实例化原点View
    private ImageView iv_point;
    private ImageView[] ivPointArray;
    private LayoutInflater layoutInflater;
    private ArrayList<View> views = new ArrayList<>();
    //private int[] imageArray = new int[]{R.mipmap.pic_1, R.mipmap.pic_2, R.mipmap.pic_3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        unbinder = ButterKnife.bind(this);
        setStateColor(true);
        initPremissions();
        initView();
    }

    private void initView() {
        layoutInflater = LayoutInflater.from(this);
        initViewPager();
        initPoint();
    }

    public void initPremissions() {
        //同时请求多个权限
        RxPermissions.getInstance(GuideActivity.this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CAMERA)//多个权限用","隔开
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //当所有权限都允许之后，返回true
                        Log.i("permissions", "btn_more_sametime：" + aBoolean);
                    } else {
                        //只要有一个权限禁止，返回false，
                        Log.i("permissions", "btn_more_sametime：" + aBoolean);
                    }
                });
    }

    /**
     * 加载ViewPager
     */
    private void initViewPager() {
        //实例化图片资源
        viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        View view1 = layoutInflater.inflate(R.layout.viewpager_page1, null);
        View view2 = layoutInflater.inflate(R.layout.viewpager_page2, null);
        View view3 = layoutInflater.inflate(R.layout.viewpager_page3, null);
        view3.findViewById(R.id.tv_submit).setOnClickListener(view -> {
            MySharePerference.addSharePerference(getApplication(), "FIRST2APP", "1");
            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        views.add(view1);
        views.add(view2);
        views.add(view3);
        //循环创建View并加入到集合中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        //View集合初始化好后，设置Adapter
        guide_vp.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        guide_vp.setOnPageChangeListener(this);
        //guide_vp.setPageTransformer(true, new DepthPageTransformer());
    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, GuideActivity.class));
    }

    /**
     * 加载底部圆点
     */
    private void initPoint() {
        //这里实例化LinearLayout
        vg = findViewById(R.id.guide_ll_point);
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = viewList.size();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(18, 0, 18, 0);
        for (int i = 0; i < size; i++) {
            iv_point = new ImageView(this);
            iv_point.setLayoutParams(layoutParams);
            ivPointArray[i] = iv_point;
            //第一个页面需要设置为选中状态，这里采用两张不同的图片
            if (i == 0) {
                iv_point.setBackgroundResource(R.mipmap.bg_guide_select);
            } else {
                iv_point.setBackgroundResource(R.mipmap.bg_guide_unselect);
            }
            //将数组中的ImageView加入到ViewGroup
            vg.addView(ivPointArray[i]);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //循环设置当前页的标记图
        int length = views.size();
        for (int i = 0; i < length; i++) {
                ivPointArray[position].setBackgroundResource(R.mipmap.bg_guide_select);
            if (position != i) {
                ivPointArray[i].setBackgroundResource(R.mipmap.bg_guide_unselect);
            }
        }
        if(position==2){
            tvJump.setVisibility(View.GONE);
        }else{
            tvJump.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Apply KitKat specific translucency.
     */


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_jump)
    public void onViewClicked() {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        MySharePerference.addSharePerference(getApplication(), "FIRST2APP", "1");
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
