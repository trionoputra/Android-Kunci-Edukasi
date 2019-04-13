package com.onoy.child.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.onoy.child.AppListActivity;
import com.onoy.child.R;
import com.onoy.child.adapter.AppListAdapter;
import com.onoy.child.entity.ApkInfo;
import com.onoy.child.entity.CustomPackageInfo;
import com.onoy.child.sqlite.DatabaseManager;
import com.onoy.child.sqlite.ds.AppsDataSource;

public class AppLockListFragment extends Fragment implements OnItemClickListener, OnClickListener{
	private View viewroot;
	private PackageManager packageManager;
	private ListView appList;
	private AppListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		viewroot = inflater.inflate(R.layout.fragment_applock, container, false);
		
		appList = (ListView)viewroot.findViewById(R.id.listView1);
		
		packageManager = getActivity().getPackageManager();
        adapter = new AppListAdapter(getActivity(), new ArrayList<CustomPackageInfo>(), packageManager,true);
        appList.setAdapter(adapter);
        appList.setOnItemClickListener(this);
		
        viewroot.findViewById(R.id.btnAdd).setOnClickListener(this);
        
		return viewroot; 
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), AppListActivity.class);
		startActivity(intent);
	}
	
	private void populateList()
	{
		ArrayList<ApkInfo> apkinfo = new ArrayList<ApkInfo>();
        
        SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		AppsDataSource DS = new AppsDataSource(db);
		apkinfo = DS.getAll();
		DatabaseManager.getInstance().closeDatabase();
		
		RelativeLayout emptyWrapper = (RelativeLayout)viewroot.findViewById(R.id.emptyWrapper);

		if(apkinfo.size() == 0 )
			emptyWrapper.setVisibility(View.VISIBLE);
		else
			emptyWrapper.setVisibility(View.GONE);

		List<CustomPackageInfo> customPackageList = new ArrayList<CustomPackageInfo>();
		for (int i = 0; i < apkinfo.size(); i++) {
			 try {
				PackageInfo p = getActivity().getPackageManager().getPackageInfo(apkinfo.get(i).getPname(), PackageManager.GET_PERMISSIONS);
				CustomPackageInfo c = new CustomPackageInfo(p);
                customPackageList.add(c);
			 } catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		adapter.set(customPackageList);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		populateList();
	}
}
