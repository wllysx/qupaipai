package com.qupp.client.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * 获取和计算屏幕尺寸类
 * @author wangqi 
 * 
 *
 */
public class ScreenAdaptive {

	public Activity context;
	
	public ScreenAdaptive(Activity context) {
		this.context = context;
	}
	/**
	 * 获得按照比例运算后的宽度
	 * @param scale 比例（比如屏幕宽度的百分之十 ，参数为10）;
	 * @return 按比例运算后的宽度；
	 */
	public int getWidthSizeByAllSize(int scale){
		Display display = context.getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		int mScreen= outMetrics.widthPixels /scale;	
		return mScreen;
		
	}
	/**
	 * 获得按照比例运算后的宽度
	 * @return 按比例运算后的宽度；
	 */
	public int getWidthSizeBySize(int scale, int totalScale){
		return totalScale * scale;
	}
	/**
	 * 获得按照比例运算后的高度
	 * @param scale 比例（比如屏幕高度的百分之十 ，参数为10）;
	 * @return 按比例运算后的高度；
	 */
	public int getHeightSizeByAllSize(int scale){
		Display display = context.getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		int mScreen= outMetrics.heightPixels /scale;	
		return mScreen;
	}
	
	/**
	 * 改变控件的宽或高
	 * @param view 要改变size的view
	 * @param viewWidthOrHeight 要改变的宽或高
	 * @param isWidth 要改变的是宽还是高 true为宽
	 */
	public void setSize2View(View view, int viewWidthOrHeight, boolean isWidth){
		LayoutParams lp = view.getLayoutParams();
		if(isWidth){
			lp.width = viewWidthOrHeight;
		}else{
			lp.height = viewWidthOrHeight;
		}
		
		view.setLayoutParams(lp);
	}
	
	/**
	 * 改变控件的宽和高
	 * @param view 要改变size的view
	 * @param viewWidth 宽
	 * @param viewHeight 高
	 */
	public void setSize2View(View view, int viewWidth, int viewHeight){
		LayoutParams lp = view.getLayoutParams();
		lp.width = viewWidth;
		lp.height = viewHeight;
		view.setLayoutParams(lp);
	}
	/**
	 * 得到屏幕尺寸
	 * @return 得到总宽度；
	 */
	public static int getWidthSizeByAllSize(Activity context){
		
		Display display = context.getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		int mScreen= outMetrics.widthPixels;	
		return mScreen;
		
	}
	/**
	  * 得到屏幕尺寸
	 * @return 得到总高度；
	 */
	public int getHeightSizeByAllSize(){
		Display display = context.getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		int mScreen= outMetrics.heightPixels;
		mScreen = mScreen-getZtHeight();
		return mScreen;
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dp2px(Context context, float dpValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
    }
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public int px2dp(float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  

    public int getZtHeight(){
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
}
