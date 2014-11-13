package com.task.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import Config.ConfigVarieble;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.json.util.EncapSendInforUtil;
import com.socket.client.R;
import com.socket.util.InternetUtil;
import com.task.acitivity.adapter.LogInforAdapter;
import com.task.activity.data.LogInforItem;
import common.WALoadListView;
import common.WALoadListView.OnRefreshListener;

public class LogInforManageActivity extends Activity {

	private int listPos;
	private int len = ConfigVarieble.LIST_LEN;

	private TextView title;
	private Handler handler;
	private TextView item1, item2, item3, item4, item5, item6, item7;//header 
	private View backView;
	private Button backBtn;

	private WALoadListView listView;
	private LogInforAdapter adapter;
	private List<Map<String, Object>> listInfor = new ArrayList<Map<String, Object>>();
	private List<LogInforItem> guidelist = new ArrayList<LogInforItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر���
		setContentView(R.layout.log_manage_activity);

		initTitles();

		initNetwork();

		initListView();
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu items for use in the action bar
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu. login_actionbar, menu);
       return super.onCreateOptionsMenu(menu);
   }


	private void initNetwork() {
		handler = new Handler() {
			@Override
			// parse the data received from server
			// maintain guidelist
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					String str = msg.getData().getString("title");
					JSONTokener jsonParser = new JSONTokener(str);
					try {
						JSONObject infor = (JSONObject) jsonParser.nextValue();
						JSONObject headOj = infor.getJSONObject("head");
						String messageType = headOj.getString("message_type");
						if (messageType.equals("12001")) {
							guidelist.clear();
							JSONObject dataOj = infor.getJSONObject("data");
							JSONArray daArray = dataOj
									.getJSONArray("LogResult");
							listInfor.clear();

							for (int i = 0; i < daArray.length(); i++) {
								JSONObject temp = (JSONObject) daArray.opt(i);
								LogInforItem item = new LogInforItem();
								item.setDescription(temp
										.getString("description"));
								item.setLogId(temp.getInt("logId"));
								item.setLogLevel(temp.getString("logLevel"));

								JSONObject timetemp = temp
										.getJSONObject("logTime");
								String beginDate = timetemp.getString("time");
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd");
								String sd = sdf.format(new Date(Long
										.parseLong(beginDate)));

								item.setLogTime(sd);
								item.setLogType(temp.getString("logType"));
								item.setOperIp(temp.getString("operIp"));
								item.setUserId(temp.getString("userId"));

								guidelist.add(item);
							}
							adapter.notifyDataSetChanged(); // ������Ϣ֪ͨListView����
							listView.setAdapter(adapter); // ��������ListView������������
							listView.onRefreshComplete();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}if(msg.what==0){
					 //����߳� ? how??
	                //thread.interrupt(); 
	               // proDialog.dismiss(); 
					listView.onRefreshComplete();
					Log.v("timeout","msg what time out!");
	                Toast.makeText(LogInforManageActivity.this, "���س�ʱ", Toast.LENGTH_SHORT) 
	                        .show(); 
				}
			}
		};

		InternetUtil.setHandler(handler);
		//?tell the server the position loaded
		InternetUtil.sendMessage(EncapSendInforUtil.encapLogImforReq(0, len));
	}

	protected void onPause() {
		super.onPause();
		InternetUtil.closeConnection();
	}

	private void initTitles() {
		/*
		title = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		//backBtn = (Button) findViewById(R.id.tasktitlepanel_leftBtn);

		title.setText("��־��Ϣ");
		/*
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LogInforManageActivity.this.finish();
			}
		});
*/
		initReportFormTitle();
	}

	private void initReportFormTitle() {

		item1 = (TextView) findViewById(R.id.item1);
		item2 = (TextView) findViewById(R.id.item2);
		item3 = (TextView) findViewById(R.id.item3);
		item4 = (TextView) findViewById(R.id.item4);
		item5 = (TextView) findViewById(R.id.item5);
		item6 = (TextView) findViewById(R.id.item6);
		item7 = (TextView) findViewById(R.id.item7);
		backView = findViewById(R.id.back_image_view);

		item1.setText("��־ID");
		item2.setText("��־ʱ��");
		item3.setText("�û�ID");
		item4.setText("��־����");
		item5.setText("��־����");
		item6.setText("����IP");
		item7.setText("��ע");
		backView.setBackgroundColor(Color.rgb(240, 240, 240));
	}

	private void initListView() {
		listPos = 0; 
		listView = (WALoadListView) findViewById(R.id.loglist);
		//show the data guidelist maintain by adapter
		adapter = new LogInforAdapter(this, guidelist);
		listView.setAdapter(adapter);

		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onUpRefresh() {
				if (listPos >= len) {
					listPos -= len;
				} else {
					listPos = 0;
				}

				InternetUtil.sendMessage(EncapSendInforUtil.encapLogImforReq(
						listPos, len));
			}

			@Override
			public void onDownRefresh() {
				listPos += len;
				InternetUtil.sendMessage(EncapSendInforUtil.encapLogImforReq(
						listPos, len));
			}

		});

		// ���ListView�е�Item�鿴���飬��һ��activity�����ݵ���һ��
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Intent intent = new Intent();

				ArrayList<String> records = new ArrayList<String>();
				ArrayList<String> headers = new ArrayList<String>();
				LogInforItem item = guidelist.get(position - 1);
				headers.add("��־id��");
				records.add(String.valueOf(item.getLogId()));
				headers.add("��־ʱ�䣺");
				records.add(item.getLogTime());
				headers.add("�û�id��");
				records.add(item.getUserId());
				headers.add("��־����");
				records.add(item.getLogLevel());
				headers.add("��־���ͣ�");
				records.add(String.valueOf(item.getLogType()));
				headers.add("����IP��");
				records.add(String.valueOf(item.getOperIp()));
				headers.add("��ע��");
				records.add(String.valueOf(item.getDescription()));

				intent.putExtra("headers", (Serializable) headers);
				intent.putExtra("records", (Serializable) records);
				intent.setClass(LogInforManageActivity.this,
						LogInforDetailActivity.class);

				startActivity(intent);
			}
		});

	}

}