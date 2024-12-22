package net.penguincoders.aipainting.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dou361.dialogui.DialogUIUtils;
import com.jyn.vcview.VerificationCodeView;

import net.penguincoders.aipainting.R;

import net.penguincoders.aipainting.util.ApiService;
import net.penguincoders.aipainting.util.RetrofitClient;
import net.penguincoders.aipainting.util.StatusBarUtil;
import net.penguincoders.aipainting.util.UserResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeActivity extends AppCompatActivity {

    private ImageView CodeExitBtn;
    private TextView CodePhone;
    private VerificationCodeView CodeInput;
    private TextView CodeResend;

    private String phone;
    private int resendTiming = 60;
    private String resendString;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        phone = getIntent().getStringExtra("phone");

        init();
        initListener();
        //验证码重新获取计时
        resendTiming();
    }

    private void init() {
        CodeExitBtn = findViewById(R.id.CodeExitBtn);
        CodePhone = findViewById(R.id.CodePhone);
        CodePhone.setText(phone);
        CodeInput = findViewById(R.id.CodeInput);
        CodeResend = findViewById(R.id.CodeResend);

    }

    private void initListener() {
        //返回按钮
        CodeExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CodeInput.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onComplete(String content) {
                dialog = DialogUIUtils.showLoading(CodeActivity.this,
                                "验证中...", false, false,
                                false, false)
                        .show();
                //若验证成功，则进入主界面，本部分代码在输入逻辑补充后可删除
                dialog.dismiss();

                // 验证码输入之后的判断逻辑
                performSmsLogin(phone, content);

            }
        });
        CodeResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendTiming = 60;
                dialog = DialogUIUtils.showLoading(CodeActivity.this,
                                "发送中", false, false,
                                false, false)
                        .show();

                // 验证码发送逻辑
                performSendCode(phone);

            }
        });
    }

    private void resendTiming() {
        CodeResend.setEnabled(false);
        CodeResend.setTextColor(getResources().getColor(R.color.colorDefaultText));
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = resendTiming; i > 0; i--) {
                    resendString = i + " 秒后可重新获取";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CodeResend.setText(resendString);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CodeResend.setEnabled(true);
                        CodeResend.setTextColor(getResources().getColor(R.color.colorTheme));
                        CodeResend.setText("重新获取验证码");
                    }
                });
            }
        }).start();
    }

    private void performSmsLogin(String phoneNumber, String smsCode) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.smsLogin(phoneNumber, smsCode);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = null;
                    try {
                        jsonResponse = response.body().string();
                        jsonResponse = jsonResponse.replaceAll("\"", "");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    UserResult result = UserResult.valueOf(jsonResponse);

                    if (result == UserResult.SUCCESS) {
                        // 登录成功，执行相应的逻辑
                        Toast.makeText(CodeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(CodeActivity.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                    } else if (result == UserResult.INVALID_CREDENTIALS) {
                        // 无效凭据，处理逻辑
                        Toast.makeText(CodeActivity.this, "无效的验证码，请重新输入", Toast.LENGTH_SHORT).show();
                    } else if (result == UserResult.ACCOUNT_NOT_FOUND) {
                        // 其他用户登录失败情况，处理逻辑
                        Toast.makeText(CodeActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CodeActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }
                    // 可以继续处理其他枚举值
                } else {
                    // 请求失败，处理逻辑
                    Toast.makeText(CodeActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的逻辑
                Toast.makeText(CodeActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CodeActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else if (result == UserResult.ACCOUNT_NOT_FOUND) {
                        // 后端发送验证码失败，执行相应的逻辑，例如显示错误消息
                        Toast.makeText(CodeActivity.this, "用户未注册，请先注册！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CodeActivity.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                    }
                    // 还可以处理其他枚举值
                } else {
                    // 请求失败，处理逻辑
                    Toast.makeText(CodeActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的逻辑
                Toast.makeText(CodeActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

}