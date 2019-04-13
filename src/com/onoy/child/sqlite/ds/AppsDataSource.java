package com.onoy.child.sqlite.ds;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.onoy.child.entity.ApkInfo;
import com.onoy.child.sqlite.DbSchema;

public class AppsDataSource {
	
	private SQLiteDatabase db;
	public AppsDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_APPS,null,null);
	}
	
	public ApkInfo get(String code) {
		 
		ApkInfo item = new ApkInfo();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_APPS  + 
						       " Where " +DbSchema.COL_APPS_CODE + " = '"+code+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				item.setPname(c.getString(c.getColumnIndex(DbSchema.COL_APPS_CODE)));
				item.setAppname(c.getString(c.getColumnIndex(DbSchema.COL_APPS_NAME)));
				item.setLockType(c.getString(c.getColumnIndex(DbSchema.COL_LOCK_TYPE)));
			} while (c.moveToNext());
		}
	
		return item;
	}
	
	public ArrayList<ApkInfo> getAll() {
		 
		ArrayList<ApkInfo> items = new ArrayList<ApkInfo>();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_APPS;
		 
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				ApkInfo item = new ApkInfo();
				item.setPname(c.getString(c.getColumnIndex(DbSchema.COL_APPS_CODE)));
				item.setAppname(c.getString(c.getColumnIndex(DbSchema.COL_APPS_NAME)));
				item.setLockType(c.getString(c.getColumnIndex(DbSchema.COL_LOCK_TYPE)));
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(ApkInfo item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_APPS_CODE, item.getPname());
		values.put(DbSchema.COL_APPS_NAME, item.getAppname());
		values.put(DbSchema.COL_LOCK_TYPE, item.getLockType());
		
		return db.insert(DbSchema.TBL_APPS, null, values);
	}
	
	public long update(ApkInfo item,String lastCode)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_APPS_CODE, item.getPname());
		values.put(DbSchema.COL_APPS_NAME, item.getAppname());
		values.put(DbSchema.COL_LOCK_TYPE, item.getLockType());
		
		return db.update(DbSchema.TBL_APPS, values, DbSchema.COL_APPS_CODE+"= '"+lastCode+"' ", null);
	}
	
	public int delete(String code)
	{
		return db.delete(DbSchema.TBL_APPS, DbSchema.COL_APPS_CODE + "= '" + code + "'", null);
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_APPS  + 
						      " Where lower(" +DbSchema.COL_APPS_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
