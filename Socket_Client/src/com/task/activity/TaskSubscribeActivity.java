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

public class TaskSubscribeActivity extends Activity {

	private TextView title;
	private Button backBtn;
	private WebView contentWebView = null;
	//private TextView msgView = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.activity_task_subscrible_layout);
		initialTitles();
		
		contentWebView = (WebView) findViewById(R.id.webview);
		//msgView = (TextView) findViewById(R.id.msg);
		// 启用javascript
		contentWebView.getSettings().setJavaScriptEnabled(true);
		contentWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                    
                    return false;
            }
		});
		// 从assets目录下面的加载html
		//contentWebView.loadUrl("http://10.108.100.199:8093/TRAK/list2.jsp");
		contentWebView.loadUrl("http://10.108.100.199:8093/TRAK/dashboard/index.html");
		//contentWebView.loadUrl("http://10.108.100.199:8093/TRAK/tasktopomaps!ReadTopoList.action");
		//contentWebView.loadUrl("file:///android_asset/wst.html");
		contentWebView.addJavascriptInterface(this, "cc");
	}

	private void initialTitles() {
		//title = (TextView)findViewById(R.id.tasktitlepanel_titleTextView);
		//backBtn = (Button)findViewById(R.id.tasktitlepanel_leftBtn);
		//title.setText("任务下发");
		/*
		backBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				TaskSubscribeActivity.this.finish();
			}
		});
	*/	
	}
	
	OnClickListener btnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			
			new AlertDialog.Builder(TaskSubscribeActivity.this).setTitle("单选框").setIcon(
				     android.R.drawable.ic_dialog_info).setSingleChoiceItems(
				     new String[] { "Item1", "Item2" }, 0,
				     new DialogInterface.OnClickListener() {
				      public void onClick(DialogInterface dialog, int which) {
				       dialog.dismiss();
				      }
				     }).setNegativeButton("取消", null).show();
			
			// 无参数调用
			contentWebView.loadUrl("javascript:javacalljs()");
			// 传递参数调用
			contentWebView.loadUrl("javascript:javacalljswithargs(" + "'hello world'" + ")");
		}
	};

	public void startFunction() {
		Toast.makeText(this, "js调用了java函数", Toast.LENGTH_SHORT).show();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				//msgView.setText(msgView.getText() + "\njs调用了java函数");
				new AlertDialog.Builder(TaskSubscribeActivity.this).setTitle("请选择").setIcon(
					     android.R.drawable.ic_dialog_info).setSingleChoiceItems(
					     new String[] { "设为发起方", "设为接收方" }, 0,
					     new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					       dialog.dismiss();
					      }
					     }).setNegativeButton("取消", null).show();
			}
		});
	}

	public void startFunction(final String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(TaskSubscribeActivity.this).setTitle("请选择").setIcon(
					     android.R.drawable.ic_dialog_info).setSingleChoiceItems(
					     new String[] { "设为发起方", "设为接收方" }, 0,
					     new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					       dialog.dismiss();
					      }
					     }).setNegativeButton("取消", null).show();

			}
		});
	}
}
