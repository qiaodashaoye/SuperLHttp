package com.qpg.superlhttpdemo.viewmodel;

import android.app.Application;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.qpg.superlhttp.SuperLHttp;
import com.qpg.superlhttp.calladapter.Resource;
import com.qpg.superlhttp.callback.SimpleCallBack;
import com.qpg.superlhttpdemo.MainActivity;
import com.qpg.superlhttpdemo.model.UserBean;


public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<Resource<UserBean>> loginResult = new MutableLiveData<>();
    private UserBean userBean;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }
    public void login(@NonNull LifecycleOwner owner){
        SuperLHttp.get("active/getMyActives")
                .request(owner, new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {

                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        System.out.println("AAAAA0");
                    }
                });
//        SuperLHttp.get("active/getMyActives").<String>asLiveData().observe(owner, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//
//            }
//        });
    }
    public void login1(@NonNull LifecycleOwner owner){
        SuperLHttp.get("active/getMyActives")
                .request(owner, new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {

                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {

                        System.out.println("AAAAA1");
                    }
                });
//        SuperLHttp.get("active/getMyActives").<String>asLiveData().observe(owner, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//
//            }
//        });
    }
}
