package com.ok.app.callback;

import okhttp3.Response;

/**
 * Created by yzd on 2017/8/21 0021.
 */

public abstract class StringCallback extends Callback<String> {
    @Override
    public String parseResponse(Response response, int id) throws Exception {
        return response.body().string();
    }

}
