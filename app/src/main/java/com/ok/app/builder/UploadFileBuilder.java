package com.ok.app.builder;

import com.ok.app.request.RequestCall;
import com.ok.app.request.UploadFileRequest;

import java.io.File;

import okhttp3.MediaType;

/**
 * Created by yzd on 2017/8/23 0023.
 */

public class UploadFileBuilder extends OkRequestBuilder<UploadFileBuilder> {

    private File file;
    private MediaType mediaType;

    public UploadFileBuilder addFile(File file){
        this.file = file;
        return this;
    }

    public UploadFileBuilder setMediaType(MediaType mediaType){
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new UploadFileRequest(url,tag,params,headers,id,file,mediaType).build();
    }
}
