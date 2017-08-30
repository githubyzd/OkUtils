package com.ok.app.builder;

import android.net.Uri;

import com.ok.app.request.GetRequest;
import com.ok.app.request.RequestCall;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yzd on 2017/8/18 0018.
 */

public class GetBuilder extends OkRequestBuilder<GetBuilder> implements HasParamsable{


    @Override
    public RequestCall build() {
        if (params != null){
            url = addParams(url,params);
        }
        return new GetRequest(url,tag,params,headers,id).build();
    }

    protected String addParams(String url, Map<String,String> params) {
        if (url == null || params == null || params.isEmpty()){
            return url;
        }

        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            builder.appendQueryParameter(key,params.get(key));
        }
        return builder.build().toString();
    }

    @Override
    public GetBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public GetBuilder addParams(String key, String val) {
        if (this.params == null){
            params = new LinkedHashMap<>();
        }
        params.put(key,val);
        return this;
    }
}
