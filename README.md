# SuperLHttp

基于LiveData+Retrofit2的可以感知生命周期的网络请求框架，跟内存泄漏说拜拜。

- 项目地址：[https://github.com/qiaodashaoye/SuperLHttp.git](https://github.com/qiaodashaoye/SuperLHttp.git)

- 项目依赖：`implementation 'com.qpg.net:superlhttp:1.0.0'`(暂未提交)


#### 眼前一亮的地方

- 链式调用，代码可读性高
- 动态切换请求基地址，人性化使用
- 感知生命周期，防止内存泄漏

### 调用实例：

第一步需要在application中进行全局初始化以及添加全局相关配置，具体使用如下：
> 简单初始化，使用默认配置，方便快捷
```
SuperLHttp.init(this);
SuperLHttp.config()
        //配置请求主机地址
        .setBaseUrl("服务器地址");
        
```
> 详细初始化,自定义配置
```
  SuperLHttp.init(this);
  SuperLHttp.config()
          //配置请求主机地址
          .setBaseUrl("http://11.11.1.1.com/")
          //配置全局请求头
          .globalHeaders(new HashMap<String, String>())
          //配置全局请求参数
          .globalParams(new HashMap<String, String>())
          //配置读取超时时间，单位秒
          .setReadTimeout(30)
          //配置写入超时时间，单位秒
          .setWriteTimeout(30)
          //配置连接超时时间，单位秒
          .setConnectTimeout(30)
          //配置请求失败重试次数
          .setRetryCount(3)
          //配置请求失败重试间隔时间，单位毫秒
          .setRetryDelayMillis(1000)
          //配置是否使用cookie
          .isUseCookie(true)
          //配置自定义cookie
          .setCookie(new CookieJarImpl(new SPCookieStore(this)));
          //配置是否使用OkHttp的默认缓存
//        .setHttpCache(true)
          //配置OkHttp缓存路径
//        .setHttpCacheDirectory(new File(SuperLHttp.getContext().getCacheDir(), SuperConfig.CACHE_HTTP_DIR))
          //配置自定义OkHttp缓存
//        .httpCache(new Cache(new File(SuperLHttp.getContext().getCacheDir(), SuperConfig.CACHE_HTTP_DIR), ViseConfig.CACHE_MAX_SIZE))
          //配置自定义离线缓存
//        .cacheOffline(new Cache(new File(SuperLHttp.getContext().getCacheDir(), SuperConfig.CACHE_HTTP_DIR), ViseConfig.CACHE_MAX_SIZE))
          //配置自定义在线缓存
//        .cacheOnline(new Cache(new File(SuperLHttp.getContext().getCacheDir(), SuperConfig.CACHE_HTTP_DIR), ViseConfig.CACHE_MAX_SIZE))
          //配置开启Gzip请求方式，需要服务器支持
//        .postGzipInterceptor()
          //配置应用级拦截器
//        .setInterceptor(new HttpLogInterceptor().setLevel(HttpLogInterceptor.Level.BODY))
          //配置网络拦截器
//        .networkInterceptor(new NoCacheInterceptor())
          //配置请求工厂
//        .setCallFactory(new Call.Factory() {
//            @Override
//            public Call newCall(Request request) {
//                return null;
//            }
//        })
          //配置连接池
//        .connectionPool(new ConnectionPool())
          //配置主机证书验证
//        .hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier)
          //配置SSL证书验证
//        .setSSLSocketFactory(HttpsUtils.getSslSocketFactory().sSLSocketFactory)
         //配置主机代理
//        .setProxy(new Proxy(Proxy.Type.HTTP, new SocketAddress() {}));

```
#### Get方式
- get方式一（推荐）
此方式需要实现ProgressDialogCallBack，第一个参数是为了方便使用者传入自定义的Dialog加载框，
第二个参数是传入回调失败后的提示信息，使用者不必手动重写onFail()方法，使用非常简单,具体
使用方法请看实例。
```
 SuperHttp.get("user/getUserInfo")
                        .addParam("userid","4556")
                        .request(new ProgressDialogCallBack<String>(mProgressDialog,"用户信息获取失败，请刷新重试") {
                            @Override
                            public void onSuccess(String data) {

                            }
                        });
```
- get方式二
```
  SuperHttp.get("user/getUserInfo")
                        .addParam("userid","4556")
                        .request(new SimpleCallBack<UserBean>() {
                            @Override
                            public void onSuccess(UserBean data) {
                                
                            }

                            @Override
                            public void onFail(int errCode, String errMsg) {

                            }
                        });
```                      
#### POST方式

 - post表单
 ```
 SuperHttp.post("user/login")
                         .addParam("userid","4556")
                         .request(new ProgressDialogCallBack<String>(mProgressDialog,"用户信息获取失败，请刷新重试") {
                             @Override
                             public void onSuccess(String data) {
 
                             }
                         });
 ```
 - post Map

```
  HashMap<String,String> map=new HashMap<>();
                map.put("key1","13213");
                map.put("key2","5456");
                SuperHttp.post("user/login")
                        .addParams(map)
                        .request(new ProgressDialogCallBack<String>(mProgressDialog,"用户信息获取失败，请刷新重试") {
                            @Override
                            public void onSuccess(String data) {

                            }
                        });
```

 - post Json

```
   UserBean userBean=new UserBean();
                  userBean.setAvatarUrl("http://www.ae.com/asd.png");
                  userBean.setUserName("小和尚");
                  SuperHttp.post("user/register")
                          .setJson(new Gson().toJson(userBean))
                          .request(new ProgressDialogCallBack<String>(mProgressDialog,"用户信息获取失败，请刷新重试") {
                              @Override
                              public void onSuccess(String data) {
  
                              }
                          });
```
## 混淆
``` java
#gson
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }


#okhttp
-dontwarn okio.**
-keep class okio.** { *; }
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
```

## 交流方式
 * QQ: 1451449315
 * QQ群: 122619758
 
 ## Licenses
 ```
  Copyright 2019 qpg
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 ```