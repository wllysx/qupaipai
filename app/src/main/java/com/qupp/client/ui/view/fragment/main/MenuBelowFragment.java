package com.qupp.client.ui.view.fragment.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.utils.event.ToShopEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * author: MrWang on 2019/7/15
 * email:773630555@qq.com
 * date: on 2019/7/15 14:56
 */


public class MenuBelowFragment extends Fragment {

    private View rootView, liner1, liner2, liner3, liner4, linervip;
    public Fragment fragment1, fragment2, fragment3, fragment4, fragmentvip,mContentFragment;
    private FragmentManager fragmentManager;
    private TextView item1, item2, item3, item4,itemvip, titem1, titem2, titem3, titem4,titemvip;
    private static MenuBelowFragment menuBelowFragment;
    boolean isToShop = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        menuBelowFragment = this;
        Log.v("Tag", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        EventBus.getDefault().register(this);
        initView();
        fragmentManager = getActivity().getSupportFragmentManager();
        initItemFragment();
        Log.v("Tag", "onCreateView");
        MyApplication.isMine = false;
        return rootView;
    }

    public static MenuBelowFragment newInstance() {
        if (menuBelowFragment == null)
            menuBelowFragment = new MenuBelowFragment();
        return menuBelowFragment;
    }

    private void initItemFragment() {
        // TODO Auto-generated method stub
        fragment1 = FragmentMainNewSpecial.newInstance();
        switchContent(null, fragment1);
    }

    private void initView() {
        // TODO Auto-generated method stub
        liner1 = rootView.findViewById(R.id.liner1);
        liner2 = rootView.findViewById(R.id.liner2);
        liner3 = rootView.findViewById(R.id.liner3);
        liner4 = rootView.findViewById(R.id.liner4);
        linervip = rootView.findViewById(R.id.linervip);
        item1 = (TextView) rootView.findViewById(R.id.item1);
        item2 = (TextView) rootView.findViewById(R.id.item2);
        item3 = (TextView) rootView.findViewById(R.id.item3);
        item4 = (TextView) rootView.findViewById(R.id.item4);
        itemvip = (TextView) rootView.findViewById(R.id.itemvip);
        titem1 = (TextView) rootView.findViewById(R.id.titem1);
        titem2 = (TextView) rootView.findViewById(R.id.titem2);
        titem3 = (TextView) rootView.findViewById(R.id.titem3);
        titem4 = (TextView) rootView.findViewById(R.id.titem4);
        titemvip = (TextView) rootView.findViewById(R.id.titemvip);
        liner1.setOnClickListener(itemListener);
        liner2.setOnClickListener(itemListener);
        liner3.setOnClickListener(itemListener);
        liner4.setOnClickListener(itemListener);
        linervip.setOnClickListener(itemListener);
    }

    public OnClickListener itemListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.liner1:
                    MyApplication.isMine = false;
                    refreshView();
                    if (fragment1 == null) {
                        fragment1 = FragmentMainNewSpecial.newInstance();
                    } else {
                        ((FragmentMainNewSpecial) fragment1).refreshBar();
                    }
                    item1.setBackgroundResource(R.mipmap.btn1_cur);
                    titem1.setTextColor(getResources().getColor(R.color.iscur));
                    switchContent(mContentFragment, fragment1);
                    break;

                case R.id.liner2:
                    MyApplication.isMine = false;
                    refreshView();
                    if (fragment2 == null) {
                        fragment2 = FragmentShop.newInstance();
                    } else {
                        ((FragmentShop) fragment2).refreshBar();
                    }
                    item2.setBackgroundResource(R.mipmap.btn2_cur);
                    titem2.setTextColor(getResources().getColor(R.color.iscur));
                    switchContent(mContentFragment, fragment2);
                    break;
                case R.id.liner3:
                    MyApplication.isMine = false;
                    refreshView();
                    if (fragment3 == null) {
                        fragment3 = FragmentMessage.newInstance();
                    } else {
                        ((FragmentMessage) fragment3).refreshBar();
                    }
                    item3.setBackgroundResource(R.mipmap.btn3_cur);
                    titem3.setTextColor(getResources().getColor(R.color.iscur));
                    switchContent(mContentFragment, fragment3);
                    break;
                case R.id.liner4:
                    if(MyApplication.getToken().equals("")){
                        MyApplication.toLogin(getActivity());
                    }else {
                        MyApplication.isMine = true;
                        refreshView();
                        if (fragment4 == null) {
                            fragment4 = FragmentMine.newInstance();
                        } else {
                            ((FragmentMine) fragment4).refreshBar();
                        }
                        item4.setBackgroundResource(R.mipmap.btn4_cur);
                        titem4.setTextColor(getResources().getColor(R.color.iscur));
                        switchContent(mContentFragment, fragment4);
                    }

                    break;
                case R.id.linervip:
                    MyApplication.isMine = false;
                    refreshView();
                    if (fragmentvip == null) {
                        fragmentvip = FragmentVip.newInstance();
                    } else {
                        ((FragmentVip) fragmentvip).refreshBar();
                    }
                    itemvip.setBackgroundResource(R.mipmap.btnvip_cur);
                    titemvip.setTextColor(getResources().getColor(R.color.iscur));
                    switchContent(mContentFragment, fragmentvip);
                    break;
                default:
                    break;
            }
        }

    };

    private void refreshView() {
        // TODO Auto-generated method stub
        item1.setBackgroundResource(R.mipmap.btn1);
        item2.setBackgroundResource(R.mipmap.btn2);
        item3.setBackgroundResource(R.mipmap.btn3);
        item4.setBackgroundResource(R.mipmap.btn4);
        itemvip.setBackgroundResource(R.mipmap.btnvip);
        titem1.setTextColor(getResources().getColor(R.color.textcolor3));
        titem2.setTextColor(getResources().getColor(R.color.textcolor3));
        titem3.setTextColor(getResources().getColor(R.color.textcolor3));
        titem4.setTextColor(getResources().getColor(R.color.textcolor3));
        titemvip.setTextColor(getResources().getColor(R.color.textcolor3));
    }

    public void switchContent(Fragment from, Fragment to) {
        try {
            if (mContentFragment != to) {
                mContentFragment = to;
                FragmentTransaction transaction = fragmentManager
                        .beginTransaction();
                if (from == null) {
                    transaction.add(R.id.contentfragment, to).commit();
                    return;
                }
                if (!to.isAdded()) {
                    Log.v("Tag", "transaction.hide(from).add(R.id.mainFragments, to).commit()");
                    transaction.hide(from).add(R.id.contentfragment, to).commit();
                } else {
                    Log.v("Tag", "transaction.hide(from).show(to).commit()");
                    transaction.hide(from).show(to).commit();
                }
            }
        }catch (Exception e){


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exit(ToShopEvent event) {
      isToShop = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        if(isToShop) {
            refreshView();
            if (fragment2 == null) {
                fragment2 = FragmentShop.newInstance();
            } else {
                ((FragmentShop) fragment2).refreshBar();
            }
            item2.setBackgroundResource(R.mipmap.btn2_cur);
            titem2.setTextColor(getResources().getColor(R.color.iscur));
            switchContent(mContentFragment, fragment2);

            isToShop = false;
        }
        if(MyApplication.toMain){
            refreshView();
            if (fragment1 == null) {
                fragment1 = FragmentMainNewSpecial.newInstance();
            } else {
                ((FragmentMainNewSpecial) fragment1).refreshBar();
            }
            item1.setBackgroundResource(R.mipmap.btn1_cur);
            titem1.setTextColor(getResources().getColor(R.color.iscur));
            switchContent(mContentFragment, fragment1);
            MyApplication.toMain = false;
        }

        if(MyApplication.isMine&&MyApplication.isTuichu){
            refreshView();
            if (fragment1 == null) {
                fragment1 = FragmentMainNewSpecial.newInstance();
            } else {
                ((FragmentMainNewSpecial) fragment1).refreshBar();
            }
            item1.setBackgroundResource(R.mipmap.btn1_cur);
            titem1.setTextColor(getResources().getColor(R.color.iscur));
            switchContent(mContentFragment, fragment1);
            MyApplication.isMine = false;
        }
        MyApplication.isTuichu = false;
        super.onResume();
    }
}
