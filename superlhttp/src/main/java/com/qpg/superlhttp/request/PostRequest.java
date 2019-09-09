package com.qpg.superlhttp.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.qpg.superlhttp.calladapter.Resource;
import com.qpg.superlhttp.callback.BaseCallback;
import com.qpg.superlhttp.mode.CacheResult;
import com.qpg.superlhttp.mode.MediaTypes;
import com.qpg.superlhttp.subscriber.ApiCallbackSubscriber;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @Description: Post请求
 */
public class PostRequest extends BaseHttpRequest<PostRequest> {

    protected Map<String, Object> forms = new LinkedHashMap<>();
    protected StringBuilder stringBuilder = new StringBuilder();
    protected RequestBody requestBody;
    protected MediaType mediaType;
    protected String content;

    public PostRequest(String suffixUrl) {
        super(suffixUrl);
    }

    @Override
    protected <T> LiveData<Resource<T>> execute(@NonNull LifecycleOwner owner, Type type) {
     //   LiveData<Resource<ResponseBody>> resourceLiveData=apiService.postForm(suffixUrl, forms).compose(this.<T>norTransformer(type));
        LiveData<Resource<ResponseBody>> resourceLiveData;
        LiveData<Resource<T>> resultLiveData;
        if (stringBuilder.length() > 0) {
            suffixUrl = suffixUrl + stringBuilder.toString();
        }
        if (forms != null && forms.size() > 0) {
            if (params != null && params.size() > 0) {
                Iterator<Map.Entry<String, String>> entryIterator = params.entrySet().iterator();
                Map.Entry<String, String> entry;
                while (entryIterator.hasNext()) {
                    entry = entryIterator.next();
                    if (entry != null) {
                        forms.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            resourceLiveData=apiService.postForm(suffixUrl, forms);
            resultLiveData= Transformations.map(resourceLiveData,this.<T>norTransformer(type));
        }else if(requestBody != null){
            resourceLiveData= apiService.postBody(suffixUrl, requestBody);
            resultLiveData= Transformations.map(resourceLiveData,this.<T>norTransformer(type));
        }else if(content != null && mediaType != null){
            requestBody = RequestBody.create(mediaType, content);
            resourceLiveData= apiService.postBody(suffixUrl, requestBody);
            resultLiveData= Transformations.map(resourceLiveData,this.<T>norTransformer(type));
        }else {
            resourceLiveData= apiService.post(suffixUrl, params);
            resultLiveData= Transformations.map(resourceLiveData,this.<T>norTransformer(type));
        }

        return resultLiveData;
    }

    @Override
    protected <T> LiveData<CacheResult<T>> cacheExecute(@NonNull LifecycleOwner owner,Type type) {
        return null;
    }

    @Override
    protected <T> void execute(@NonNull LifecycleOwner owner,BaseCallback<T> callback) {
        Observer observer = new ApiCallbackSubscriber(callback);
        if (super.tag != null) {
         //   ApiManager.get().add(super.tag, observer);
        }
        if (isLocalCache) {
            this.cacheExecute(owner,getSubType(callback)).observe(owner,observer);
        } else {
            this.execute(owner,getType(callback)).observe(owner,observer);
        }
    }

    public PostRequest addUrlParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append("?");
            } else {
                stringBuilder.append("&");
            }
            stringBuilder.append(paramKey).append("=").append(paramValue);
        }
        return this;
    }

    public PostRequest addForm(String formKey, Object formValue) {
        if (formKey != null && formValue != null) {
            forms.put(formKey, formValue);
        }
        return this;
    }

    public PostRequest setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public PostRequest setString(String string) {
        this.content = string;
        this.mediaType = MediaTypes.TEXT_PLAIN_TYPE;
        return this;
    }

    public PostRequest setString(String string, MediaType mediaType) {
        this.content = string;
        this.mediaType = mediaType;
        return this;
    }

    public PostRequest setJson(String json) {
        this.content = json;
        this.mediaType = MediaTypes.APPLICATION_JSON_TYPE;
        return this;
    }

    public PostRequest setJson(JSONObject jsonObject) {
        this.content = jsonObject.toString();
        this.mediaType = MediaTypes.APPLICATION_JSON_TYPE;
        return this;
    }

    public PostRequest setJson(JSONArray jsonArray) {
        this.content = jsonArray.toString();
        this.mediaType = MediaTypes.APPLICATION_JSON_TYPE;
        return this;
    }
}
