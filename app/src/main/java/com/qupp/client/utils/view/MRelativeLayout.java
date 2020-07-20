package com.qupp.client.utils.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

@SuppressLint("AppCompatCustomView")
public class MRelativeLayout extends RelativeLayout {

	  public MRelativeLayout(Context context) {
	        super(context);
	    }

	    public MRelativeLayout(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public MRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    }


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		this.setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth();
		//设置高度与宽度一样
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	

}
