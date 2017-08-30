package com.ok.app.builder;
import com.ok.app.request.PostRequest;
import com.ok.app.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yzd on 2017/8/22 0022.
 */

public class PostBuilder extends OkRequestBuilder<PostBuilder> implements HasParamsable{
    @Override
    public RequestCall build() {
        return new PostRequest(url,tag,params,headers,id).build();
    }

    @Override
    public PostBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostBuilder addParams(String key, String val) {
        if (this.params == null){
            params = new LinkedHashMap<>();
        }
        params.put(key,val);
        return this;
    }


}
