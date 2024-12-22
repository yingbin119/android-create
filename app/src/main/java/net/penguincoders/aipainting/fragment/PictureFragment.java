package net.penguincoders.aipainting.fragment;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.activity.MainActivity;
import net.penguincoders.aipainting.activity.Picture_PaintActivty;
import net.penguincoders.aipainting.activity.Show_PictureActivity;
import net.penguincoders.aipainting.activity.Text_PaintActivity;
import net.penguincoders.aipainting.util.ApiService;
import net.penguincoders.aipainting.util.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureFragment extends Fragment {

    private View view;
    private Context context;
    private Activity activity;
    private static final int FILE_PICKER_REQUEST = 4;
    private RelativeLayout ClearBtn;
    private ImageView Picture1;
    private EditText InputText;
    private Button UploadPicture;
    String TextDescription;

    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_picture, container, false);
        context = getActivity();
        activity=getActivity();

        init();
        initListener();
        return view;
    }



    private void init() {
        ClearBtn = view.findViewById(R.id.ClearBtn);
        UploadPicture = view.findViewById(R.id.UploadPicture);
        Picture1=view.findViewById(R.id.Picture1);
        InputText=view.findViewById(R.id.InputText);
        fab = view.findViewById(R.id.fab);

    }

    private void initListener() {
        ClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picture1.setImageBitmap(null);

                UploadPicture.setVisibility(View.VISIBLE);

            }
        });
        UploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the gallery to pick an image
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, REQUEST_IMAGE_PICK);
                 openFilePicker();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 将 Picture1 转换为 Bitmap
                Picture1.setDrawingCacheEnabled(true);
                Picture1.buildDrawingCache();
                Bitmap bitmap = Picture1.getDrawingCache();

                TextDescription = InputText.getText().toString();

               // performSendInputAsync("1", TextDescription);
                // 将 Bitmap 编码为 Base64 字符串
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                String base64String = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    base64String = Base64.getEncoder().encodeToString(imageBytes);
                }

                // 清理 Bitmap
                bitmap.recycle();
                // 此时，base64String 包含了图像的 Base64 编码字符串
                performSendInputAsync("2", base64String);

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

        if (requestCode == FILE_PICKER_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Image selected from the gallery
            Uri selectedImageUri = data.getData();

            try {
                // Load and display the selected image
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImageUri);
                Picture1.setImageBitmap(bitmap);

                // Make the UploadPicture button not visible
                UploadPicture.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void performSendInputAsync(String applicationId, String imgBase64) {
        // 启动异步任务并传递参数
        new SendInputAsyncTask().execute(applicationId, imgBase64);
    }

    private class SendInputAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 在这里显示加载中的标志，例如显示一个 ProgressDialog
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            // 在这里执行网络请求，这部分代码与之前的 performSendInput 相同
            String applicationId = params[0];
            String imgBase64 = params[1];
            // 创建 JSON 数据
            JsonObject jsonData = new JsonObject();

            // 创建包含一个元素的列表
            List<String> initImages = new ArrayList<>();
            initImages.add(imgBase64);
            jsonData.addProperty("applicationId", applicationId);
//            jsonData.addProperty("init_images", imgBase64);
            jsonData.add("init_images", new Gson().toJsonTree(initImages));
            jsonData.addProperty("prompt", TextDescription);
            // 将 JSON 数据放入 RequestBody
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonData.toString());

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<ResponseBody> call = apiService.sendInput(requestBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        progressDialog.dismiss();
                        // 请求成功，处理响应
                        String jsonResponse = null;
                        try {
                            jsonResponse = response.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        // 使用 Gson 的 JsonParser 解析 JSON 字符串
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(jsonResponse);
                        String firstImage = null;
                        if (element.isJsonObject()) {
                            JsonObject jsonObject = element.getAsJsonObject();

                            if (jsonObject.has("images") && jsonObject.get("images").isJsonArray()) {
                                JsonArray imagesArray = jsonObject.get("images").getAsJsonArray();

                                if (imagesArray.size() > 0) {
                                    firstImage = imagesArray.get(0).getAsString();
                                    // 现在 firstImage 包含了 "xxxxx" 字符串
//                                    System.out.println("First Image: " + firstImage);
                                }
                            }
                        }
//                        JSONObject object = null;
//                        String base64Image = null;
//                        try {
//                            object = new JSONObject(jsonResponse);
//                            base64Image = object.getString("images");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }

//                        base64Image = base64Image.substring(2, base64Image.length()-2);

                        String base64Image = firstImage;
//                        // 解析 JSON 数据
//                        Gson gson = new Gson();
//                        Text2ImgResponse text2ImgResponse = gson.fromJson(jsonResponse, Text2ImgResponse.class);
//
//                        // get img data
//                        String base64Image = text2ImgResponse.getImg();
                        // 这里您可以解码 Base64 图像字符串并执行相应的逻辑
                        byte[] imageData = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            imageData = Base64.getDecoder().decode(base64Image);
                        }
                        // TODO:现在，imageData 包含解码后的图像数据，可以根据需要进行处理, 带到下一个界面
                        Intent intent = new Intent(context, Show_PictureActivity.class);
                        intent.putExtra("Receive", imageData);
                        context.startActivity(intent);
                    } else {
                        // 请求失败，处理逻辑
                        Toast.makeText(PictureFragment.this.getContext(), "请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // 请求失败，关闭加载中的标志，并处理请求失败的逻辑
                    Toast.makeText(PictureFragment.this.getContext(), "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

            return null;
        }
    }
}