package com.example.qingliao;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;


public class Imglist extends Activity {

    private WebView webView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imglist);


        webView = findViewById(R.id.idWebView);

        LoadWebView();
    }

    //加载页面
    private void LoadWebView() {

        webView.loadUrl("http://122.51.104.232/html5/res/www/List.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//支持js交互，可以点击网页中按钮链接
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持js可以打开新的页面
        settings.setSupportZoom(true);//是否可以缩放，默认true
        settings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(false);//是否使用缓存
        settings.setDomStorageEnabled(true);//DOM Storage

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();//解决webview 加载https 出现没内容
            }
        });

    }

}
