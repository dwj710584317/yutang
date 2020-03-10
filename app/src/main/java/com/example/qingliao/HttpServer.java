package com.example.qingliao;

import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpServer {

    public void sendJson(String json){
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8")
                , json);
        Log.d("sendjson",json);

        Request request = new Request.Builder()
//                .url("http://smallsky.mynatapp.cc/Aip/AppAip/getInform")//请求的url

                .url("http://122.51.104.232/Aip/AppAip/getInform")//请求的url
                .post(requestBody)
                .build();

        //创建/Call
        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("httpserver","连接失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("httpserver",response.body().string());
            }
        });
    }


}
