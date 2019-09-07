package com.qpg.superlhttp;

import android.app.Application;
import android.content.Context;

import com.qpg.superlhttp.config.HttpGlobalConfig;
import com.qpg.superlhttp.request.BaseHttpRequest;
import com.qpg.superlhttp.request.GetRequest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author qpg
 * @date 2019/09/06 0003
 * @description: 网络请求入口
 */
public class SuperLHttp {
    private static Context mContext;
    private OkHttpClient.Builder okHttpBuilder;
    private Retrofit.Builder retrofitBuilder;
   // private ApiCache.Builder apiCacheBuilder;
    private OkHttpClient okHttpClient;
 //   private ApiCache apiCache;
   //全局配置
    private static final HttpGlobalConfig NET_GLOBAL_CONFIG = HttpGlobalConfig.getInstance();

    private SuperLHttp() {
        okHttpBuilder = new OkHttpClient.Builder();
        retrofitBuilder = new Retrofit.Builder();
        if(mContext!=null){
          //  apiCacheBuilder = new ApiCache.Builder(mContext);
        }
    }

    private static class SuperLVRHttpHolder {
        private static final SuperLHttp INSTANCE = new SuperLHttp();
    }

    public static final SuperLHttp getInstance() {
        return SuperLVRHttpHolder.INSTANCE;
    }

    public static void init(Application app) {
        mContext = app;
    }

    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        testInitialize();
        return mContext;
    }

    public static HttpGlobalConfig config() {
        return NET_GLOBAL_CONFIG;
    }

    private static void testInitialize() {
        if (mContext == null) {
            throw new ExceptionInInitializerError("Please call SuperLHttp.init(this) in Application to initialize!");
        }
    }

    public static OkHttpClient.Builder getOkHttpBuilder() {
        if (getInstance().okHttpBuilder == null) {
            throw new IllegalStateException("Please call SuperLHttp.init(this) in Application to initialize!");
        }
        return getInstance().okHttpBuilder;
    }

    public static Retrofit.Builder getRetrofitBuilder() {
        if (getInstance().retrofitBuilder == null) {
            throw new IllegalStateException("Please call SuperLHttp.init(this) in Application to initialize!");
        }
        return getInstance().retrofitBuilder;
    }
//
//    public static ApiCache.Builder getApiCacheBuilder() {
//        if (getInstance().apiCacheBuilder == null) {
//            throw new IllegalStateException("Please call SuperLHttp.init(this) in Application to initialize!");
//        }
//        return getInstance().apiCacheBuilder;
//    }

    public static OkHttpClient getOkHttpClient() {
        if (getInstance().okHttpClient == null) {
            getInstance().okHttpClient = getOkHttpBuilder().build();
        }
        return getInstance().okHttpClient;
    }

//    public static ApiCache getApiCache() {
//        if (getInstance().apiCache == null || getInstance().apiCache.isClosed()) {
//            getInstance().apiCache = getApiCacheBuilder().build();
//        }
//        return getInstance().apiCache;
//    }

    /**
     * 通用请求，可传入自定义请求
     * @param request
     * @return
     */
    public static BaseHttpRequest customRequest(BaseHttpRequest request) {
        if (request != null) {
            return request;
        } else {
            return new GetRequest("");
        }
    }



    /**
     * get请求
     * @param suffixUrl
     * @return
     */
    public static GetRequest get(String suffixUrl) {
        return new GetRequest(suffixUrl);
    }

    /**
     * 可传入自定义Retrofit接口服务的请求类型
     * @return
     */
   /* public static <T> RetrofitRequest retrofit() {
        return new RetrofitRequest();
    }

    *//**
     * post请求
     * @param suffixUrl
     * @return
     *//*
    public static PostRequest post(String suffixUrl) {
        return new PostRequest(suffixUrl);
    }

    *//**
     * head请求
     * @param suffixUrl
     * @return
     *//*
    public static HeadRequest head(String suffixUrl) {
        return new HeadRequest(suffixUrl);
    }

    *//**
     * put请求
     * @param suffixUrl
     * @return
     *//*
    public static PutRequest put(String suffixUrl) {
        return new PutRequest(suffixUrl);
    }

    *//**
     * patch请求
     * @param suffixUrl
     * @return
     *//*
    public static PatchRequest patch(String suffixUrl) {
        return new PatchRequest(suffixUrl);
    }

    *//**
     * options请求
     * @param suffixUrl
     * @return
     *//*
    public static OptionsRequest options(String suffixUrl) {
        return new OptionsRequest(suffixUrl);
    }

    *//**
     * delete请求
     * @param suffixUrl
     * @return
     *//*
    public static DeleteRequest delete(String suffixUrl) {
        return new DeleteRequest(suffixUrl);
    }

    *//**
     * 上传
     * @param suffixUrl
     * @return
     *//*
    public static UploadRequest upload(String suffixUrl) {
        return new UploadRequest(suffixUrl);
    }

    *//**
     * 上传（包含上传进度回调）
     * @param suffixUrl
     * @return
     *//*
    public static UploadRequest upload(String suffixUrl, UCallback uCallback) {
        return new UploadRequest(suffixUrl, uCallback);
    }

    *//**
     * 下载（回调DownProgress）
     * @param suffixUrl
     * @return
     *//*
    public static DownloadRequest download(String suffixUrl) {
        return new DownloadRequest(suffixUrl);
    }

    *//**
     * 添加请求订阅者
     * @param tag
     * @param disposable
     *//*
    public static void addDisposable(Object tag, Disposable disposable) {
        ApiManager.get().add(tag, disposable);
    }

    *//**
     * 根据Tag取消请求
     *//*
    public static void cancelTag(Object tag) {
        ApiManager.get().cancel(tag);
    }

    *//**
     * 取消所有请求请求
     *//*
    public static void cancelAll() {
        ApiManager.get().cancelAll();
    }

    *//**
     * 清除对应Key的缓存
     * @param key
     *//*
    public static void removeCache(String key) {
        getApiCache().remove(key);
    }

    *//**
     * 清除所有缓存并关闭缓存
     * @return
     *//*
    public static Disposable clearCache() {
        return getApiCache().clear();
    }
*/
}
