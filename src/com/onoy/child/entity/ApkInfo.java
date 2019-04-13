package com.onoy.child.entity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
public class ApkInfo {
	private String appname;
	private String pname;
	private String versionName;
	private int versionCode;
	private Drawable icon;
	private PackageInfo p;
	private String lockType;
	
	public PackageInfo getP() {
		return p;
	}
	public void setP(PackageInfo p) {
		this.p = p;
	}
	public String getLockType() {
		return lockType;
	}
	public void setLockType(String lockType) {
		this.lockType = lockType;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
	
	public static ApkInfo getInfoFromPackageName(String pkgName,
	        Context mContext) {
	    ApkInfo newInfo = new ApkInfo();
	    try {
	        PackageInfo p = mContext.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);

	        newInfo.setAppname(p.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
	        newInfo.setPname(p.packageName);;
	        newInfo.setVersionName(p.versionName);
	        newInfo.setVersionCode(p.versionCode);
	        newInfo.setIcon(p.applicationInfo.loadIcon(mContext.getPackageManager()));
	    } catch (NameNotFoundException e) {
	        e.printStackTrace();
	        return null;
	    }
	    return newInfo;
	}
	
	
	
}
