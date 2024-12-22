package net.penguincoders.aipainting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;

import net.penguincoders.aipainting.R;

import net.penguincoders.aipainting.util.ApiService;
import net.penguincoders.aipainting.util.RetrofitClient;
import net.penguincoders.aipainting.util.StatusBarUtil;
import net.penguincoders.aipainting.util.UserResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneActivity extends AppCompatActivity {

    private EditText PhoneInput;
    private Button PhoneGoBtn;
    private ImageView PhoneExitBtn;
    private LinearLayout PhoneQQBtn;
    private LinearLayout PhoneWeChatBtn;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
        DialogUIUtils.init(this);

    }

    private void init() {
//        settingPhoneExitBtn = findViewById(R.id.settingPhoneExitBtn);
        PhoneInput = findViewById(R.id.PhoneInput);
        PhoneGoBtn = findViewById(R.id.PhoneGoBtn);
        PhoneGoBtn.setEnabled(false);
        PhoneQQBtn = findViewById(R.id.PhoneQQBtn);
        PhoneWeChatBtn = findViewById(R.id.PhoneWeChatBtn);
        PhoneExitBtn=findViewById(R.id.PhoneExitBtn);

    }

    private void initListener() {
        PhoneExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
           }
        });
        PhoneGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = PhoneInput.getText().toString();

                performSendCode(phoneNumber);

                // move to perform
//                Intent CodeIntent = new Intent(PhoneActivity.this, CodeActivity.class);
//                CodeIntent.putExtra("phone", PhoneInput.getText().toString());
//                startActivity(CodeIntent);
            }
        });
        PhoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                //手机号输入合法
                if (isMobile(s.toString())) {
                    PhoneGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    PhoneGoBtn.setEnabled(true);
                } else {
                    PhoneGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    PhoneGoBtn.setEnabled(false);
                }
            }
        });
        PhoneQQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
        PhoneWeChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });

    }

    private void performSendCode(String phoneNumber) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.sendSmsCode(phoneNumber);

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
                        // 后端成功发送验证码，执行相应的逻辑，例如显示提示消息
                        Toast.makeText(PhoneActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                        // jump
                        Intent CodeIntent = new Intent(PhoneActivity.this, CodeActivity.class);
                        CodeIntent.putExtra("phone", PhoneInput.getText().toString());
                        startActivity(CodeIntent);
                    } else if (result == UserResult.ACCOUNT_NOT_FOUND) {
                        // 后端发送验证码失败，执行相应的逻辑，例如显示错误消息
                        Toast.makeText(PhoneActivity.this, "用户未注册，请先注册！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhoneActivity.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                    }
                    // 还可以处理其他枚举值
                } else {
                    // 请求失败，处理逻辑
                    Toast.makeText(PhoneActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的逻辑
                Toast.makeText(PhoneActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static boolean isMobile(String str) {
        Pattern p;
        Matcher m;
        boolean b = false;
        String s2 = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";// 验证手机号
        p = Pattern.compile(s2);
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
}