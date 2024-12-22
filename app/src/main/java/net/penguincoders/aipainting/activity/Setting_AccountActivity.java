package net.penguincoders.aipainting.activity;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.reponse.UserProfileResponse;
import net.penguincoders.aipainting.util.ApiService;
import net.penguincoders.aipainting.util.RetrofitClient;
import net.penguincoders.aipainting.util.StatusBarUtil;
import net.penguincoders.aipainting.util.UserIdCacher;
import net.penguincoders.aipainting.util.UserResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setting_AccountActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CODE = 1;

    private static final int FILE_PICKER_REQUEST = 1;

    private ImageView settingAccountBackBtn;
    private TextView settingAccountSaveBtn;
    private ImageView settingAccountAvatar;
    private EditText settingAccountName;
    private EditText settingAccountGender;
    private TextView settingAccountBirth;
    private TextView settingAccountIntro;
    private TextView settingAccountGit;
    private List<Uri> uriSelected;
    private String avatarName = "";
    String base64String = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        init();
        initListener();
        initData();
        DialogUIUtils.init(this);
    }


    private void initData() {  // TODO: now you can use this function
        // TODO: how to use：see the performGetProfile method.
        performGetProfile();
    }

    private void init() {
        settingAccountBackBtn = findViewById(R.id.settingAccountBackBtn);
        settingAccountSaveBtn = findViewById(R.id.settingAccountSaveBtn);
        settingAccountAvatar = findViewById(R.id.settingAccountAvatar);
        settingAccountName = findViewById(R.id.settingAccountName);
        settingAccountGender = findViewById(R.id.settingAccountGender);
        settingAccountIntro = findViewById(R.id.settingAccountIntro);
        settingAccountBirth = findViewById(R.id.settingAccountBirth);
        settingAccountGit = findViewById(R.id.settingGitAddr);

    }

    private void initListener() {
        settingAccountBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //TODO 头像上传加载

        settingAccountAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });


        settingAccountSaveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 将 Picture1 转换为 Bitmap
                settingAccountAvatar.setDrawingCacheEnabled(true);
                settingAccountAvatar.buildDrawingCache();
                Bitmap bitmap = settingAccountAvatar.getDrawingCache();

                // 将 Bitmap 编码为 Base64 字符串
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageBytes = outputStream.toByteArray();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    base64String = Base64.getEncoder().encodeToString(imageBytes);
                }

                // 清理 Bitmap
                bitmap.recycle();
                // 此时，base64String 包含了图像的 Base64 编码字符串
                //performSendInputAsync("2", base64String);
                update();
                DialogUIUtils.showToastCenter("资料保存成功");
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                settingAccountAvatar.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {

        HashMap<String, String> hm = new HashMap<>();
        hm.put("nickName", settingAccountName.getText().toString());
        hm.put("gender", settingAccountGender.getText().toString().equals("男") ? "male" : "female");
        hm.put("avater",base64String);
        // TODO: upload avater should be an independent function
//        hm.put("head", avatarName);  // FIXME: only support nickname/gender/avater edit currently
//        hm.put("birthday", settingAccountBirth.getText().toString());
//        hm.put("git",settingAccountGit.getText().toString());
//        hm.put("selfdes", settingAccountIntro.getText().toString());

        // get userId
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        UserIdCacher userIdCacher = new UserIdCacher(sharedPreferences);
        String userId = userIdCacher.getUserId();
        if (!userId.isEmpty()) {  // userId 存在
            // 发送更新请求，将 hm 中的数据发送到 updateUrl
            performUpdate(userId, hm.get("nickName"), hm.get("gender"));
        } else {
            // userId 不存在，可能用户尚未登录或已注销
            // 在此处执行相应的处理逻辑，例如要求用户登录
            // TODO: 输出一下错误的信息
            DialogUIUtils.showToastCenter("用户不存在，请登录！");

        }
    }


    private void performUpdate(String userId, String nickName, String gender){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateUserInfo(userId, nickName, gender);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 请求成功，处理响应
                    String jsonResponse = null;
                    try {
                        jsonResponse = response.body().string();
                        jsonResponse = jsonResponse.replaceAll("\"", "");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    UserResult result = UserResult.valueOf(jsonResponse);

                    if (result == UserResult.SUCCESS) {
                        Toast.makeText(Setting_AccountActivity.this, "更新信息成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Setting_AccountActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // 更新失败，处理逻辑
                    Toast.makeText(Setting_AccountActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的逻辑
                Toast.makeText(Setting_AccountActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performGetProfile() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        // get cached userId
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        UserIdCacher userIdCacher = new UserIdCacher(sharedPreferences);
        String userId = userIdCacher.getUserId();

        Call<ResponseBody> call = apiService.getProfile(userId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 请求成功，处理响应
                    String jsonResponse = null;
                    try {
                        jsonResponse = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // parse the response
                    Gson gson = new Gson();
                    UserProfileResponse userProfileResponse = gson.fromJson(jsonResponse, UserProfileResponse.class);

                    // TODO: the profile is here.
                    String nickname = userProfileResponse.getNickname();
                    String gender = userProfileResponse.getGender();
                    String avater = userProfileResponse.getAvater();
                    setshowing(nickname,gender,avater);

                } else {
                    // 请求失败，处理逻辑
                    Toast.makeText(Setting_AccountActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的逻辑
                Toast.makeText(Setting_AccountActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setshowing(String nickname,String gender,String avater){
        settingAccountName.setText(nickname);
        settingAccountGender.setText(gender);
//        byte[] imageData = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            imageData = Base64.getDecoder().decode(avater);
//        }
//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//        settingAccountAvatar.setImageBitmap(bitmap);
        //settingAccountAvatar.setImageBitmap(avater);

    }

}