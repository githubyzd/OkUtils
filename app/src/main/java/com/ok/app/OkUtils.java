package com.ok.app;


import com.ok.app.builder.GetBuilder;
import com.ok.app.builder.PostBuilder;
import com.ok.app.builder.PostFormBuilder;
import com.ok.app.builder.UploadFileBuilder;
import com.ok.app.callback.Callback;
import com.ok.app.request.RequestCall;
import com.ok.app.utils.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by yzd on 2017/8/18 0018.
 */

public class OkUtils {
    private volatile static OkUtils mInstance;
    private OkHttpClient mOkClien;
    private Platform mPlatform;

    public OkUtils(OkHttpClient client) {
        if (client == null) {
            mOkClien = new OkHttpClient();
        } else {
            mOkClien = client;
        }
        mPlatform = Platform.get();
    }

    public static OkUtils getInstance() {
        return init(null);
    }

    public static OkUtils init(OkHttpClient client){
        if (mInstance ==  null) {
            synchronized (OkHttpClient.class){
                if (mInstance == null){
                    mInstance = new OkUtils(client);
                }
            }
        }
        return mInstance;
    }

    public static GetBuilder get(){
        return new GetBuilder();
    }
    public static PostBuilder post(){
        return new PostBuilder();
    }
    public static UploadFileBuilder uploadFile(){
        return new UploadFileBuilder();
    }
    public static PostFormBuilder postForm(){
        return new PostFormBuilder();
    }

    public OkHttpClient getOkHttpClient(){
        return mOkClien;
    }

    public void execute(final RequestCall requestCall, Callback callback){
        if (callback == null){
            callback = Callback.CALLBACK_DEFAULT;
        }

        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall().enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                sendFailResultCallback(call,e,finalCallback,id);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call.isCanceled()){
                    sendFailResultCallback(call,new IOException("Canceled"),finalCallback,id);
                    return;
                }

                if (!finalCallback.validateResponse(response,id)){
                    sendFailResultCallback(call,new IOException("request faild, response code is : " + response.code()),finalCallback,id);
                    return;
                }

                try {
                    Object obj = finalCallback.parseResponse(response, id);
                    sendSuccessResultCallback(obj,finalCallback,id);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback, id);
                }finally {
                    if (response.body() != null){
                        response.body().close();
                    }
                }
            }
        });
    }

    private void sendSuccessResultCallback(final Object obj, final Callback callback, final int id) {
        if (callback == null)return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(obj,id);
                callback.onAfter(id);
            }
        });
    }

    private void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call,e,id);
                callback.onAfter(id);
            }
        });
    }

    public Executor getDelivery(){
        return mPlatform.defaultCallbackExecutor();
    }
}
