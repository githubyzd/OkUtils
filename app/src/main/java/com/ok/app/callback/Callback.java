package com.ok.app.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yzd on 2017/8/21 0021.
 */

public abstract class Callback<T> {
    public void onBefore(Request request, int id) {
    }

    public void onAfter(int id) {
    }

    public void inProgress(float progress, long total, int id) {
    }

    public boolean validateResponse(Response response, int id) {
        return response.isSuccessful();
    }

    public abstract T parseResponse(Response response, int id) throws Exception;

    public abstract void onError(Call call, Exception e, int id);

    public abstract void onResponse(T response, int id);

    public static Callback CALLBACK_DEFAULT = new Callback() {
        @Override
        public Object parseResponse(Response response, int id) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(Object response, int id) {

        }
    };

}
