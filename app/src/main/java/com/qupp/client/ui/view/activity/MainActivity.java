package com.qupp.client.ui.view.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadFailedListener;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.baidu.mobstat.StatService;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.login.YinsiWeb;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForAddress;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.network.updataapp.DownLoadApk;
import com.qupp.client.utils.MySharePerference;
import com.qupp.client.utils.adapter.SmallMiddleDialog;
import com.qupp.client.utils.dialog.MiddleDialogForAgreement;
import com.qupp.client.utils.dialog.MiddleUpdata;
import com.qupp.client.utils.dialog.UpdataVersionDialog;
import com.qupp.client.utils.event.ExitEvent;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;


import butterknife.ButterKnife;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.PreLoginListener;
import cn.jpush.android.api.JPushInterface;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



@SuppressLint("all")
public class MainActivity extends BaseActivity {

    public Intent intent;
    public Bundle bundle;

    private long exitTime = 0;

    private DownloadBuilder builder;

    private MiddleUpdata middleUpdata;
    String updataUrl = "";
    boolean isFirstOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        //sendRequest();
        initDialog();
        ininPush();
        StatService.start(this);
        isFirstOpen = true;

        if(!MySharePerference.getShardPerferience(this, "FIRSTtoAPP", "").equals("1")){
            new MiddleDialogForAgreement(MainActivity.this).setSureListener(() -> {
            }).setSeeListener(() -> {
                startActivity(new Intent(MainActivity.this, YinsiWeb.class));
            }).setCancelListener(() -> {
                new SmallMiddleDialog(MainActivity.this).show("您需要同意趣拍拍隐私政策，才能继续使用我们的产品及服务。","返回");
            }).setSureListener(() ->{
                MySharePerference.addSharePerference(MainActivity.this, "FIRSTtoAPP", "1");
                MyApplication.setAgreeStates("1");
                initPremissions();
            } ).show("温馨提示", "", "不同意", "同意并继续", false);
        }else{
            initPremissions();
        }
        preLogin();
    }

    private void preLogin() {
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);
        if (!verifyEnable) {
            return;
        }
        JVerificationInterface.preLogin(this, 5000, new PreLoginListener() {
            @Override
            public void onResult(final int code, final String content) {
            }
        });
    }

    public void initPremissions() {
        //同时请求多个权限
        RxPermissions.getInstance(MainActivity.this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CAMERA)//多个权限用","隔开
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //当所有权限都允许之后，返回true
                        Log.i("permissions", "btn_more_sametime：" + aBoolean);
                    } else {
                        //只要有一个权限禁止，返回false，
                        Log.i("permissions", "btn_more_sametime：" + aBoolean);
                    }
                });

    }

    private void ininPush() {
        JPushInterface.setAlias(this, ((int) System.currentTimeMillis()), MyApplication.getUserId());
        // JPushInterface.resumePush(this);
    }

    private void initDialog() {
        middleUpdata = new MiddleUpdata(MainActivity.this);
        middleUpdata.setSureListener(() -> {
            openBrower(updataUrl);
        });
    }

    private void getVersion() {
        ApiUtil.getApiService().versioninfoedition("androidCustomMade", MyApplication.getVersionCode() + "").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if (Integer.valueOf(entity.getData().getVerCode()) <= Integer.valueOf(MyApplication.getVersionCode())) {
                            //不更新
                        } else {
                            updataUrl = entity.getData().getFileUrl();
                            //更行
                            if (entity.getData().getIsForce().equals("1")) {
                                //强制更新
                                sendRequest1(System.currentTimeMillis() + "", updataUrl, entity.getData().getContent());
                            } else {
                                if(isFirstOpen) {
                                    if (entity.getData().getAlert().equals("1")) {
                                        if (entity.getData() != null) {
                                            //非强制更新
                                            sendRequest(System.currentTimeMillis() + "", updataUrl, entity.getData().getContent());
                                        }
                                    }
                                }

                            }
                        }
                        isFirstOpen = false;
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
     * 打开升级页面
     *
     * @param str
     */
    protected void openBrower(String str) {
        if (str != null && !str.equals("")) {
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse(str));
            it = Intent.createChooser(it, null);
            startActivity(it);
        }
    }

    private void updata(String url) {
        DownLoadApk.download(MainActivity.this, url, "趣拍拍", "jiayikou");
    }


    protected void onSaveInstanceState(Bundle outState) {
        // activity被回收的时候不保存fragment避免重复
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {// MENU键
            // 监控/拦截菜单键
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次返回键退出系统");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                EventBus.getDefault().post(new ExitEvent());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVersion();

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 发送请求获取更新信息
     */
    private void sendRequest(String name, String url, String content) {

        builder = AllenVersionChecker.getInstance().downloadOnly(crateUIData(url, content));
        //更新提示框样式
        builder.setCustomVersionDialogListener(createCustomDialogOne());
        builder.setOnCancelListener(() -> Toast.makeText(MainActivity.this, "更新已取消", Toast.LENGTH_SHORT).show());
        //进度条样式
        builder.setCustomDownloadingDialogListener(createCustomDownloadingDialog());
        //下载失败样式
        builder.setCustomDownloadFailedListener(createCustomDownloadFailedDialog());
        //自定义下载路径
        builder.setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/JYK/Version/");
        builder.setApkName("jyk" + name);
        builder.executeMission(this);
    }

    /**
     * 发送请求获取更新信息(强制)
     */
    private void sendRequest1(String name, String url, String content) {

        builder = AllenVersionChecker.getInstance().downloadOnly(crateUIData(url, content));
        //更新提示框样式
        builder.setCustomVersionDialogListener(createCustomDialogOne1());
        builder.setOnCancelListener(() ->{
            Toast.makeText(MainActivity.this, "更新已取消", Toast.LENGTH_SHORT).show();
            finish();
        } );
        //进度条样式
        builder.setCustomDownloadingDialogListener(createCustomDownloadingDialog());
        //下载失败样式
        builder.setCustomDownloadFailedListener(createCustomDownloadFailedDialog());
        //自定义下载路径
        builder.setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/JYK/Version/");
        builder.setApkName("jyk" + name);
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
     *
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
    private UIData crateUIData(String url, String content) {
        UIData uiData = UIData.create();
        uiData.setTitle("版本更新");
        uiData.setDownloadUrl(url);
        uiData.setContent(content);
        return uiData;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AllenVersionChecker.getInstance().cancelAllMission(this);
    }

    //緩存省市区数据
    private void initData() {
        ApiUtil.getApiService().allAddressList().enqueue(new Callback<MessageForAddress>() {
            @Override
            public void onResponse(Call<MessageForAddress> call, Response<MessageForAddress> response) {
                MessageForAddress entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        Paper.book("jyk").write("citydata", entity.getData());
                    } else {
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<MessageForAddress> call, Throwable t) {
            }
        });
    }


}
