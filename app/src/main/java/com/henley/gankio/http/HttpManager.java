package com.henley.gankio.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henley.gankio.BuildConfig;
import com.henley.gankio.gank.GankApiService;
import com.henley.gankio.gank.GankConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Http管理器
 *
 * @author Henley
 * @date 2018/7/3 15:58
 */
public class HttpManager {

    private Gson gson;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private GankApiService gankApiService;

    private static class SingletonHolder {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    public static HttpManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private HttpManager() {
        initGson();
        initOkHttpClient();
        initRetrofit();
        initApiService();
    }

    /**
     * 初始化{@link Gson}
     */
    private void initGson() {
        gson = new GsonBuilder()
                .setDateFormat(GankConfig.GANK_DATA_FORMAT)
                .create();
    }


    /**
     * 初始化{@link OkHttpClient}
     */
    private void initOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(GankConfig.HTTP_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(GankConfig.HTTP_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(GankConfig.HTTP_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new NetworkInterceptor())
                .build();
    }

    /**
     * 初始化{@link Retrofit}
     */
    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(GankConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    /**
     * 初始化{@link GankApiService}
     */
    private void initApiService() {
        gankApiService = retrofit.create(GankApiService.class);
    }

    public Gson getGson() {
        return gson;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public GankApiService getGankApiService() {
        return gankApiService;
    }

}
