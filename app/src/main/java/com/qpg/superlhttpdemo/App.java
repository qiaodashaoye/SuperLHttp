package com.qpg.superlhttpdemo;

import android.app.Application;
import com.qpg.superlhttp.SuperLHttp;

public class App extends Application {
    private static App appInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
        SuperLHttp.init(this.getApplication());
        SuperLHttp.config().setBaseUrl("http://192.168.0.101:8081/ctcFront/");
    }

    public static App getApplication(){
        return appInstance;
    }

}
