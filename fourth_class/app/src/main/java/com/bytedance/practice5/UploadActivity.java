package com.bytedance.practice5;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.practice5.model.Message;
import com.bytedance.practice5.model.MessageListResponse;
import com.bytedance.practice5.model.UploadResponse;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {
    private static final String TAG = "chapter5";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    private static final int REQUEST_CODE_COVER_IMAGE = 101;
    private static final String COVER_IMAGE_TYPE = "image/*";
    private IApi api;
    private Uri coverImageUri;
    private String path;
    private SimpleDraweeView coverSD;
    private EditText toEditText;
    private EditText contentEditText;
    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNetwork();
        setContentView(R.layout.activity_upload);
        coverSD = findViewById(R.id.sd_cover);
        toEditText = findViewById(R.id.et_to);
        contentEditText = findViewById(R.id.et_content);
        findViewById(R.id.btn_cover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(REQUEST_CODE_COVER_IMAGE, COVER_IMAGE_TYPE, "选择图片");
            }
        });


        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d (TAG, "I'm going to submit");
                submit();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_COVER_IMAGE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverImageUri = data.getData();
                path = data.getData().getPath();
                coverSD.setImageURI(coverImageUri);

                if (coverImageUri != null) {
                    Log.d(TAG, "pick cover image " + coverImageUri.toString());
                } else {
                    Log.d(TAG, "uri2File fail " + data.getData());
                }

            } else {
                Log.d(TAG, "file pick fail");
            }
        }
    }

    private void initNetwork() {
        //TODO 3
        // 创建Retrofit实例
        // 生成api对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-sjtu-camp-2021.bytedance.com/homework/invoke/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create (IApi.class);
    }

    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    private void submit() {
        byte[] coverImageData = readDataFromUri(coverImageUri);
        if (coverImageData == null || coverImageData.length == 0) {
            Toast.makeText(this, "封面不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        String to = toEditText.getText().toString();
        if (TextUtils.isEmpty(to)) {
            Toast.makeText(this, "请输入TA的名字", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入想要对TA说的话", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( coverImageData.length >= MAX_FILE_SIZE) {
            Toast.makeText(this, "文件过大", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO 5
        // 使用api.submitMessage()方法提交留言
        // 如果提交成功则关闭activity，否则弹出toast
        MultipartBody.Part from_part = MultipartBody.Part.createFormData ("from", Constants.USER_NAME);
        MultipartBody.Part to_part = MultipartBody.Part.createFormData ("to", to);
        MultipartBody.Part content_part = MultipartBody.Part.createFormData ("content", content);
        MultipartBody.Part image_part = MultipartBody.Part.createFormData ("image", "myImage.jpg",
                RequestBody.create(MediaType.parse("multipart/form-data"), coverImageData));
        Call<UploadResponse> get = api.submitMessage (Constants.STUDENT_ID, "",
                from_part, to_part, content_part, image_part, Constants.token);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<UploadResponse> response = get.execute();
                    if (response.isSuccessful()) {
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText (getApplicationContext(), "文件过大", Toast.LENGTH_SHORT).show();
                }

            }
        }).start();
    }

    // TODO 7 选做 用URLConnection的方式实现提交 （没完成）
    private void submitMessageWithURLConnection(){
        byte[] coverImageData = readDataFromUri(coverImageUri);
        if (coverImageData == null || coverImageData.length == 0) {
            Toast.makeText(this, "封面不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        String to = toEditText.getText().toString();
        if (TextUtils.isEmpty(to)) {
            Toast.makeText(this, "请输入TA的名字", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入想要对TA说的话", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( coverImageData.length >= MAX_FILE_SIZE) {
            Toast.makeText(this, "文件过大", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlStr =
                        String.format(Constants.BASE_URL + "messages?student_id=" + Constants.STUDENT_ID + "&extra_value=");
                File mImage;
                Log.d (TAG, "here1");
                try {
                    Log.d (TAG, "here2");
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout (6000);

                    conn.setRequestMethod ("POST");

                    conn.setRequestProperty ("token", Constants.token);

                    StringBuilder sb = new StringBuilder ();
                    sb.append ("\r\n--" + BOUNDARY + "\r\n");
                    sb.append ("Content-Disposition: form-data; name=\"from\"\r\n");
                    sb.append ("\r\n");
                    sb.append (Constants.USER_NAME + "\r\n");

                    sb.append ("--" + BOUNDARY + "\r\n");
                    sb.append ("Content-Disposition: form-data; name=\"to\"\r\n");
                    sb.append ("\r\n");
                    sb.append (to + "\r\n");

                    sb.append ("--" + BOUNDARY + "\r\n");
                    sb.append ("Content-Disposition: form-data; name=\"content\"\r\n");
                    sb.append ("\r\n");
                    sb.append (content + "\r\n");

                    sb.append ("--" + BOUNDARY + "\r\n");
                    sb.append ("Content-Disposition: form-data; name=\"image\"; filename=\"myImage.jpg\"\r\n");
                    sb.append ("Content-Type: image/jpeg");
                    sb.append ("\r\n");

                    byte[] headerInfo = sb.toString ().getBytes ("UTF-8");
                    byte[] endInfo = ("\r\n--" + BOUNDARY + "\r\n").getBytes ("UTF-8");

                    conn.setRequestProperty ("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);;
                    //conn.setRequestProperty ("Content-Length", String.valueOf (headerInfo.length + mImage.length () + endInfo.length));
                    conn.setDoOutput (true);

                    OutputStream out = conn.getOutputStream ();
                    InputStream in = getContentResolver().openInputStream(coverImageUri);

                    out.write (headerInfo);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) != -1) {
                        out.write (buf, 0, len);
                    }
                    out.write (endInfo);
                    in.close ();
                    out.close ();

                    if ( conn.getResponseCode() == 200) {
                        in = conn.getInputStream ();
                        BufferedReader reader = new BufferedReader (new InputStreamReader (in, StandardCharsets.UTF_8));

                        UploadResponse uploadResponse = new Gson().fromJson (reader, UploadResponse.class);
                        Log.d (TAG, String.valueOf(uploadResponse.success));

                        reader.close();
                        in.close();
                    } else {
                        // 错误处理
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d (TAG, e.toString());
                }
            }
        }).start();
    }

    private byte[] readDataFromUri(Uri uri) {
        byte[] data = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            data = Util.inputStream2bytes(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
