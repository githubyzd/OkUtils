package com.ok.app.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by yzd on 2017/8/23 0023.
 */

public class PostRequest extends OkRequest {
    public PostRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, int id) {
        super(url, tag, params, headers, id);
    }

    @Override
    protected RequestBody buildRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        addParams(builder);
        FormBody formBody = builder.build();
        return formBody;
    }

    private void addParams(FormBody.Builder builder) {
        if (params != null){
            for (String key : params.keySet()){
                builder.add(key,params.get(key));
            }
        }
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}
