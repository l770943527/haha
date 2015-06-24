package com.socket.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.socket.util.InternetUtil;
import com.task.activity.AlarmInforManageActivity;
import com.task.activity.DAInforManageActivity;
import com.task.activity.LogInforManageActivity;
import com.task.activity.TaskInforManageActivity;
import com.task.activity.TaskSubscribeActivity;
import com.task.activity.TopoEditActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainPageActivity extends Activity  {

	private Button backBtn;
	private Button test;
	private TextView head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// Òþ²Ø±êÌâ
		setContentView(R.layout.activity_main_page_layout);

		initTitle();

		initViews();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		InternetUtil.closeConnection();
		this.finish();
	}

	private void initTitle() {
		//backBtn = (Button) findViewById(R.id.tasktitlepanel_leftBtn);
		//backBtn.setOnClickListener(this);
		head = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		head.setText("Ö÷Ò³");
	}

	private void initViews() {
		GridView gridview = (GridView) findViewById(R.id.gridview);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 1; i < 10; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (i == 1) {
				map.put("ItemImage", R.drawable.grid1);
				map.put("ItemText", getResources()
						.getString(R.string.gridview1));
			}
			if (i == 2) {
				map.put("ItemImage", R.drawable.grid1);
				map.put("ItemText", getResources()
						.getString(R.string.gridview2));
			}
			if (i == 3) {
				map.put("ItemImage", R.drawable.grid1);
				map.put("ItemText", getResources()
						.getString(R.string.gridview3));
			}
			if (i == 4) {
				map.put("ItemImage", R.drawable.grid1);
				map.put("ItemText", getResources()
						.getString(R.string.gridview4));
			}
			if (i == 5) {
				map.put("ItemImage", R.drawable.grid1);
				map.put("ItemText", getResources()
						.getString(R.string.gridview5));
			}
			if (i == 6) {
				map.put("ItemImage", R.drawable.grid1);
				map.put("ItemText", getResources()
						.getString(R.string.gridview6));
			}
			lstImageItem.add(map);

		}

		SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem,
				R.layout.grid_item, new String[] { "ItemImage", "ItemText" },
				new int[] { R.id.ItemImage, R.id.ItemText });

		gridview.setAdapter(saImageItems);
		gridview.setOnItemClickListener(new ItemClickListener());
	}// end initView

	class ItemClickListener implements OnItemClickListener {

		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {

			HashMap<String, Object> item = (HashMap<String, Object>) arg0
					.getItemAtPosition(arg2);

			if (item.get("ItemText").equals(
					getResources().getString(R.string.gridview1))) {
				Intent intent = new Intent();
				intent.setClass(MainPageActivity.this,
						TaskInforManageActivity.class);
				startActivity(intent);
			}
			if (item.get("ItemText").equals(
					getResources().getString(R.string.gridview2))) {
				Intent intent = new Intent();
				intent.setClass(MainPageActivity.this,
						TaskSubscribeActivity.class);
				startActivity(intent);
			}
			if (item.get("ItemText").equals(
					getResources().getString(R.string.gridview3))) {
				Intent intent = new Intent();
				intent.setClass(MainPageActivity.this, LogInforManageActivity.class);
				startActivity(intent);
			}
			if (item.get("ItemText").equals(
					getResources().getString(R.string.gridview4))) {
				Intent intent = new Intent();
				intent.setClass(MainPageActivity.this,
						AlarmInforManageActivity.class);
				startActivity(intent);
			}
			if (item.get("ItemText").equals(
					getResources().getString(R.string.gridview5))) {
				// Toast.makeText(MainPageActivity.this, R.string.gridview5,
				// Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(MainPageActivity.this, TopoEditActivity.class);
				startActivity(intent);
			}
			if (item.get("ItemText").equals(
					getResources().getString(R.string.gridview6))) {
				Intent intent = new Intent();
				intent.setClass(MainPageActivity.this,
						DAInforManageActivity.class);
				startActivity(intent);
			}
		}
	}
	/*
	@Override
	
	public void onClick(View bt) {
		if (bt.equals(backBtn)) {
			Intent intent = new Intent();
			intent.setClass(this, TANCLoadActivity.class);
			startActivity(intent);
		}
	}
	*/

}
