package com.qpg.superlhttp.request;

import com.qpg.superlhttp.SuperLHttp;
import com.qpg.superlhttp.calladapter.LiveDataCallAdapterFactory;
import com.qpg.superlhttp.callback.UCallback;
import com.qpg.superlhttp.config.HttpGlobalConfig;
import com.qpg.superlhttp.config.SuperLVRConfig;
import com.qpg.superlhttp.cookie.CookieJarImpl;
import com.qpg.superlhttp.cookie.store.SPCookieStore;
import com.qpg.superlhttp.interceptor.HeadersInterceptor;
import com.qpg.superlhttp.mode.ApiHost;
import com.qpg.superlhttp.mode.HttpHeaders;
import com.qpg.superlhttp.utils.HttpsUtils;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @Description: 请求基类
 */
public abstract class BaseRequest<R extends BaseRequest> {
    protected HttpGlobalConfig httpGlobalConfig;//全局配置
    protected Retrofit retrofit;//Retrofit对象
    protected List<Interceptor> interceptors = new ArrayList<>();//局部请求的拦截器
    protected List<Interceptor> networkInterceptors = new ArrayList<>();//局部请求的网络拦截器
    protected HttpHeaders headers = new HttpHeaders();//请求头
    protected String baseUrl;//基础域名
    protected Object tag;//请求标签
    protected long readTimeOut;//读取超时时间
    protected long writeTimeOut;//写入超时时间
    protected long connectTimeOut;//连接超时时间
    protected boolean isHttpCache;//是否使用Http缓存
    protected UCallback uploadCallback;//上传进度回调

    /**
     * 设置基础域名，当前请求会替换全局域名
     *
     * @param baseUrl
     * @return
     */
    public R baseUrl(String baseUrl) {
        if (baseUrl != null) {
            this.baseUrl = baseUrl;
        }
        return (R) this;
    }

    /**
     * 添加请求头
     *
     * @param headerKey
     * @param headerValue
     * @return
     */
    public R addHeader(String headerKey, String headerValue) {
        this.headers.put(headerKey, headerValue);
        return (R) this;
    }

    /**
     * 添加请求头
     *
     * @param headers
     * @return
     */
    public R addHeaders(Map<String, String> headers) {
        this.headers.put(headers);
        return (R) this;
    }

    /**
     * 移除请求头
     *
     * @param headerKey
     * @return
     */
    public R removeHeader(String headerKey) {
        this.headers.remove(headerKey);
        return (R) this;
    }

    /**
     * 设置请求头
     *
     * @param headers
     * @return
     */
    public R setHeaders(HttpHeaders headers) {
        if (headers != null) {
            this.headers = headers;
        }
        return (R) this;
    }

    /**
     * 设置请求标签
     *
     * @param tag
     * @return
     */
    public R tag(Object tag) {
        this.tag = tag;
        return (R) this;
    }

    /**
     * 设置连接超时时间（秒）
     *
     * @param connectTimeOut
     * @return
     */
    public R setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return (R) this;
    }

    /**
     * 设置读取超时时间（秒）
     *
     * @param readTimeOut
     * @return
     */
    public R setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return (R) this;
    }

    /**
     * 设置写入超时时间（秒）
     *
     * @param writeTimeOut
     * @return
     */
    public R setWriteTimeOut(int writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return (R) this;
    }

    /**
     * 设置是否进行HTTP缓存
     *
     * @param isHttpCache
     * @return
     */
    public R setHttpCache(boolean isHttpCache) {
        this.isHttpCache = isHttpCache;
        return (R) this;
    }

    /**
     * 局部设置拦截器
     *
     * @param interceptor
     * @return
     */
    public R interceptor(Interceptor interceptor) {
        if (interceptor != null) {
            interceptors.add(interceptor);
        }
        return (R) this;
    }

    /**
     * 局部设置网络拦截器
     *
     * @param interceptor
     * @return
     */
    public R networkInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            networkInterceptors.add(interceptor);
        }
        return (R) this;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public long getReadTimeOut() {
        return readTimeOut;
    }

    public long getWriteTimeOut() {
        return writeTimeOut;
    }

    public long getConnectTimeOut() {
        return connectTimeOut;
    }

    public boolean isHttpCache() {
        return isHttpCache;
    }

    /**
     * 生成局部配置
     */
    protected void generateLocalConfig() {
        OkHttpClient.Builder newBuilder = SuperLHttp.getOkHttpClient().newBuilder();

        if (httpGlobalConfig.getGlobalHeaders() != null) {
            headers.put(httpGlobalConfig.getGlobalHeaders());
        }

        if (!interceptors.isEmpty()) {
            for (Interceptor interceptor : interceptors) {
                newBuilder.addInterceptor(interceptor);
            }
        }

        if (!networkInterceptors.isEmpty()) {
            for (Interceptor interceptor : networkInterceptors) {
                newBuilder.addNetworkInterceptor(interceptor);
            }
        }

        if (headers.headersMap.size() > 0) {
            newBuilder.addInterceptor(new HeadersInterceptor(headers.headersMap));
        }

        if (uploadCallback != null) {
         //--   newBuilder.addNetworkInterceptor(new UploadProgressInterceptor(uploadCallback));
        }

        if (readTimeOut > 0) {
            newBuilder.readTimeout(readTimeOut, TimeUnit.SECONDS);
        }

        if (writeTimeOut > 0) {
            newBuilder.readTimeout(writeTimeOut, TimeUnit.SECONDS);
        }

        if (connectTimeOut > 0) {
            newBuilder.readTimeout(connectTimeOut, TimeUnit.SECONDS);
        }

        if (isHttpCache) {
            try {
                if (httpGlobalConfig.getHttpCache() == null) {
                    httpGlobalConfig.httpCache(new Cache(httpGlobalConfig.getHttpCacheDirectory(), SuperLVRConfig.CACHE_MAX_SIZE));
                }
                httpGlobalConfig.cacheOnline(httpGlobalConfig.getHttpCache());
                httpGlobalConfig.cacheOffline(httpGlobalConfig.getHttpCache());
            } catch (Exception e) {

            }
            newBuilder.cache(httpGlobalConfig.getHttpCache());
        }

        if (baseUrl != null) {
            Retrofit.Builder newRetrofitBuilder = new Retrofit.Builder();
            newRetrofitBuilder.baseUrl(baseUrl);
            if (httpGlobalConfig.getConverterFactory() != null) {
                newRetrofitBuilder.addConverterFactory(httpGlobalConfig.getConverterFactory());
            }
            if (httpGlobalConfig.getCallAdapterFactory() != null) {
                newRetrofitBuilder.addCallAdapterFactory(httpGlobalConfig.getCallAdapterFactory());
            }
            if (httpGlobalConfig.getCallFactory() != null) {
                newRetrofitBuilder.callFactory(httpGlobalConfig.getCallFactory());
            }
            newBuilder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
            newRetrofitBuilder.client(newBuilder.build());
            retrofit = newRetrofitBuilder.build();
        } else {
            SuperLHttp.getRetrofitBuilder().client(newBuilder.build());
            retrofit = SuperLHttp.getRetrofitBuilder().build();
        }
    }

    /**
     * 生成全局配置
     */
    protected void generateGlobalConfig() {
        httpGlobalConfig = SuperLHttp.config();

        if (httpGlobalConfig.getBaseUrl() == null) {
            httpGlobalConfig.setBaseUrl(ApiHost.getHost());
        }
        SuperLHttp.getRetrofitBuilder().baseUrl(httpGlobalConfig.getBaseUrl());

        if (httpGlobalConfig.getConverterFactory() != null) {
            SuperLHttp.getRetrofitBuilder().addConverterFactory(httpGlobalConfig.getConverterFactory());
        }

        if (httpGlobalConfig.getCallAdapterFactory() == null) {
            httpGlobalConfig.callAdapterFactory(LiveDataCallAdapterFactory.create());
        }
        SuperLHttp.getRetrofitBuilder().addCallAdapterFactory(httpGlobalConfig.getCallAdapterFactory());

        if (httpGlobalConfig.getCallFactory() != null) {
            SuperLHttp.getRetrofitBuilder().callFactory(httpGlobalConfig.getCallFactory());
        }

        if (httpGlobalConfig.getHostnameVerifier() == null) {
            httpGlobalConfig.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        }
        SuperLHttp.getOkHttpBuilder().hostnameVerifier(httpGlobalConfig.getHostnameVerifier());

        if (httpGlobalConfig.getSslSocketFactory() == null||httpGlobalConfig.getX509TrustManager()==null) {
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
            httpGlobalConfig.setSSLSocketFactory(sslParams.sSLSocketFactory);
            httpGlobalConfig.setX509TrustManager(sslParams.trustManager);
        }
        SuperLHttp.getOkHttpBuilder().sslSocketFactory(httpGlobalConfig.getSslSocketFactory(),httpGlobalConfig.getX509TrustManager());

        if (httpGlobalConfig.getConnectionPool() == null) {
            httpGlobalConfig.connectionPool(new ConnectionPool(SuperLVRConfig.DEFAULT_MAX_IDLE_CONNECTIONS,
                    SuperLVRConfig.DEFAULT_KEEP_ALIVE_DURATION, TimeUnit.SECONDS));
        }
        SuperLHttp.getOkHttpBuilder().connectionPool(httpGlobalConfig.getConnectionPool());

        if (httpGlobalConfig.isCookie() && httpGlobalConfig.getApiCookie() == null) {
            httpGlobalConfig.setCookie(new CookieJarImpl(new SPCookieStore(SuperLHttp.getContext())));
        }
        if (httpGlobalConfig.isCookie()) {
            SuperLHttp.getOkHttpBuilder().cookieJar(httpGlobalConfig.getApiCookie());
        }

        if (httpGlobalConfig.getHttpCacheDirectory() == null) {
            httpGlobalConfig.setHttpCacheDirectory(new File(SuperLHttp.getContext().getCacheDir(), SuperLVRConfig.CACHE_HTTP_DIR));
        }
        if (httpGlobalConfig.isHttpCache()) {
            try {
                if (httpGlobalConfig.getHttpCache() == null) {
                    httpGlobalConfig.httpCache(new Cache(httpGlobalConfig.getHttpCacheDirectory(), SuperLVRConfig.CACHE_MAX_SIZE));
                }
                httpGlobalConfig.cacheOnline(httpGlobalConfig.getHttpCache());
                httpGlobalConfig.cacheOffline(httpGlobalConfig.getHttpCache());
            } catch (Exception e) {

            }
        }
        if (httpGlobalConfig.getHttpCache() != null) {
            SuperLHttp.getOkHttpBuilder().cache(httpGlobalConfig.getHttpCache());
        }
        SuperLHttp.getOkHttpBuilder().connectTimeout(SuperLVRConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        SuperLHttp.getOkHttpBuilder().writeTimeout(SuperLVRConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        SuperLHttp.getOkHttpBuilder().readTimeout(SuperLVRConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 获取第一级type
     *
     * @param t
     * @param <T>
     * @return
     */
    protected <T> Type getType(T t) {
        Type genType = t.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        Type finalNeedType;
        if (params.length > 1) {
            if (!(type instanceof ParameterizedType)) {
                throw new IllegalStateException("没有填写泛型参数");
            }
            finalNeedType = ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            finalNeedType = type;
        }
        return finalNeedType;
    }

    /**
     * 获取次一级type(如果有)
     *
     * @param t
     * @param <T>
     * @return
     */
    protected <T> Type getSubType(T t) {
        Type genType = t.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        Type finalNeedType;
        if (params.length > 1) {
            if (!(type instanceof ParameterizedType)) {
                throw new IllegalStateException("没有填写泛型参数");
            }
            finalNeedType = ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            if (type instanceof ParameterizedType) {
                finalNeedType = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                finalNeedType = type;
            }
        }
        return finalNeedType;
    }
}
