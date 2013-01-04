package com.mq.android.oa.bytter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Activity02 extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity02);
		
		Intent intent = this.getIntent();
		TextView tv = (TextView)this.findViewById(R.id.activity02_textview);
		tv.setText("testdata" + intent.getStringExtra("testdata"));
		
	}

}
