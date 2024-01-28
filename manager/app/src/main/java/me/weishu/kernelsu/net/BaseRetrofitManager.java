package me.weishu.kernelsu.net;


import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import me.weishu.kernelsu.net.interceptor.HttpUrlInterceptor;
import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author: ji xin
 * @date : 2020/7/1下午12:32
 * @desc :
 */
public class BaseRetrofitManager {

    public static final String BASE_URL = "http://123.206.213.34:9999";
    //超时时间 60s
    private static final int CONNECT_TIME_OUT = 60;

    //设置缓存的大小50M
    private static final int CACHE_MAX = 1024 * 1024 * 50;
    private Retrofit retrofit;

    private OkHttpClient getOkHttpClient() {

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//
//        File cacheFile = new File(BaseApplication.getContext().getCacheDir(), "cache");
//        Cache cache = new Cache(cacheFile, CACHE_MAX);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
//                .addInterceptor(interceptor)
                .addInterceptor(new HttpUrlInterceptor())
//                .addNetworkInterceptor(interceptor)
//                .cache(cache)
                .build();
        return okHttpClient;
    }


    protected Retrofit getRetrofit() {
        Gson gson = GsonUtils.createGson(true).setLenient().create();
        retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())

                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        return retrofit;
    }

}

