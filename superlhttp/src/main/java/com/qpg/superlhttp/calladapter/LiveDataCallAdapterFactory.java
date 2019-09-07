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
package com.qpg.superlhttp.calladapter;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

public final class LiveDataCallAdapterFactory extends CallAdapter.Factory {
    private LiveDataCallAdapterFactory() {
    }

    public static LiveDataCallAdapterFactory create() {
        return new LiveDataCallAdapterFactory();
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            throw  new IllegalArgumentException("返回值不是LiveData类型");
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException("Response must be parametrized as " +
                    "LiveData<Resource> or LiveData<? extends Resource>");
        }

        //先解释一下getParameterUpperBound
        //官方例子
        //For example, index 1 of {@code Map<String, ? extends Runnable>} returns {@code Runnable}.
        //获取的是Map<String,? extends Runnable>参数列表中index序列号的参数类型,即0为String,1为Runnable
        //这里的0就是LiveData<?>中?的序列号,因为只有一个参数
        //其实这个就是我们请求返回的实体

        Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        if (getRawType(responseType) == Response.class) {
            if (!(responseType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parametrized as " +
                        "LiveData<Response<Resource>> or LiveData<Response<? extends Resource>>");
            }

            return new LiveDataResponseCallAdapter<>(responseType);
        }else if(getRawType(responseType) == Resource.class){
            if (!(responseType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parametrized as " +
                        "LiveData<Resource> or LiveData<? extends Resource>");
            }
            return new LiveDataBodyCallAdapter<>(getParameterUpperBound(0, (ParameterizedType) responseType));

        }else {
            return null;
        }
    }
}
