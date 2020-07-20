package com.qupp.client.ui.view.activity.customerservice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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

import java.io.File;

public class MoorWebCenter extends Activity {

	private WebView mWebView;
	String Now_Url;
	private ValueCallback mUploadMessage;
	private View linear;

	private final static int FILECHOOSER_RESULTCODE = 10000;


	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_customer_service);

		/*linear = findViewById(R.id.linear);
		int stateBarHeight = MyApplication.getStateBar(this);
		linear.setPadding(0, stateBarHeight, 0, 0);*/

		mWebView = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);

		Intent intent = getIntent();
		Now_Url = intent.getStringExtra("OpenUrl");

		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

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
			// For Android 3.0+
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
			Dialog progressDialog = ProgressDialog.show(MoorWebCenter.this, null, "正在加载，请稍后...");

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Now_Url = url;
				view.loadUrl(url);

				return true;
			}

			public void onPageStarted(WebView view, String url,
									  Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				progressDialog.show();
				progressDialog.setCancelable(true);

			}

			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressDialog.cancel();

			}
		});
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.loadUrl(Now_Url);

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
