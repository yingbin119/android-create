package net.penguincoders.aipainting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dou361.dialogui.DialogUIUtils;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.fragment.TextFragment;
import net.penguincoders.aipainting.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class Text_PaintActivity extends AppCompatActivity {

    private ImageView BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_paint);
        StatusBarUtil.setStatusBarMode(this, false, R.color.purple_200);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        // Hide the system status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.statusBars());
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        // 获取FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开始Fragment事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 创建TextFragment实例
        TextFragment textFragment = new TextFragment();

        // 将TextFragment添加到容器中
        fragmentTransaction.add(R.id.fragment_container, textFragment);

        // 提交事务
        fragmentTransaction.commit();
        init();
        initListener();
    }
    private void init() {
        BackBtn = findViewById(R.id.BackBtn);
    }

    private void initListener() {
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}