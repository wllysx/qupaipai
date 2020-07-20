/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qupp.client;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.RecyclerView;

import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.utils.MySharePerference;
import com.qupp.client.utils.Util;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import net.danlew.android.joda.JodaTimeAndroid;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Locale;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jpush.android.api.JPushInterface;
import io.paperdb.Paper;


public class MyApplication extends Application {

    public static String baseurl = "http://192.168.0.100:8804/";
    public static String baseSocketUrl = "ws://192.168.0.100:8891/websocket";

    //全局参数
    public static Context applicationContext;
    public static MyApplication instance;
    public static String WxPayType = "", token = "", userId = "", firstCategoryId = "", firstvipCategoryId = "";
    public static int statusBarHeight = 0, itemHeight = 200;
    public static boolean toMain = false, isMine = false, isTuichu = false, isNewPeople = false;


    //第三方key
    //友盟appid
    public static final String UM_APP_ID = "";
    //微信APP_ID
    public static final String WX_APP_ID = "";
    //微信
    public static final String WX_SECRET = "";
    //微信商户号
    public static final String WX_MCH_ID = "";
    //支付宝appid
    public static final String ALI_APP_ID = "";
    //bugly
    public static final String BUGLY_APP_ID = "";
    //百度统计id在AndroidManifest.xml中引入
    public static final String BaiduMobAd_STAT_ID = "";
    //极光推送及意见登录APPkey在build.gradle中引入
    public static final String JPUSH_APPKEY = "";


    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        applicationContext = this;
        instance = this;
        closeAndroidPDialog();
        UMConfigure.init(this, UM_APP_ID, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin(WX_APP_ID, WX_SECRET);
        JodaTimeAndroid.init(this);
        Paper.init(applicationContext);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //小程序预览版
        //Config.setMiniPreView();
        //热更新
        setStrictMode();
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = false;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;

        /**
         * 补丁回调接口，可以监听补丁接收、下载、合成的回调
         */
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFileUrl) {
                // Toast.makeText(getApplicationContext(), patchFileUrl, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(), "%s %d%%", Beta.strNotificationDownloading, (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadSuccess(String patchFilePath) {
                Toast.makeText(getApplicationContext(), "下载成功文件路径" + patchFilePath, Toast.LENGTH_SHORT).show();
//                Beta.applyDownloadedPatch();
            }

            @Override
            public void onDownloadFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplySuccess(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplyFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPatchRollback() {
                Toast.makeText(getApplicationContext(), "onPatchRollback", Toast.LENGTH_SHORT).show();
            }
        };

        long start = System.currentTimeMillis();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        Bugly.init(this, BUGLY_APP_ID, true);
        long end = System.currentTimeMillis();
        //一键登录
        JVerificationInterface.setDebugMode(false);
        JVerificationInterface.init(this, (code, result) -> Log.d("MyApp", "[init] code = " + code + " result = " + result + " consists = " + (System.currentTimeMillis() - start)));

    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        // 安装tinker
        Beta.installTinker();
    }

    /**
     * 获取登录手机号
     */
    public static String getPhone() {
        return MySharePerference.getShardPerferience(applicationContext, "PHONE", "");
    }

    public static void setPhone(String phone) {
        MySharePerference.addSharePerference(applicationContext, "PHONE", phone);
    }

    /**
     * 获取邀请码
     *
     * @return
     */
    public static String getCode() {
        return MySharePerference.getShardPerferience(applicationContext, "CODE", "");
    }

    public static void setCode(String code) {
        MySharePerference.addSharePerference(applicationContext, "CODE", code);
    }

    /**
     * 获取token
     */
    public static String getToken() {
        token = MySharePerference.getShardPerferience(applicationContext, "TOKEN", "");
        return token;
    }

    public static void setToken(String mToken) {
        token = mToken;
        MySharePerference.addSharePerference(applicationContext, "TOKEN", token);
    }


    /**
     * 获取读取消息的时间
     */
    public static String getMessageTime() {
        return MySharePerference.getShardPerferience(applicationContext, "MESSAGETIME", "2000-10-10 10:10:00");
    }

    public static void setMessageTime(String mtime) {
        MySharePerference.addSharePerference(applicationContext, "MESSAGETIME", mtime);
    }

    /**
     * 获取token
     */
    public static String getPushToken() {
        return MySharePerference.getShardPerferience(applicationContext, "PUSHTOKEN", "");
    }

    public static void setPushToken(String pushToken) {
        MySharePerference.addSharePerference(applicationContext, "PUSHTOKEN", pushToken);
    }


    /**
     * 获取token
     */
    public static String getUserId() {
        userId = MySharePerference.getShardPerferience(applicationContext, "USERID", "");
        return userId;
    }

    public static void setUserId(String muserId) {
        userId = muserId;
        MySharePerference.addSharePerference(applicationContext, "USERID", muserId);
    }

    public static String getAgreeStates() {
        return MySharePerference.getShardPerferience(applicationContext, "AGREESTATES", "");
    }

    public static void setAgreeStates(String agreeStates) {
        MySharePerference.addSharePerference(applicationContext, "AGREESTATES", agreeStates);
    }


    /**
     * 去登录
     *
     * @param activity
     */
    public static void toLogin(Activity activity) {
        LoginActivity.startActivityInstance(activity);
    }


    /**
     * 获得版本号
     *
     * @return
     */
    public static String getVersion() {
        // TODO Auto-generated method stub
        PackageInfo pi;
        try {
            pi = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 获得版本code
     *
     * @return
     */
    public static int getVersionCode() {
        // TODO Auto-generated method stub
        PackageInfo pi;
        try {
            pi = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 1;

    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStateBar(Activity context) {
        Class c = null;
        if (statusBarHeight > 0) {
            return statusBarHeight;
        } else {
            statusBarHeight = (int) Math.ceil(20 * context.getResources().getDisplayMetrics().density);
            try {
                c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return statusBarHeight;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 跳转设置页面 去设置通知权限
     *
     * @param context
     */
    public static void toNotificationSetting(Context context) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", pkg);
            intent.putExtra("app_uid", uid);
            context.startActivity(intent);
        } else if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getApplicationContext().getPackageName()));
            context.startActivity(intent);
        }
    }

    //设定RecyclerView最大滑动速度
    public static void setMaxFlingVelocity(RecyclerView recycleview) {
        try {
            Field field = recycleview.getClass().getDeclaredField("mMaxFlingVelocity");
            field.setAccessible(true);
            field.set(recycleview, 100000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 去除小数点后面对于的0
     *
     * @param s
     * @return
     */
    public static String rvZeroAndDot(String s) {
        if (s.isEmpty()) {
            return null;
        }
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }


    /**
     * 四舍五入保留两位小数
     *
     * @param decimal
     * @return
     */
    public static String fourAndFive(String decimal) {
        try {
            double d = Double.parseDouble(decimal);
            BigDecimal b = new BigDecimal(d);
            double result = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return Util.parseDoubleTwoPoint(result);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 手机号中间隐藏
     *
     * @param phone
     * @return
     */
    public static String getPhonePass(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public static String getIMEI1() {
        try {
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            } else {
                TelephonyManager tm = (TelephonyManager) applicationContext.getSystemService(TELEPHONY_SERVICE);
                String IMEI = tm.getDeviceId();
                return IMEI;
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }


    //热更新

    @TargetApi(9)
    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
