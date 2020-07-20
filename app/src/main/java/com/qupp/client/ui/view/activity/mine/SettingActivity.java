package com.qupp.client.ui.view.activity.mine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadFailedListener;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.MainActivity;
import com.qupp.client.ui.view.activity.login.LoginActivity;
import com.qupp.client.ui.view.activity.mine.setting.AboutActivity;
import com.qupp.client.ui.view.activity.mine.setting.AccountSafeActivity;
import com.qupp.client.ui.view.activity.mine.setting.MyDataActivity;
import com.qupp.client.ui.view.activity.mine.setting.PushActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.GlideCacheUtil;
import com.qupp.client.utils.dialog.MiddleDialog;
import com.qupp.client.utils.dialog.UpdataVersionDialog;
import com.moor.imkf.IMChatManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的-设置
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    String url ="";
    boolean isNew=true;

    private DownloadBuilder builder;

    String content="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(SettingActivity.this), 0, 0);
        setStateColor(false);
        initView();
        getVersion();
    }


    private void initView() {
        tvTitle.setText("设置");
        tvCache.setText(GlideCacheUtil.getInstance().getCacheSize(this));
        tvVersion.setText(MyApplication.getVersion());
    }


    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, SettingActivity.class));
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
        AllenVersionChecker.getInstance().cancelAllMission(this);
    }


    @OnClick({R.id.back, R.id.ll_mymessage, R.id.ll_save, R.id.ll_push, R.id.ll_clear, R.id.ll_about, R.id.tv_unlogin, R.id.ll_version})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_mymessage:
                MyDataActivity.startActivityInstance(this);
                break;
            case R.id.ll_save:
                AccountSafeActivity.startActivityInstance(this);
                break;
            case R.id.ll_push:
                PushActivity.startActivityInstance(this);
                break;
            case R.id.ll_clear:
                new MiddleDialog(this).setSureListener(() -> {
                    GlideCacheUtil.getInstance().clearImageDiskCache(this);
                    new Handler().postDelayed(() -> {
                        tvCache.setText(GlideCacheUtil.getInstance().getCacheSize(this));
                        showToast("缓存清除成功");
                    }, 2000);

                }).show("清除缓存", "你确定要清除缓存数据吗？", "取消", "清除", false);
                break;
            case R.id.ll_about:
                AboutActivity.startActivityInstance(this);
                break;
            case R.id.tv_unlogin:
                new MiddleDialog(this).setSureListener(() -> {
                    JPushInterface.setAlias(SettingActivity.this, ((int) System.currentTimeMillis()), "");
                    MyApplication.toMain = true;
                    MyApplication.setToken("");
                    MyApplication.setPushToken("");
                    MyApplication.setCode("");
                    startActivity(new Intent(SettingActivity.this, MainActivity.class));
                    LoginActivity.startActivityInstance(SettingActivity.this);
                    IMChatManager.getInstance().quitSDk();
                    finish();
                }).show("退出登录", "你确定要退出当前登录吗？", "取消", "退出", false);
                break;
            case R.id.ll_version:
                if(isNew){
                    showToast("您已经是最新版");
                }else{
                    sendRequest(System.currentTimeMillis()+"",url,content);
                }
                break;
        }
    }

    private void getVersion(){
        ApiUtil.getApiService().versioninfoedition("androidCustomMade", MyApplication.getVersionCode()+"").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if(entity.getData()!=null){
                            if(Integer.valueOf(entity.getData().getVerCode())<=Integer.valueOf(MyApplication.getVersionCode())){
                                //不更新
                                isNew = true;
                            }else{
                                isNew = false;
                                tvPoint.setVisibility(View.VISIBLE);
                                content = entity.getData().getContent();
                                url = entity.getData().getFileUrl();
                            }
                        }else{
                            //showToast("您已经是最新版");
                        }
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

    /**
     * 发送请求获取更新信息
     */
    private void sendRequest(String name,String url,String content) {

        builder = AllenVersionChecker.getInstance().downloadOnly(crateUIData(url,content));
        //更新提示框样式
        builder.setCustomVersionDialogListener(createCustomDialogOne());
        builder.setOnCancelListener(() -> Toast.makeText(SettingActivity.this, "更新已取消", Toast.LENGTH_SHORT).show());
        //进度条样式
        builder.setCustomDownloadingDialogListener(createCustomDownloadingDialog());
        //下载失败样式
        builder.setCustomDownloadFailedListener(createCustomDownloadFailedDialog());
        //自定义下载路径
        builder.setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/JYK/Version/");
        builder.setApkName("jyk"+name);
        builder.executeMission(this);
    }

    /**
     * 自定义更新弹窗页面
     * 务必用库传回来的context 实例化你的dialog
     * 自定义的dialog UI参数展示，使用versionBundle
     *
     * @return
     */
    private CustomVersionDialogListener createCustomDialogOne() {
        return (context, versionBundle) -> {
            UpdataVersionDialog baseDialog = new UpdataVersionDialog(context, R.style.BaseDialog, R.layout.custom_dialog_one_layout);
            TextView textView = baseDialog.findViewById(R.id.tv_msg);
            textView.setText(versionBundle.getContent());
            return baseDialog;
        };
    }

    /**
     * 自定义更新弹窗页面（强制）
     * 务必用库传回来的context 实例化你的dialog
     * 自定义的dialog UI参数展示，使用versionBundle
     *
     * @return
     */
    private CustomVersionDialogListener createCustomDialogOne1() {
        return (context, versionBundle) -> {
            UpdataVersionDialog baseDialog = new UpdataVersionDialog(context, R.style.BaseDialog, R.layout.custom_dialog_one_layout1);
            TextView textView = baseDialog.findViewById(R.id.tv_msg);
            textView.setText(versionBundle.getContent());
            return baseDialog;
        };
    }

    /**
     * 自定义下载失败
     * 务必用库传回来的context 实例化你的dialog
     *
     * @return
     */
    private CustomDownloadFailedListener createCustomDownloadFailedDialog() {
        return (context, versionBundle) -> {
            UpdataVersionDialog baseDialog = new UpdataVersionDialog(context, R.style.BaseDialog, R.layout.custom_download_failed_dialog);
            return baseDialog;
        };
    }


    /**
     * 自定义下载中对话框，下载中会连续回调此方法 updateUI
     * 务必用库传回来的context 实例化你的dialog
     * @return
     */
    private CustomDownloadingDialogListener createCustomDownloadingDialog() {
        return new CustomDownloadingDialogListener() {
            @Override
            public Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle) {
                UpdataVersionDialog baseDialog = new UpdataVersionDialog(context, R.style.BaseDialog, R.layout.custom_download_layout);
                return baseDialog;
            }

            @Override
            public void updateUI(Dialog dialog, int progress, UIData versionBundle) {
                TextView tvProgress = dialog.findViewById(R.id.tv_progress);
                ProgressBar progressBar = dialog.findViewById(R.id.pb);
                progressBar.setProgress(progress);
                tvProgress.setText(getString(R.string.versionchecklib_progress, progress));
            }
        };
    }


    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    private UIData crateUIData(String url,String content) {
        UIData uiData = UIData.create();
        uiData.setTitle("版本更新");
        uiData.setDownloadUrl(url);
        uiData.setContent(content);
        return uiData;
    }


}
