package net.penguincoders.aipainting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.util.StatusBarUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Show_PictureActivity extends AppCompatActivity {

    private byte[] receivedImageData;
    private static final int PERMISSION_REQUEST_CODE = 1;

    //android13相册权限申请变量
    private static final String[] PERMISSIONS_STORAGE_33 = {Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.CAMERA};
    //请求状态码
    private static final int REQUEST_PERMISSION_CODE_33 = 2;
    private ImageView ReceivePicture;
    private Button SaveBtn;
    private FloatingActionButton BackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        StatusBarUtil.setStatusBarMode(this, true, R.color.Dark_Blue);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        // Hide the system status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.statusBars());
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        receivedImageData = getIntent().getByteArrayExtra("Receive");
        init();
        initListener();
    }
    private void init() {
        ReceivePicture= findViewById(R.id.PictureShow);
        SaveBtn= findViewById(R.id.saveButton);
        BackBtn=findViewById(R.id.BackBtn);
        // 检查并请求存储权限
        checkPermission();
        Bitmap bitmap;
        if (receivedImageData != null) {
            // 将字节数组转换为位图
            bitmap = BitmapFactory.decodeByteArray(receivedImageData, 0, receivedImageData.length);
            ReceivePicture.setImageBitmap(bitmap);
        } else {
            // 如果 receivedImageData 为 null，可以设置一个默认图像或者错误图像
            //ReceivePicture.setImageResource(R.drawable.error_image);
            BitmapDrawable drawable = (BitmapDrawable) ReceivePicture.getDrawable();
            bitmap = drawable.getBitmap();
            Toast.makeText(this, "图片生成失败", Toast.LENGTH_SHORT).show();
        }
        SaveBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_theme_circle));

    }
    private void initListener() {
        ReceivePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveImage();
            }
        });
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE_33, REQUEST_PERMISSION_CODE_33);
            } else {
                //android13以下的权限申请

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 存储权限已授予
                Toast.makeText(this, "存储权限已授予", Toast.LENGTH_SHORT).show();

            } else {
                // 存储权限被拒绝
                Toast.makeText(this, "存储权限被拒绝，无法保存图片", Toast.LENGTH_SHORT).show();
//                SaveBtn.setEnabled(false);
            }
        }
    }

    private void SaveImage() {

        BitmapDrawable drawable = (BitmapDrawable) ReceivePicture.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        String fileName = "my_image.jpg";
        File file;
        //读取系统相册路径

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String cameraPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/Camera";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            file = new File(cameraPath, fileName);
        }
        else {
            file = new File(directory, fileName);
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, null, null);

            Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
        }
    }

}