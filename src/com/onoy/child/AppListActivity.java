package com.onoy.child;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.onoy.child.adapter.AppListAdapter;
import com.onoy.child.entity.ApkInfo;
import com.onoy.child.entity.CustomPackageInfo;
import com.onoy.child.service.AppService;
import com.onoy.child.sqlite.DatabaseManager;
import com.onoy.child.sqlite.ds.AppsDataSource;
import com.onoy.child.utils.Shared;

public  class AppListActivity extends Activity implements OnItemClickListener, OnClickListener {
	private PackageManager packageManager;
	private  ListView appList;
	private AppListAdapter adapter;
	private List<CustomPackageInfo> customPackageList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applist);
		
		appList = (ListView)findViewById(R.id.listView1);
		
		packageManager = getPackageManager();
		
        List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
         
        customPackageList = new ArrayList<CustomPackageInfo>();
        
        ArrayList<ApkInfo> apkinfo = new ArrayList<ApkInfo>();
        
        SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		AppsDataSource DS = new AppsDataSource(db);
		apkinfo = DS.getAll();
		DatabaseManager.getInstance().closeDatabase();
		
		
        for(PackageInfo pi : packageList) {
            boolean b = isSystemPackage(pi);
            if(!b && !pi.packageName.equals(getPackageName())) {
            	CustomPackageInfo c = new CustomPackageInfo(pi);
            	for (int i = 0; i < apkinfo.size(); i++) {
            		if(apkinfo.get(i).getPname().equals(pi.packageName))
            		{
                		c.setSelected(true);
                		break;
            		}
				} 
                customPackageList.add(c);
            }
        }
        
        adapter = new AppListAdapter(this, customPackageList, packageManager);
        appList.setAdapter(adapter);
        appList.setOnItemClickListener(this);
        
        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
	}
	
	private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		// TODO Auto-generated method stub
		CheckBox c = (CheckBox)view.findViewById(R.id.checkBox1);
		c.setChecked(!c.isChecked());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnAdd:
			saveList();
			break;
		case R.id.btnCancel:
			finish();
			break;
		default:
			break;
		}
	}
	
	private void saveList()
	{
		SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		AppsDataSource DS = new AppsDataSource(db);
		DS.truncate();
		for (int i = 0; i < customPackageList.size(); i++)
		{
			CustomPackageInfo cpi = (CustomPackageInfo) adapter.getItem(i);
			
			if(cpi.isSelected())
			{
				ApkInfo apk = new ApkInfo();
				PackageInfo pi = customPackageList.get(i).getPackageInfo();
				apk.setAppname(pi.applicationInfo.loadLabel(getPackageManager()).toString());
				apk.setPname(pi.packageName);
				apk.setLockType("1");
				DS.insert(apk);
			}
		}
		DatabaseManager.getInstance().closeDatabase();
		AppService.hasUpdate = true;
		finish();
	}
}
