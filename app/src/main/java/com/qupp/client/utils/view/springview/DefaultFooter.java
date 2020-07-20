package com.qupp.client.utils.view.springview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.qupp.client.R;
import com.liaoinstan.springview.container.BaseFooter;


/**
 * Created by Administrator on 2016/3/21.
 */
public class DefaultFooter extends BaseFooter {
    private Context context;
    private int rotationSrc;
    private TextView footerTitle;
    private ProgressBar footerProgressbar;
    private TextView tv_content;

    public DefaultFooter(Context context){
        this(context, R.drawable.progress_small);
    }

    public DefaultFooter(Context context, int rotationSrc){
        this.context = context;
        this.rotationSrc = rotationSrc;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.springview_default_footer, viewGroup, true);
        footerTitle = (TextView) view.findViewById(R.id.default_footer_title);
        tv_content = view.findViewById(R.id.tv_content);
        footerProgressbar = (ProgressBar) view.findViewById(R.id.default_footer_progressbar);
        footerProgressbar.setIndeterminateDrawable(ContextCompat.getDrawable(context,rotationSrc));
        return view;
    }

    @Override
    public void onPreDrag(View rootView) {
    }

    @Override
    public void onDropAnim(View rootView, int dy) {
    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {
        if (upORdown) {
            tv_content.setText("松开载入更多");
        } else {
            tv_content.setText("查看更多");
        }
    }

    @Override
    public void onStartAnim() {
        footerTitle.setVisibility(View.INVISIBLE);
        footerProgressbar.setVisibility(View.VISIBLE);
        tv_content.setText("加载中...");
    }

    @Override
    public void onFinishAnim() {
        tv_content.setText("查看更多");
        footerTitle.setVisibility(View.VISIBLE);
        footerProgressbar.setVisibility(View.INVISIBLE);
    }
}