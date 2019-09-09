package com.qpg.superlhttp.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.qpg.superlhttp.calladapter.Resource;
import com.qpg.superlhttp.callback.BaseCallback;
import com.qpg.superlhttp.mode.CacheResult;
import com.qpg.superlhttp.subscriber.ApiCallbackSubscriber;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * @Description: Get请求
 */
public class GetRequest extends BaseHttpRequest<GetRequest> {
    public GetRequest(String suffixUrl) {
        super(suffixUrl);
    }
    protected LiveData<?> result;
    @Override
    protected <T> LiveData<Resource<T>> execute(@NonNull LifecycleOwner owner,Type type) {
        LiveData<Resource<ResponseBody>> resourceLiveData=apiService.get(suffixUrl, params);
      //  LiveData<T> resultLiveData= Transformations.map(resourceLiveData,new ResultFunction<T>(type));
        LiveData<Resource<T>> resultLiveData= Transformations.map(resourceLiveData,this.<T>norTransformer(type));
        return resultLiveData;
    }

//    @Override
//    protected <T> LiveData<T> execute(Type type) {
//        return apiService.get(suffixUrl, params).compose(this.<T>norTransformer(type));
//    }

    @Override
    protected <T> LiveData<CacheResult<T>> cacheExecute(@NonNull LifecycleOwner owner, Type type) {
        return null;
    }

//    @Override
//    protected <T> LiveData<CacheResult<T>> cacheExecute(Type type) {
//        return this.<T>execute(type).compose(SuperLHttp.getApiCache().<T>transformer(cacheMode, type));
//    }

    @Override
    protected <T> void execute(@NonNull LifecycleOwner owner, BaseCallback<T> callback) {
        Observer observer = new ApiCallbackSubscriber(callback);
        if (super.tag != null) {
         //   ApiManager.get().add(super.tag, observer);
        }
        if (isLocalCache) {
            this.cacheExecute(owner,getSubType(callback)).observe(owner,observer);
        } else {
            result=this.execute(owner, getType(callback));
           // this.execute(owner,getType(callback)).observe(owner,observer);
            result.observe(owner,observer);
        }
    }

    public <T>  LiveData<T> asLiveData() {
        return (LiveData<T>)result;
    }
}
