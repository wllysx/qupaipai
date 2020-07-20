package com.qupp.client.utils;

import android.text.InputFilter;
import android.widget.EditText;

/**
 * 日期处理工具类
 * author：wangqi on 2017/4/26 17:48
 * email：773630555@qq.com
 */
public class EdittextUtil {

    public static void setEdittextCount(EditText editText,int maxCount){
        editText.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            // 获取字符个数(一个中文算2个字符)
            if (getTextLength(dest.toString()) + getTextLength(source.toString()) > maxCount) {
                return "";
            }
            return null;
        }});
    }

    /**
     * 获取字符数量 汉字占2个，英文占一个
     *
     * @param text
     * @return
     */
    public static double getTextLength(String text) {
        double length = 0;
        for (int i = 0; i < text.length(); i++) {
            // text.charAt(i)获取当前字符是的chart值跟具ASCII对应关系255以前的都是英文或者符号之等而中文并不在这里面所以此方法可行</span>
            if (text.charAt(i) > 255) {
                length += 2;
            } else {
                length++;
            }
        }
        return length;
    }


}