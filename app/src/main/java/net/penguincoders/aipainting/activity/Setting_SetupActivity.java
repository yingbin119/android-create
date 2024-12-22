package net.penguincoders.aipainting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dou361.dialogui.DialogUIUtils;
import com.suke.widget.SwitchButton;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.util.StatusBarUtil;

public class Setting_SetupActivity extends AppCompatActivity {

    private ImageView settingSetupBackBtn;
    private Button settingAccountExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_setup);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        init();
        initListener();
        DialogUIUtils.init(this);
    }

    private void init() {
        settingSetupBackBtn = findViewById(R.id.settingSetupBackBtn);
        settingAccountExit = findViewById(R.id.settingAccountExit);
    }

    private void initListener() {
        settingSetupBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        settingAccountExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("wd", MODE_PRIVATE).edit();
                editor.putBoolean("isPermit", false);
                editor.apply();
                Intent intent = new Intent(Setting_SetupActivity.this, PhoneActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}