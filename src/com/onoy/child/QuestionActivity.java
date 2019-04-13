package com.onoy.child;

import com.onoy.child.entity.Question;
import com.onoy.child.utils.Shared;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity implements OnClickListener {
	private RadioGroup pilihan;
	private int counter;
	private Question q; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		TextView t1 = (TextView)findViewById(R.id.textView1);
		TextView t2 = (TextView)findViewById(R.id.textView2);
		TextView t3 = (TextView)findViewById(R.id.textView3);
		
		Button btnNext = (Button)findViewById(R.id.btnNext);
		
		t1.setTypeface(Shared.ArialRounded);
		t2.setTypeface(Shared.ArialRounded);
		t3.setTypeface(Shared.ArialRounded);
		btnNext.setTypeface(Shared.ArialRounded);
		
		pilihan = (RadioGroup)findViewById(R.id.Radiopilihan);
		
		RadioButton pilihanA = (RadioButton)findViewById(R.id.pilihanA);
		RadioButton pilihanB = (RadioButton)findViewById(R.id.pilihanB);
		RadioButton pilihanC = (RadioButton)findViewById(R.id.pilihanC);
		RadioButton pilihanD = (RadioButton)findViewById(R.id.pilihanD);
		
		pilihanA.setTypeface(Shared.ArialRounded);
		pilihanB.setTypeface(Shared.ArialRounded);
		pilihanC.setTypeface(Shared.ArialRounded);
		pilihanD.setTypeface(Shared.ArialRounded);
		
		
		counter = 0;
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			counter  = extras.getInt("counter");
		}
		
		q = WelcomeActivity.soal.get(counter);
		t1.setText(q.getNamaPelajaran() + " - "+ q.getNamaKelas());
		t3.setText("Petanyaan ke "+(counter+1)+"/"+WelcomeActivity.soal.size());
		t2.setText(q.getPertanyaan());
		
		pilihanA.setText("A. "+q.getPlihanA());
		pilihanB.setText("B. "+q.getPlihanB());
		pilihanC.setText("C. "+q.getPlihanC());
		pilihanD.setText("D. "+q.getPlihanD());
		
		btnNext.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		cekJawaban();
	}
	
	private void cekJawaban()
	{
		int cekid = pilihan.getCheckedRadioButtonId();
		if( cekid == -1)
		{
			Toast.makeText(this, "kamu harus memilih jawaban yang benar", Toast.LENGTH_SHORT).show();
		}
		else
		{
			if((q.getJawaban().toUpperCase().equals("A") && cekid == R.id.pilihanA) || 
					(q.getJawaban().toUpperCase().equals("B") && cekid == R.id.pilihanB) || 
					(q.getJawaban().toUpperCase().equals("C") && cekid == R.id.pilihanC) || 
					(q.getJawaban().toUpperCase().equals("D") && cekid == R.id.pilihanD)
					)
			{
				Shared.write("benar",String.valueOf( (Integer.valueOf(Shared.read("benar", "0")) + 1)));
			}
			
			if(counter == WelcomeActivity.soal.size() - 1)
			{
				Intent intent = new Intent(this, ResultActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
			else
			{
				Intent intent = new Intent(this, QuestionActivity.class);
				intent.putExtra("counter", (counter+1));
				startActivity(intent);
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
			
				
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}


}
