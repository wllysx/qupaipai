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
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.customerservice.FileUtils;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.ui.view.activity.mine.coupon.MyCoupon;
import com.qupp.client.ui.view.activity.mine.myaction.MyActionActivity;
import com.qupp.client.ui.view.activity.mine.myaction.MyActionDetails;
import com.qupp.client.ui.view.activity.scoreshop.ShopCommodityTypeActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.ShareUtils;
import com.qupp.client.utils.dialog.BelowShardDialogH5;
import com.qupp.client.utils.event.SendTokenToH5;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class H5Web extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webView1)
    WebView mWebView;
    @BindView(R.id.iv_shard)
    ImageView ivShard;
    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    @BindView(R.id.linear)
    LinearLayout linear;
    private ValueCallback mUploadMessage;

    private final static int FILECHOOSER_RESULTCODE = 10000;
    String url = "", key = "",linkid="";
    /**
     * 带参分享：0否，1.是
     */
    String parameterShare = "";
    /**
     * 分享配置：1.微信好友，2.微信朋友圈，3.微博
     */
    String shareConfig;
    /**
     * 分享标题
     */
    String shareTitle;

    /**
     * 分享内容
     */
    String shareContent;
    /**
     * 分享链接
     */
    String shareUrl;
    /**
     * 分享图标
     */
    String shareImg;
    /**
     * 是否支持小程序：0.否，1.是
     */
    String isXcx;
    /**
     * 小程序分享标题
     */
    String xcxShareTitle;
    /**
     * 小程序分享链接
     */
    String xcxShareUrl;
    /**
     * 小程序分享图标
     */
    String xcxShareImg;

    String resultShardUrl;

    String resultxcxShareUrl;

    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_h5);
        ButterKnife.bind(this);
        key = getIntent().getStringExtra("key");
        if(getIntent().hasExtra("linkid")) {
            linkid = getIntent().getStringExtra("linkid");
        }else{
            linkid = "";
        }

        linear.setPadding(0, MyApplication.getStateBar(H5Web.this), 0, 0);
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
                if(key.equals("")||key.equals("0")){
                    String title = view.getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        tvTitle.setText(title);
                    }
                }
                Log.d("loadingurl", url);
                super.onPageFinished(view, url);
                if (url.contains("homeback_url")) {
                    //添加地址
                    String activityLogId = url.substring(url.lastIndexOf("/") + 1).replace("homeback_url", "");
                    Log.d("loadingurl", activityLogId);
                    MyActionDetails.startActivityInstance(H5Web.this, activityLogId);
                } else if (url.contains("loginout_url")) {
                    MyApplication.setToken("");
                    LoginActivity.startActivityInstance(H5Web.this);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //mWebView.loadUrl("file:///android_asset/test.html");
        mWebView.addJavascriptInterface(new JsInterface(), "jyk");

        ivDefault.setImageResource(R.mipmap.icon_default13);
        tvDefault.setText("亲，暂时没有活动～");
        if(key.equals("")||key.equals("0")){
            mWebView.loadUrl(linkid+(MyApplication.getToken().equals("") ? "token" : MyApplication.getToken()));
        }else{
            getH5(key);
        }

    }


    public static void startActivityInstance(Context activity, String key) {
        activity.startActivity(new Intent(activity, H5Web.class)
                .putExtra("key", key)
        );

    }

    public static void startActivityInstance(Context activity, String key,String linkid) {
        activity.startActivity(new Intent(activity, H5Web.class)
                .putExtra("key", key)
                .putExtra("linkid",linkid)
        );

    }


    private void getH5(String key) {
        ApiUtil.getApiService().getH5Manage(MyApplication.getToken(), key).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        url = entity.getData().getH5Url() + (MyApplication.getToken().equals("") ? "token" : MyApplication.getToken());
                        mWebView.loadUrl(url);
                        tvTitle.setText(entity.getData().getH5Name());

                        parameterShare = entity.getData().getParameterShare();
                        shareConfig = entity.getData().getShareConfig();
                        shareTitle = entity.getData().getShareTitle();
                        shareContent = entity.getData().getShareContent();
                        shareUrl = entity.getData().getShareUrl();
                        shareImg = entity.getData().getShareImg();
                        isXcx = entity.getData().getIsXcx();
                        xcxShareTitle = entity.getData().getXcxShareTitle();
                        xcxShareUrl = entity.getData().getXcxShareUrl();
                        shareContent = entity.getData().getShareContent();
                        xcxShareImg = entity.getData().getXcxShareImg();

                        if (!shareConfig.equals("")) {
                            ivShard.setVisibility(View.VISIBLE);
                        } else {
                            ivShard.setVisibility(View.INVISIBLE);
                        }


                    } else {
                        if (entity.getCode().equals("1")) {
                            llDefault.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(H5Web.this, entity.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
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
                resultShardUrl = "";
                resultxcxShareUrl = "";
                if (parameterShare != null && parameterShare.equals("1")) {
                    if (MyApplication.getToken().equals("")) {
                        LoginActivity.startActivityInstance(H5Web.this);
                    } else {
                        resultShardUrl = shareUrl + MyApplication.getCode();
                        resultxcxShareUrl = xcxShareUrl + "&name="+xcxShareTitle+"&code="+MyApplication.getCode();
                    }
                } else {
                    resultShardUrl = shareUrl + "token";
                    resultxcxShareUrl = xcxShareUrl + "&name="+xcxShareTitle;
                }

                new BelowShardDialogH5(this).setSureListener(item -> {
                    switch (item) {
                        case 1:
                            if (isXcx != null && isXcx.equals("1")) {

                                //微信小程序
                                ShareUtils.shareUmMin(this, resultShardUrl, xcxShareTitle, "", xcxShareImg, resultxcxShareUrl);
                            } else {
                                //微信
                                ShareUtils.shareWeb(this, resultShardUrl, shareTitle, shareContent, shareImg, R.mipmap.icon_logo1, SHARE_MEDIA.WEIXIN);
                            }
                            break;
                        case 2:
                            //朋友圈
                            ShareUtils.shareWeb(this, resultShardUrl, shareTitle, "", shareImg, R.mipmap.ic_logo, SHARE_MEDIA.WEIXIN_CIRCLE);
                            break;
                        case 3:
                            //微博
                            ShareUtils.shareWeb(this, resultShardUrl, shareTitle, "", shareImg, R.mipmap.ic_logo, SHARE_MEDIA.SINA);
                            break;
                    }
                }).show(shareConfig);
                break;
        }
    }

    public final class JsInterface {
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void toLogin() {
            LoginActivity.startActivityInstance(H5Web.this);
        }

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void toAddress(String activityLogId) {
            MyActionDetails.startActivityInstance(H5Web.this, activityLogId);
        }


        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void toActivityList() {
            MyActionActivity.startActivityInstance(H5Web.this);
        }

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void totype1(String name,String id) {
            CommodityList.startActivityInstance(H5Web.this, name, id);
        }

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void totype2(String name,String id) {
            ShopCommodityTypeActivity.startActivityInstance(H5Web.this, name, id, "", "");
        }

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void totype3() {
            ShopActivity.startActivityInstance(H5Web.this);
        }

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void totype4() {
            LoginActivity.startActivityInstance(H5Web.this);
        }

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void totype5() {
            if(MyApplication.getToken().equals("")){
                LoginActivity.startActivityInstance(H5Web.this);
            }else {
                MyActionActivity.startActivityInstance(H5Web.this);
            }
        }

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void totype6(String id,String url) {
            H5Web.startActivityInstance(H5Web.this, id,url);
        }

        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void totype7() {
           if(MyApplication.getToken().equals("")){
               LoginActivity.startActivityInstance(H5Web.this);
           }else {
               MyCoupon.startActivityInstance(H5Web.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        getBlanceAndIntegralAndFans();
    }

    /**
     * 获取邀请码
     */
    private void getBlanceAndIntegralAndFans() {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.setCode(entity.getData().getInvitationCode());
                    } else {
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


}
