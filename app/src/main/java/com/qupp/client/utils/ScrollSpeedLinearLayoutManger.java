package com.qupp.client.utils;

/**
 * author: MrWang on 2019/12/17
 * email:773630555@qq.com
 * date: on 2019/12/17 15:16
 */

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import cn.jpush.android.helper.Logger;

/**
 * 控制滑动速度的LinearLayoutManager
 */

public class ScrollSpeedLinearLayoutManger extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 2f;
    private Context contxt;

    public ScrollSpeedLinearLayoutManger(Context context) {
        super(context);

    }

    public ScrollSpeedLinearLayoutManger(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.contxt = context;
        setSpeedSlow();
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(
                recyclerView.getContext()) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return ScrollSpeedLinearLayoutManger.this
                        .computeScrollVectorForPosition(targetPosition);
            }

            // This returns the milliseconds it takes to
// scroll one pixel.
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                Logger.i("dfdsafsdafdsa", "calculateSpeedPerPixel");
                return MILLISECONDS_PER_INCH / displayMetrics.density;
// 返回滑动一个pixel需要多少毫秒
            }


            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state,
                                         RecyclerView.SmoothScroller.Action action) {
                Logger.i("dfdsafsdafdsa", "onTargetFound");
                if (getLayoutManager() == null) {
                    return;
                }
                int dx = calculateDxToMakeVisible(targetView, getHorizontalSnapPreference());
                int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());
                if (dx > 0) {
                    dx = dx - getLayoutManager().getLeftDecorationWidth(targetView);
                } else {
                    dx = dx + getLayoutManager().getRightDecorationWidth(targetView);
                }
                if (dy > 0) {
                    dy = dy - getLayoutManager().getTopDecorationHeight(targetView);
                } else {
                    dy = dy + getLayoutManager().getBottomDecorationHeight(targetView);
                }
                final int distance = (int) Math.sqrt(dx * dx + dy * dy);
                final int time = calculateTimeForDeceleration(distance);
                if (time > 0) {// new AccelerateInterpolator()
                    action.update(-dx, -dy, time, new DecelerateInterpolator());
                }
            }

        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public void setSpeedSlow() {
// 自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
// 0.3f是自己估摸的一个值，可以根据不同需求自己修改
        MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.5f;
    }

    public void setSpeedFast() {
        MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.03f;
    }
}