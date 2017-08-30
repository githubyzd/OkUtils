package com.ok.app.request;


import com.ok.app.OkUtils;
import com.ok.app.builder.PostFormBuilder;
import com.ok.app.callback.Callback;

import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by yzd on 2017/8/24 0024.
 */

public class PostFormRequest extends OkRequest {

    private List<PostFormBuilder.FileBean> files;

    public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers,
                           int id, List<PostFormBuilder.FileBean> files) {
        super(url, tag, params, headers, id);
        this.files = files;
    }

    @Override
    protected RequestBody buildRequestBody() {

        if (files == null || files.isEmpty()) {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            FormBody formBody = builder.build();
            return formBody;
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(builder);

            for (int i = 0; i < files.size(); i++) {
                PostFormBuilder.FileBean fileBean = files.get(i);
                RequestBody  requestBody = RequestBody.create(MediaType.parse(getStream(fileBean.fileName)),fileBean.file);
                builder.addFormDataPart(fileBean.name,fileBean.fileName,requestBody);
            }

            return builder.build();
        }
    }

    private String getStream(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = null;
        try {
            contentType  = fileNameMap.getContentTypeFor(URLEncoder.encode(fileName,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    public void addParams(MultipartBody.Builder builder) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),RequestBody.create(null,params.get(key)));
            }
        }
    }

    private void addParams(FormBody.Builder builder) {
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    @Override
    public RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
        if (callback == null) return requestBody;

        ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, new ProgressRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {
                OkUtils.getInstance().getDelivery().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(bytesWritten * 1.0f / contentLength,contentLength,id);
                    }
                });
            }
        });


        return progressRequestBody;
    }
}
