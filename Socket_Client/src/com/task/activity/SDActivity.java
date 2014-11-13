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
import com.task.acitivity.adapter.SdInforAdapter;
import com.task.activity.data.SdInforItem;
import common.WALoadListView;
import common.WALoadListView.OnRefreshListener;

public class SDActivity extends Activity {

	private int listPos;
	private int len = ConfigVarieble.LIST_LEN;

	private TextView item1, item2, item3, item4,item5,item6;
	private View backView;
	private TextView title;
	private Handler handler;
	private Button backBtn;
	private int taskid;
	private String taskid_c;

	private WALoadListView listView;
	private SdInforAdapter adapter;
	private List<SdInforItem> guidelist = new ArrayList<SdInforItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.sd_info_activity);
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
					System.out.println("the sd result is "+str);
					JSONTokener jsonParser = new JSONTokener(str);
					try {
						JSONObject infor = (JSONObject) jsonParser.nextValue();
						JSONObject headOj = infor.getJSONObject("head");
						String messageType = headOj.getString("message_type");
						if (messageType.equals(ConfigVarieble.TASK_MANAGE_ACK)) {
							guidelist.clear();
							JSONObject dataOj = infor.getJSONObject("data");
							JSONArray daArray = dataOj
									.getJSONArray("SDResult");

							for (int i = 0; i < daArray.length(); i++) {
								JSONObject temp = (JSONObject) daArray.opt(i);
								SdInforItem item = new SdInforItem();
								//item.set(Integer.parseInt(temp
										//.getString("ReportTime")));
								item.setSdTime(temp.getString("reportTime"));
								item.setDstIp(temp.getString("dstIP"));
								item.setSrcIp(temp.getString("srcIP"));
								item.setDstPort(temp.getString("dstPort"));
								item.setSrcPort(temp.getString("srcPort"));
								//item.setSrcIp(temp.getString("JoinIp"));
								//item.setSrcIp(temp.getString("JoinPort"));
								//item.setSrcIp(temp.getString("JoinIp"));
								item.setVideoType(temp.getString("type"));

								
								guidelist.add(item);
							}
							adapter.notifyDataSetChanged(); // 发送消息通知ListView更新
							listView.setAdapter(adapter); // 重新设置ListView的数据适配器
							listView.onRefreshComplete();
							Log.v("sdresult","guidelist"+guidelist.get(0).getSdTime());
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}if(msg.what==0){
					 //打断线程 ? how??
	                //thread.interrupt(); 
	               // proDialog.dismiss(); 
					listView.onRefreshComplete();
					Log.v("timeout","msg what time out!");
	                Toast.makeText(SDActivity.this, "加载超时", Toast.LENGTH_SHORT) 
	                        .show(); 
				}
			}
		};

		InternetUtil.setHandler(handler);
		//int taskid=guidelist.get(2).;
		InternetUtil.sendMessage(EncapSendInforUtil.encapSdImforReq(0, len,taskid));
	}

	private void initTitles() {
		title = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		backBtn = (Button) findViewById(R.id.tasktitlepanel_leftBtn);

		title.setText("视频流侦听");
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SDActivity.this.finish();
			}
		});
		initReportFormTitle();
	}

	private void initReportFormTitle() {
		item1 = (TextView) findViewById(R.id.sd1);
		item2 = (TextView) findViewById(R.id.sd2);
		item3 = (TextView) findViewById(R.id.sd3);
		item4 = (TextView) findViewById(R.id.sd4);
		item5 = (TextView) findViewById(R.id.sd5);
		item6 = (TextView) findViewById(R.id.sd6);
		
		backView = findViewById(R.id.sdback_image_view);

		item1.setText("时间");
		item2.setText("源IP");
		item3.setText("目的IP");
		item4.setText("源端口号");
		item5.setText("目的端口号");
		item6.setText("视频类型");
		
		//backView.setBackgroundColor(Color.rgb(240, 240, 240));
	}

	private void initListView() {
		listPos = 0;
		listView = (WALoadListView) findViewById(R.id.sdlist);
		adapter = new SdInforAdapter(this, guidelist);
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

				InternetUtil.sendMessage(EncapSendInforUtil.encapSwImforReq(
						listPos, len,taskid));
			}

			@Override
			public void onDownRefresh() {
				listPos += len;
				InternetUtil.sendMessage(EncapSendInforUtil.encapSwImforReq(
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