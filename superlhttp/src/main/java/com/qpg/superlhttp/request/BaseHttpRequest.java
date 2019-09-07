package com.qpg.superlhttp.request;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.qpg.superlhttp.api.SuperService;
import com.qpg.superlhttp.calladapter.Resource;
import com.qpg.superlhttp.callback.BaseCallback;
import com.qpg.superlhttp.mode.CacheMode;
import com.qpg.superlhttp.mode.CacheResult;
import com.qpg.superlhttp.transform.ResultFunction;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * @Description: 通用的请求基类
 */
public abstract class BaseHttpRequest<R extends BaseHttpRequest> extends BaseRequest<R> {

    protected SuperService apiService;  //通用接口服务
    protected String suffixUrl = "";//链接后缀
    protected int retryDelayMillis;//请求失败重试间隔时间
    protected int retryCount;//重试次数
    protected boolean isLocalCache;//是否使用本地缓存
    protected CacheMode cacheMode;//本地缓存类型
    protected String cacheKey;//本地缓存Key
    protected long cacheTime;//本地缓存时间
    protected Map<String, String> params = new LinkedHashMap<>();//请求参数
    protected Function function;

    public BaseHttpRequest() {
    }

    public BaseHttpRequest(String suffixUrl) {
        if (!TextUtils.isEmpty(suffixUrl)) {
            this.suffixUrl = suffixUrl;
        }
    }

    public <T> LiveData<Resource<T>> request(@NonNull LifecycleOwner owner,Type type) {
        generateGlobalConfig();
        generateLocalConfig();
        return execute(owner,type);
    }

    public <T> LiveData<CacheResult<T>> cacheRequest(@NonNull LifecycleOwner owner, Type type) {
        generateGlobalConfig();
        generateLocalConfig();
        return cacheExecute(owner,type);
    }

    public <T> void request(@NonNull LifecycleOwner owner, BaseCallback<T> callback) {
        generateGlobalConfig();
        generateLocalConfig();
        execute(owner,callback);
    }

    @Override
    protected void generateLocalConfig() {
        super.generateLocalConfig();
        if (httpGlobalConfig.getGlobalParams() != null) {
            params.putAll(httpGlobalConfig.getGlobalParams());
        }
        if (retryCount <= 0) {
            retryCount = httpGlobalConfig.getRetryCount();
        }
        if (retryDelayMillis <= 0) {
            retryDelayMillis = httpGlobalConfig.getRetryDelayMillis();
        }
//        if (isLocalCache) {
//            if (cacheKey != null) {
//                SuperLHttp.getApiCacheBuilder().cacheKey(cacheKey);
//            } else {
//                SuperLHttp.getApiCacheBuilder().cacheKey(ApiHost.getHost());
//            }
//            if (cacheTime > 0) {
//                SuperLHttp.getApiCacheBuilder().cacheTime(cacheTime);
//            } else {
//                SuperLHttp.getApiCacheBuilder().cacheTime(SuperLVRConfig.CACHE_NEVER_EXPIRE);
//            }
//        }
//        if (baseUrl != null && isLocalCache && cacheKey == null) {
//            SuperLHttp.getApiCacheBuilder().cacheKey(baseUrl);
//        }
        apiService = retrofit.create(SuperService.class);
    }

    protected abstract <T> LiveData<Resource<T>> execute(@NonNull LifecycleOwner owner,Type type);

    protected abstract <T> LiveData<CacheResult<T>> cacheExecute(@NonNull LifecycleOwner owner, Type type);

    protected abstract <T> void execute(@NonNull LifecycleOwner owner, BaseCallback<T> callback);

    protected <T> Function<Resource<ResponseBody>, Resource<T>> norTransformer(final Type type) {
        return new ResultFunction<>(type);

    }

    /**
     * 添加自定义解析器
     * @param function
     * @return
     */
    public R addCustomParse(Function function) {
        if (function != null) {
            this.function=function;
        }
        return (R) this;
    }

    /**
     * 添加请求参数
     *
     * @param paramKey
     * @param paramValue
     * @return
     */
    public R addParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            this.params.put(paramKey, paramValue);
        }
        return (R) this;
    }

    /**
     * 添加请求参数
     *
     * @param params
     * @return
     */
    public R addParams(Map<String, String> params) {
        if (params != null) {
            this.params.putAll(params);
        }
        return (R) this;
    }

    /**
     * 移除请求参数
     *
     * @param paramKey
     * @return
     */
    public R removeParam(String paramKey) {
        if (paramKey != null) {
            this.params.remove(paramKey);
        }
        return (R) this;
    }

    /**
     * 设置请求参数
     *
     * @param params
     * @return
     */
    public R params(Map<String, String> params) {
        if (params != null) {
            this.params = params;
        }
        return (R) this;
    }

    /**
     * 设置请求失败重试间隔时间（毫秒）
     *
     * @param retryDelayMillis
     * @return
     */
    public R setRetryDelayMillis(int retryDelayMillis) {
        this.retryDelayMillis = retryDelayMillis;
        return (R) this;
    }

    /**
     * 设置请求失败重试次数
     *
     * @param retryCount
     * @return
     */
    public R setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return (R) this;
    }

    /**
     * 设置是否进行本地缓存
     *
     * @param isLocalCache
     * @return
     */
    public R setLocalCache(boolean isLocalCache) {
        this.isLocalCache = isLocalCache;
        return (R) this;
    }

    /**
     * 设置本地缓存类型
     *
     * @param cacheMode
     * @return
     */
    public R setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
        return (R) this;
    }

    /**
     * 设置本地缓存Key
     *
     * @param cacheKey
     * @return
     */
    public R setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
        return (R) this;
    }

    /**
     * 设置本地缓存时间(毫秒)，默认永久
     *
     * @param cacheTime
     * @return
     */
    public R setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
        return (R) this;
    }

    public String getSuffixUrl() {
        return suffixUrl;
    }

    public int getRetryDelayMillis() {
        return retryDelayMillis;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public boolean isLocalCache() {
        return isLocalCache;
    }

    public CacheMode getCacheMode() {
        return cacheMode;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public Map<String, String> getParams() {
        return params;
    }

}
