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
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر���
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
							adapter.notifyDataSetChanged(); // ������Ϣ֪ͨListView����
							listView.setAdapter(adapter); // ��������ListView������������
							listView.onRefreshComplete();
							Log.v("sdresult","guidelist"+guidelist.get(0).getSdTime());
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
	                Toast.makeText(SDActivity.this, "���س�ʱ", Toast.LENGTH_SHORT) 
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

		title.setText("��Ƶ������");
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

		item1.setText("ʱ��");
		item2.setText("ԴIP");
		item3.setText("Ŀ��IP");
		item4.setText("Դ�˿ں�");
		item5.setText("Ŀ�Ķ˿ں�");
		item6.setText("��Ƶ����");
		
		//backView.setBackgroundColor(Color.rgb(240, 240, 240));
	}

	private void initListView() {
		listPos = 0;
		listView = (WALoadListView) findViewById(R.id.sdlist);
		adapter = new SdInforAdapter(this, guidelist);
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