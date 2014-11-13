package com.task.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import Config.ConfigVarieble;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.json.util.EncapSendInforUtil;
import com.socket.client.R;
import com.socket.util.InternetUtil;
import com.task.acitivity.adapter.AlarmInforAdapter;
import com.task.activity.data.AlarmInforItem;
import common.WALoadListView;
import common.WALoadListView.OnRefreshListener;

public class AlarmInforManageActivity extends Activity {

	private int listPos;
	private int len = ConfigVarieble.LIST_LEN;

	private TextView title;
	private TextView alarmTime, alarmType, alarmIp, alarmInterface, taskId,
			alarmLevel;
	private View backView;
	private Handler handler;
	private Button backBtn;
	private TextView text;

	private WALoadListView listView;
	private AlarmInforAdapter adapter;
	private List<AlarmInforItem> guidelist = new ArrayList<AlarmInforItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.alarm_manage_activity);

		initTitles();

		initNetwork();
		
		initListView();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	private void initNetwork() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// 动态更新UI界面
					String str = msg.getData().getString("title");
					JSONTokener jsonParser = new JSONTokener(str);
					try {
						JSONObject infor = (JSONObject) jsonParser.nextValue();
						JSONObject headOj = infor.getJSONObject("head");
						String messageType = headOj.getString("message_type");
						if (messageType.equals(ConfigVarieble.ALARM_MANAGE_ACK)) {
							guidelist.clear();
							JSONObject dataOj = infor.getJSONObject("data");
							JSONArray daArray = dataOj
									.getJSONArray("AlarmResult");
							for (int i = 0; i < daArray.length(); i++) {
								JSONObject temp = (JSONObject) daArray.opt(i);
								AlarmInforItem item = new AlarmInforItem();

								JSONObject timetemp = temp
										.getJSONObject("alarmTime");
								String beginDate = timetemp.getString("time");
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd");
								String sd = sdf.format(new Date(Long
										.parseLong(beginDate)));
								item.setAlarmTime(sd);

								item.setAlarmType(temp.getString("alarmType"));
								item.setAlarmIp(temp.getString("alarmIp"));
								item.setAlarmInterface(temp
										.getInt("alarmInterface"));
								item.setTaskId(temp.getInt("taskId"));
								item.setAlarmLevel(temp.getInt("alarmLevel"));
								item.setDescription(temp
										.getString("description"));
								guidelist.add(item);
							}
							adapter.notifyDataSetChanged(); // 发送消息通知ListView更新
							listView.setAdapter(adapter); // 重新设置ListView的数据适配器
							listView.onRefreshComplete();// stop loading
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if(msg.what==0){
					 //打断线程 ? how??
	                //thread.interrupt(); 
	               // proDialog.dismiss(); 
					listView.onRefreshComplete();
					Log.v("timeout","msg what time out!");
	                Toast.makeText(AlarmInforManageActivity.this, "加载超时", Toast.LENGTH_SHORT) 
	                        .show(); 
				}
			}
		};

		InternetUtil.setHandler(handler);
		InternetUtil.sendMessage(EncapSendInforUtil.encapAlarmImforReq(0, len));
	}

	private void initTitles() {
		/*
		title = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		//backBtn = (Button) findViewById(R.id.tasktitlepanel_leftBtn);

		title.setText("告警信息");
	*/	
		/*
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlarmInforManageActivity.this.finish();
			}
		});
*/
		initalReportFormTitle();
	}

	private void initalReportFormTitle() {
		alarmTime = (TextView) findViewById(R.id.item1);
		alarmType = (TextView) findViewById(R.id.item2);
		alarmIp = (TextView) findViewById(R.id.item3);
		alarmInterface = (TextView) findViewById(R.id.item4);
		taskId = (TextView) findViewById(R.id.item5);
		alarmLevel = (TextView) findViewById(R.id.item6);
		backView = findViewById(R.id.back_image_view);

		alarmTime.setText("告警时间");
		alarmType.setText("告警类型（告警事件）");
		alarmIp.setText("告警源");
		alarmInterface.setText("告警接口");
		taskId.setText("任务号");
		alarmLevel.setText("告警级别");
		backView.setBackgroundColor(Color.rgb(240, 240, 240));
	}

	private void initListView() {
		// define the up/down behavior
		listPos = 0;
		//text = (TextView) findViewById(R.id.textView1);
		listView = (WALoadListView) findViewById(R.id.alarmlist);
		adapter = new AlarmInforAdapter(this, guidelist);
		listView.setAdapter(adapter);

		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onUpRefresh() {
				if (listPos >= len) {
					listPos -= len;
				} else {
					listPos = 0;
				}
				InternetUtil.sendMessage(EncapSendInforUtil.encapAlarmImforReq(
						listPos, len));
			}

			@Override
			public void onDownRefresh() {
				listPos += len;
				InternetUtil.sendMessage(EncapSendInforUtil.encapAlarmImforReq(
						listPos, len));
			}
		});
	}

}