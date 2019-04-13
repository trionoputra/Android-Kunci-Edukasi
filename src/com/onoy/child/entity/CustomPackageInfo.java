package com.onoy.child.entity;

import android.content.pm.PackageInfo;

public class CustomPackageInfo {
	private boolean isSelected = false;
	private PackageInfo packageInfo;
	public CustomPackageInfo()
	{
	}
	public CustomPackageInfo(PackageInfo packageInfo)
	{
		this.packageInfo = packageInfo;
	}
	public PackageInfo getPackageInfo() {
		return packageInfo;
	}
	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
