package com.socket.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.json.util.EncapSendInforUtil;
import com.json.util.ParseJSONStringUtil;
import com.socket.util.InternetUtil;
import com.task.activity.LogInforManageActivity;
import common.CommonHeader;
import Config.ConfigVarieble;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TANCLoadActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	// 定义声明需要用到的UI元素
	private EditText userNameEdt;
	private EditText passwordEdt;
	private Button btnSend;
	private TextView msgRec;
	private TextView title;
	private Button btnSetConnection;
	public Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.tanc_load_activity);
		
		InitTitle();
		
		InitView();

		InitNetwork();
	}
	
	public void onDestroy(){
		super.onDestroy();
	}

	private void InitTitle() {
		btnSetConnection = (Button) findViewById(R.id.tasktitlepanel_rightBtn);
		btnSetConnection.setOnClickListener(this);
		title = (TextView)findViewById(R.id.tasktitlepanel_titleTextView);
		title.setText("用户登录");
	}

	private void InitNetwork() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// 动态更新UI界面
					String str = msg.getData().getString("title");
					Log.v("result","in login...");
					// msg.what=1,get("title") in InternetUtil L210
					JSONTokener jsonParser = new JSONTokener(str);
					try {
						JSONObject infor = (JSONObject) jsonParser.nextValue();
						JSONObject headOj = infor.getJSONObject("head");
						String messageType = headOj.getString("message_type");
						if (messageType.equals(ConfigVarieble.LOGIN_ACK)) {
							JSONObject dataOj = infor.getJSONObject("data");
							String loginAck = dataOj.getString("loginResult");
							if (loginAck.equals("true")) {
								loginSuccess();
							} else {
								msgRec.setText("用户名或者密码有误");
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
		};

		InternetUtil.setHandler(handler);
		// begin to receive msg from server
		Thread t = new Thread(new InternetUtil());
		t.start();
	}

	public void loginSuccess() {
		Intent intent = new Intent();
		intent.setClass(this, MainPageActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //设置MainPageActivity为历史栈栈顶
		startActivity(intent);
		this.finish();
	}

	private void InitView() {
		// 通过id获取ui元素对象
		userNameEdt = (EditText) findViewById(R.id.login_usernameEditText);
		passwordEdt = (EditText) findViewById(R.id.login_passwordEditText);
		btnSend = (Button) findViewById(R.id.login_loginBtn);
		msgRec = (TextView) findViewById(R.id.login_errorTextView);
		
		// 设置点击事件
		btnSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View bt) {
		if (bt.equals(btnSend)) {
			try {
				 Intent intent1 = new Intent();
				 intent1.setClass(this, MainPageActivity.class);
				 startActivity(intent1);
				String username = userNameEdt.getText().toString();
				String password = passwordEdt.getText().toString();
				if (!TextUtils.isEmpty(username)
						&& !TextUtils.isEmpty(password)) {
					InternetUtil.sendMessage(EncapSendInforUtil
							.encapLoginInfor(username, password) + "\r\n\r\n");
				} else {
					msgRec.setText("请输入用户名或密码");
					userNameEdt.requestFocus();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (bt.equals(btnSetConnection)) {
			Intent intent = new Intent();
			intent.setClass(this, SetConnectionActivity.class);
			startActivity(intent);
		}
	}
}