package com.qupp.client.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 开发者：hongzhen
 * 创建时间： 2019/8/6 11:49
 * 类名：ImageViewScale.java
 * 描述：根据父控件保持比例缩放的图片展示控件
 */
@SuppressLint("AppCompatCustomView")
public class ImageViewScale extends ImageView {
    public ImageViewScale(Context context) {
        super(context);
    }

    public ImageViewScale(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewScale(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            //根据宽度来计算比例，保持比例
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) Math.ceil((float) width * (float) drawable.getIntrinsicHeight() / (float) drawable.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}