package com.task.activity;

import java.io.Serializable;

import com.socket.client.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TopoEditActivity extends Activity {

	private TextView title;
	private Button backBtn;
	private WebView contentWebView = null;
	//private TextView msgView = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//���ر���
		setContentView(R.layout.activity_task_subscrible_layout);
		//initialTitles();
		
		contentWebView = (WebView) findViewById(R.id.webview);
		//msgView = (TextView) findViewById(R.id.msg);
		// ����javascript
		contentWebView.getSettings().setJavaScriptEnabled(true);
		contentWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                    
                    return false;
            }
		});
		// ��assetsĿ¼����ļ���html
		//contentWebView.loadUrl("http://10.108.100.199:8093/TRAK/topomaps!ReadTopoList.action");
		
		contentWebView.loadUrl("http://10.108.100.199:8093/TRAK/MobileTest.html");
		//contentWebView.loadUrl("file:///android_asset/wst.html");
		contentWebView.addJavascriptInterface(this, "cc");
	}

	private void initialTitles() {
		title = (TextView)findViewById(R.id.tasktitlepanel_titleTextView);
		backBtn = (Button)findViewById(R.id.tasktitlepanel_leftBtn);
		title.setText("���˱༭");
		backBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				TopoEditActivity.this.finish();
			}
		});
	}
}
