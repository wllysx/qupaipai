package com.qupp.client.ui.view.activity.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.customerservice.FileUtils;

import java.io.File;

public class DefaultWeb extends BaseActivity {

    private WebView mWebView;
    private ValueCallback mUploadMessage;

    private final static int FILECHOOSER_RESULTCODE = 10000;
    String url="",name = "";

    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_defaultweb);
        mWebView = (WebView) findViewById(R.id.webView1);
        findViewById(R.id.back).setOnClickListener(v -> finish());
        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        ((TextView) findViewById(R.id.tv_title)).setText(name);
        //黑色字体状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        WebSettings webSettings = mWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("gb2312");
        webSettings.setSupportZoom(true);
        //设置Web视图
        mWebView.setWebViewClient(new WebViewClient() {

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //注意：super句话一定要删除，或者注释掉，否则又走handler.cancel()默认的不支持https的了。
                //super.onReceivedSslError(view, handler, error);
                //handler.cancel(); // Android默认的处理方式
                //handler.handleMessage(Message msg); // 进行其他处理
                handler.proceed(); // 接受所有网站的证书
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (TextUtils.isEmpty(name)){
                    String title = view.getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        ((TextView) findViewById(R.id.tv_title)).setText(title);
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(url);
    }


    public static void startActivityInstance(Context activity, String url,String name) {
        activity.startActivity(new Intent(activity, DefaultWeb.class)
                .putExtra("url", url)
                .putExtra("name", name)
        );

    }

    public static void startActivityInstance1(Context activity, String url,String name) {
        activity.startActivity(new Intent(activity, H5Web.class)
                .putExtra("url", url)
                .putExtra("name", name).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result == null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
            String path =  FileUtils.getPath(this, result);
            if (TextUtils.isEmpty(path)) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
            Uri uri = Uri.fromFile(new File(path));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUploadMessage.onReceiveValue(new Uri[]{uri});
            } else {
                mUploadMessage.onReceiveValue(uri);
            }
            mUploadMessage = null;
        }
    }
}
