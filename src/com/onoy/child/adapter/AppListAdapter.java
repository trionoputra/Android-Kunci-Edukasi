package com.onoy.child.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.onoy.child.R;
import com.onoy.child.entity.CustomPackageInfo;

public class AppListAdapter extends BaseAdapter {
 
    private List<CustomPackageInfo> packageList;
    private Activity context;
    private PackageManager packageManager;
    private boolean isLockedList = false;
    private LayoutInflater inflater;
    public AppListAdapter(Activity context, List<CustomPackageInfo> packageList,PackageManager packageManager) {
      
        this.context = context;
        this.packageList = packageList;
        this.packageManager = packageManager;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public AppListAdapter(Activity context, List<CustomPackageInfo> packageList,PackageManager packageManager,boolean islockList) {
     
        this.context = context;
        this.packageList = packageList;
        this.packageManager = packageManager;
        this.isLockedList = islockList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    private class ViewHolder {
        TextView appName;
        ImageView icon;
        CheckBox check;
    }
 
    public int getCount() {
        return packageList.size();
    }
    
    public void set(List<CustomPackageInfo> list) {
        packageList = list;
        notifyDataSetChanged();
    }
 
    public Object getItem(int position) {
        return packageList.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.applist_item, null);
            holder = new ViewHolder();
 
            holder.appName = (TextView) vi.findViewById(R.id.appname);
            holder.icon = (ImageView) vi.findViewById(R.id.imageView1);
            holder.check = (CheckBox) vi.findViewById(R.id.checkBox1);
            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag(); 
                    packageList.get(getPosition).setSelected(buttonView.isChecked()); 
                }
            });
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        if(isLockedList)
        	holder.check.setVisibility(View.GONE);
        
        holder.check.setTag(position);
        holder.check.setChecked(packageList.get(position).isSelected());
        
        CustomPackageInfo customPackageInfo= (CustomPackageInfo) getItem(position);
        PackageInfo packageInfo = customPackageInfo.getPackageInfo();
        
        Drawable appIcon = packageManager.getApplicationIcon(packageInfo.applicationInfo);
        String appName = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
        appIcon.setBounds(0, 0, 40, 40);
        holder.icon.setImageDrawable(appIcon);
        holder.appName.setText(appName);
        return vi;
    }
}