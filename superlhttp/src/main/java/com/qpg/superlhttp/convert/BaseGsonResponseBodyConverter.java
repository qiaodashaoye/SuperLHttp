package com.qpg.superlhttp.convert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownServiceException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class BaseGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;
    private Gson gson;
    private final TypeAdapter<T> adapter;
    public BaseGsonResponseBodyConverter(Gson gson, Type type,TypeAdapter<T> adapter) {
        this.gson = gson;
        this.type = type;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        if (adapter != null && gson != null) {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                T data = adapter.read(jsonReader);
                if (data == null) throw new UnknownServiceException("server back data is null");
//                if (data instanceof ApiResult) {
//                    ApiResult apiResult = (ApiResult) data;
//                    if (!ResponseHelper.isSuccess(apiResult)) {
//                        throw new UnknownServiceException(apiResult.getMsg() == null ? "unknow error" : apiResult.getMsg());
//                    }
//                }
                return data;
            } finally {
                value.close();
            }
        } else {
            return null;
        }
    }
}
