package com.qpg.superlhttp.transform;

import androidx.arch.core.util.Function;

import com.google.gson.Gson;
import com.qpg.superlhttp.calladapter.Resource;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

public class ResultFunction<O extends Resource> implements Function<Resource<ResponseBody>, O> {
    private Type type;

    public ResultFunction(Type type){
        this.type=type;
    }
    @Override
    public O apply(Resource<ResponseBody> input) {
        if(!input.isSuccess()){
            return (O)Resource.error(input.getError());
        }
        ResponseBody responseBody=input.getResource();
        Gson gson = new Gson();
        String json;
        try {
            json = responseBody.string();
            if(input.isSuccess()){
                if (type.equals(String.class)) {
                    return (O)Resource.success(json);
                } else {
                    return (O)Resource.success(gson.fromJson(json, type));
                }
            }else {
                return (O)Resource.error(input.getError());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            responseBody.close();
        }
        return null;
    }
}
