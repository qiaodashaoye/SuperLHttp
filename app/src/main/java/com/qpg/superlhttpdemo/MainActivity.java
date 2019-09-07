package com.qpg.superlhttpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.qpg.superlhttp.SuperLHttp;
import com.qpg.superlhttp.callback.SimpleCallBack;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.101:8081/ctcFront/")
//                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
//                .addConverterFactory(LiveDataResponseBodyConverterFactory.create())
//              //  .addConverterFactory(GsonConverterFactory.create())
//                .build();

        SuperLHttp.init(this.getApplication());
        SuperLHttp.config().setBaseUrl("http://192.168.0.100:8081/ctcFront/");
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

                            }
                        });
            }
        });

        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*LiveData<Resource<ResponseBody>> resourceLiveData=  retrofit.create(SuperService.class)
                        .get("auth/asd",new HashMap<String, String>());
            //    LiveData<UserBean> userBean= Transformations.map(resourceLiveData,new ResultFunction<UserBean>());
                LiveData<String> userBean= Transformations.map(resourceLiveData, new Function<Resource<ResponseBody>, String>() {
                    @Override
                    public String apply(Resource<ResponseBody> input) {
                        try {
                            return input.getResource().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }
                });

                userBean.observe(MainActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String userBean) {

                    }
                });*/
//                userBean.observe(MainActivity.this, new Observer<UserBean>() {
//                            @Override
//                            public void onChanged(UserBean stringResource) {
//
//                                //   Toast.makeText(MainActivity.this,stringResource.getResource().toString(),Toast.LENGTH_LONG).show();
//                                if (stringResource.isSuccess()) {
//                                    try {
//                                        ResponseBody responseBody=stringResource.getResource();
//                                        String body= responseBody.string();
//                                        LiveData<ResponseBody> responseBodyLiveData=new MutableLiveData<>();
//                                        LiveData<UserBean> userBean= Transformations.map(responseBodyLiveData,new ResultFunction<UserBean>());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                } else {
//                                    //doErrorAction with resource.error
//                                }
//                            }
//
//                        });
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
