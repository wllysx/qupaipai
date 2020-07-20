package com.qupp.client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static android.content.Context.MODE_PRIVATE;


public class MySharePerference {
	
	public static void addSharePerference(Context context, String name, String value){
		SharedPreferences sp= context.getSharedPreferences("JYK", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(name,value);
		editor.commit();
	}
	public static String getShardPerferience(Context context, String name, String defaultValue){
		SharedPreferences sp= context.getSharedPreferences("JYK", Context.MODE_PRIVATE);
		return sp.getString(name, defaultValue);
	}

	public static void addShareLongPerference(Context context,String name, long value){
		try {
			SharedPreferences sp= context.getSharedPreferences("JYK",MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putLong(name,value);
			editor.apply();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static long getShardPerferienceLong(Context context,String name, long defaultValue){
		SharedPreferences sp;
		try {
			sp= context.getSharedPreferences("JYK",MODE_PRIVATE);
		}catch (Exception e){
			return -1L;
		}
		return sp.getLong(name, defaultValue);
	}
}
