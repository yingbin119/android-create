package net.penguincoders.aipainting.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.activity.AnalyseCvActivity;
import net.penguincoders.aipainting.activity.Text_PaintActivity;
import net.penguincoders.aipainting.util.ApiService;
import net.penguincoders.aipainting.util.DirectRetrofitClient;
import net.penguincoders.aipainting.util.FileUploadService;
import net.penguincoders.aipainting.util.RetrofitClient;

import net.penguincoders.aipainting.view.RoundedRectProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class LoadCvFragment extends Fragment {
    private View view;
    private int progress;
    private Timer timer;
    private RoundedRectProgressBar progressbar;
    private static final int FILE_PICKER_REQUEST = 3;
    private Context context;
    private Activity activity;
    RelativeLayout UploadCv;
    Button check;
    private Uri selectedFileUri;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_load_cv, container, false);
        context = getActivity();
        activity=getActivity();

        init();
        initListener();
        return view;
    }
    private void init() {
        UploadCv = view.findViewById(R.id.UploadCv);
        check = view.findViewById(R.id.AnalyseBtn);
        progressbar= view.findViewById(R.id.progress);
        check.setEnabled(false);
    }
    private void initListener() {
        UploadCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnalyseCvActivity.class);
                context.startActivity(intent);

            }
        });

    }
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_PICKER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            //TODO:上传到后端
            if (selectedFileUri != null) {
                // 将 selectedFileUri 转换为 File
                //String fileName = getFileName(selectedFileUri);
                //File fileToUpload = new File(selectedFileUri);

                ContentResolver contentResolver = context.getContentResolver();

                // 创建请求体
                RequestBody requestBody = null;
                try {
                    InputStream inputStream = contentResolver.openInputStream(selectedFileUri);
                    if (inputStream != null) {
                        requestBody = RequestBody.create(MediaType.parse(contentResolver.getType(selectedFileUri)), toByteArray(inputStream));
                    } else {
                        // 处理无法打开输入流的情况
                        Toast.makeText(LoadCvFragment.this.getContext(), "FAILED in open file", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                MultipartBody.Part filePart = null;
                try {
                    String fileName = getFileName(selectedFileUri);
                    byte[] fileNameBytes = fileName.getBytes("UTF-8"); // 将文件名转换为UTF-8编码的字节数组
                    String encodedFileName = new String(fileNameBytes, "ISO-8859-1"); // 使用ISO-8859-1编码将字节数组转换回字符串
                    filePart = MultipartBody.Part.createFormData("file", encodedFileName, requestBody);
//                    filePart = MultipartBody.Part.createFormData("file", URLEncoder.encode(getFileName(selectedFileUri), "utf-8"), requestBody);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                // 创建 Retrofit 服务实例
                Retrofit client = DirectRetrofitClient.getDirectClient();
                FileUploadService fileUploadService = client.create(FileUploadService.class);

                // 发起文件上传请求
                Call<ResponseBody> call = fileUploadService.uploadFile(filePart);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // 文件上传成功
                            Toast.makeText(LoadCvFragment.this.getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                            check.setText("等待分析……");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            checkUploadSuccess(fileUploadService);
                        } else {
                            // 文件上传失败
                            Toast.makeText(LoadCvFragment.this.getContext(), "FAILED", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // 处理上传失败的情况
                        Toast.makeText(LoadCvFragment.this.getContext(), "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        Context context = requireContext();

        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int ColumnIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if(ColumnIndex>0)
                        result = cursor.getString(ColumnIndex);
                }
            }
        }

        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }

        return result;
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return outputStream.toByteArray();
    }

    private void checkUploadSuccess(FileUploadService fileUploadService) {
        Call<ResponseBody> getCall = fileUploadService.emptyGetRequest();

        getCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 处理 GET 请求成功的情况
                    String jsonResponse = null;
                    try {
                        jsonResponse = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    JSONObject object = null;
                    String state = null;
                    try {
                        object = new JSONObject(jsonResponse);
                        state = object.getString("success");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if (state.equals("True")) {
                        Toast.makeText(LoadCvFragment.this.getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoadCvFragment.this.getContext(), "返回异常", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 处理 GET 请求失败的情况
                    Toast.makeText(LoadCvFragment.this.getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoadCvFragment.this.getContext(), "服务端分析中，上传成功", Toast.LENGTH_SHORT).show();
                check.setText("正在分析中……");
                startprogress();
//                check.setEnabled(true);
//                check.setText("点击查看简历反馈");
//                check.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
            }
        });
    }
    public void startprogress(){
        progress = 0;
        timer = new Timer();
        //以timer为参数，指定某个时间点执行线程
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progressbar.setProgress(progress);//设置当前进度
                progress ++;
                if (progress > 100) {
                    timer.cancel();
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            check.setEnabled(true);
                            check.setText("点击查看简历反馈");
                            check.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                        }
                    });
                }
            }
        }, 0, 600); //0秒后启动任务,以后每隔0.3秒执行一次线程

    }


}