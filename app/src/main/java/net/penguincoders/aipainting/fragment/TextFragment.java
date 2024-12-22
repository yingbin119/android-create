package net.penguincoders.aipainting.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import net.penguincoders.aipainting.reponse.Text2ImgResponse;
import net.penguincoders.aipainting.reponse.UserResultResponse;
import net.penguincoders.aipainting.util.ApiService;
import net.penguincoders.aipainting.util.RetrofitClient;
import net.penguincoders.aipainting.util.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextFragment extends Fragment {

    private View view;
    private Context context;

    private RelativeLayout ClearBtn;
    private EditText InputText;
    private FloatingActionButton fab;
    String TextDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_text, container, false);
        context = getActivity();
        init();
        initListener();
        return view;
    }



    private void init() {
        ClearBtn = view.findViewById(R.id.ClearBtn);
        InputText = view.findViewById(R.id.InputText);
        fab = view.findViewById(R.id.fab);
        TextDescription=null;
    }

    private void initListener() {
        ClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputText.setText("");

            }
        });
        InputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to perform
//                Intent intent = new Intent(context, Show_PictureActivity.class);
//                intent.putExtra("TextDescription",TextDescription);
//                context.startActivity(intent);

                // currently, we use applicationId = 1
                TextDescription = InputText.getText().toString();
                if(TextDescription.length()!=0){
                    performSendInputAsync("1", TextDescription);
                } else {
                    Toast.makeText(TextFragment.this.getContext(), "请输入文字描述后再尝试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


//    private void performSendInputSync(String applicationId, String textDescription) {  // test version
//        // 创建 JSON 数据
//        JsonObject jsonData = new JsonObject();
//        jsonData.addProperty("applicationId", applicationId);
//        jsonData.addProperty("text", textDescription);
//        // 将 JSON 数据放入 RequestBody
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonData.toString());
//
//        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//        Call<ResponseBody> call = apiService.sendInput(requestBody);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    // 请求成功，处理响应
//                    String jsonResponse = null;
//                    try {
//                        jsonResponse = response.body().string();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    // 解析 JSON 数据
//                    Gson gson = new Gson();
//                    Text2ImgResponse text2ImgResponse = gson.fromJson(jsonResponse, Text2ImgResponse.class);
//
//                    // get img data
//                    String base64Image = text2ImgResponse.getImg();
//                    // 这里您可以解码 Base64 图像字符串并执行相应的逻辑
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                        byte[] imageData = Base64.getDecoder().decode(base64Image);
//                    }
//
//                    // TODO:现在，imageData 包含解码后的图像数据，可以根据需要进行处理, 带到下一个界面
//                    Intent intent = new Intent(context, Show_PictureActivity.class);
//                    intent.putExtra("TextDescription",TextDescription);
//                    context.startActivity(intent);
//                } else {
//                    // 请求失败，处理逻辑
//                    Toast.makeText(TextFragment.this.getContext(), "请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                // 请求失败，关闭加载中的标志，并处理请求失败的逻辑
//                Toast.makeText(TextFragment.this.getContext(), "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    private void performSendInputAsync(String applicationId, String textDescription) {
        // 启动异步任务并传递参数
        new SendInputAsyncTask().execute(applicationId, textDescription);
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
            String textDescription = params[1];
            // 创建 JSON 数据
            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("applicationId", applicationId);
            jsonData.addProperty("prompt", textDescription);
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
                        Toast.makeText(TextFragment.this.getContext(), "请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // 请求失败，关闭加载中的标志，并处理请求失败的逻辑
                    Toast.makeText(TextFragment.this.getContext(), "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

            return null;
        }
    }

}