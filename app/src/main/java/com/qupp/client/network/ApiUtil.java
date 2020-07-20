package com.qupp.client.network;


import android.util.Log;

import com.qupp.client.MyApplication;
import com.qupp.client.utils.MobileInfoUtil;
import com.qupp.client.utils.event.EventTokenPastdue;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * 数据请求控制工具类
 */


public class ApiUtil {
    private static final String HOST = MyApplication.baseurl;//换成你上传用的服务器地址
    private static Retrofit retrofit;
    private static final int DEFAULT_TIMEOUT = 10;//超时时长，单位：秒

    /**
     * 获取根服务地址
     */
    public static String getHOST() {
        return HOST;
    }

    /**
     * 初始化 Retrofit
     *
     * @return Retrofit实体
     */
    private static Retrofit getApiRetrofit() {
        if (retrofit == null) {

            //日志
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
            //Header拦截器
            okHttpBuilder.addInterceptor(chain -> {
                Request request = chain.request()
                        .newBuilder()
                        //.addHeader("Authorization", MyApplication.getToken())
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("appVersion", MyApplication.getVersion())
                        .addHeader("deviceType", android.os.Build.MODEL==null?"":android.os.Build.MODEL)
                        .addHeader("userAgent", android.os.Build.VERSION.RELEASE)
                        .addHeader("imeiSerial", MyApplication.getIMEI1()==null?"0000000000000000": MyApplication.getIMEI1())
                        .build();
                return chain.proceed(request);
            }).addInterceptor(loggingInterceptor).addInterceptor(chain -> {
                Request request = chain.request();
                Response response = chain.proceed(request);//执行此次请求
                if (response.code() == 401) {//返回为token失效
                    Request newRequest = request.newBuilder().build();//
                    Log.d("http401", response.body().string());
                    //退出重新登录
                    EventBus.getDefault().post(new EventTokenPastdue());
                    return chain.proceed(newRequest);
                }
                return response;
            });

            okHttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            retrofit = new Retrofit.Builder()
                    .client(okHttpBuilder.build())
                    .baseUrl(HOST)
                    //增加返回值为String的支持
                    .addConverterFactory(ScalarsConverterFactory.create())
                    //增加返回值为Gson的支持(以实体类返回)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * 创建数据请求服务
     *
     * @return ApiService实体
     */
    public static ApiService getApiService() {
        return ApiUtil.getApiRetrofit().create(ApiService.class);
    }

    /**
     * 上传文件数据处理
     *
     * @param filePaths
     * @return 文件的键值对MAP
     */
    public static Map<String, RequestBody> upload(String... filePaths) {
        int i = 0;
        Map<String, RequestBody> bodyMap = new HashMap<>();
        for (String filePath : filePaths) {
            i++;
            File file = new File(filePath);//filePath 图片地址
            //RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            RequestBody imageBody = RequestBody.create(null, file);
            bodyMap.put("multipartFileList\";filename=\"" + file.getName(), imageBody);
        }
        return bodyMap;
    }

    public static RequestBody getData(HashMap map) {
        map.put("clientType", "2");
        map.put("channel", "test");
        map.put("imei", MobileInfoUtil.getIMEI(MyApplication.applicationContext));
        map.put("AppVersion", MyApplication.getVersion());
        map.put("DeviceModel", android.os.Build.MODEL);
        map.put("OSVersion", android.os.Build.VERSION.RELEASE);


        JSONObject jasonObject = new JSONObject(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jasonObject.toString());
        return body;
    }

}  