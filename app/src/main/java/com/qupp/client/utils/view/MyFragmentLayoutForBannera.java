package com.qupp.client.utils.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@SuppressLint("AppCompatCustomView")
public class MyFragmentLayoutForBannera extends LinearLayout {

	  public MyFragmentLayoutForBannera(Context context) {
	        super(context);
	    }

	    public MyFragmentLayoutForBannera(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public MyFragmentLayoutForBannera(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);

		int height = (int) Math.ceil((float) width * 110 / 690);
	  //  int width = Math.round((height/667)*375);
	    setMeasuredDimension(width,height);
	}
	
	

}
