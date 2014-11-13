package com.task.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.json.util.EncapSendInforUtil;
import com.socket.client.R;
import com.socket.client.R.layout;
import com.socket.util.InternetUtil;
import com.task.acitivity.adapter.DAInforAdapter;
import com.task.activity.data.DAInforItem;

import common.WALoadListView;

import Config.ConfigVarieble;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import common.WALoadListView.OnRefreshListener;

public class DAInforManageActivity extends Activity {

	private int listPos;
	private int len = ConfigVarieble.LIST_LEN;
	private TextView title;
	private Button backBtn;
	private TextView item1, item2, item3, item4, item5, item6;
	private View backView;
	private WALoadListView listView;
	private Handler handler;
	private DAInforAdapter adapter;
	private List<DAInforItem> guidelist = new ArrayList<DAInforItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.da_manage_activity);

		initialTitles();

		initNetwork();

		initListView();
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu items for use in the action bar
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu. da_action, menu);
       return super.onCreateOptionsMenu(menu);
   }

	protected void onPause() {
		super.onPause();
		InternetUtil.closeConnection();
		this.finish();
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
						if (messageType.equals("11001")) {
							guidelist.clear();
							JSONObject dataOj = infor.getJSONObject("data");
							JSONArray daArray = dataOj.getJSONArray("DAResult");

							for (int i = 0; i < daArray.length(); i++) {
								JSONObject temp = (JSONObject) daArray.opt(i);
								DAInforItem item = new DAInforItem();
								item.setId(Integer.parseInt(temp
										.getString("id")));
								item.setIp(temp.getString("ip"));
								item.setSn(temp.getString("sn"));
								item.setSlotsused(Double.parseDouble(temp
										.getString("slotsused")));
								item.setStatus(temp.getInt("status"));
								item.setCpuusage(temp.getDouble("cpuusage"));
								item.setMemusage(temp.getDouble("memusage"));
								guidelist.add(item);
							}
							Log.v("QLQ", "after*****************2**");
							adapter.notifyDataSetChanged(); // 发送消息通知ListView更新
							listView.setAdapter(adapter); // 重新设置ListView的数据适配器
							listView.onRefreshComplete();
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
	                Toast.makeText(DAInforManageActivity.this, "加载超时", Toast.LENGTH_SHORT) 
	                        .show(); 
				}
			};
		};

		
		InternetUtil.setHandler(handler);
		InternetUtil.sendMessage(EncapSendInforUtil.encapDataAnalystStatusReq(
				listPos, len));
	}

	private void initialTitles() {
		/*
		title = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		backBtn = (Button) findViewById(R.id.tasktitlepanel_leftBtn);

		title.setText("告警信息");
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DAInforManageActivity.this.finish();
			}
		});
*/
		initalReportFormTitle();
	}

	private void initalReportFormTitle() {
		item1 = (TextView) findViewById(R.id.item1);
		item2 = (TextView) findViewById(R.id.item2);
		item3 = (TextView) findViewById(R.id.item3);
		item4 = (TextView) findViewById(R.id.item4);
		item5 = (TextView) findViewById(R.id.item5);
		item6 = (TextView) findViewById(R.id.item6);
		backView = findViewById(R.id.back_image_view);

		item1.setText("数据分析器ID");
		item2.setText("数据分析器地址");
		item3.setText("状态");
		item4.setText("任务槽使用率");
		item5.setText("CPU使用率");
		item6.setText("内存使用率");
		backView.setBackgroundColor(Color.rgb(240, 240, 240));
	}

	private void initListView() {
		listPos = 0;
		listView = (WALoadListView) findViewById(R.id.dataanalystlist);
		adapter = new DAInforAdapter(this, guidelist);
		listView.setAdapter(adapter);

		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onUpRefresh() {
				if (listPos >= len) {
					listPos -= len;
				} else {
					listPos = 0;
				}

				InternetUtil.sendMessage(EncapSendInforUtil
						.encapDataAnalystStatusReq(listPos, len));
				adapter.notifyDataSetChanged(); // 发送消息通知ListView更新
				listView.setAdapter(adapter); // 重新设置ListView的数据适配器
			}

			@Override
			public void onDownRefresh() {
				listPos += len;
				InternetUtil.sendMessage(EncapSendInforUtil
						.encapDataAnalystStatusReq(listPos, len));
				adapter.notifyDataSetChanged(); // 发送消息通知ListView更新
				listView.setAdapter(adapter); // 重新设置ListView的数据适配器
			}
		});

		// 点击ListView中的Item查看详情
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Intent intent = new Intent();

				ArrayList<String> records = new ArrayList<String>();
				ArrayList<String> headers = new ArrayList<String>();
				DAInforItem item = guidelist.get(position - 1);
				headers.add("数据分析器id：");
				records.add(String.valueOf(item.getId()));
				headers.add("数据分析器IP：");
				records.add(item.getIp());
				headers.add("数据分析器sn：");
				records.add(item.getSn());
				headers.add("数据分析器状态：");
				records.add((item.getStatus() == 0) ? "运行中" : "停止");
				headers.add("任务槽使用率：");
				records.add(String.valueOf(item.getSlotsused() * 100 + "%"));
				headers.add("处理器使用率：");
				records.add(String.valueOf(item.getCpuusage() * 100 + "%"));
				headers.add("内存使用率：");
				records.add(String.valueOf(item.getMemusage() * 100 + "%"));

				intent.putExtra("headers", (Serializable) headers);
				intent.putExtra("records", (Serializable) records);
				intent.setClass(DAInforManageActivity.this,
						DAInforDetailActivity.class);

				startActivity(intent);
			}
		});
	}
}