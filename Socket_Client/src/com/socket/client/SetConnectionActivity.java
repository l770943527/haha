package com.socket.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SetConnectionActivity extends Activity  {

	private Button backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// Òþ²Ø±êÌâ
		setContentView(R.layout.set_connection_activity);

		//backBtn = (Button) findViewById(R.id.back_setconnectionBtn);

		//backBtn.setOnClickListener(this);
	}
/*
	@Override
	// back to login
	
	public void onClick(View bt) {
		if (bt.equals(backBtn)) {
			Intent intent = new Intent();
			intent.setClass(this, TANCLoadActivity.class);
			startActivity(intent);
		}
	}
	*/
}
