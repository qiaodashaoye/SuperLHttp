package com.qpg.superlhttp.subscriber;

import androidx.lifecycle.Observer;

import com.qpg.superlhttp.calladapter.Resource;
import com.qpg.superlhttp.callback.BaseCallback;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ApiCallbackSubscriber<T,R extends Resource<T>> implements Observer<R> {
    BaseCallback<T> callBack;
    R data;
    public ApiCallbackSubscriber(BaseCallback<T> callBack) {
        if (callBack == null) {
            throw new NullPointerException("this callback is null!");
        }

        this.callBack = callBack;
    }

    @Override
    public void onChanged(R r) {
        this.data = r;
        if(r.isSuccess()){
            T t=r.getResource();
            callBack.onSuccess(t);
        }else {
            //此处异常分类根据LiveDataBodyCallAdapter类postValue
            if(r.getError() instanceof HttpException){
                HttpException httpException=(HttpException)r.getError();
                ResponseBody errorBody= httpException.response().errorBody();

                try {
                    String errBody=errorBody.string();
                    if(!errBody.isEmpty()){
                        callBack.onFail(httpException.code(),errBody);

                    }else {
                        callBack.onFail(httpException.code(),httpException.message());

                    }
                } catch (IOException e) {
                    errorBody.close();
                    callBack.onFail(httpException.code(),httpException.message());
                    e.printStackTrace();
                }finally {

                }

            }else {
                callBack.onFail(500,r.getError().getMessage());
            }
        }
    }
    public R getData() {
        return data;
    }
}
