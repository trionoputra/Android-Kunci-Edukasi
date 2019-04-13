package com.onoy.child;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.onoy.child.utils.Constants;
import com.onoy.child.utils.Shared;

public class ResultActivity extends Activity implements OnClickListener {
	private boolean isSucces = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		TextView t2 = (TextView)findViewById(R.id.textView2);
		TextView t3 = (TextView)findViewById(R.id.textView3);
		TextView t4 = (TextView)findViewById(R.id.textView4);
		TextView t5 = (TextView)findViewById(R.id.textView5);
		TextView t6 = (TextView)findViewById(R.id.textView6);
		TextView t7 = (TextView)findViewById(R.id.textView7);
		TextView t8 = (TextView)findViewById(R.id.textView8);
		
		Button btnNext = (Button)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		
		t2.setTypeface(Shared.ArialRounded);
		t3.setTypeface(Shared.ArialRounded);
		t4.setTypeface(Shared.ArialRounded);
		t5.setTypeface(Shared.ArialRounded);
		t6.setTypeface(Shared.ArialRounded);
		t7.setTypeface(Shared.ArialRounded);
		t8.setTypeface(Shared.ArialRounded);
		
		btnNext.setTypeface(Shared.ArialRounded);
		
		int benar = Integer.valueOf(Shared.read("benar","0"));
		int totalSoal = WelcomeActivity.soal.size();
		int salah = totalSoal - benar;
		
		int goal = Integer.valueOf(Shared.read(Constants.SETTING_NUMBER_OF_GOALS,"0"));
		
		if(goal > totalSoal)
			goal = totalSoal;
		
		t3.setText(String.valueOf(benar));
		t5.setText(String.valueOf(salah));
		if(benar < goal)
		{
			t7.setText("GAGAL");
			t7.setTextColor(getResources().getColor(R.color.pink));
			t8.setVisibility(View.VISIBLE);
			t8.setText("kamu harus benar "+goal+" untuk bisa berhasil");
			isSucces = false;

			btnNext.setText("COBA LAGI");
		}
		else
		{
			t7.setText("BERHASIL");
			t7.setTextColor(getResources().getColor(R.color.ijo));
			t8.setVisibility(View.GONE);
			isSucces = true;
		}
		
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		WelcomeActivity.isSucces = isSucces;
		if(isSucces)
		{
			finish();
		}
		else
		{
			finish();
		}
	}
}
