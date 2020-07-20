package com.qupp.client.ui.view.activity.main;

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
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.customerservice.FileUtils;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.ui.view.activity.mine.myaction.MyActionDetails;
import com.qupp.client.utils.ShareUtils;
import com.qupp.client.utils.event.SendTokenToH5;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动h5
 */
public class ActionH5Web extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private WebView mWebView;
    private ValueCallback mUploadMessage;

    private final static int FILECHOOSER_RESULTCODE = 10000;
    String url = "";
    String shardurl = "";

    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_action_h5);
        ButterKnife.bind(this);
        mWebView = (WebView) findViewById(R.id.webView1);
        url = getIntent().getStringExtra("url");

        CookieSyncManager.createInstance(getApplicationContext());
        CookieManager cookieManager = CookieManager.getInstance();
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebStorage.getInstance().deleteAllData();
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

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress != 100) {
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        //设置Web视图
        mWebView.setWebViewClient(new WebViewClient() {

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有网站的证书
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("loadingurl", url);
                super.onPageFinished(view, url);
                if (url.contains("homeback_url")) {
                    //添加地址
                    String activityLogId = url.substring(url.lastIndexOf("/") + 1).replace("homeback_url", "");
                    Log.d("loadingurl", activityLogId);
                    MyActionDetails.startActivityInstance(ActionH5Web.this, activityLogId);
                } else if (url.contains("loginout_url")) {
                    MyApplication.setToken("");
                    LoginActivity.startActivityInstance(ActionH5Web.this);
                } else {
                    shardurl = url;
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("loadingurl", url);
                view.loadUrl(url);
                return true;
            }
        });

        //Map extraHeaders = new HashMap();
        //extraHeaders.put("Authorization", MyApplication.getToken());
        mWebView.loadUrl(url);
        mWebView.addJavascriptInterface(new JsInterface(), "jyk");

    }


    public static void startActivityInstance(Context activity, String url) {
        activity.startActivity(new Intent(activity, ActionH5Web.class)
                .putExtra("url", url)
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

    @OnClick({R.id.back, R.id.iv_shard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.iv_shard:
              /*  new BelowShardDialogH5(this).setSureListener(item -> {
                    switch (item) {
                        case 2:

                            String path;
                            if (shardurl.contains("turntableAmount")) {
                                path = "pages/gameList/gameList";
                            } else {
                                path = "pages/webview/webview?url=" + shardurl + "/";
                            }
                            Log.d("loadingurl", path + ",000");
                            ShareUtils.shareUmMin1(this, shardurl, "参与活动赢大奖", "轻轻松松抽奖大奖，积攒多年的人品终于有用了，你也赶紧来抽奖吧！！", R.mipmap.icon_shard_action, path.replace("#", "and"));
                            break;
                        case 3:
                            ShareUtils.shareWeb(this, shardurl, "参与活动赢大奖", "", "", R.mipmap.icon_shard_action, SHARE_MEDIA.WEIXIN_CIRCLE);
                            break;
                    }
                }).show();*/
                String path;
                if (shardurl.contains("turntableAmount")) {
                    path = "pages/gameList/gameList";
                } else {
                    path = "pages/webview/webview?url=" + shardurl + "/";
                }
                Log.d("loadingurl", path + ",000");
                ShareUtils.shareUmMin1(this, shardurl, "参与活动赢大奖", "轻轻松松抽奖大奖，积攒多年的人品终于有用了，你也赶紧来抽奖吧！！", R.mipmap.icon_shard_action, path.replace("#", "and"));


                break;
        }
    }

    public class JsInterface {
        @JavascriptInterface
        public void addAddress(String id) {
            try {
                showToast(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendToken(SendTokenToH5 event) {
        if (event != null) {
            //showToast(MyApplication.getToken());
            String token = MyApplication.getToken();
            mWebView.loadUrl("javascript:setTokenKey(" + "'" + token + "'" + ")");
        }
    }

}
