package com.qupp.client.utils;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class DoubleClick {
    private static long lastClickTime;
    private static long lastClickTime1;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 700) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     *  是否是快速点击
     * @param limiTime 设定的时间阀值
     * @return true 快速点击;false 正常的点击
     */
    public static boolean isFastDoubleClick(long limiTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < limiTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick1() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime1;
        if ( 0 < timeD && timeD < 2000) {
            return true;
        }
        lastClickTime1 = time;
        return false;
    }

}
