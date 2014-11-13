package com.task.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import Config.ConfigVarieble;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.json.util.EncapSendInforUtil;
import com.socket.client.R;
import com.socket.util.InternetUtil;
import com.task.acitivity.adapter.BOBWInforAdapter;
import com.task.activity.data.AVBWInforItem;
import com.task.activity.data.BOBWInforItem;
import common.WALoadListView;
import common.WALoadListView.OnRefreshListener;

public class BOBWActivity extends Activity {

	private int listPos;
	private int len = ConfigVarieble.LIST_LEN;

	private TextView item1, item2;//column : time, avbw  
	private View backView;
	private TextView title;
	private Handler handler;
	private Button backBtn;
	private int taskid;
	private String taskid_c;

	private WALoadListView listView;
	private BOBWInforAdapter adapter;
	private List<BOBWInforItem> guidelist = new ArrayList<BOBWInforItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.bobw_info_activity);
		Bundle bundle=getIntent().getExtras();
		taskid_c= bundle.getString("taskid");
        System.out.println("taskid : "+taskid_c);
        taskid=Integer.parseInt(taskid_c);
		initTitles();

		initNetwork();

		initListView();
	}

	private void initNetwork() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					String str = msg.getData().getString("title");
					System.out.println("In bobw Activity str is "+str);
					JSONTokener jsonParser = new JSONTokener(str);
					try {
						JSONObject infor = (JSONObject) jsonParser.nextValue();
						JSONObject headOj = infor.getJSONObject("head");
						String messageType = headOj.getString("message_type");
						if (messageType.equals("18001")) {
							guidelist.clear();
							JSONObject dataOj = infor.getJSONObject("data");
							JSONArray daArray = dataOj
									.getJSONArray("BobwResult");

							for (int i = 0; i < daArray.length(); i++) {
								JSONObject temp = (JSONObject) daArray.opt(i);
								BOBWInforItem item = new BOBWInforItem();
								//item.set(Integer.parseInt(temp
										//.getString("reportTime")));
								item.setReportTime(temp.getString("reportTime"));
								
								double current =(Double.parseDouble(temp.getString("bobw")));
				            	double c=(double)(Math.round(current/10000)/100.0);
								
								item.setBobw(c+"");

								
								guidelist.add(item);
							}
							
							System.out.println("guidelist "+guidelist.get(0).getBobw());
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
	                Toast.makeText(BOBWActivity.this, "加载超时", Toast.LENGTH_SHORT) 
	                        .show(); 
				}
			}
		};

		InternetUtil.setHandler(handler);
		//int taskid=guidelist.get(2).;
		InternetUtil.sendMessage(EncapSendInforUtil.encapBobwImforReq(0, len,taskid));
	}

	private void initTitles() {
		title = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		backBtn = (Button) findViewById(R.id.tasktitlepanel_leftBtn);

		title.setText("瓶颈带宽");
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BOBWActivity.this.finish();
			}
		});
		initReportFormTitle();
	}

	private void initReportFormTitle() {
		item1 = (TextView) findViewById(R.id.bobw1);
		item2 = (TextView) findViewById(R.id.bobw2);
		
		//backView = findViewById(R.id.avbw_back_image_view);

		item1.setText("时间");
		item2.setText("瓶颈带宽(Mbps)");
		
		
		//backView.setBackgroundColor(Color.rgb(240, 240, 240));
	}

	private void initListView() {
		listPos = 0;
		listView = (WALoadListView) findViewById(R.id.bobwlist);
		adapter = new BOBWInforAdapter(this, guidelist);
		listView.setAdapter(adapter);

		// 设置刷新加载
		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onUpRefresh() {
				if (listPos >= len) {
					listPos -= len;
				} else {
					listPos = 0;
				}

				InternetUtil.sendMessage(EncapSendInforUtil.encapBobwImforReq(
						listPos, len,taskid));
			}

			@Override
			public void onDownRefresh() {
				listPos += len;
				InternetUtil.sendMessage(EncapSendInforUtil.encapBobwImforReq(
						listPos, len,taskid));
			}
		});

		/* 点击ListView中的Item查看详情
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Intent intent = new Intent();

				ArrayList<String> records = new ArrayList<String>();
				ArrayList<String> headers = new ArrayList<String>();
				TaskInforItem item = guidelist.get(position - 1);
				headers.add("任务类型：");
				records.add(String.valueOf(item.getTaskType()));
				headers.add("用户ID：");
				records.add(item.getUserId());
				headers.add("任务Id：");
				records.add(String.valueOf(item.getTaskId()));
				headers.add("测量源IP：");
				records.add(item.getSrcIp());
				headers.add("测量目的IP：");
				records.add(item.getDstIp());
				headers.add("开始时间：");
				records.add(String.valueOf(item.getStartTime()));
				headers.add("持续时间：");
				records.add(String.valueOf(item.getDuration() / 1000) + "s");
				headers.add("执行状态：");
				records.add(String.valueOf(item.getStatus()));

				intent.putExtra("headers", (Serializable) headers);
				intent.putExtra("records", (Serializable) records);
				intent.setClass(TaskInforManageActivity.this,
						TaskInforDetailActivity.class);

				startActivity(intent);
			}
		});*/
	}

}