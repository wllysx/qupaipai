package com.qupp.client.ui.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gcssloop.widget.RCImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.ScreenAdaptive;
import com.qupp.client.utils.glide.GlideRoundTransform1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分享二维码
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class ShardActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_saveview)
    LinearLayout llSaveview;
    EntityForSimple entityForSimple;
    @BindView(R.id.iv_logo)
    RCImageView ivLogo;
    @BindView(R.id.tv_goodsName)
    TextView tvGoodsName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.tv_code)
    ImageView tvCode;
    String from="0";
    String url="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shard);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(ShardActivity.this), 0, 0);
        setStateColor(false);
        entityForSimple = (EntityForSimple) getIntent().getSerializableExtra("entityForSimple");
        from = getIntent().getStringExtra("from");
        url = getIntent().getStringExtra("url");
        initView();

    }


    private void initView() {
        tvTitle.setText("分享");
        RequestOptions options1 = new RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
                .transform(new GlideRoundTransform1(6));
        Glide.with(this).load(entityForSimple.getLogo()).apply(options1).into(ivLogo);
        tvCode.setImageBitmap( generateBitmap(url, ScreenAdaptive.dp2px(ShardActivity.this,60),ScreenAdaptive.dp2px(ShardActivity.this,60)));
        tvGoodsName.setText(entityForSimple.getGoodsName());
        if(from.equals("0")){
            tvIntegral.setVisibility(View.VISIBLE);
            tvPrice.setText("￥"+entityForSimple.getStartPrice());
        }else {
            tvIntegral.setVisibility(View.VISIBLE);
            tvPrice.setText("￥"+entityForSimple.getStartPrice());
            if(entityForSimple.getPriceType().equals("1")){
                //积分
                tvIntegral.setVisibility(View.GONE);
                tvPrice.setText(entityForSimple.getIntegral().replace(".00","")+"积分");
            }else if(entityForSimple.getPriceType().equals("2")){
                tvIntegral.setVisibility(View.VISIBLE);
                tvPrice.setText("￥"+entityForSimple.getPrice().replace(".00",""));
                tvIntegral.setText(" +"+entityForSimple.getIntegral().replace(".00","")+"积分");
            }else{
                tvIntegral.setVisibility(View.GONE);
                tvPrice.setText("￥"+entityForSimple.getPrice().replace(".00",""));
            }

        }

    }

    /**
     *
     * @param activity
     * @param entityForSimple
     * @param from 0 拍卖 1积分商城
     */
    public static void startActivityInstance(Activity activity, EntityForSimple entityForSimple,String from,String url) {
        activity.startActivity(new Intent(activity, ShardActivity.class)
                .putExtra("entityForSimple", entityForSimple)
                .putExtra("from",from)
                .putExtra("url",url)
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


    @OnClick({R.id.back, R.id.tv_save})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_save:
                saveBmp2Gallery(createViewBitmap(llSaveview), "jykshard");
                break;
        }
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
     * @param QR_WIDTH 编码时指定图片的宽
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

}
