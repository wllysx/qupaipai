package com.qupp.client.ui.view.activity.login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.KeyboardUtils;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.TimeCount2;
import com.qupp.client.utils.event.RemoveLogin;
import com.qupp.client.utils.event.SendTokenToH5;
import com.qupp.client.utils.secretUtils.JwtUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.bt_login)
    TextView btLogin;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    boolean isshow = false;
    @BindView(R.id.tv_pass)
    TextView tvPass;
    private boolean autoFinish = false;

    private static final String LOGIN_CODE = "login_code";
    private static final String LOGIN_CONTENT = "login_content";
    private static final String LOGIN_OPERATOR = "login_operator";
    private static final int LOGIN_CODE_UNSET = -1562;
    private static Bundle savedLoginState = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(LoginActivity.this), 0, 0);
        setStateColor(false);
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);
        if (verifyEnable) {
            loginAuth();
        }
        initView();
    }


    private void initView() {
        keepLoginBtnNotOver(linear, llContent);
        etPhone.addTextChangedListener(textWatcher);
        etCode.addTextChangedListener(textWatcher);
        etPhone.setText(MyApplication.getPhone());
        etPhone.setSelection(MyApplication.getPhone().length());
        if (MyApplication.getAgreeStates().equals("")) {
            checkbox.setChecked(false);

        } else {
            checkbox.setChecked(true);
        }

        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                MyApplication.setAgreeStates("1");
            } else {
                MyApplication.setAgreeStates("");
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ivClose.setVisibility(View.GONE);
            } else {
                ivClose.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(etPhone.getText()) && !TextUtils.isEmpty(etCode.getText())) {
                btLogin.setClickable(true);
                btLogin.setEnabled(true);
                btLogin.setBackgroundResource(R.drawable.bg_login_enable);
            } else {
                btLogin.setClickable(false);
                btLogin.setEnabled(false);
                btLogin.setBackgroundResource(R.drawable.bg_login_unable);
            }
        }
    };

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void weiXinLogin() {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
    }


    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.d(TAG, "onComplete " + "授权完成");
            String uid = data.get("uid");
            String openid = data.get("openid");//微博没有
            String unionid = data.get("unionid");//微博没有
            String access_token = data.get("access_token");
            String refresh_token = data.get("refresh_token");//微信,qq,微博都没有获取到
            String expires_in = data.get("expires_in");
            String name = data.get("name");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");

            //o87An1Ml-p4Cmu01vMY6JjhMByJg

            loginWithOpneId(openid, name, iconurl, unionid, gender);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.bt_login, R.id.tv_wechat, R.id.tv_code, R.id.ll_agress, R.id.tv_agreement, R.id.tv_pass, R.id.iv_close, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                if (!checkbox.isChecked()) {
                    showToast("请先同意协议");
                    return;
                }
                login();
                break;
            case R.id.tv_wechat:
                weiXinLogin();
                break;
            case R.id.tv_pass:
                // showToast("一键登录");
                loginAuth();
                break;
            case R.id.tv_code:
                if (DoubleClick.isFastDoubleClick()) {
                    return;
                }
                getCode();
                break;
            case R.id.ll_agress:
                checkbox.setChecked(!checkbox.isChecked());
                break;
            case R.id.tv_agreement:
                startActivity(new Intent(LoginActivity.this, KnowWeb.class));
                break;
            case R.id.iv_close:
                etPhone.setText("");
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    /**
     * 微信登录
     *
     * @param openId  微信openid
     * @param name    微信昵称
     * @param iconurl 微信图标
     * @param uid     uid
     * @param gender  性别
     */
    private void loginWithOpneId(String openId, String name, String iconurl, String uid, String gender) {

        ApiUtil.getApiService().loginWithOpneId(openId).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (entity.getData() == null) {
                            LoginBindActivity.startActivityInstance(LoginActivity.this, openId, name, iconurl, uid, gender);
                        } else {
                            MyApplication.setToken(response.headers().get("Authorization"));
                            JwtUtils.setUserId(response.headers().get("Authorization"));
                            JPushInterface.setAlias(LoginActivity.this, ((int) System.currentTimeMillis()), MyApplication.getUserId());
                            EventBus.getDefault().post(new SendTokenToH5());
                            finish();
                        }

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


    /**
     * 登录
     */
    private void login() {

        ApiUtil.getApiService().loginWithCode(etPhone.getText().toString(), etCode.getText().toString(), "4", "8").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.setToken(response.headers().get("Authorization"));
                        JwtUtils.setUserId(response.headers().get("Authorization"));
                        JPushInterface.setAlias(LoginActivity.this, ((int) System.currentTimeMillis()), MyApplication.getUserId());
                        EventBus.getDefault().post(new SendTokenToH5());
                        finish();
                    } else {
                        tvError.setText(entity.getMsg());
                        tvError.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });


    }


    /**
     * 获取验证码
     */
    private void getCode() {
        if (etPhone.getText().toString().length() < 11) {
            showToast("请输入正确手机号");
            return;
        }
        ApiUtil.getApiService().codesms(etPhone.getText().toString()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        TimeCount2 time = new TimeCount2(60000, 1000, tvCode, "获取验证码");
                        time.start();
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void remove(RemoveLogin event) {
        if (event != null) {
            finish();
        }
    }

    /**
     * 保持登录按钮始终不会被覆盖
     *
     * @param root
     * @param subView
     */
    private void keepLoginBtnNotOver(final View root, final View subView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            // 获取root在窗体的可视区域
            root.getWindowVisibleDisplayFrame(rect);
            // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
            int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
            // 若不可视区域高度大于200，则键盘显示,其实相当于键盘的高度
            if (rootInvisibleHeight > 200) {
                // 显示键盘时
                int srollHeight = rootInvisibleHeight - (subView.getHeight()) - KeyboardUtils.getNavigationBarHeight(root.getContext());
                if (srollHeight > 0) {
                    if (!isshow) {
                        isshow = true;
                        //当键盘高度覆盖按钮时
                        scrollToPosition(root, 0, srollHeight + ScreenAdaptive.dp2px(LoginActivity.this, 30));
                    }
                }
            } else {
                // 隐藏键盘时
                isshow = false;
                scrollToPosition(root, 0, 0);
            }
        });
    }

    public void scrollToPosition(View root, int x, int y) {

        ObjectAnimator xTranslate = ObjectAnimator.ofInt(root, "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(root, "scrollY", y);
        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(300L);
        animators.playTogether(xTranslate, yTranslate);
        animators.start();
    }

    //一键登录
    private void loginAuth() {
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);
        if (!verifyEnable) {
            showToast("当前网络环境不支持认证");
            return;
        }
        setUIConfig();
        JVerificationInterface.loginAuth(this, autoFinish, (code, content, operator) -> {
            Log.d("loginsuccess", "[" + code + "]message=" + content + ", operator=" + operator);
            Bundle bundle = new Bundle();
            bundle.putInt(LOGIN_CODE, code);
            bundle.putString(LOGIN_CONTENT, content);
            bundle.putString(LOGIN_OPERATOR, operator);
            savedLoginState = bundle;
            if (code == 6000) {
                //content为token 拿去调接口
                tologin(content);
            } else {
                if (code != 6002) {
                    //showToast(content);
                }
            }
            //这里通过static bundle保存数据是为了防止出现授权页方向和MainActivity不相同时，MainActivity被销毁重新初始化导致回调数据无法展示到MainActivity
        }, new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
                Log.d(TAG, "[onEvent]. [" + cmd + "]message=" + msg);
            }
        });
    }

    /**
     * 登录
     */
    private void tologin(String token) {

        ApiUtil.getApiService().oneClick(token, "4", "8").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.setToken(response.headers().get("Authorization"));
                        JwtUtils.setUserId(response.headers().get("Authorization"));
                        JPushInterface.setAlias(LoginActivity.this, ((int) System.currentTimeMillis()), MyApplication.getUserId());
                        EventBus.getDefault().post(new SendTokenToH5());
                        JVerificationInterface.dismissLoginAuthActivity();
                        finish();
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });


    }

    private void setUIConfig() {
        JVerifyUIConfig portrait = getPortraitConfig();
        JVerifyUIConfig landscape = getLandscapeConfig();

        //支持授权页设置横竖屏两套config，在授权页中触发横竖屏切换时，sdk自动选择对应的config加载。
        JVerificationInterface.setCustomUIWithConfig(portrait, landscape);
    }

    private JVerifyUIConfig getPortraitConfig() {
        JVerifyUIConfig.Builder configBuilder = new JVerifyUIConfig.Builder();

        TextView mtxt = new TextView(this);
        mtxt.setText("其他登录方式");
        mtxt.setGravity(Gravity.CENTER_HORIZONTAL);
        mtxt.setWidth(ScreenAdaptive.getWidthSizeByAllSize(LoginActivity.this));
        mtxt.setTextSize(14);
        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLayoutParams1.setMargins(ScreenAdaptive.dp2px(LoginActivity.this,0), ScreenAdaptive.dp2px(LoginActivity.this,480), 0, 0);
        mtxt.setLayoutParams(mLayoutParams1);

        ImageView loadingView = new ImageView(getApplicationContext());
        loadingView.setImageResource(R.drawable.umcsdk_load_dot_white);
        RelativeLayout.LayoutParams loadingParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingView.setLayoutParams(loadingParam);





        configBuilder.setAuthBGImgPath("main_bg")
                .setNavColor(0xffDD0000)
                .setNavText("登录")
                .setNavTextColor(0xffffffff)
                .setNavReturnImgPath("umcsdk_return_bg")
                .setLogoWidth(70)
                .setLogoHeight(70)
                .setLogoHidden(false)
                .setNumberColor(0xff333333)
                .setLogBtnText("本机号码一键登录")
                .setLogBtnTextColor(0xffffffff)
                .setLogBtnHeight(45)
                .setLogBtnImgPath("bg_login_selector")
                .setAppPrivacyOne("趣拍拍用户协议", "https://apph5.jiayikou.com/#/ruleKey")
                .setAppPrivacyTwo("趣拍拍用户隐私保护政策", "https://apph5.jiayikou.com/#/adver/user_hide_key/token")
                .setAppPrivacyColor(0xff666666, 0xffDD0000)
                .setUncheckedImgPath("umcsdk_uncheck_image")
                .setCheckedImgPath("umcsdk_check_image")
                .setSloganTextColor(0xff999999)
                .setLogoOffsetY(70)
                .setLogoImgPath("logo_cm")
                .setNumFieldOffsetY(190)
                .setSloganOffsetY(220)
                .setLogBtnOffsetY(265)
                .setNumberSize(18)
                .setPrivacyState(true)
                .setNavTransparent(false)
                .setPrivacyOffsetY(20)
                .setPrivacyCheckboxSize(10)
                .setPrivacyNavColor(0xffDD0000)
                .setPrivacyStatusBarColorWithNav(true)
                .setStatusBarColorWithNav(true).addCustomView(mtxt, true, (context, view) -> {

        });


        return configBuilder.build();
    }

    private JVerifyUIConfig getLandscapeConfig() {
        JVerifyUIConfig.Builder configBuilder = new JVerifyUIConfig.Builder();

        //导航栏按钮示例
        Button navBtn = new Button(this);
        // navBtn.setText("导航栏按钮");
        navBtn.setTextColor(0xff3a404c);
        navBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        RelativeLayout.LayoutParams navBtnParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        navBtnParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        navBtn.setLayoutParams(navBtnParam);

        configBuilder
                .setAuthBGImgPath("main_bg")
                .setNavColor(0xff0086d0)
                .setNavText("登录")
                .setNavTextColor(0xffffffff)
                .setNavReturnImgPath("umcsdk_return_bg")
                .setLogoWidth(70)
                .setLogoHeight(70)
                .setLogoHidden(false)
                .setNumberColor(0xff333333)
                .setLogBtnText("本机号码一键登录")
                .setLogBtnTextColor(0xffffffff)
                .setLogBtnImgPath("umcsdk_login_btn_bg")
                .setAppPrivacyOne("应用自定义服务条款一", "https://www.jiguang.cn/about")
                .setAppPrivacyTwo("应用自定义服务条款二", "https://www.jiguang.cn/about")
                .setAppPrivacyColor(0xff666666, 0xff0085d0)
                .setUncheckedImgPath("umcsdk_uncheck_image")
                .setCheckedImgPath("umcsdk_check_image")
                .setSloganTextColor(0xff999999)
                .setLogoOffsetY(30)
                .setLogoImgPath("logo_cm")
                .setNumFieldOffsetY(150)
                .setSloganOffsetY(185)
                .setLogBtnOffsetY(210)
                .setPrivacyOffsetY(10);
        return configBuilder.build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null != savedLoginState) {
            initLoginResult(savedLoginState);
        }
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);
        if (verifyEnable) {
            tvPass.setVisibility(View.VISIBLE);
        } else {
            tvPass.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent.getExtras()) {
            initLoginResult(intent.getExtras());
        }
    }

    private void initLoginResult(Bundle extras) {
        int loginCode = extras.getInt(LOGIN_CODE, LOGIN_CODE_UNSET);
        if (loginCode != LOGIN_CODE_UNSET) {
            String loginContent = extras.getString(LOGIN_CONTENT);
            String operator = extras.getString(LOGIN_OPERATOR);
        }
    }

}
