package com.onoy.child.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.onoy.child.R;
import com.onoy.child.adapter.SpinnerCategoryAdapter;
import com.onoy.child.entity.Category;
import com.onoy.child.service.AppService;
import com.onoy.child.sqlite.DatabaseManager;
import com.onoy.child.sqlite.ds.AppsDataSource;
import com.onoy.child.sqlite.ds.CategoryDataSource;
import com.onoy.child.utils.Constants;
import com.onoy.child.utils.Shared;
import com.onoy.child.widget.ChangePINDialog;
import com.onoy.child.widget.DownloadQuestionDialog;
import com.onoy.child.widget.NumberOfGoalDialog;
import com.onoy.child.widget.NumberOfQuestionDialog;

public class SettingFragment extends Fragment implements OnCheckedChangeListener, OnClickListener {
	private View viewroot;
	private CheckBox cService,cStartup,cSetting,cUninstal;
	private RelativeLayout wPin,wCategory,wServices,wStartup,wlockSetting,wlockInstaller,wNumberQuestion,wNumberGoal,wDownload;
	
	private Spinner spinnerCategory;
	private SpinnerCategoryAdapter spinnerCategoryAdapter;
	private RelativeLayout loader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		viewroot = inflater.inflate(R.layout.fragment_settings, container, false);
	
		cService = (CheckBox)viewroot.findViewById(R.id.checkservice);
		cSetting = (CheckBox)viewroot.findViewById(R.id.checkstop);
		cUninstal = (CheckBox)viewroot.findViewById(R.id.checkuninstal);
		cStartup = (CheckBox)viewroot.findViewById(R.id.checkstartup);
		
		wPin = (RelativeLayout)viewroot.findViewById(R.id.settingChangepin);
		wCategory = (RelativeLayout)viewroot.findViewById(R.id.settingchangeCategory);
		wServices = (RelativeLayout)viewroot.findViewById(R.id.serviceWrapper);
		wStartup = (RelativeLayout)viewroot.findViewById(R.id.startupWrapper);
		wlockSetting = (RelativeLayout)viewroot.findViewById(R.id.locksettingWrapper);
		wlockInstaller = (RelativeLayout)viewroot.findViewById(R.id.lockinstallerWrapper);
		wNumberQuestion = (RelativeLayout)viewroot.findViewById(R.id.settingNumberquestion);
		wNumberGoal = (RelativeLayout)viewroot.findViewById(R.id.settingNumbergoal);
		wDownload = (RelativeLayout)viewroot.findViewById(R.id.settingDownload);
		
		cService.setChecked(AppService.serviceRunning);
		
		if(Shared.read(Constants.SETTING_STARTUP,"0").equals("1"))
			cStartup.setChecked(true);
		
		if(Shared.read(Constants.SETTING_LOCK_SETTING,"0").equals("1"))
			cSetting.setChecked(true);
		
		if(Shared.read(Constants.SETTING_PACKAGE_INSTALLER,"0").equals("1"))
			cUninstal.setChecked(true);
		
		cService.setOnCheckedChangeListener(this);
		cSetting.setOnCheckedChangeListener(this);
		cUninstal.setOnCheckedChangeListener(this);
		cStartup.setOnCheckedChangeListener(this);
		
		wPin.setOnClickListener(this);
		wCategory.setOnClickListener(this);
		wServices.setOnClickListener(this);
		wStartup.setOnClickListener(this);
		wlockSetting.setOnClickListener(this);
		wlockInstaller.setOnClickListener(this);
		wNumberQuestion.setOnClickListener(this);
		wNumberGoal.setOnClickListener(this);
		wDownload.setOnClickListener(this);
		
		loader = (RelativeLayout)getActivity().findViewById(R.id.loader);
		loader.setVisibility(View.GONE);
		spinnerCategory = (Spinner) viewroot.findViewById(R.id.spinner1);
		spinnerCategoryAdapter = new SpinnerCategoryAdapter(getActivity());
		
		spinnerCategory.setAdapter(spinnerCategoryAdapter);
		setSelectedCategory();
		
		getCategori();
		
		spinnerCategory.setOnItemSelectedListener(categoryOnchange);
		
		TextView numberquestion = (TextView)viewroot.findViewById(R.id.txtnumberofquestion);
		TextView numbergoal = (TextView)viewroot.findViewById(R.id.txtnumberofgoal);
		
		numberquestion.setText(Shared.read(Constants.SETTING_NUMBER_OF_QUESTION,"10"));
		numbergoal.setText(Shared.read(Constants.SETTING_NUMBER_OF_GOALS,"0"));
		
		return viewroot; 
	}
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.checkservice:
			toggleService(arg1);
			break;
		case R.id.checkstartup:
			toggleStartup(arg1);
			break;
		case R.id.checkstop:
			toggleLockSetting(arg1);;
			break;
		case R.id.checkuninstal:
			toggleLockInstaller(arg1);;
			break;
		default:
			break;
		}
	}
	
	private void toggleService(boolean checked)
	{
		Intent service = new Intent(getActivity(), AppService.class);
		if(checked)
		{
			if (!AppService.serviceRunning) {
	            service.setAction(AppService.STARTFOREGROUND_ACTION);
	            AppService.serviceRunning = true;
	        } 
		}
		else
		{
			if(AppService.serviceRunning) {
				 service.setAction(AppService.STOPFOREGROUND_ACTION);
		            AppService.serviceRunning = false;
	        }
		}
		
		getActivity().startService(service);
	}
	
	private void toggleStartup(boolean checked)
	{
		if(checked)
			Shared.write(Constants.SETTING_STARTUP, "1");
		else
			Shared.write(Constants.SETTING_STARTUP, "0");
	}
	
	private void toggleLockSetting(boolean checked)
	{
		if(checked)
			Shared.write(Constants.SETTING_LOCK_SETTING, "1");
		else
			Shared.write(Constants.SETTING_LOCK_SETTING, "0");
	}
	
	private void toggleLockInstaller(boolean checked)
	{
		if(checked)
			Shared.write(Constants.SETTING_PACKAGE_INSTALLER, "1");
		else
			Shared.write(Constants.SETTING_PACKAGE_INSTALLER, "0");
	}
	
	private void setCategory(String id,String name)
	{
		Shared.write(Constants.SETTING_CATEGORY_ID, id);
		Shared.write(Constants.SETTING_CATEGORY_NAME, name);
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		
		switch (arg0.getId()) {
		case R.id.settingChangepin:
			ChangePINDialog dialog = new ChangePINDialog();
        	dialog.show(getActivity().getSupportFragmentManager(), "");
			break;
		case R.id.settingchangeCategory:
			
			break;
		case R.id.serviceWrapper:
			cService.setChecked(!cService.isChecked());
			break;
		case R.id.startupWrapper:
			cStartup.setChecked(!cStartup.isChecked());
			break;
		case R.id.locksettingWrapper:
			cSetting.setChecked(!cSetting.isChecked());
			break;
		case R.id.lockinstallerWrapper:
			cUninstal.setChecked(!cUninstal.isChecked());
			break;
		case R.id.settingNumberquestion:
			NumberOfQuestionDialog dialog2 = new NumberOfQuestionDialog();
			dialog2.show(getActivity().getSupportFragmentManager(), "");
			break;
		case R.id.settingNumbergoal:
			NumberOfGoalDialog dialog3 = new NumberOfGoalDialog();
			dialog3.show(getActivity().getSupportFragmentManager(), "");
			break;
		case R.id.settingDownload:
			DownloadQuestionDialog dialog5 = new DownloadQuestionDialog();
			dialog5.show(getActivity().getSupportFragmentManager(), "");
			break;
		default:
			break;
		}
	}
	
	
	private void getCategori()
	{
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Shared.SERVER_URL+"getclass", new AsyncHttpResponseHandler() {
		    @Override
		    public void onStart() {
		    	if(spinnerCategoryAdapter.getCount() == 0)
		    		loader.setVisibility(View.VISIBLE);
		    }
		    @Override
		    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
		        // called when response HTTP status is "200 OK"
		    	loader.setVisibility(View.GONE);
		    	if(statusCode == 200)
		    	{
		    		try {
		    			String res = new String(response);
		    			JSONObject jsonObject = new JSONObject(res);
		    			
						JSONArray array = new JSONArray(jsonObject.getString("result"));
						
						SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
						CategoryDataSource DS = new CategoryDataSource(db);
						
						if(array.length() != 0 )
						{
							DS.truncate();
						}
						
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							Category data = new Category();
							data.setCode(obj.getString("id_kelas"));
							if(obj.getString("levels").toUpperCase().equals("TK"))
								data.setName(obj.getString("levels"));
							else
								data.setName(obj.getString("cls"));
							
							DS.insert(data);
						}
						
						spinnerCategoryAdapter.set(DS.getAll());
						setSelectedCategory();
						DatabaseManager.getInstance().closeDatabase();
						
					} catch (JSONException e) {}
		    	}
		    }
		    
		    @Override
		    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
		    	loader.setVisibility(View.GONE);
		    }

		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		});
	}
	
	private OnItemSelectedListener categoryOnchange = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			// TODO Auto-generated method stub
			Category cat = (Category) spinnerCategoryAdapter.getItem(position);
			setCategory(cat.getCode(), cat.getName());
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void setSelectedCategory()
	{
		if(spinnerCategoryAdapter.getCount() != 0)
		{
			ArrayList<Category> catlist = (ArrayList<Category>) spinnerCategoryAdapter.getAll();
			for (int i = 0; i < catlist.size(); i++) {
				if(Shared.read(Constants.SETTING_CATEGORY_ID,"").equals(catlist.get(i).getCode()))
						spinnerCategory.setSelection(i);
			}
		}
	}
}
