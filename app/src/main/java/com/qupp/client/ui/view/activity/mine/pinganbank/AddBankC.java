package com.qupp.client.ui.view.activity.mine.pinganbank;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.customerservice.FileUtils;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.utils.event.CloseBandPage;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBankC extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webView1)
    WebView mWebView;
    @BindView(R.id.linear)
    LinearLayout linear;
    private ValueCallback mUploadMessage;

    private final static int FILECHOOSER_RESULTCODE = 10000;
    String url = "";

    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_h5);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");

        linear.setPadding(0, MyApplication.getStateBar(AddBankC.this), 0, 0);
        setStateColor(false);
        //黑色字体状态栏
        //  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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
        webSettings.setUserAgentString("jiayikouandroid");
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
                    String title = view.getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        tvTitle.setText(title);
                    }
                Log.d("loadingurl", url);
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //mWebView.loadUrl("file:///android_asset/test.html");
        mWebView.addJavascriptInterface(new JsInterface(), "jyk");
        mWebView.loadUrl(url);

    }


    public static void startActivityInstance(Context activity, String url) {
        activity.startActivity(new Intent(activity, AddBankC.class)
                .putExtra("url", url)
        );

    }


    //---------------------------------------------------

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
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result == null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
            String path = FileUtils.getPath(this, result);
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

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    public final class JsInterface {
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void toLogin() {
            LoginActivity.startActivityInstance(AddBankC.this);
        }

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void toSuccess() {
            EventBus.getDefault().post(new CloseBandPage());
            finish();
        }

    }


   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendToken(SendTokenToH5 event) {
        if (event != null) {
            //showToast(MyApplication.getToken());
            String token = MyApplication.getToken();
            mWebView.loadUrl("javascript:setTokenKey(" + "'" + token + "'" + ")");
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }



}
