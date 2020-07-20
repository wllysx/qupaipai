package com.qupp.client.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qupp.client.R;

public class ToastUtils {


    public static void showToast(String toast,Context mContext) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 取消收藏
     */
    public static void showCancelShoucang(Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.toast_view, null);
        Toast toast = null;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 添加收藏
     */
    public static void showAddShoucang(Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.toast_view_add, null);
        Toast toast = null;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 添加收藏
     */
    public static void showAddTixing(Context mContext,String title) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.toast_view_tixing, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        Toast toast = null;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
