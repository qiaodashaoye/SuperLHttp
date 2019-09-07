package com.qpg.superlhttp.calladapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public final class LiveDataBodyCallAdapter<R> implements CallAdapter<R, LiveData<Resource<R>>> {
    private final Type responseType;

    LiveDataBodyCallAdapter(Type type) {
        this.responseType = type;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<Resource<R>> adapt(Call<R> call) {
        //这个作用是业务在多线程中,业务处理的线程安全问题,确保单一线程作业
     //   AtomicBoolean
        final MutableLiveData<Resource<R>> liveDataResponse = new MutableLiveData<>();
        call.enqueue(new LiveDataBodyCallCallback<>(liveDataResponse));
        return liveDataResponse;
    }

    private static class LiveDataBodyCallCallback<T> implements Callback<T> {
        private final MutableLiveData<Resource<T>> liveData;

        LiveDataBodyCallCallback(MutableLiveData<Resource<T>> liveData) {
            this.liveData = liveData;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (call.isCanceled()) return;

            if (response.isSuccessful()) {
                liveData.postValue(Resource.success(response.body()));
            } else {
                liveData.postValue(Resource.error(new HttpException(response)));
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (call.isCanceled()) return;
            liveData.postValue(Resource.error(t));
        }
    }
}
