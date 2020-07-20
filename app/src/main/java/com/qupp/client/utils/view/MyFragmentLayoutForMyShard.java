package com.qupp.client.utils.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@SuppressLint("AppCompatCustomView")
public class MyFragmentLayoutForMyShard extends LinearLayout {

	  public MyFragmentLayoutForMyShard(Context context) {
	        super(context);
	    }

	    public MyFragmentLayoutForMyShard(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public MyFragmentLayoutForMyShard(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		int width = (int) Math.ceil((float) height * 375 / 667);
	  //  int width = Math.round((height/667)*375);
	    setMeasuredDimension(width,height);
	}
	
	

}
