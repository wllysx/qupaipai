package com.qupp.client.ui.view.activity.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qupp.client.R;
import com.qupp.client.ui.view.activity.customerservice.FileUtils;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimpleString;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YinsiWeb extends Activity {

    private WebView mWebView;
    private ValueCallback mUploadMessage;

    private final static int FILECHOOSER_RESULTCODE = 10000;


    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_yinsi);
        mWebView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        //黑色字体状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mWebView.setWebChromeClient(new WebChromeClient() {

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
                i.setType(type);
                startActivityForResult(Intent.createChooser(i, "File Chooser"),
                        FILECHOOSER_RESULTCODE);
            }

            // For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
                i.setType(type);
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            //Android 5.0+
            @Override
            @SuppressLint("NewApi")
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
                        && fileChooserParams.getAcceptTypes().length > 0 && !"".equals(fileChooserParams.getAcceptTypes()[0])) {
                    i.setType(fileChooserParams.getAcceptTypes()[0]);
                } else {
                    i.setType("*/*");
                }
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                return true;
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            //	Dialog progressDialog = ProgressDialog.show(KnowWeb.this, null, "正在加载，请稍后...");
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Now_Url = url;
                view.loadUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url,Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //progressDialog.cancel();
            }
        });

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setUserAgentString("jiayikouandroid");
        initData();
    }

    /**
     * 获取系统参数
     */
    private void initData() {
        ApiUtil.getApiService().syskey("user_hide_key").enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        mWebView.loadUrl(entity.getData()+"/token");
                    } else {
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
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
}
