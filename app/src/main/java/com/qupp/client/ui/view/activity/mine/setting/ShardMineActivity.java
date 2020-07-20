package com.qupp.client.ui.view.activity.mine.setting;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcssloop.widget.RCImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.ui.view.activity.main.DefaultWeb;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.ShareUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 分享二维码(我的)
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class ShardMineActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_saveview)
    RelativeLayout llSaveview;
    @BindView(R.id.iv_code)
    ImageView ivCode;

    public static String url = "";
    public static String text = "每日红包抢不停，商城的商品任你换";
    public static String title = "快来趣拍拍抢红包啦";
    public EntityForSimple entityForSimple;
    @BindView(R.id.iv_photo)
    RCImageView ivPhoto;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shard_mine);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(ShardMineActivity.this), 0, 0);
        setStateColor(false);
        initView();
        if (MyApplication.getCode().equals("")) {
            getBlanceAndIntegralAndFans();
        } else {
            initData();
        }

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
                        initData();
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


    private void initView() {
        tvTitle.setText("分享好友");
        entityForSimple = Paper.book("jyk").read("userdata", new EntityForSimple());
        Glide.with(ShardMineActivity.this).load(entityForSimple.getAvatar()).apply(new RequestOptions().placeholder(R.mipmap.icon_tx_default).error(R.mipmap.icon_tx_default)).into(ivPhoto);
        tvCode.setText("邀请码："+entityForSimple.getInvitationCode());
        tvNickname.setText(entityForSimple.getNickname());

    }

    private void initData() {
        ApiUtil.getApiService().syskey("app_share_download").enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        url = entity.getData();
                        Log.d("shard", url + "/" + MyApplication.getCode() + "");
                        ivCode.setImageBitmap(generateBitmap(url + "/" + MyApplication.getCode(), ScreenAdaptive.dp2px(ShardMineActivity.this, 500), ScreenAdaptive.dp2px(ShardMineActivity.this, 500)));
                    } else {
                        Toast.makeText(ShardMineActivity.this, entity.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, ShardMineActivity.class)
        );
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


    @OnClick({R.id.back, R.id.tv_save, R.id.tv_wx, R.id.tv_friend, R.id.tv_guize})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_save:
                initPremissions();
                break;
            case R.id.tv_wx:
                Log.d("shard", url + "/" + MyApplication.getCode() + "");
                ShareUtils.shareWeb(ShardMineActivity.this, url + "/" + MyApplication.getCode(), title, text, "", R.mipmap.icon_logo1, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.tv_friend:
                Log.d("shard", url + "/" + MyApplication.getCode() + "");
                ShareUtils.shareWeb(ShardMineActivity.this, url + "/" + MyApplication.getCode(), title, text, "", R.mipmap.icon_logo1, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.tv_guize:
                geturl();
                break;
        }
    }


    private void geturl() {
        ApiUtil.getApiService().syskey("invite_config_h5").enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        String url = entity.getData();
                        DefaultWeb.startActivityInstance(ShardMineActivity.this, url,"邀请规则");
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



    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * @param bmp     获取的bitmap数据
     * @param picName 自定义的图片名
     */
    public void saveBmp2Gallery(Bitmap bmp, String picName) {
        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;

        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;
        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName + ".jpg");
            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //通知相册更新
        MediaStore.Images.Media.insertImage(getContentResolver(), bmp, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);
        showToast("已保存到相册");
    }


    /**
     * 用字符串生成二维码
     *
     * @param text
     * @param QR_WIDTH  编码时指定图片的宽
     * @param QR_HEIGHT 编码时指定图片的高
     * @return Bitmap 返回一张图片
     * @throw
     */
    public static Bitmap generateBitmap(String text, int QR_WIDTH, int QR_HEIGHT) {
        try {
            if (text == null || "".equals(text) || text.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 比特矩阵
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 比特矩阵转颜色数组
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;// 黑点
                    } else {
                        pixels[y * QR_WIDTH + x] = 0x00ffffff;// 透明点,白点为0xffffffff
                    }

                }
            }

            // 解析颜色数组,其他的java平台可以选择其他的API
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initPremissions() {
        //同时请求多个权限
        RxPermissions.getInstance(ShardMineActivity.this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)//多个权限用","隔开
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //当所有权限都允许之后，返回true
                        Log.i("permissions", "btn_more_sametime：" + aBoolean);
                        saveBmp2Gallery(createViewBitmap(llSaveview), "jykshardmy");
                    } else {
                        //只要有一个权限禁止，返回false，
                        Log.i("permissions", "btn_more_sametime：" + aBoolean);
                    }
                });
    }

}
