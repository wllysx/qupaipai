package com.qupp.client.ui.view.activity.mine.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForListString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.dialog.DialogPhoto;
import com.qupp.client.utils.dialog.MiddleDialog;
import com.qupp.client.utils.event.RefreshUserInfo;
import com.qupp.client.utils.secretUtils.JwtUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的-设置-我的资料
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class MyDataActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.iv_coderight)
    ImageView ivCoderight;


    private int SYS_INTENT_REQUEST = 0XFF01, CAMERA_INTENT_REQUEST = 0XFF02, IMAGE_CUT = 1;
    public String photoPath, filename = "";
    private Uri photoUri, imageUri;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_mydata);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(MyDataActivity.this), 0, 0);
        setStateColor(false);
        initView();
        getBlanceAndIntegralAndFans();
        initData();
    }


    private void initView() {
        tvTitle.setText("我的资料");
        EntityForSimple entityForSimple = Paper.book("jyk").read("userdata", new EntityForSimple());
        Glide.with(MyDataActivity.this).load(entityForSimple.getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into(ivPhoto);
        tvNick.setText(entityForSimple.getNickname());
    }

    private void getBlanceAndIntegralAndFans() {
        ApiUtil.getApiService().balanceAndIntegralAndFans(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvNick.setText(entity.getData().getNickname());
                    } else {
                        //Toast.makeText(getContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }



    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, MyDataActivity.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
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


    @OnClick({R.id.back, R.id.ll_invite,R.id.tv_submit})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_photo:
                new DialogPhoto(this).setSureListener(item -> {
                    switch (item) {
                        case 1:
                            cameraPhoto();
                            break;
                        case 2:
                            systemPhoto();
                            break;
                        default:
                            break;
                    }
                }).show();
                break;
            case R.id.ll_nick:
                ChangeNickActivity.startActivityInstance(this, tvNick.getText().toString());
                break;
            case R.id.ll_invite:
                if(tvCode.getText().toString().equals("无")) {
                    InviteActivity.startActivityInstance(this);
                }
                break;
            case R.id.tv_submit:
                new MiddleDialog(MyDataActivity.this).setSureListener(() -> {
                    weiXinLogin();
                }).show("温馨提示", "是否同步微信头像和昵称？", "取消", "确认同步", false);

                break;
        }
    }

    /**
     * 打开系统相册
     */
    private void systemPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SYS_INTENT_REQUEST);
    }

    /**
     * 调用相机拍照
     */
    @SuppressLint("all")
    private void cameraPhoto() {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            showToast("SD卡不可用");
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        filename = format.format(date);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path, filename + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //imageUri = Uri.fromFile(outputImage);
        imageUri = getUriForFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        try {
            startActivityForResult(intent, CAMERA_INTENT_REQUEST);
        } catch (Exception e) {

        }
    }

    private Uri getUriForFile(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(getApplicationContext(), "com.jyk.client.fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == SYS_INTENT_REQUEST
                && resultCode == RESULT_OK && data != null) {
            cameraCamera(data);
        } else if (requestCode == CAMERA_INTENT_REQUEST
                && resultCode == RESULT_OK) {
            cameraCamera();
        } else if (requestCode == IMAGE_CUT
                && resultCode == RESULT_OK) {
            bmp = data.getParcelableExtra("data");
            if (bmp == null) {
                if (photoUri != null) {
                    try {
                        bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            photoPath = photoUri.getEncodedPath();
            ApiUtil.getApiService().uploadMobileFileList(MyApplication.getToken(),ApiUtil.upload(photoPath)).enqueue(new Callback<MessageForListString>() {
                @Override
                public void onResponse(Call<MessageForListString> call, Response<MessageForListString> response) {
                    try {
                        MessageForListString entity = response.body();
                        if (entity.getCode().equals("0")) {
                            if(entity.getData().size()>0) {
                                ivPhoto.setImageBitmap(bmp);
                                initData(entity.getData().get(0));
                            }
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MessageForListString> call, Throwable t) {
                }
            });

            // upload(EncodeUtil.bitmapToBase64(bmp),bmp);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    /**
     * 拍照后获取照片
     */
    private void cameraCamera() {
        Uri uri = null;
        if (imageUri != null) {
            uri = imageUri;
        }
        Intent intent = getImageClipIntent(uri);
        //广播刷新相册
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(imageUri);
        sendBroadcast(intentBc);
        startActivityForResult(intent, IMAGE_CUT);
    }

    /**
     * @param data 相册选择后后获取照片
     */
    private void cameraCamera(Intent data) {
        Uri uri = data.getData();
        // 相册中文件绝对路径
        Intent intent = getImageClipIntent(uri);
        startActivityForResult(intent, IMAGE_CUT);
    }

    /**
     * 获取剪切后的图片
     */
    public Intent getImageClipIntent(Uri uri) {
        //图片名称 时间命名
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path, "1.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将File对象转换为Uri并启动照相程序
        photoUri = Uri.fromFile(outputImage);

        Intent intent = new Intent("com.android.camera.action.CROP", null);
        intent.setDataAndType(uri, "image/*");
        //需要加上这两句话 ： uri 权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("scale", true);
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 105);// 裁剪框比例
        intent.putExtra("aspectY", 105);
        intent.putExtra("outputX", 200);// 输出图片大小
        intent.putExtra("outputY", 200);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("return-data", false);
        return intent;
    }

    /**
     * 获取上级信息
     */
    private void initData() {
        ApiUtil.getApiService().superiorInfo(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        //有上级
                        tvCode.setText(entity.getData().getInvitationCode());
                        ivCoderight.setVisibility(View.INVISIBLE);
                    } else {
                        //无上级
                        ivCoderight.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    private void upload(String base64,Bitmap bmp) {

        ApiUtil.getApiService().upload(MyApplication.getToken(),"data:image/jpg;base64"+base64).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        ivPhoto.setImageBitmap(bmp);
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

    private void initData(String url) {
        ApiUtil.getApiService().memberupdateAvatar(MyApplication.getToken(),url,null).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        MyApplication.setToken(response.headers().get("Authorization"));
                        JwtUtils.setUserId(response.headers().get("Authorization"));
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

    public void weiXinLogin() {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        UMShareAPI.get(MyDataActivity.this).getPlatformInfo(MyDataActivity.this, SHARE_MEDIA.WEIXIN, authListener);
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

            initData(iconurl,name);
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



    private void initData(String url,String nickname) {
        ApiUtil.getApiService().memberupdateAvatar(MyApplication.getToken(),url,nickname).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        tvNick.setText(nickname);
                        Glide.with(MyDataActivity.this).load(url).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into(ivPhoto);
                        MyApplication.setToken(response.headers().get("Authorization"));
                        JwtUtils.setUserId(response.headers().get("Authorization"));
                        showToast("同步成功");
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
    public void exit(RefreshUserInfo event) {
        if (event != null) {
            initData();
            getBlanceAndIntegralAndFans();
        }
    }


}
