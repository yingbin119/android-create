package net.penguincoders.aipainting.activity;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import androidx.appcompat.app.AppCompatActivity;
import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.util.StatusBarUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dou361.dialogui.DialogUIUtils;

public class SettingActivity extends AppCompatActivity {

    private ImageView settingBackBtn;
    private ImageView settingSetupBtn1;
    private ImageView settingAvatar;
    private TextView settingNickname;


    private RelativeLayout settingAccountBtn;
    private RelativeLayout settingLoveBtn;
    private RelativeLayout settingGitBtn;
    private RelativeLayout settingSetupBtn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        StatusBarUtil.setStatusBarMode(this, false, R.color.colorDarkText);
        //隐藏系统状态栏
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        init();
        initListener();
        DialogUIUtils.init(this);

    }
    //TODO:后端接口有关，待实现
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Glide.with(this)
//                .load(ApiUtil.USER_AVATAR)
//                .apply(bitmapTransform(new CircleCrop()))
//                .into(settingAvatar);
//        settingNickname.setText(ApiUtil.USER_NAME);
//    }

    private void init() {
        settingBackBtn = findViewById(R.id.settingBackBtn);
        settingSetupBtn1 = findViewById(R.id.settingSetupBtn1);
        settingAvatar = findViewById(R.id.settingAvatar);
        settingNickname = findViewById(R.id.settingNickname);
        settingAccountBtn = findViewById(R.id.settingAccountBtn);
        settingLoveBtn = findViewById(R.id.settingLoveBtn);
        settingGitBtn = findViewById(R.id.settingGitBtn);
        settingSetupBtn2 = findViewById(R.id.settingSetupBtn2);
    }

    private void initListener() {
        settingBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        settingSetupBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Setting_SetupActivity.class);
                startActivity(intent);
            }
        });
        settingAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Setting_AccountActivity.class);
                startActivity(intent);
            }
        });

        settingAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Setting_AccountActivity.class);
                startActivity(intent);
            }
        });
        settingLoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        settingGitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        settingSetupBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Setting_SetupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}