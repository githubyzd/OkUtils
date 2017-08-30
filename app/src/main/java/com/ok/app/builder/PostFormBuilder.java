package com.ok.app.builder;

import com.ok.app.request.PostFormRequest;
import com.ok.app.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yzd on 2017/8/24 0024.
 */

public class PostFormBuilder extends OkRequestBuilder<PostFormBuilder> implements HasParamsable {

    private List<FileBean> files = new ArrayList<>();

    @Override
    public RequestCall build() {
        return new PostFormRequest(url, tag, params, headers, id, files).build();
    }

    @Override
    public PostFormBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostFormBuilder addParams(String key, String val) {
        if (params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    public PostFormBuilder addFile(String name, String fileName, File file) {
        files.add(new FileBean(name, fileName, file));
        return this;
    }

    public PostFormBuilder addFiles(String name, Map<String, File> files) {
        for (String fileName : files.keySet()) {
            this.files.add(new FileBean(name, fileName, files.get(fileName)));
        }
        return this;
    }

    public static class FileBean {

        public String name;
        public String fileName;
        public File file;

        public FileBean(String name, String fileName, File file) {
            this.name = name;
            this.fileName = fileName;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileBean{" +
                    "name='" + name + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", file=" + file +
                    '}';
        }
    }
}
