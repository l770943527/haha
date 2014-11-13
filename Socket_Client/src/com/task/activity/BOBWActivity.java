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
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر���
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
							adapter.notifyDataSetChanged(); // ������Ϣ֪ͨListView����
							listView.setAdapter(adapter); // ��������ListView������������
							listView.onRefreshComplete();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if(msg.what==0){
					 //����߳� ? how??
	                //thread.interrupt(); 
	               // proDialog.dismiss(); 
					listView.onRefreshComplete();
					Log.v("timeout","msg what time out!");
	                Toast.makeText(BOBWActivity.this, "���س�ʱ", Toast.LENGTH_SHORT) 
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

		title.setText("ƿ������");
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

		item1.setText("ʱ��");
		item2.setText("ƿ������(Mbps)");
		
		
		//backView.setBackgroundColor(Color.rgb(240, 240, 240));
	}

	private void initListView() {
		listPos = 0;
		listView = (WALoadListView) findViewById(R.id.bobwlist);
		adapter = new BOBWInforAdapter(this, guidelist);
		listView.setAdapter(adapter);

		// ����ˢ�¼���
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

		/* ���ListView�е�Item�鿴����
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Intent intent = new Intent();

				ArrayList<String> records = new ArrayList<String>();
				ArrayList<String> headers = new ArrayList<String>();
				TaskInforItem item = guidelist.get(position - 1);
				headers.add("�������ͣ�");
				records.add(String.valueOf(item.getTaskType()));
				headers.add("�û�ID��");
				records.add(item.getUserId());
				headers.add("����Id��");
				records.add(String.valueOf(item.getTaskId()));
				headers.add("����ԴIP��");
				records.add(item.getSrcIp());
				headers.add("����Ŀ��IP��");
				records.add(item.getDstIp());
				headers.add("��ʼʱ�䣺");
				records.add(String.valueOf(item.getStartTime()));
				headers.add("����ʱ�䣺");
				records.add(String.valueOf(item.getDuration() / 1000) + "s");
				headers.add("ִ��״̬��");
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