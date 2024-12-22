package net.penguincoders.aipainting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.jyn.vcview.VerificationCodeView;

import net.penguincoders.aipainting.R;

import net.penguincoders.aipainting.reponse.UserResultResponse;
import net.penguincoders.aipainting.util.ApiService;
import net.penguincoders.aipainting.util.RetrofitClient;
import net.penguincoders.aipainting.util.StatusBarUtil;
import net.penguincoders.aipainting.util.UserIdCacher;
import net.penguincoders.aipainting.util.UserResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private String phone;
    private EditText SetPhone;
    private EditText SetPassword;
    private EditText VerifyPassword;
    private EditText VerifyCode;
    private int resendTiming = 60;
    private String resendString;
    private TextView SendCode;
    private Button SetGoBtn;
    private ImageView PhoneExitBtn;
    private LinearLayout PhoneQQBtn;
    private LinearLayout PhoneWeChatBtn;
    boolean passwordflag;
    boolean phoneflag;
    private Dialog dialog;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
        DialogUIUtils.init(this);

        passwordflag=false;


    }

    private void init() {
        SetGoBtn=findViewById(R.id.RegisterGoBtn);
        SetPhone = findViewById(R.id.SetPhone);
        SetPassword = findViewById(R.id.SetPassword);
        VerifyPassword = findViewById(R.id.VerifyPassword);
        VerifyCode = findViewById(R.id.VerifyCode);
        SendCode = findViewById(R.id.SendCode);
        PhoneQQBtn = findViewById(R.id.RegisterQQBtn);
        PhoneWeChatBtn = findViewById(R.id.RegisterWeChatBtn);
        PhoneExitBtn = findViewById(R.id.RegisterExitBtn);

    }

    private void initListener() {
        PhoneExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SetGoBtn.setOnClickListener(new View.OnClickListener() {  // FIXME：what is this ?
            @Override
            public void onClick(View v) {

                    // TODO perform register.
                    content=VerifyCode.getText().toString();
                    String password = SetPassword.getText().toString();
                    performSmsLogin(phone, password, content);


            }
        });

        SetPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //手机号输入合法可以发送验证码
                if (isMobile(s.toString())) {
                    SendCode.setEnabled(true);
                    phoneflag=true;
                } else {
                    SendCode.setEnabled(false);
                    phoneflag=false;
                }
            }
        });

        SendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge();
                if(phoneflag==false)
                    DialogUIUtils.showToastCenter("非法手机号");
                else {
                    resendTiming = 60;
                    // 验证码发送逻辑
                    phone = SetPhone.getText().toString();
                    performSendCode(phone);
                    //验证码重新获取计时
                    resendTiming();
                }

            }
        });
        VerifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //judge();
            }
        });



//        VerifyCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {  // TODO：这里是不是要按个按钮再验证？
//                content=s.toString();
//                performSmsLogin(phone,content);
//            }
//
//        });

        PhoneQQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
        PhoneWeChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });

    }
    public void judge()
    {
        if (!SetPassword.getText().toString().equals(VerifyPassword.getText().toString())) {
            VerifyPassword.setText("");
            DialogUIUtils.showToastCenter( "密码输入不一致");
        }
        else {
            passwordflag = true;
            SetGoBtn.setEnabled(true);
            SetGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
        }
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

    private void resendTiming() {
        SendCode.setEnabled(false);
        SendCode.setTextColor(getResources().getColor(R.color.colorDefaultText));
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = resendTiming; i > 0; i--) {
                    resendString = i + " 秒后可重新获取";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SendCode.setText(resendString);
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
                        SendCode.setEnabled(true);
                        SendCode.setTextColor(getResources().getColor(R.color.colorTheme));
                        SendCode.setText("重新获取验证码");
                    }
                });
            }
        }).start();
    }

    private void performSendCode(String phoneNumber) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.sendSmsCodeInRegister(phoneNumber);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // handle the response
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
                        Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else if (result == UserResult.ACCOUNT_EXISTS) {
                        Toast.makeText(RegisterActivity.this, "用户已注册！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                    }
                    // 还可以处理其他枚举值
                } else {
                    // 请求失败，处理逻辑
                    Toast.makeText(RegisterActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的逻辑
                Toast.makeText(RegisterActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSmsLogin(String phoneNumber, String password, String smsCode) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.smsLoginRegister(phoneNumber, password, smsCode);

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
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                    } else if (result == UserResult.INVALID_CREDENTIALS) {
                        // 无效凭据，处理逻辑
                        Toast.makeText(RegisterActivity.this, "无效的验证码，请重新输入", Toast.LENGTH_SHORT).show();
                    } else if (result == UserResult.ACCOUNT_NOT_FOUND) {
                        // 其他用户登录失败情况，处理逻辑
                        Toast.makeText(RegisterActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }
                    // 可以继续处理其他枚举值
                } else {
                    // 请求失败，处理逻辑
                    Toast.makeText(RegisterActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的逻辑
                Toast.makeText(RegisterActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
