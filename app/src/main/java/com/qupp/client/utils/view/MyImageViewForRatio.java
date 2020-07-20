package com.qupp.client.utils.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class MyImageViewForRatio extends ImageView {

	  public MyImageViewForRatio(Context context) {
	        super(context);
	    }

	    public MyImageViewForRatio(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public MyImageViewForRatio(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = Math.round(width);
	    setMeasuredDimension(width,height);
	}
	
	

}
