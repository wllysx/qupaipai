package com.qupp.client.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 数据解析的示例，数据来自于R.raw.his_data的json
 * Created by guoziwei on 2017/11/23.
 */

public class Util {

    private static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());


    /**
     *
     * @param type 0天 1周 2月 3三月 4一年 5今年 6全部
     * @return
     */
    public static String getStartTime(int type){
        String startTime;
        Date d = new Date();
        int i= 0;
        switch (type){
            case 0:
                i = 1;
                break;
            case 1:
                i = 7;
                break;
            case 2:
                i = 30;
                break;
            case 3:
                i = 90;
                break;
            case 4:
                i = 365;
                break;
        }

        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)-i);
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        startTime = formatter.format(now.getTime());
        if(type==5){
            startTime = startTime.substring(0,4)+"-01-01 00:00:00";
        }else if(type==6){
            startTime = "2017-01-01 00:00:00";
        }else{

        }

        return startTime;
    }

    public static String getEndtime(){
        Long a=new Long(System.currentTimeMillis());
        return stampToDate(a.toString());
    }



    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;

    }

    /**
     * 压缩图片
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1024) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Bitmap getBitmap(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 将Bitmap转换成文件
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm, String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path , fileName + ".jpg");
        try {
            if (myCaptureFile.exists()) {
                myCaptureFile.delete();
            }
            myCaptureFile.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }

    //图片压缩
    public Bitmap setImage(String path, Intent intent) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

//        double value = new BigDecimal(options.outWidth).divide(new BigDecimal(options.outHeight), 4, BigDecimal.ROUND_HALF_UP).doubleValue();

//        intent.putExtra(Constants.ISNULL, false);
//        intent.putExtra(Constants.IMGPATH, path);
//        intent.putExtra(Constants.ASPECT_RATIO, value == 0d ? "1.0000" : format.format(value));

        options.inJustDecodeBounds = false;

        int be = Math.max(options.outWidth, options.outHeight) / 20;
        if (be % 10 != 0)
            be += 10;
        be = be / 10;
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap compBitmap(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 解决Double变为科学计数法
     * @param str
     * @return
     */
    public static String parseString(String str){
        str = str.replace("¥","").replace("$","").replaceAll(",","");
        try {
            Double dValue = Double.parseDouble(str);
            return parseDouble(dValue);
        }catch (Exception e){
            return "";
        }

    }

    public static String parseDouble(Double bb){
        //1:数字用BigDecimal表示，然后在输出string
        DecimalFormat df = new DecimalFormat("###############.####################");// 最多保留几位小数，就用几个#，最少位就用0来确定
        String s = df.format(bb);
        return s;
    }

    public static String parseDouble1(Double bb){
        //1:数字用BigDecimal表示，然后在输出string
        DecimalFormat df = new DecimalFormat("###############.##########");// 最多保留几位小数，就用几个#，最少位就用0来确定
        String s = df.format(bb);
        return s;
    }

    public static String parseDoubleTwoPoint(Double bb){
        //1:数字用BigDecimal表示，然后在输出string
        DecimalFormat df = new DecimalFormat("###############.##");// 最多保留几位小数，就用几个#，最少位就用0来确定
        String s = df.format(bb);
        return s;
    }

    public static String parseDoubleFourPoint(Double bb){
        //1:数字用BigDecimal表示，然后在输出string
        DecimalFormat df = new DecimalFormat("###############.####");// 最多保留几位小数，就用几个#，最少位就用0来确定
        String s = df.format(bb);
        return s;
    }

    public static String parseDoubleSixPoint(Double bb){
        //1:数字用BigDecimal表示，然后在输出string
        DecimalFormat df = new DecimalFormat("###############.######");// 最多保留几位小数，就用几个#，最少位就用0来确定
        String s = df.format(bb);
        return s;
    }

    public static String parseDoubleTenPoint(Double bb){
        //1:数字用BigDecimal表示，然后在输出string
        DecimalFormat df = new DecimalFormat("###############.##########");// 最多保留几位小数，就用几个#，最少位就用0来确定
        String s = df.format(bb);
        return s;
    }

    public static String parseDoubleFourPoint(float bb){
        //1:数字用BigDecimal表示，然后在输出string
        DecimalFormat df = new DecimalFormat("###############.####");// 最多保留几位小数，就用几个#，最少位就用0来确定
        String s = df.format(bb);
        return s;
    }

    public static String getWsRequest(Object obj) {
        String requestParams = "";
        if (null == obj){
            return requestParams;
        }
        try {
            Gson gson = new Gson();
            requestParams = gson.toJson(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 生成二维码
     * @param string
     * @param format
     * @return
     * @throws WriterException
     */
    public static Bitmap createCode(String string, BarcodeFormat format)
            throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = writer.encode(string, format, 400, 400, hst);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 边字符串颜色
     * @param color
     * @param text
     * @param keyword
     * @return
     */
    public static SpannableString changeTvColor(int color, String text, String keyword) {
//        Pattern pattern = Pattern.compile(key);
//        Matcher matcher = pattern.matcher(string);
//        SpannableString ss = new SpannableString(text);
//        while (matcher.find()) {
//            int start = matcher.start();
//            int end = matcher.end();
//            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        return null;
    }

    /**
     * 获取字符串数字跟+
     * @param activeDetail
     * @return
     */
    public static String getNumberText(String activeDetail) {
        if (TextUtils.isEmpty(activeDetail)){
            return "";
        }
        String numStr = "0123456789+";
        List<String> digitList = new ArrayList<>();
        for (int i = 0; i < activeDetail.length(); i++) {
            if (numStr.contains(activeDetail.substring(i, i+1))){
                digitList.add(activeDetail.substring(i, i+1));
            }
        }
        if (digitList.size() > 1){
            for (int i = 0; i < digitList.size(); i++) {
                if (digitList.get(i).equals(digitList.get(i+1))){
                    digitList.remove(i+1);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String str : digitList) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String a="每日分享币种行情+10，最多+30";
        List<String> digitList = new ArrayList<String>();
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(a);
        String result = m.replaceAll("");
        for (int i = 0; i < result.length(); i++) {
            digitList.add(result.substring(i, i+1));
        }
        System.out.println(digitList);

    }

    public static String getMD5(String string) throws NoSuchAlgorithmException {
//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        md5.update(val.getBytes());
//        byte[] m = md5.digest();//加密
//        return getString(m);

        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }
    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }

    public static Bitmap getBitmapFromView(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }


    public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName + ".png");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    /***
     * 图片压缩方法二
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 滑动到头部
     * @param view
     * @param x
     * @param y
     */
    public static void scrollToPosition(View view, int x, int y) {
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(view, "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(view, "scrollY", y);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(500L);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }


    public static int getStateBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Bitmap setBmpSize(Bitmap bm, int newWidth , int newHeight) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    /**
     * 获取底部虚拟键盘的高度
     */
    public static int getBottomKeyboardHeight(Activity context){
        int screenHeight =  getAccurateScreenDpi(context)[1];
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return screenHeight - dm.heightPixels;
    }

    /**
     * 获取精确的屏幕大小
     */
    private static int[] getAccurateScreenDpi(Activity context) {
        int[] screenWH = new int[2];
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class<?> c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            screenWH[0] = dm.widthPixels;
            screenWH[1] = dm.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
            screenWH[0] = 0;
            screenWH[1] = 0;
        }
        return screenWH;

    }
}
