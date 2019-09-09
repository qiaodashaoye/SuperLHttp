package com.qpg.superlhttp.api;

import androidx.lifecycle.LiveData;

import com.qpg.superlhttp.calladapter.Resource;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface SuperService {
    @GET()
    LiveData<Resource<ResponseBody>> get(@Url String url, @QueryMap Map<String, String> maps);
//    @GET()
//    LiveData<Response<Resource<ResponseBody>>> getPimbas();

    @FormUrlEncoded
    @POST()
    LiveData<Resource<ResponseBody>> post(@Url() String url, @FieldMap Map<String, String> maps);

    @FormUrlEncoded
    @POST()
    LiveData<Resource<ResponseBody>> postForm(@Url() String url, @FieldMap Map<String, Object> maps);

    @POST()
    LiveData<Resource<ResponseBody>> postBody(@Url() String url, @Body RequestBody requestBody);
}
