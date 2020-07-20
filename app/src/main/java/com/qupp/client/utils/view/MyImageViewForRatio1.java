package com.qupp.client.utils.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class MyImageViewForRatio1 extends ImageView {

	  public MyImageViewForRatio1(Context context) {
	        super(context);
	    }

	    public MyImageViewForRatio1(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public MyImageViewForRatio1(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
	    int width = Math.round(height);
	    setMeasuredDimension(width,height);
	}
	
	

}
