package com.qupp.client.ui.view.activity.mine.balance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.OpenAuthTask;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的-绑定支付账号
 */

public class BandPayAccountActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.tv_ali)
    TextView tvAli;
    String aLiccount="",aLiname="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_payaccount);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(BandPayAccountActivity.this), 0, 0);
        setStateColor(false);
        initView();
    }


    private void initView() {
        tvTitle.setText("账号绑定");
    }


    public void weiXinLogin() {

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);

        authorization(SHARE_MEDIA.WEIXIN);

       // auth1(SHARE_MEDIA.WEIXIN);
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
            Toast.makeText(getBaseContext(), "成功了", Toast.LENGTH_LONG).show();

            UMShareAPI.get(BandPayAccountActivity.this).getPlatformInfo(BandPayAccountActivity.this, platform, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                    Log.d(TAG, "onStart " + "授权开始");
                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    Log.d(TAG, "onComplete " + "授权完成");

                    //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                    String uid = map.get("uid");
                    String openid = map.get("openid");//微博没有
                    String unionid = map.get("unionid");//微博没有
                    String access_token = map.get("access_token");
                    String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                    String expires_in = map.get("expires_in");
                    String name = map.get("name");
                    String gender = map.get("gender");
                    String iconurl = map.get("iconurl");


                    withdrawAccountadd(openid,name);
                    //拿到信息去请求登录接口。。。
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    Log.d(TAG, "onError " + "授权失败");
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {
                    Log.d(TAG, "onCancel " + "授权取消");
                }
            });
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast("失败了");
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("取消了");
        }
    };
    private void auth1(SHARE_MEDIA weixin){


        boolean isauth=UMShareAPI.get(getBaseContext()).isAuthorize(BandPayAccountActivity.this, weixin);

        if (isauth) {//删除授权
            UMShareAPI.get(BandPayAccountActivity.this).deleteOauth(BandPayAccountActivity.this,weixin,authListener);
        } else {
            //UMShareAPI.get(getBaseContext()).doOauthVerify(BandPayAccountActivity.this,weixin,authListener);
        }

    }

    //授权
    private void authorization(SHARE_MEDIA share_media) {

        //boolean isauth=UMShareAPI.get(getBaseContext()).isAuthorize(BandPayAccountActivity.this, share_media);

        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.d(TAG, "onComplete " + "授权完成");

                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");


                withdrawAccountadd(openid,name);
                //拿到信息去请求登录接口。。。
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d(TAG, "onError " + "授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d(TAG, "onCancel " + "授权取消");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, BandPayAccountActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        withdrawAccountlist();
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


    @OnClick({R.id.back, R.id.ll_ali, R.id.ll_wx})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_ali:
              //  BandAliPayActivity.startActivityInstance(this,aLiccount,aLiname);
                openAuthScheme();
                break;
            case R.id.ll_wx:
                weiXinLogin();
                break;
        }
    }

    /**
     * 账号是否绑定
     */
    private void withdrawAccountlist() {
        ApiUtil.getApiService().withdrawAccountlist(MyApplication.getToken()).enqueue(new Callback<MessageForList>() {
            @Override
            public void onResponse(Call<MessageForList> call, Response<MessageForList> response) {
                MessageForList entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        for(EntityForSimple entity1:entity.getData()){
                            if(entity1.getAccountType().equals("1")){
                                //支付宝
                                tvAli.setText(entity1.getName());
                            }else{
                                //微信
                                tvWx.setText(entity1.getName());
                            }
                        }

                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForList> call, Throwable t) {
            }
        });
    }

    private void withdrawAccountadd(String account,String name) {
        ApiUtil.getApiService().withdrawAccountadd(MyApplication.getToken(),account,name,"0").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvWx.setText(name);
                        showToast("绑定微信成功");
                    } else {
                        showToast(entity.getMsg());
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


    /**
     * 通用跳转授权业务的回调方法。
     * 此方法在支付宝 SDK 中为弱引用，故你的 App 需要以成员变量等方式保持对 Callback 的强引用，
     * 以免对象被回收。
     * 以局部变量保持对 Callback 的引用是不可行的。
     */
    final OpenAuthTask.Callback openAuthCallback = new OpenAuthTask.Callback() {
        @Override
        public void onResult(int i, String s, Bundle bundle) {
            //showToast(bundle.getString("auth_code"));
            if (i == OpenAuthTask.OK) {
                try {
                    withdrawAccountadd1(bundle.getString("auth_code"));
                }catch (Exception e){

                }
            }else{
                showToast(s);
            }
        }
    };



    private void withdrawAccountadd1(String code) {
        if(code==null){
            return;
        }
        ApiUtil.getApiService().withdrawAccountadd1(MyApplication.getToken(),code,"1","1").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                       // tvAli.setText(name);
                        showToast("绑定支付宝成功");
                        withdrawAccountlist();
                    } else {
                        showToast(entity.getMsg());
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


    /**
     * 通用跳转授权业务 Demo
     */
    public void openAuthScheme() {

        // 传递给支付宝应用的业务参数
        final Map<String, String> bizParams = new HashMap<>();
        bizParams.put("url", "https://authweb.alipay.com/auth?auth_type=PURE_OAUTH_SDK&app_id="+MyApplication.ALI_APP_ID+"&scope=auth_user&state=init");

        // 支付宝回跳到你的应用时使用的 Intent Scheme。请设置为不和其它应用冲突的值。
        // 如果不设置，将无法使用 H5 中间页的方法(OpenAuthTask.execute() 的最后一个参数)回跳至
        // 你的应用。
        //
        // 参见 AndroidManifest 中 <AlipayResultActivity> 的 android:scheme，此两处
        // 必须设置为相同的值。
        final String scheme = "jykapp";

        // 唤起授权业务
        final OpenAuthTask task = new OpenAuthTask(this);
        task.execute(
                scheme,	// Intent Scheme
                OpenAuthTask.BizType.AccountAuth, // 业务类型
                bizParams, // 业务参数
                openAuthCallback, // 业务结果回调。注意：此回调必须被你的应用保持强引用
                false); // 是否需要在用户未安装支付宝 App 时，使用 H5 中间页中转
    }


    private static String bundleToString(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        for (String key: bundle.keySet()) {
            sb.append(key).append("=>").append(bundle.get(key)).append("\n");
        }
        return sb.toString();
    }

}
