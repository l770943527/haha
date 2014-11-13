package com.task.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import Config.ConfigVarieble;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.json.util.EncapSendInforUtil;
import com.socket.client.R;
import com.socket.util.InternetUtil;
import com.task.acitivity.adapter.TaskInforAdapter;
import com.task.activity.data.TaskInforItem;
import common.WALoadListView;
import common.WALoadListView.OnRefreshListener;

public class TaskInforManageActivity extends Activity implements
		SearchView.OnQueryTextListener {

	private int listPos;
	private int len = ConfigVarieble.LIST_LEN;

	private TextView item1, item2, item3, item4, item5, item6, item7, item8;
	private View backView;
	private TextView title;
	private int taskid;
	
	private Handler handler;
	private Button backBtn;

	private WALoadListView listView;
	private TaskInforAdapter adapter;
	private List<TaskInforItem> guidelist = new ArrayList<TaskInforItem>();
	private Set<TaskInforItem> mSearchList = new HashSet<TaskInforItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.task_manage_activity);

		initTitles();

		initNetwork();

		initListView();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// setUpSerchView(menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		// showInfo("searchView : " + searchView);
		if (searchView == null) {
			Log.v("result", "Fail to get Search View.");

		} else {
			Log.v("result", "!!!!! Search View.");
		}
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(false);

		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {

		// updateLayout(obj);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		 searchItem(query);
		//Set<TaskInforItem> obj =mSearchList;
		Log.v("result","query mSearchList size : "+mSearchList.size());
		 updateLayout(mSearchList);
		return false;
	}

	public void searchItem(String name) {
		Log.v("result", "------------name is " + name);
         taskid=Integer.parseInt(name);
/*		do the search in the guidelist(current page ) ,useless
		for (int i = 0; i < guidelist.size(); i++) {
			int cur_id = guidelist.get(i).getTaskId();
			Log.v("result", "taskid " + guidelist.get(i).getTaskId());
			// 存在匹配的数据

			if (cur_id != -1) {
				if (name == null)
					Log.v("result", "search name is null!!");
				else {
					if (cur_id == Integer.parseInt(name)) {

						mSearchList.add(guidelist.get(i));
					}
				}
			}
		}
		*/
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 1) {
					String str = msg.getData().getString("title");
					System.out.println("the  search str is " + str);
					JSONTokener jsonParser = new JSONTokener(str);
					try {
						// nextValue: json文本解析为对象
						JSONObject infor = (JSONObject) jsonParser.nextValue();
						JSONObject headOj = infor.getJSONObject("head");
						String messageType = headOj.getString("message_type");
						if (messageType.equals(ConfigVarieble.TASK_MANAGE_ACK)) {
							guidelist.clear();
							JSONObject dataOj = infor.getJSONObject("data");
							JSONArray daArray = dataOj
									.getJSONArray("TaskResult");

							for (int i = 0; i < daArray.length(); i++) {
								JSONObject temp = (JSONObject) daArray.opt(i);
								TaskInforItem item = new TaskInforItem();
								item.setTaskId(Integer.parseInt(temp
										.getString("taskId")));
								System.out.println("!in search taskid is : "+item.getTaskId());
								item.setUserId(temp.getString("userId"));
								item.setSrcIp(temp.getString("srcIp"));
								item.setDstIp(temp.getString("dstIp"));

								JSONObject timetemp = temp
										.getJSONObject("startTime");
								String beginDate = timetemp.getString("time");
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd");
								String sd = sdf.format(new Date(Long
										.parseLong(beginDate)));

								item.setStartTime(sd);
								item.setDuration(temp.getLong("duration"));
								System.out.println("item dura"+item.getDuration());
								item.setTaskType(temp.getInt("taskType"));
								System.out.println("item tasktype "+item.getTaskType());
								item.setStatus(temp.getInt("status"));
								item.setComment(temp.getString("comment"));
								guidelist.add(item);
								mSearchList.add(item);
							}
							Log.v("result","msearch list size "+mSearchList.size());
							adapter.notifyDataSetChanged(); // 发送消息通知ListView更新
							listView.setAdapter(adapter); // 重新设置ListView的数据适配器
							listView.onRefreshComplete();
							
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					InternetUtil.setHandler(handler);
					InternetUtil.sendMessage(EncapSendInforUtil.encapTaskSearchImforReq(0, len,taskid));
				}
				
			}
			
		};//end handler
		//sendmsg should carry taskid ,the server return the 
		
		//InternetUtil.setHandler(handler);
		//InternetUtil.sendMessage(EncapSendInforUtil.encapTaskSearchImforReq(0, len,taskid));
		System.out.println("msearch list size  ,not in the layout"+mSearchList.size());
		
	}

	public void updateLayout(Set<TaskInforItem> obj) {
		// listView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),
		// android.R.layout.simple_expandable_list_item_1, obj));
		listPos = 0;
		listView = (WALoadListView) findViewById(R.id.tasklist);
		List<TaskInforItem> search=new ArrayList<TaskInforItem>();
		for(TaskInforItem s:mSearchList){
			search.add(s);
			System.out.println("in update layout!"+s.getTaskId());
		}
		adapter = new TaskInforAdapter(this, search);
		listView.setAdapter(adapter);
	}


	private void initNetwork() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					String str = msg.getData().getString("title");
					System.out.println("the  task mgnt str is " + str);
					JSONTokener jsonParser = new JSONTokener(str);
					try {
						// nextValue: json文本解析为对象
						JSONObject infor = (JSONObject) jsonParser.nextValue();
						JSONObject headOj = infor.getJSONObject("head");
						String messageType = headOj.getString("message_type");
						if (messageType.equals(ConfigVarieble.TASK_MANAGE_ACK)) {
							guidelist.clear();
							JSONObject dataOj = infor.getJSONObject("data");
							JSONArray daArray = dataOj
									.getJSONArray("TaskResult");

							for (int i = 0; i < daArray.length(); i++) {
								JSONObject temp = (JSONObject) daArray.opt(i);
								TaskInforItem item = new TaskInforItem();
								item.setTaskId(Integer.parseInt(temp
										.getString("taskId")));
								item.setUserId(temp.getString("userId"));
								item.setSrcIp(temp.getString("srcIp"));
								item.setDstIp(temp.getString("dstIp"));

								JSONObject timetemp = temp
										.getJSONObject("startTime");
								String beginDate = timetemp.getString("time");
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd");
								String sd = sdf.format(new Date(Long
										.parseLong(beginDate)));

								item.setStartTime(sd);
								item.setDuration(temp.getLong("duration"));
								item.setTaskType(temp.getInt("taskType"));
								item.setStatus(temp.getInt("status"));
								item.setComment(temp.getString("comment"));
								guidelist.add(item);
							}
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
	                Toast.makeText(TaskInforManageActivity.this, "加载超时", Toast.LENGTH_SHORT) 
	                        .show(); 
				}
			}
		};// end of handler

		InternetUtil.setHandler(handler);
		InternetUtil.sendMessage(EncapSendInforUtil.encapTaskImforReq(0, len));
	}

	private void initTitles() {
		
		// hide the tasktitlepanel for the search
		
		/*
		 * title = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		 * backBtn = (Button) findViewById(R.id.tasktitlepanel_leftBtn);
		 * 
		 * title.setText("任务信息"); backBtn.setOnClickListener(new
		 * OnClickListener() { public void onClick(View v) {
		 * TaskInforManageActivity.this.finish(); } });
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
		item8 = (TextView) findViewById(R.id.item8);
		backView = findViewById(R.id.back_image_view);

		item1.setText("任务类型");
		item2.setText("用户ID");
		item3.setText("任务ID");
		item4.setText("测量源IP");
		item5.setText("测量目的IP");
		item6.setText("开始时间");
		item7.setText("持续时间");
		item8.setText("执行状态");
		backView.setBackgroundColor(Color.rgb(240, 240, 240));
	}

	private void initListView() {
		listPos = 0;
		listView = (WALoadListView) findViewById(R.id.tasklist);
		adapter = new TaskInforAdapter(this, guidelist);
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

				InternetUtil.sendMessage(EncapSendInforUtil.encapTaskImforReq(
						listPos, len));
			}

			@Override
			public void onDownRefresh() {
				listPos += len;
				InternetUtil.sendMessage(EncapSendInforUtil.encapTaskImforReq(
						listPos, len));
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
				System.out.println("position : "+position);
				
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
		});
	}

}