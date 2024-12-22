package net.penguincoders.aipainting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.fragment.LoadCvFragment;
import net.penguincoders.aipainting.fragment.PictureFragment;
import net.penguincoders.aipainting.util.StatusBarUtil;

public class LoadCvActivity extends AppCompatActivity {
    private ImageView BackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_cv);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorBack);
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
        LoadCvFragment loadCvFragment = new LoadCvFragment();

        // 将TextFragment添加到容器中
        fragmentTransaction.add(R.id.fragment_container, loadCvFragment);

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