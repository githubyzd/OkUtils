package com.ok.app.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by yzd on 2017/8/18 0018.
 */

public class GetRequest extends OkRequest{
    public GetRequest(String url,Object tag,Map<String,String> params,Map<String,String> headers,int id){
        super(url,tag,params,headers,id);
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }

}
