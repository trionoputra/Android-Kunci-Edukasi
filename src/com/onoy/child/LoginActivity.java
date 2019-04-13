package com.onoy.child;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onoy.child.service.AppService;
import com.onoy.child.utils.Constants;
import com.onoy.child.utils.Shared;

public class LoginActivity extends Activity implements OnClickListener {
	private boolean isOpenFromService = false;
	private EditText txtpin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Shared.initialize(getBaseContext());
		
		Button btnOK = (Button)findViewById(R.id.button1);
		btnOK.setOnClickListener(this);
		
		txtpin = (EditText)findViewById(R.id.editText1);
		
		btnOK.setTypeface(Shared.ArialRounded);
		txtpin.setTypeface(Shared.ArialRounded);
		
		TextView t1 = (TextView)findViewById(R.id.textView1);
		t1.setTypeface(Shared.ArialRounded);
		
		if(Shared.read(Constants.SETTING_PIN,Constants.DEFAULT_PIN).equals(Constants.DEFAULT_PIN))
			t1.setVisibility(View.VISIBLE);
		else
			t1.setVisibility(View.GONE);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			isOpenFromService = extras.getBoolean(Constants.IS_OPEN_FORM_SERVICE);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(txtpin.getText().toString().equals(Shared.read(Constants.SETTING_PIN,Constants.DEFAULT_PIN)))
		{
			if(!isOpenFromService)
			{
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			}
			else
			{
				AppService.currentState = AppService.TAKS_COMPLETE_AND_CONTINUE_APPS;
			}
			
			finish();
		}
		else
		{
			Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(isOpenFromService)
			AppService.currentState = AppService.APP_CLOSE_AND_LOCK_OPEN; 
		super.onResume();

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(isOpenFromService)
			if(AppService.currentState != AppService.TAKS_COMPLETE_AND_CONTINUE_APPS)
				AppService.currentState = AppService.WAITING_FOR_OPEN_APPS;
		
		super.onDestroy();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(isOpenFromService)
			if(AppService.currentState != AppService.TAKS_COMPLETE_AND_CONTINUE_APPS)
				AppService.currentState = AppService.WAITING_FOR_OPEN_APPS;
		super.onPause();

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		 Intent intent = new Intent();
	        intent
	                .setAction(Intent.ACTION_MAIN)
	                .addCategory(Intent.CATEGORY_HOME)
	                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
	        finish();
	}
}
