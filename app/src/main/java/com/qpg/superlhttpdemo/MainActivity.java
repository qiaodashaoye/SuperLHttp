package com.qpg.superlhttpdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;

import com.qpg.superlhttp.SuperLHttp;
import com.qpg.superlhttp.callback.SimpleCallBack;
import com.qpg.superlhttpdemo.viewmodel.LoginViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuperLHttp.get("active/getMyActives")
                        .request(MainActivity.this, new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String data) {

                            }

                            @Override
                            public void onFail(int errCode, String errMsg) {
                                System.out.println("AAAAA0");
                            }
                        });
            }
        });

        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.login(MainActivity.this);
                loginViewModel.login1(MainActivity.this);
            }
        });

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
}
