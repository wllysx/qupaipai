package com.qupp.client.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;


public class MyImageGetter implements Html.ImageGetter {

    private URLDrawable urlDrawable = null;
    private TextView textView;
    private Context context;
    private Bitmap bitmap;

    public MyImageGetter(Context context, TextView textView) {
        this.textView = textView;
        this.context = context;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable drawable = new URLDrawable();
        Glide.with(context)

                .asBitmap()
                .load(source)
                .apply(new RequestOptions() .skipMemoryCache(true).format(DecodeFormat.PREFER_RGB_565)) // 不使用内存缓存  )
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        float width = textView.getWidth();
                        //  if (resource.getWidth() > width) {
                        float scale = width / resource.getWidth();
                        int afterWidth = (int) (resource.getWidth() * scale);
                        int afterHeight = (int) (resource.getHeight() * scale);
                        drawable.setBounds(0, 0, afterWidth, afterHeight);
                        Log.d("config",resource.getConfig().toString());
                        bitmap = resizeBitmap(resource, afterWidth, afterHeight);
                        drawable.setBitmap(bitmap);
                       /* } else {
                            drawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                            drawable.setBitmap(resource);
                        }*/
                        // 这两行代码是让文本刷新一下 不会出现图文重叠
                        textView.invalidate();
                        textView.setText(textView.getText());
                    }
                });
        return drawable;
    }


    public class URLDrawable extends BitmapDrawable {
        public Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (bitmap != null) {
                try {
                    try {
                        canvas.drawBitmap(bitmap, 0, 0, getPaint());
                        //canvas.drawBitmap(bit565(bitmap), 0, 0, getPaint());
                    }catch (OutOfMemoryError e1){

                    }

                }catch (Exception e){

                }
            }
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

    }

    private Bitmap bit565(Bitmap bitmap){
        Bitmap returnBitmap =   bitmap.copy(Bitmap.Config.RGB_565,true);
        return returnBitmap;
    }


    private Bitmap compressQuality(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bytes = bos.toByteArray();
        Bitmap returnBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Log.d("bitmapwh",returnBitmap.getHeight()+","+returnBitmap.getWidth()+"原始");
        Log.d("bitmapwh",returnBitmap.getByteCount()+"原始");
        return returnBitmap;
    }



    private Bitmap compressMatrix(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.6f, 0.6f);
        Bitmap returnBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Log.d("bitmapwh",returnBitmap.getHeight()+","+returnBitmap.getWidth()+"原始");
        Log.d("bitmapwh",returnBitmap.getByteCount()+"原始");

        return returnBitmap;

    }

    public Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        try {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                float scaleWidth = ((float) w) / width;
                float scaleHeight = ((float) h) / height;

                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);

                return Bitmap.createBitmap(bitmap, 0, 0, width,
                        height, matrix, true);
            }catch (Exception e){
                return bitmap;
            }

        }catch (OutOfMemoryError e1){
            return bitmap;

        }

    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }


}
