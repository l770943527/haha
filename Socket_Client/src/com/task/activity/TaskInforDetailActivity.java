package com.task.activity;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.socket.client.R;
import com.task.reportform.activity.IPTVMDIReportFormActivity;
import com.task.reportform.activity.IPTVReqReportFormActivity;
import com.task.reportform.activity.RTTReportFormActivity;
import common.TaskTypeAdapter;
import common.WADetailGroupView;
import common.WADetailRowView;
import common.WADetailRowView.RowType;
import common.WADetailView;

public class TaskInforDetailActivity extends Activity{

	private TextView title;
	private Button backBtn;
	private Button reportformBtn;
	private ScrollView detailScrollView;
    private ArrayList<String> records = new ArrayList<String>();
    private ArrayList<String> headers = new ArrayList<String>();
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.task_detail_infor_activity);
		
		Bundle  bundle = this.getIntent().getExtras();
		headers = bundle.getStringArrayList("headers");
		records = bundle.getStringArrayList("records");	
		
		initialTitles();
		
		updateView();
	}
	
	private void initialTitles() {
		title = (TextView)findViewById(R.id.tasktitlepanel_titleTextView);
		//backBtn = (Button)findViewById(R.id.tasktitlepanel_leftBtn);
		reportformBtn = (Button)findViewById(R.id.task_infor_Btn);
		title.setText("任务详细信息");
		//backBtn.setOnClickListener(new OnClickListener(){
			//public void onClick(View v){
			//	TaskInforDetailActivity.this.finish();
		//	}
		//});
		reportformBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent();
				System.out.println("headers size: "+headers.size());
				for(int i=0;i<headers.size();i++){
				System.out.println("header: "+headers.get(i));
				System.out.println("records : "+records.get(i));
				}
				intent.putExtra("headers", (Serializable)headers);
				intent.putExtra("records", (Serializable)records);
				if(TaskTypeAdapter.getTaskTypeName(Integer.parseInt(records.get(0))).equals("时延丢包")){
					intent.setClass(TaskInforDetailActivity.this, RTTReportFormActivity.class);		
					startActivity(intent);
				}
				else{
					if(TaskTypeAdapter.getTaskTypeName(Integer.parseInt(records.get(0))).equals("IPTV点播")){
						intent.setClass(TaskInforDetailActivity.this, IPTVReqReportFormActivity.class);		
						startActivity(intent);
					}
					else{
						if(TaskTypeAdapter.getTaskTypeName(Integer.parseInt(records.get(0))).equals("IPTV直播")){
							intent.setClass(TaskInforDetailActivity.this, IPTVMDIReportFormActivity.class);		
							startActivity(intent);
						}
						else {
							if(TaskTypeAdapter.getTaskTypeName(Integer.parseInt(records.get(0))).equals("主动流侦听")){
								intent.setClass(TaskInforDetailActivity.this, SDActivity.class);
								Bundle bundle=new Bundle();  
								bundle.putString("taskid",records.get(2) );  
								intent.putExtras(bundle);
								startActivity(intent);
							
							}
							else {
								if(TaskTypeAdapter.getTaskTypeName(Integer.parseInt(records.get(0))).equals("频道切换时间")){
									intent.setClass(TaskInforDetailActivity.this,SWActivity.class);
									//intent.putExtra("taskid",records.get(2) );
									Bundle bundle=new Bundle();  
									bundle.putString("taskid",records.get(2) );  
									intent.putExtras(bundle);
									startActivity(intent);
									System.out.println("!!");
								}
								else {
									 if(TaskTypeAdapter.getTaskTypeName(Integer.parseInt(records.get(0))).equals("瓶颈带宽")){
									intent.setClass(TaskInforDetailActivity.this, BOBWActivity.class);
									Bundle bundle=new Bundle();  
									bundle.putString("taskid",records.get(2) );  
									intent.putExtras(bundle);
									startActivity(intent);
								     }
									 else {
										 if(TaskTypeAdapter.getTaskTypeName(Integer.parseInt(records.get(0))).equals("可用带宽")){
											 intent.setClass(TaskInforDetailActivity.this, AVBWActivity.class);
												Bundle bundle=new Bundle();  
												bundle.putString("taskid",records.get(2) );  
												intent.putExtras(bundle);
												startActivity(intent);
										 }
									 }
								}
							}
						}
						
					}
				
					
				}
				
				
				
					
				
			}
		});
	}

	private void updateView() {
		detailScrollView = (ScrollView) findViewById(R.id.reportform_TaskdetailScrollView);
		WADetailView detailView = new WADetailView(this);
		WADetailGroupView groupView = new WADetailGroupView(this);
		int size = headers.size();
		for(int i=0; i<size; i++) {
				WADetailRowView rowView = new WADetailRowView(this, RowType.NAME_C_VALUE);
				rowView.setName(headers.get(i));
				rowView.setValue(records.get(i));
				rowView.setHorizontalScrollBarEnabled(true);
				rowView.setHorizontalGravity(10);
				groupView.addRow(rowView); 
		}

		detailView.addGroup(groupView);
		detailView.setClickable(true);
		detailScrollView.addView(detailView);
	}
}
