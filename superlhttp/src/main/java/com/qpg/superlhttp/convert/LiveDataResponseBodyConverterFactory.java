/*
 * Copyright 2018 Leonardo Rossetto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qpg.superlhttp.convert;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.qpg.superlhttp.calladapter.Resource;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Bypass the default retrofit body converter behaviour
 * as we are using the {@link Resource} as an response wrapper we should
 * tell to the original converter the correct type
 */
public final class LiveDataResponseBodyConverterFactory extends Converter.Factory {
    private final Gson gson;
    private LiveDataResponseBodyConverterFactory(Gson gson) {
        this.gson = gson;
    }

    public static LiveDataResponseBodyConverterFactory create(Gson gson) {
        return new LiveDataResponseBodyConverterFactory(gson);
    }
    public static LiveDataResponseBodyConverterFactory create() {
        return create(new Gson());
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getRawType() == Response.class) {
                Type subType = parameterizedType.getActualTypeArguments()[0];
                if (subType instanceof ParameterizedType) {
                    parameterizedType = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
                }
            }

            if (parameterizedType.getRawType() == Resource.class) {
                Type realType = parameterizedType.getActualTypeArguments()[0];
                TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(realType));

                return new BaseGsonResponseBodyConverter<>(gson,type,adapter);
            }
        }
        return retrofit.nextResponseBodyConverter(this, type, annotations);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonRequestBodyConverter<>(gson, adapter);
    }
}
