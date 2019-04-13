package com.onoy.child;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.onoy.child.entity.Question;
import com.onoy.child.service.AppService;
import com.onoy.child.sqlite.DatabaseHelper;
import com.onoy.child.sqlite.DatabaseManager;
import com.onoy.child.sqlite.ds.SoalDataSource;
import com.onoy.child.utils.Constants;
import com.onoy.child.utils.Shared;
import com.onoy.child.widget.DownloadQuestionDialog;

public class WelcomeActivity extends Activity {
	private String TAG = "ONOY SERVICE";
	public static ArrayList<Question> soal = new ArrayList<Question>();
	public static boolean isSucces = false;
	
	private TextView t1;
	private TextView t2;
	private Button btnStart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Shared.initialize(getBaseContext());
		DatabaseManager.initializeInstance(new DatabaseHelper(this));
		setContentView(R.layout.activity_welcome);
		
		isSucces = false;
		
		Shared.write("benar", "0");
		
		t1 = (TextView)findViewById(R.id.textView1);
		t2 = (TextView)findViewById(R.id.textView2);
		
		btnStart = (Button)findViewById(R.id.btnStart);
		
		t1.setTypeface(Shared.ArialRounded);
		t2.setTypeface(Shared.ArialRounded);
		btnStart.setTypeface(Shared.ArialRounded);
		
		btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(soal.size() == 0)
				{
					AppService.currentState = AppService.TAKS_COMPLETE_AND_CONTINUE_APPS;
					finish();
				}
				else
				{
					Intent intent = new Intent(WelcomeActivity.this, QuestionActivity.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Shared.write("benar", "0");
		if(isSucces)
		{
			finish();
		}
		else
			AppService.currentState = AppService.APP_CLOSE_AND_LOCK_OPEN; 
		

		getQuestion();
		
		Log.d(TAG, "onResume");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(isSucces)
		{
			AppService.currentState = AppService.TAKS_COMPLETE_AND_CONTINUE_APPS;
			isSucces = false;
		}
		Log.d(TAG, "onDestroy");
		if(AppService.currentState != AppService.TAKS_COMPLETE_AND_CONTINUE_APPS)
			AppService.currentState = AppService.WAITING_FOR_OPEN_APPS;
		
		super.onDestroy();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(AppService.currentState != AppService.TAKS_COMPLETE_AND_CONTINUE_APPS)
			AppService.currentState = AppService.WAITING_FOR_OPEN_APPS;
		
		super.onPause();
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		 Intent intent = new Intent();
	        intent
	                .setAction(Intent.ACTION_MAIN)
	                .addCategory(Intent.CATEGORY_HOME)
	                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
	        finish();
	}
	
	private void getQuestion()
	{
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Shared.SERVER_URL+"getrandsoal/"+Shared.read(Constants.SETTING_CATEGORY_ID,""), new AsyncHttpResponseHandler() {
		    @Override
		    public void onStart() {
		    	btnStart.setEnabled(false);
				btnStart.setText("Loading...");
		    }
		    @Override
		    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
		        // called when response HTTP status is "200 OK"
		    	btnStart.setEnabled(true);
				btnStart.setText("START");
		    	if(statusCode == 200)
		    	{
		    		try {
		    			String res = new String(response);
		    			JSONObject jsonObject = new JSONObject(res);
		    			
						JSONArray array = new JSONArray(jsonObject.getString("result"));
						if(array.length() != 0 )
						{
							soal = new ArrayList<Question>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj = array.getJSONObject(i);
								Question data = new Question();
								data.setId(obj.getString("id_soal"));
								data.setPertanyaan(obj.getString("pertanyaan"));
								data.setTipe(obj.getString("tipe"));
								data.setGambar(obj.getString("gambar"));
								data.setPlihanA(obj.getString("pilihanA"));
								data.setPlihanB(obj.getString("pilihanB"));
								data.setPlihanC(obj.getString("pilihanC"));
								data.setPlihanD(obj.getString("pilihanD"));
								data.setJawaban(obj.getString("jawaban"));
								data.setIdPelajaran(obj.getString("id_matapelajaran"));
								data.setIdKelas(obj.getString("id_kelas"));
								data.setNamaPelajaran(obj.getString("mapel"));
								
								if(obj.getString("levels").toUpperCase().equals("TK"))
									data.setNamaKelas(obj.getString("levels"));
								else
									data.setNamaKelas(obj.getString("kelas")+" "+obj.getString("levels"));
								
								soal.add(data);
							}
						}
						else
							getLocalQuestion();
						
					} catch (JSONException e) {
						getLocalQuestion();
					}
		    	}
		    	else
		    	{
		    		getLocalQuestion();
		    	}
		    }
		    
		    @Override
		    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
		    	btnStart.setEnabled(true);
				btnStart.setText("START");
		    	getLocalQuestion();
		    }

		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		});
		
	}
	
	private void getLocalQuestion()
	{
		SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		SoalDataSource DS = new SoalDataSource(db);
		soal = DS.getAll(true,Integer.valueOf(Shared.read(Constants.SETTING_NUMBER_OF_QUESTION,"0")));
		DatabaseManager.getInstance().closeDatabase();
		
		if(soal.size() == 0)
		{
			t1.setText("selamat tidak ada pertanyaan pada saat ini :D");
			t2.setText("tekan lanjut untuk kembali ke permainan");
			btnStart.setText("LANJUT");
		}
		
	}
	
	
}
