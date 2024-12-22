package net.penguincoders.aipainting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.util.StatusBarUtil;

public class AnalyseCvActivity extends AppCompatActivity {
    WebView CvAnalysis;
    private WebSettings mWebSettings;
    private String mUrl = "http://10.12.52.33:5003";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_cv);
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

        initViews();
        initDatas();
    }
    private void initViews() {
        //获取页面中的组件
        CvAnalysis= findViewById(R.id.webview);
    }
    private void initDatas() {
        webSettings();
        webView();
    }
    private void webView() {
        //为了更好的支持表单
        CvAnalysis.setFocusable(true);
        CvAnalysis.setFocusableInTouchMode(true);
        CvAnalysis.requestFocus();
        CvAnalysis.setWebViewClient(new WebViewClient() {
            //目的是要让我们应用自己来加载网页，而不是交给浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        CvAnalysis.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }
        });
        CvAnalysis.clearCache(true);
        CvAnalysis.loadUrl(mUrl);
    }

    /**
     * web的相关设置
     */
    private void webSettings() {
        mWebSettings = CvAnalysis.getSettings();
        //让webview支持js
        mWebSettings.setJavaScriptEnabled(true);
        //设置是否支持缩放模式
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        // 是否显示+ -
        mWebSettings.setDisplayZoomControls(false);
        //判断是否存储
        mWebSettings.setDomStorageEnabled(true);

//        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        mWebSettings.setAllowFileAccess(true);
//        mWebSettings.setSupportMultipleWindows(true);
//        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        mWebSettings.setTextZoom(100);
//        mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

    }

    /**
     * back键处理
     */
    @Override
    public void onBackPressed() {
        if (CvAnalysis.canGoBack()) CvAnalysis.goBack();
        else super.onBackPressed();
    }

}