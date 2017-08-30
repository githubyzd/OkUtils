package com.ok.app.request;

import com.ok.app.OkUtils;
import com.ok.app.callback.Callback;
import com.ok.app.utils.ExceptionUtil;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by yzd on 2017/8/23 0023.
 */

public class UploadFileRequest extends OkRequest {

    private static MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    private File file;
    private MediaType mediaType;

    public UploadFileRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers,
                             int id, File file, MediaType mediaType) {
        super(url, tag, params, headers, id);
        this.file = file;
        this.mediaType = mediaType;

        if (this.file == null) {
            ExceptionUtil.illegaArgument("the file can not be null !");
        }

        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_STREAM;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return  RequestBody.create(mediaType,file);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return new Request.Builder().url(url).post(requestBody).build();
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
