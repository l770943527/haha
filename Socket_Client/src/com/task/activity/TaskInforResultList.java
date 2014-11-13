package com.task.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.socket.client.R;

public class TaskInforResultList extends Activity  {
	
	// for search result
	private ArrayList<String> ResultList = new ArrayList<String>();
    private TextView tv=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);// Òþ²Ø±êÌâ
		setContentView(R.layout.basic_test);
		Bundle  bundle = this.getIntent().getExtras();
		ResultList = bundle.getStringArrayList("records");	
		tv = (TextView)findViewById(R.id.textViewId);
		tv.setText("");
		
		Log.v("result","onCreate() is called");
		doSearchQuery(getIntent());

		//initTitles();

		

		//initListView();
	}
	//public void initListView(){}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.v("result","onNewIntent() is called");
		super.onNewIntent(intent);
		doSearchQuery(intent);
	}
	private void doSearchQuery(Intent intent){
		Log.v("result"," doSearchQuery() is called");
		if(intent == null)
			return;
		
		String queryAction = intent.getAction();
		if(Intent.ACTION_SEARCH.equals(queryAction)){
			String queryString = intent.getStringExtra(SearchManager.QUERY);
			Log.v("result","ËÑË÷ÄÚÈÝ£º" + queryString);
		}		
	}
}
