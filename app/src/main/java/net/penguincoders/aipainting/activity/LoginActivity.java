package net.penguincoders.aipainting.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dou361.dialogui.DialogUIUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.reponse.UserResultResponse;
import net.penguincoders.aipainting.util.ApiService;
import net.penguincoders.aipainting.util.RetrofitClient;
import net.penguincoders.aipainting.util.StatusBarUtil;
import net.penguincoders.aipainting.util.UserIdCacher;
import net.penguincoders.aipainting.util.UserResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //    private ImageView settingLoginExitBtn;
    private EditText LoginAccount;
    private EditText LoginPassword;
    private Boolean accountFlag = false;
    private Boolean passwordFlag = false;
    private Button LoginGoBtn;
    private TextView LoginPhoneBtn;
    private TextView RegisterBtn;

    private LinearLayout LoginQQBtn;
    private LinearLayout LoginWeChatBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);
        //隐藏系统状态栏
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
        DialogUIUtils.init(this);
    }

    private void init() {
        //获取页面中的组件
        LoginAccount= findViewById(R.id.LoginAccount);
        LoginPassword= findViewById(R.id.LoginPassword);
        LoginGoBtn= findViewById(R.id.LoginGoBtn);
        LoginGoBtn.setEnabled(false);
        LoginPhoneBtn = findViewById(R.id.LoginPhoneBtn);
        LoginQQBtn= findViewById(R.id.LoginQQBtn);
        LoginWeChatBtn= findViewById(R.id.LoginWeChatBtn);
        RegisterBtn=findViewById(R.id.RegisterBtn);
    }

    private void initListener() {

        LoginGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的账号和密码
                String account = LoginAccount.getText().toString();
                String password = LoginPassword.getText().toString();

                // 执行登录逻辑，发送账号和密码到服务器进行验证
                performLogin(account, password);

                // 如果登陆成功，跳转到主页  NOTES: has been moved to performLogin
//                Intent HomeIntent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(HomeIntent);
            }
        });
        LoginPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到手机免密登录页面
                Intent loginPhoneIntent = new Intent(LoginActivity.this, PhoneActivity.class);
                startActivity(loginPhoneIntent);
            }
        });
        LoginAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                accountFlag = s.length() > 0;
                if (accountFlag && passwordFlag) {
                    LoginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    LoginGoBtn.setEnabled(true);
                } else {
                    LoginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    LoginGoBtn.setEnabled(false);
                }
            }
        });

        LoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordFlag = s.length() > 0;
                if (accountFlag && passwordFlag) {
                    LoginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));
                    LoginGoBtn.setEnabled(true);
                } else {
                    LoginGoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_pale_circle));
                    LoginGoBtn.setEnabled(false);
                }
            }
        });
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册页面
                Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(RegisterIntent);
            }
        });
        LoginQQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });
        LoginWeChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showToastCenter("请使用手机免登录进行登录");
            }
        });

    }

    void performLogin(String account, String password) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.login(account, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonResponse = response.body().string();
                        // 解析 JSON 数据
                        Gson gson = new Gson();
                        UserResultResponse userResultResponse = gson.fromJson(jsonResponse, UserResultResponse.class);

                        // 现在你可以获取 userResult 和 userId 的值
                        UserResult result = UserResult.valueOf(userResultResponse.getResult());
                        String userId = userResultResponse.getUserId();

                        // 根据 userResult 的值进行适当的处理
                        switch (result) {
                            case SUCCESS:
                                // save the id into local storage
                                SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
                                UserIdCacher userIdCacher = new UserIdCacher(sharedPreferences);
                                userIdCacher.saveUserId(userId); // 保存用户ID
                                // hint
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                // jump to the main
                                Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(homeIntent);
                                break;
                            case INVALID_CREDENTIALS:
                                // 无效凭据，处理逻辑
                                Toast.makeText(LoginActivity.this, "登录失败，请检查账号和密码", Toast.LENGTH_SHORT).show();
                                break;
                            case ACCOUNT_NOT_FOUND:
                                // 处理账号未找到情况
                                Toast.makeText(LoginActivity.this, "用户不存在，请先注册", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                // 其他情况，处理逻辑
                                Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败，处理逻辑
                    Toast.makeText(LoginActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的逻辑
                Toast.makeText(LoginActivity.this, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

