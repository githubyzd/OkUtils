package com.ok.app.request;

import com.ok.app.OkUtils;
import com.ok.app.callback.Callback;
import com.ok.app.utils.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yzd on 2017/8/18 0018.
 */

public class RequestCall {
    private OkRequest okRequest;
    private Request request;
    private Call call;

    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

    private OkHttpClient client;

    public RequestCall(OkRequest request){
        this.okRequest = request;
    }

    public void execute(Callback callback){
        buildCall(callback);
        if (callback != null){
            callback.onBefore(request,getOkHttpRequest().getId());
        }

        OkUtils.getInstance().execute(this,callback);
    }

    private Call buildCall(Callback callback) {
        request = buildRequest(callback);

        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0){
            readTimeOut = readTimeOut > 0 ? readTimeOut : Constant.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : Constant.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : Constant.DEFAULT_MILLISECONDS;

            client = OkUtils.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut,TimeUnit.MILLISECONDS)
                    .connectTimeout(connTimeOut,TimeUnit.MILLISECONDS)
                    .build();

            call = client.newCall(request);
        }else {
            call = OkUtils.getInstance().getOkHttpClient().newCall(request);
        }

        return call;

    }

    private Request buildRequest(Callback callback) {
        return okRequest.generateRequest(callback);
    }

    public OkRequest getOkHttpRequest()
    {
        return okRequest;
    }

    public Call getCall()
    {
        return call;
    }
}
