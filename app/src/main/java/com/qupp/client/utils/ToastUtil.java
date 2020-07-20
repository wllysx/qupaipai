package com.qupp.client.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {
	private static Toast toast;

	/**
	 * Toast工具类
	 * @param context 上下文
	 * @param content 内容
	 * @param time 时间 0:短时间 1:长时间
	 */
	public static void showToast(Context context, String content,int time) {
		if (toast == null) {
			toast = Toast.makeText(context, content, time);
		} else {
			toast.setText(content);
		}
		toast.show();
	}
}
