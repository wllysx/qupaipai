package com.qupp.client.utils.view.headerviewpager;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ScrollView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/8
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class HeaderScrollHelper {

    private int sysVersion;         //当前sdk版本，用于判断api版本
    private ScrollableContainer mCurrentScrollableContainer;

    public HeaderScrollHelper() {
        sysVersion = Build.VERSION.SDK_INT;
    }

    /**
     * 包含有 ScrollView ListView RecyclerView 的组件
     */
    public interface ScrollableContainer {

        /**
         * @return ScrollView ListView RecyclerView 或者其他的布局的实例
         */
        View getScrollableView();
    }

    public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer) {
        this.mCurrentScrollableContainer = scrollableContainer;
    }

    private View getScrollableView() {
        if (mCurrentScrollableContainer == null) return null;
        return mCurrentScrollableContainer.getScrollableView();
    }

    /**
     * 判断是否滑动到顶部方法,ScrollAbleLayout根据此方法来做一些逻辑判断
     * 目前只实现了AdapterView,ScrollView,RecyclerView
     * 需要支持其他view可以自行补充实现
     */
    public boolean isTop() {
        View scrollableView = getScrollableView();
        if (scrollableView == null) {
            throw new NullPointerException("You should call ScrollableHelper.setCurrentScrollableContainer() to set ScrollableContainer.");
        }
        if (scrollableView instanceof AdapterView) {
           // Log.d("111111111", isAdapterViewTop((AdapterView) scrollableView) + "1");
            Log.d("111111111", canChildScrollUp((AdapterView) scrollableView) + "1");

            return !canChildScrollUp((AdapterView) scrollableView)&&((AdapterView) scrollableView).getFirstVisiblePosition()==0;
        }
        if (scrollableView instanceof ScrollView) {
            //Log.d("111111111", isAdapterViewTop((AdapterView) scrollableView) + "2");
            return isScrollViewTop((ScrollView) scrollableView);
        }
        if (scrollableView instanceof RecyclerView) {
           // Log.d("111111111", isAdapterViewTop((AdapterView) scrollableView) + "3");
            return isRecyclerViewTop((RecyclerView) scrollableView);
        }
        if (scrollableView instanceof WebView) {
           // Log.d("111111111", isAdapterViewTop((AdapterView) scrollableView) + "4");
            return isWebViewTop((WebView) scrollableView);
        }
        if (scrollableView instanceof AbsListView) {
            //Log.d("111111111", isAdapterViewTop((AdapterView) scrollableView) + "5");
            return isAbsListviewTop(((AbsListView) scrollableView));
        }
        throw new IllegalStateException("scrollableView must be a instance of AdapterView|ScrollView|RecyclerView");
    }

    private boolean isRecyclerViewTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || (firstVisibleItemPosition == 0 && childAt.getTop() == 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAdapterViewTop(AdapterView adapterView) {
        if (adapterView != null) {
            int firstVisiblePosition = adapterView.getFirstVisiblePosition();
            View childAt = adapterView.getChildAt(0);
            if (childAt == null || (firstVisiblePosition == 0 && childAt.getTop() == 0)) {
                return true;
            }
        }
        return false;
    }

    public boolean canChildScrollUp(AdapterView mTarget) {

        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }

    }


    private boolean isScrollViewTop(ScrollView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }
        return false;
    }

    private boolean isWebViewTop(WebView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }
        return false;
    }

    private boolean isAbsListviewTop(AbsListView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }
        return false;
    }


    /**
     * 将特定的view按照初始条件滚动
     *
     * @param velocityY 初始滚动速度
     * @param distance  需要滚动的距离
     * @param duration  允许滚动的时间
     */
    @SuppressLint("NewApi")
    public void smoothScrollBy(int velocityY, int distance, int duration) {
        View scrollableView = getScrollableView();
        if (scrollableView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) scrollableView;
            if (sysVersion >= 21) {
                absListView.fling(velocityY*2);
            } else {
                absListView.smoothScrollBy(distance*2, duration/2);
            }
        } else if (scrollableView instanceof ScrollView) {
            ((ScrollView) scrollableView).fling(velocityY);
        } else if (scrollableView instanceof RecyclerView) {
            ((RecyclerView) scrollableView).fling(0, velocityY);
        } else if (scrollableView instanceof WebView) {
            ((WebView) scrollableView).flingScroll(0, velocityY);
        }
    }
}
