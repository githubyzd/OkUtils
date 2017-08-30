package com.ok.app.builder;

import java.util.Map;

/**
 * Created by yzd on 2017/8/21 0021.
 */

public interface HasParamsable {
    OkRequestBuilder params(Map<String,String> params);
    OkRequestBuilder addParams(String key,String val);
}
