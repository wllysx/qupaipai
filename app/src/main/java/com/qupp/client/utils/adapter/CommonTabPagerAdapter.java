package com.qupp.client.utils.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class CommonTabPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT;
    private List<String> list;
    private Context context;
    private TabPagerListener listener;

    public interface TabPagerListener{
        Fragment getFragment(int position);
    }

    public void setListener(TabPagerListener listener) {
        this.listener = listener;
    }

    public CommonTabPagerAdapter(FragmentManager fm, int count, List<String> list, Context context) {
        super(fm);
        if (list==null||list.isEmpty()){
            throw new ExceptionInInitializerError("list can't be null or empty");
        }
        if (count<=0){
            throw new ExceptionInInitializerError("count value error");
        }
        this.PAGE_COUNT = count;
        this.list = list;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
       return listener.getFragment(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }



}
