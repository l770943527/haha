package com.task.activity;

import java.util.ArrayList;

import com.socket.client.R;

import common.WADetailGroupView;
import common.WADetailRowView;
import common.WADetailRowView.RowType;
import common.WADetailView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;



public class LogInforDetailActivity extends Activity{
	
	private TextView title;
	private Button backBtn;
	private ScrollView detailScrollView;
    private ArrayList<String> records = new ArrayList<String>();
    private ArrayList<String> headers = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.log_detail_infor_activity);
		
		//get data from bundle
		Bundle  bundle = this.getIntent().getExtras();
		headers = bundle.getStringArrayList("headers");
		records = bundle.getStringArrayList("records");	
		
		initialTitles();
		
		updateView();
	}
	
	private void initialTitles() {
		title = (TextView)findViewById(R.id.tasktitlepanel_titleTextView);
		backBtn = (Button)findViewById(R.id.tasktitlepanel_leftBtn);
		
		title.setText("日志信息");
		backBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				LogInforDetailActivity.this.finish();
			}
		});
	}

	private void updateView() {
		//
		detailScrollView = (ScrollView) findViewById(R.id.reportform_LogdetailScrollView);
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
