package net.penguincoders.aipainting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.fragment.AccountFragment;
import net.penguincoders.aipainting.fragment.CreateFragment;
import net.penguincoders.aipainting.fragment.ExporeFragment;
import net.penguincoders.aipainting.fragment.HomeFragment;
import net.penguincoders.aipainting.fragment.OptionFragment;
import net.penguincoders.aipainting.util.StatusBarUtil;

import com.next.easynavigation.constant.Anim;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private String[] tabText = {"随心作画", "创作", "个人中心"};
    private int[] normalIcon = {R.mipmap.paint_normal,R.mipmap.circle_normal, R.mipmap.explore_normal};
    private int[] selectIcon = {R.mipmap.paint_select, R.mipmap.circle_select,R.mipmap.explore_select};
    private List<Fragment> fragments = new ArrayList<>();
    private EasyNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setStatusBarMode(this, false, R.color.colorBack);
        //隐藏系统状态栏
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        //底部导航
        // Hide the system status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.statusBars());
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        initNav();
    }
    private void initNav() {
        fragments.add(new HomeFragment());
        fragments.add(new CreateFragment());
        //fragments.add(new ExporeFragment());
        fragments.add(new AccountFragment());
        //fragments.add(new CircleFragment());
        navigationBar = findViewById(R.id.navigationBar);
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .iconSize(22)     //Tab图标大小
                .tabTextSize(12)   //Tab文字大小
                .normalTextColor(getResources().getColor(R.color.colorDefaultText))   //Tab未选中时字体颜色
                .selectTextColor(getResources().getColor(R.color.colorTheme))   //Tab选中时字体颜色
                .navigationBackground(getResources().getColor(R.color.colorBotBarBack))   //导航栏背景色
                .lineColor(getResources().getColor(R.color.colorTopLine)) //分割线颜色
                .smoothScroll(false)  //点击Tab  Viewpager切换是否有动画
                .canScroll(false)    //Viewpager能否左右滑动
                .anim(Anim.ZoomIn)  //点击Tab时的动画
                .hintPointLeft(-3)  //调节提示红点的位置hintPointLeft hintPointTop（看文档说明）
                .hintPointTop(-7)
                .hintPointSize(6)    //提示红点的大小
                .msgPointLeft(-10)  //调节数字消息的位置msgPointLeft msgPointTop（看文档说明）
                .msgPointTop(-15)
                .msgPointTextSize(9)  //数字消息中字体大小
                .msgPointSize(18)    //数字消息红色背景的大小
//                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
//                    @Override
//                    public boolean onTabClickEvent(View view, int i) {
//                        if (i == 0) {
//                           StatusBarUtil.setStatusBarMode(MainActivity.this, false, R.color.colorTheme);
//                        } else if (i==1) {
//                            StatusBarUtil.setStatusBarMode(MainActivity.this, false, R.color.purple_200);
//                        } else if(i==3){
//                            StatusBarUtil.setStatusBarMode(MainActivity.this, false, R.color.colorDarkText);
//                        }
//                        else {
//                            StatusBarUtil.setStatusBarMode(MainActivity.this, true, R.color.colorBack);
//                        }
//                        return false;
//                    }
//                })
                .build();
//        navigationBar.getmViewPager().setOffscreenPageLimit(4);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}