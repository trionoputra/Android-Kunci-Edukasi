package com.onoy.child.widget;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.onoy.child.R;
import com.onoy.child.entity.Category;
import com.onoy.child.entity.Question;
import com.onoy.child.sqlite.DatabaseManager;
import com.onoy.child.sqlite.ds.CategoryDataSource;
import com.onoy.child.sqlite.ds.SoalDataSource;
import com.onoy.child.utils.Constants;
import com.onoy.child.utils.Shared;

public class DownloadQuestionDialog extends DialogFragment {
	private ProgressDialog loader;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),DialogFragment.STYLE_NO_FRAME);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.view_downloadquestion, null);
		builder.setView(v);
		
		AlertDialog dialog = builder.create();
		
		Button ok = (Button)v.findViewById(R.id.btnSave);
		Button cancel = (Button)v.findViewById(R.id.btnCancel);
		
		ok.setTypeface(Shared.ArialRounded);
		cancel.setTypeface(Shared.ArialRounded);
		
		TextView t1 = (TextView)v.findViewById(R.id.textView1);
		TextView t2 = (TextView)v.findViewById(R.id.textView2);
		
		t1.setTypeface(Shared.ArialRounded);
		t2.setTypeface(Shared.ArialRounded);

		loader = new ProgressDialog(getActivity());
		
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Shared.read(Constants.SETTING_CATEGORY_ID,"").equals(""))
				{
					Toast.makeText(getActivity(), "Pilih kategori terlibih dahulu", Toast.LENGTH_SHORT).show();
					dismiss();
				}
				else
				{
					getSoal(Shared.read(Constants.SETTING_CATEGORY_ID));
				}
			}
		});
		
	  	    
		return dialog;
	}
	
	
	private void getSoal(String id)
	{
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Shared.SERVER_URL+"getsoal/"+id, new AsyncHttpResponseHandler() {
		    @Override
		    public void onStart() {
		    		loader.show();
		    		DownloadQuestionDialog.this.setCancelable(false);
		    }
		    @Override
		    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
		        // called when response HTTP status is "200 OK"
		    	loader.dismiss();
		    	if(statusCode == 200)
		    	{
		    		try {
		    			String res = new String(response);
		    			JSONObject jsonObject = new JSONObject(res);
		    			
						JSONArray array = new JSONArray(jsonObject.getString("result"));
						SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
						SoalDataSource DS = new SoalDataSource(db);
						
						if(array.length() != 0 )
						{
							DS.truncate();
						}
						
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
							
							DS.insert(data);
						}
						
						DatabaseManager.getInstance().closeDatabase();
						
						Toast.makeText(getActivity(), "soal berhasil didownload", Toast.LENGTH_SHORT).show();
						dismiss();
					} catch (JSONException e) {
						Toast.makeText(getActivity(), "internal server error", Toast.LENGTH_SHORT).show();
					}
		    	}
		    	else
		    	{
		    		Toast.makeText(getActivity(), "gagal mendownload soal", Toast.LENGTH_SHORT).show();
		    	}
		    }
		    
		    @Override
		    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
		    	loader.dismiss();
		    	Toast.makeText(getActivity(), "gagal mendownload soal", Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		});
	}
	

}
