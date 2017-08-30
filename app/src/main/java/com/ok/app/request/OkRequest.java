package com.ok.app.request;

import com.ok.app.callback.Callback;
import com.ok.app.utils.ExceptionUtil;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by yzd on 2017/8/18 0018.
 */

public abstract class OkRequest {
    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;
    protected int id;

    protected Request.Builder builder = new Request.Builder();

    protected OkRequest(String url,Object tag,Map<String,String> params,Map<String,String> headers,int id){
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        this.id = id;

        if (url == null){
            ExceptionUtil.illegaArgument("url can not be null");
        }
        
        initBuilder();
    }

    private void initBuilder() {
        builder.url(url).tag(tag);
        addHeaders();
    }

    protected void addHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();

        if (headers == null || headers.isEmpty())
            return;

        for (String key : headers.keySet()){
            headerBuilder.add(key,headers.get(key));
        }

        builder.headers(headerBuilder.build());
    }

    protected abstract RequestBody buildRequestBody();

    protected abstract Request buildRequest(RequestBody requestBody);

    public RequestCall build(){
        return new RequestCall(this);
    }

    public int getId(){
        return id;
    }

    public Request generateRequest(Callback callback) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrappedRequestBody = wrapRequestBody(requestBody,callback);
        Request request = buildRequest(wrappedRequestBody);
        return request;
    }

    public RequestBody wrapRequestBody(RequestBody requestBody, Callback callback){
        return requestBody;
    }
}
