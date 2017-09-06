package com.hicc.cloud.teacher.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hicc.cloud.R;


public class NewsActivity extends AppCompatActivity {

    private WebView wv_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ininUI();
    }

    private void ininUI() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wv_news = (WebView) findViewById(R.id.wv_news);
        WebSettings settings = wv_news.getSettings();
        // 将图片调整至适合webview的大小
        settings.setUseWideViewPort(true);
        // 缩放至屏幕大小
        settings.setLoadWithOverviewMode(true);
        // 支持缩放
        settings.setSupportZoom(true);
        wv_news.loadUrl("http://www.hicc.cn/xynew/index.jhtml");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv_news.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv_news.canGoBack()) {
            wv_news.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wv_news.removeAllViews();
        wv_news.destroy();
    }
}
