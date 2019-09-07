package com.qpg.superlhttp.api;

import androidx.lifecycle.LiveData;

import com.qpg.superlhttp.calladapter.Resource;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface SuperService {
    @GET()
    LiveData<Resource<ResponseBody>> get(@Url String url, @QueryMap Map<String, String> maps);
//    @GET()
//    LiveData<Response<Resource<ResponseBody>>> getPimbas();
}
