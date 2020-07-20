package com.qupp.client.network;


import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

public class SessionInterceptor implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return new Response.Builder()
                .body(newResponseBody(response))
                .headers(response.headers())
                .message(response.message())
                .code(response.code())
                .request(response.request())
                .protocol(response.protocol())
                .build();
    }
    private ResponseBody newResponseBody(final Response response)
    {
        return new ResponseBody()
        {
            @Override
            public MediaType contentType()
            {
                return response.body().contentType();
            }
            @Override
            public long contentLength()
            {
                return response.body().contentLength();
            }
            @Override
            public BufferedSource source()
            {
                try
                {
                    String result = response.body().string();
                   /* if(JSON.parseObject(result).getInteger("code") == 500)
                    {

                         //这里改变返回的数据，如果服务器返回的是一个HTML网页，那么移动端也能拿到一个Json数据，用于保证数据可解析不至于崩溃

                        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream("{code:500,success:false}".getBytes());
                        BufferedSource source = Okio.buffer(Okio.source(tInputStringStream));
                        return source;
                    }else
                    {*/
                    Log.d("result","返回数据："+result);
                        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(result.getBytes());
                        BufferedSource source = Okio.buffer(Okio.source(tInputStringStream));
                        return source;
                   // }


                } catch (IOException e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
}