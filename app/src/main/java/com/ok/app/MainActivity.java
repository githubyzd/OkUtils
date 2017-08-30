package com.ok.app;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ok.app.callback.FileCallBack;
import com.ok.app.callback.StringCallback;
import com.ok.app.utils.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final String BASE_URL = "http://192.168.43.50:8080/";
    private final OkHttpClient client = new OkHttpClient();
    private final String TAG = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void get_execute(View view) {
        Request request = new Request.Builder()
                .url(BASE_URL + "getapi?id=123")
                .build();

        final Call call = client.newCall(request);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    Log.d(TAG, response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void get_enqueue(View view) {
        Request request = new Request.Builder()
                .url(BASE_URL + "getapi?id=123")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().string());
            }
        });
    }

    public void post_execute(View view) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", "1000972")
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "postapi")
                .post(requestBody)
                .build();
        final Call call = client.newCall(request);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    Log.d(TAG, response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }

    public void post_enqueue(View view) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", "1000972")
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "postapi")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().string());
            }
        });
    }

    public void upload(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"username\""),
                        RequestBody.create(null, "yue"))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"file\"; filename=\"test.jpg\""), body)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "upload")
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().string());
            }
        });
    }

    public void dwonload(View view) {
        Request request = new Request.Builder()
                .url(BASE_URL + "test.jpg")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
                byte[] bytes = response.body().bytes();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                Log.d(TAG, "写入成功");
            }
        });
    }

    public void image(View view) {

    }

    private class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            L.d("before");
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            L.d("after");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            L.e(e.toString());
        }

        @Override
        public void onResponse(String response, int id) {
            L.d(response);
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            super.inProgress(progress, total, id);
            L.d(progress + "");
        }
    }

    public void ok_get(View view) {
        OkUtils.get()
//                .url("https://m.bjbzbz.com/m/indexfloor")
                .url("http://192.168.43.50:8080/Demo/getapi")
                .addParams("name", "yuezengdi")
                .addParams("age","25")
                .tag("get")
                .addHeader("Cookie", "testCookie")
                .id(123)
                .build()
                .execute(new MyStringCallback());
    }

    public void ok_post(View view) {
        OkUtils.post()
                .url(BASE_URL + "/Demo/postapi")
                .addParams("age", "25")
                .addParams("name", "yue")
                .addHeader("Cookie", "testCookie")
                .tag("post")
                .id(345)
                .build()
                .execute(new MyStringCallback());

    }

    public void ok_upload(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "test.avi");
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
            return;
        }
        OkUtils.uploadFile()
                .url("http://192.168.43.50:8080/Demo/upload")
                .addFile(file)
                .build()
                .execute(new MyStringCallback());

    }

    public void post_form(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        File file1 = new File(Environment.getExternalStorageDirectory(), "test.avi");
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
            return;
        }

        String url = "http://192.168.43.50:8080/Demo/upload";
        OkUtils.postForm()
                .url(url)
                .addFile("mFile", "asd.jpg", file)
                .addFile("mFile","test.mp4",file1)
//                .addParams("userName", "yuezengdi")
                .build()
                .execute(new MyStringCallback());
    }

    public void ok_download(View view) {
        OkUtils.get()
                .url(BASE_URL + "Demo/download")
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "test.jpg") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        L.e(e.toString());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        L.d(response.getAbsolutePath());
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        L.d(progress + "");
                    }
                });
    }
}
