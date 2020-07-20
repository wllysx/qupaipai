package com.qupp.client.ui.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.alipay.sdk.app.PayTask;
import com.qupp.client.MyApplication;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.dialog.MiddleUpdata;
import com.qupp.client.utils.event.EventTokenPastdue;
import com.qupp.client.utils.event.ExitEvent;
import com.qupp.client.utils.event.NetRefreshEvent;
import com.moor.imkf.IMChatManager;
import com.skl.networkmonitor.NetType;
import com.skl.networkmonitor.NetworkLiveData;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;


/**
 * Base Activity
 */


public abstract class BaseActivity extends AppCompatActivity {

    /***获取TAG的activity名称**/
    protected final String TAG = this.getClass().getSimpleName();

    private static final int SDK_PAY_FLAG = 1;
    private IWXAPI api;
    Toast toast;
    //上一个状态没网络
    boolean befaultNetStatesIsNot = false;
    MiddleUpdata middleUpdata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        api = WXAPIFactory.createWXAPI(this, MyApplication.WX_APP_ID);
        api.registerApp(MyApplication.WX_APP_ID);
        initViews();
        initDatas();
        listenerNet();
        //applyKitKatTranslucency();
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    /**
     * 初始化组件
     */
    public void initViews(){
    }

    /**
     * 初始化数据
     */
    public void initDatas(){


    }


    /**
     * 沉浸状态栏
     */
    public void applyKitKatTranslucency() {

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTintManager.setStatusBarTintResource(R.color.transparency);//通知栏所需颜色
            } else {
                mTintManager.setStatusBarTintResource(R.color.textcolor2);//通知栏所需颜色
            }
        }

        //先将状态栏调整为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        //Android6.0以上亮色状态栏模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/

    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
    }


    /**
     * 显示toast
     * @param msg
     */
    public void showToast(String msg){
        try {
            if(toast!=null){
                toast.cancel();
            }
            toast = Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT);
            toast.show();
        }catch (Exception e){

        }
    }

    /**
     * 设置状态栏文字颜色
     * @param iswhite true白色
     */
    public void setStateColor(boolean iswhite){
        if(iswhite){
            //白色字体状态栏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }else{
            //黑色字体状态栏
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }


    /**
     * 支付宝支付
     * @param orderInfo 预支付订单
     * @param type 支付的地方（充值 订单等）
     */
    public void payV2(String orderInfo, String type, Handler mHandler) {
        // String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDFpSFXJPSspuzjWfmReLccsy74Rhl/KO961p+I1umi6U/Hox7zj18G1FffXKAprdFOu+HgKJnJzlQari2Vblqu3s+oTUWXopT/HH5QiWi8KNo8ShZn2NEDXIRXX+BXOhtmDT4qCU9OVEY8NFO4rua6/xINBrgJqggvxU/N7+ToZieQg9Wg8zs9ZGtest48ub7P5hlyCANGejlUaT24vySKEon5YBvgTk4bHx6Vug/wc8CrkAFdn2HVb46mHvibTdNXPEo/fbSl6ofywE84A7kVcXcGN8aJlbnTMoTCwnJTmkkC+my2JSwISaG/GQeY4R6SWQRj5kSanSdHmLOVOzW/AgMBAAECggEAf1XPMCTYGjAFaMh8GLQMTUpIFZ6cKKfH+n0yFWhY6COmybkXZNXW5ECESJyVd6TLuA0mDlQ6GKF84B+dsKuDcyyL3yPBlVsi4WWLH/oXZzput+8HlmpC5waBIQXdNHV0V4Bqr4yF8rIq+fjAg7jzYECbcZcFlPxd/+EAdG59LRgYyiFhjaY1rJxFNLU8k17KiN91oqW5AlBymX9/GRrLzhA/tTguPvASSOF8Txm5v+XzAbRN+ZFvT/vFol6EYgYff9+whRixpRviguhVvacCBGodFjAyeEijx1FQ/d0CExJOGNz1Sglcqzmo6r5OgfDWG/9z+tSNNGr5fESypqPQAQKBgQD0Uf/I/mUioRsU8tD9BeZg98Vm8MRKaHxgmATCmK0KB+2g90Gg0i07Mz/GUyxcFvCspyDrp8UDwtDfub2I2EFhJq1kVFmd1QsrnFDBb1AS5VS/AllmKiR2SZmOugrKsaLY7sUi00U4jZ9kqWUYjOYsTNJj/hkfeONOHsDcfpwdvwKBgQDPF+rcws/ZEJaR3RhMx/LQ8Otn9Yoao726/a3GBLiQpc6DXt9J7oIsFG5gfwB7pDUh2vRpw11zhXD57zxCbNv+OlY0zM/0LEBj0hk8g9bVxKZjrSKTX0vXojsoZhBmZl7dxULKQ+RIKgrs1oGiGvEX92++qBdqyk7RYp9ywtboAQKBgQC4ZKK92XIO/rRnzRL5w9gp2xY7KP+cgNtOM610CLKgXXd1AuefLsFAat95GRDjt9SRGdn4wTgqLIBkOdTYUQ6W+g/nJto0CrRRh7pTI2W1vSuGtYkvWCMIu2ePls3KSOL1As/6BqW5lvlziX1pV/3n9VrcaMwhOqWjMgaw40WEFQKBgGKC5xcCmKqR6H4UZUXI62mW4PQ/6eUINO2pVKtYfY9ZQCZfz+m0Sjm7slG2uD0YfKnRJOjWpTKZrIdH1Stn6zJA/9b4AogNMMgLHjQaJuzjMZ9CsarW7xXxNyDdhDmhwsjN2BqtNv9Y+H92/o1lMYCMFJzmCtWizOFvf5ewtVgBAoGBAIsNmQBAKsiRdEb7AT7Wt4jnXJ6ZfT2ICbFde7oaJRAzFvZ2rjCKRC5cIW6FriexvRdaGtl27bCGh2iaqmuWXia8PkSGwhLJ1xmkXhPBU0GjLBDhuV/2VS8Hfjs8HluaSrEKMR3sFUrzTFWmfSF/H444QsPg/mcjV16CQEILweXy";

      /*  String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCIbJGyv/227DM0Erv8ZmmVMsSpRJcj8oUzuYwfiVkh9lxmryYN2eAxLDaTU4KoaxmzStRNQu4xBIaVVi/q8o/W/ierrmVjkWRkAIMViMrxt/GuyuCpElE1hLL8AbQci9o9Eu1PyxZKEycFAWNpqAPR0C6/5TiUAlPZNRAAL5K9+pDQZZdl55cBz1HNyeDteV8eE4agAVyaEwmR1+t+yRkb8Yk0Te0mxnmoxVHQf1HTwLrxGeKceVhdfhzvVMR3arxQvOA7kH6f0VVlsT8fxfuV9Ge4ssuSeSVNY2h3TAd/007QL/lh9UjAhueoqTFO64yFAj+/3WrY/GamT8+qjUjnAgMBAAECggEAaF53EMNk/P6zJZF3k99dwzD113GMF4pAcZPwKWhQcnUht8UjXdFjfPsQPaHUAbj+Dnae+QTNaV7G/5TST/KGLLp/6w4erw/aN89UebMPX+GT2l+HhJFo44LD05wSCQzNR5AlUbhTMxR385JeVcSeN3QHb/OTV1h4gWHg/eNnZkMcKeUY31aRF52/suBo+PvPRke0U1vGuS3YLx/tgGpuLZjxX8CfaiTuuoBmkGkAaeKeHGJ/EDnE22MlJWV5n/9a9OfR8WL80tAKCZb4LOGgXeb2BtH6ih3B5pr6UPI7s63vIiDKsMnaBMhfBGKOI9fcgJvueW90FzroibwzrS4XsQKBgQDZjo+hpRcw1E1zvNh8QS/kimx6WlBnjfkPirUZwHs6qkXY19DZKL5lHbib6cgDWwxf4qvIZr0vGLQc+pA+rQU/FXKpLJKZ3gJZWumwBSwR1R4AhDr2suUKrSDtA007BGDSf3uhB18PuTU/PDcjVDQB71PUA4ZVrJg0cJAkvJCsMwKBgQCgh99HiZItJNKtUm5GOXYdBAIVS0bOEvibkueRMVczEbb3z09s4dr6/GKD1rn/jNEHzfW1OLkKg9jVuj2WtD30UgmxRxH/5BCUteymRnjhuA8MZxFO5NNtaU4ww6el11XBcE93+MZ4L3hHruf8+YMPoczwPLvTHrE2/vqdAP38fQKBgHq0scZWOzJLTDQPO3s5uS2J9Y09C0SdCvD9JxATRG3eQ+7+KsmQ9XJKxSkLPsjFvcEHNCSf7ex2XoBJwrnXFLJ0Amvwu+8VDPYhFNU21ZhpP7bC3g7u78iJxZxo1FKJMaMITE6Svcc8iw/TgrXmdD7zIbDfHBGOe3p4UpSl1mOJAoGAYo3oM3gvSQ3hVc1UrEJc1EWAregQKTYWcRTInCgJm+3VQeCBp5wln49RQBWVDWI7dqoonkCbweveNKDpbNttadDv6Gl0kIBI+bOS/SYo0pL4kim4w5HSh8pt2ZxVEYjZOjAkachOuLyUAJZOEM5yeydftWHkAiJGtDfoTnmsdNUCgYBXg1zwsimIw3NGGrkVOB+NKVwrm7+f/4CCuXrfHezeS0dXKLhzxGX1Gur/CBChDxiQbcZJkpJs152udwciI1pegaA46PjzAkvPNylXrcbidWvWpmkakguJzQ1Rba6I2Glv3aKL7pzA/ZtJTwHjUWqU5oPaH9bSloKaCojueY/s8A==";
        String APPID = "2019082066267966";
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2, "1", "充值");
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String sign = OrderInfoUtil2_0.getSign(params, RSA2_PRIVATE, rsa2);
        final String orderInfo2 = orderParam + "&" + sign;*/

        //final String orderInfo = "alipay_sdk=alipay-sdk-java-3.3.49.ALL&app_id=2018072760801577&biz_content=%7B%22out_trade_no%22%3A%22yxgpay-ali-003%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22title%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fapi.imyxg.com%2Fpay%2Fnotify&sign=PXFrjLDmxSRBHtc2BV0uz3VIcC3PnnixBZ%2FSr7MA8sBXRV3v90i8ARumpQuyz%2FiDYfFc%2F%2Fdda5344J%2FTfkzDmyJYwcYEKAL7NjRI%2BwwgMrDVvW4%2BpHz8Mvq%2BWfxzesvbAVPs14kYPeNFEGQ1ryivnbdClMaZsSQeNbK26S%2FxGFryenfjr37YAsqJUIKqbOk%2FgotrklgQu3cQ%2BOmpMjt6Y%2FccJjGDA6kkXZD9O81a4VLt%2BlJpIjxpnVioxcVPf%2BF1d9orKatn4Dmkk00g5y53VN1fBG59n397sTa4q3U4FCvPV5uCTfhuI5jLzLKzdzBzt%2Bm4JnhVoaVjO%2F4yUrgkqg%3D%3D&sign_type=RSA2&timestamp=2018-11-27+11%3A16%3A10&version=1.0";
        Log.d("orderinfo", orderInfo);
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(BaseActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, false);
            Log.i("msp", result.toString());

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void wxPay(EntityForSimple content, String type){
        MyApplication.WxPayType = type;
        try {
            //获取服务端返回的数据 调支支付
            PayReq req = new PayReq();
            //req.appId = Constants.WX_APP_ID;  // 测试用appId
            req.appId			= content.getAppId();
            req.partnerId		= MyApplication.WX_MCH_ID;
            req.prepayId		= content.getPrepay_id();
            req.nonceStr		= content.getNoncestr();
            req.timeStamp		= content.getTimestamp();
            req.packageValue	= "Sign=WXPay";
            req.sign			= content.getSign();
            //req.extData			= "app data"; // optional
            //Toast.makeText(BaseActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.itemHeight = (ScreenAdaptive.getWidthSizeByAllSize(this)-ScreenAdaptive.dp2px(this,30))/2;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exit(ExitEvent event) {
        if (event != null) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toLogin(EventTokenPastdue event) {
        if (event != null) {
            try {
                JPushInterface.setAlias(BaseActivity.this, ((int) System.currentTimeMillis()), "");
                MyApplication.setToken("");
                MyApplication.setPushToken("");
                MyApplication.setCode("");
                MyApplication.isTuichu = true;
                IMChatManager.getInstance().quitSDk();
                startActivity(new Intent(BaseActivity.this, MainActivity.class));
                if(middleUpdata==null){
                    middleUpdata = new MiddleUpdata(BaseActivity.this);
                    middleUpdata.setSureListener(() -> {
                        if(middleUpdata!=null) {
                            middleUpdata.dismiss();
                        }
                        LoginActivity.startActivityInstance(BaseActivity.this);
                    });
                    middleUpdata.show("下线提示", "当前登录已失效，为了确保您的账号安全，请您重新登录", "取消", "登录", false, "");
                }else{
                    if(!middleUpdata.isShowing()){
                        middleUpdata.show("下线提示", "当前登录已失效，为了确保您的账号安全，请您重新登录", "取消", "登录", false, "");
                    }
                }
            }catch (Exception e){


            }
        }
    }

    private void listenerNet(){
        NetworkLiveData.get(BaseActivity.this).observe(this, new Observer<NetType>() {

            @Override
            public void onChanged(@Nullable NetType netType) {
                switch (netType) {
                    case NET_UNKNOW:
                        if(befaultNetStatesIsNot) {
                            //Toast.makeText(BaseActivity.this, "未知网络", Toast.LENGTH_LONG).show();
                            EventBus.getDefault().post(new NetRefreshEvent());
                            Log.e("uuu", "---MainActivity---未知网络");
                            showToast("网络已恢复");
                        }
                        befaultNetStatesIsNot = false;
                        break;
                    case NET_4G:
                        if(befaultNetStatesIsNot) {
                            //Toast.makeText(BaseActivity.this, "4G", Toast.LENGTH_LONG).show();
                            EventBus.getDefault().post(new NetRefreshEvent());
                            Log.e("uuu", "---MainActivity---4G");
                            showToast("网络已恢复");
                        }
                        befaultNetStatesIsNot = false;
                        break;
                    case NET_3G:
                        if(befaultNetStatesIsNot) {
                            //Toast.makeText(BaseActivity.this, "3G", Toast.LENGTH_LONG).show();
                            EventBus.getDefault().post(new NetRefreshEvent());
                            Log.e("uuu", "---MainActivity---3G");
                            showToast("网络已恢复");
                        }
                        befaultNetStatesIsNot = false;
                        break;
                    case NET_2G:
                        //有网络
                        if(befaultNetStatesIsNot) {
                            //Toast.makeText(BaseActivity.this, "2G", Toast.LENGTH_LONG).show();
                            EventBus.getDefault().post(new NetRefreshEvent());
                            Log.e("uuu", "---MainActivity---2G");
                            showToast("网络已恢复");
                        }
                        befaultNetStatesIsNot = false;
                        break;
                    case WIFI:
                        if(befaultNetStatesIsNot) {
                            //Toast.makeText(BaseActivity.this, "WIFI", Toast.LENGTH_LONG).show();
                            EventBus.getDefault().post(new NetRefreshEvent());
                            Log.e("uuu", "---MainActivity---WIFI");
                            showToast("网络已恢复");
                        }
                        befaultNetStatesIsNot = false;
                        break;
                    case NOME:
                        //Toast.makeText(BaseActivity.this, "没网络", Toast.LENGTH_LONG).show();
                        Log.e("uuu", "---MainActivity---没网络");
                        showToast("当前无网络");
                        //没有网络，提示用户跳转到设置
                        befaultNetStatesIsNot = true;
                        break;
                    default:
                        break;
                }
            }
        });
    }

}

