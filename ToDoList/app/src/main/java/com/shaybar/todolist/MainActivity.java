package com.shaybar.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.shaybar.todolist.services.TagService;
import com.shaybar.todolist.services.TaskService;

public class MainActivity extends AppCompatActivity {
    WebView mainWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TaskService taskService = new TaskService(this);
        TagService tagService = new TagService(this);
        mainWebView = (WebView) findViewById(R.id.webView);
        mainWebView.setWebChromeClient(new WebChromeClient());
        WebSettings ws = mainWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccessFromFileURLs(true);
        ws.setAllowUniversalAccessFromFileURLs(true);

        mainWebView.addJavascriptInterface(taskService, "TaskService");
        mainWebView.addJavascriptInterface(tagService, "TagService");
        mainWebView.loadUrl("file:////android_asset/index.html");
    }
}
