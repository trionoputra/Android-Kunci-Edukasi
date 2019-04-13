package com.onoy.child.sqlite.ds;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.onoy.child.entity.Category;
import com.onoy.child.entity.Question;
import com.onoy.child.sqlite.DbSchema;

public class CategoryDataSource {
	
	private SQLiteDatabase db;
	public CategoryDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_CATEGORY,null,null);
	}
	
	public Category get(String code) {
		 
		Category item = new Category();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_CATEGORY  + 
						       " Where " +DbSchema.COL_CATEGORY_CODE + " = '"+code+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				item.setCode(c.getString(c.getColumnIndex(DbSchema.COL_CATEGORY_CODE)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_CATEGORY_NAME)));
				
			} while (c.moveToNext());
		}
		return item;
	}
	
	public ArrayList<Category> getAll(){
		return this.getAll(false,0);
	}
	
	public ArrayList<Category> getAll(boolean isRandom,int limit) {
		 
		ArrayList<Category> items = new ArrayList<Category>();
		
		String sorderby = "";
		String slimit = "";
		
		if(isRandom)
			sorderby = " ORDER BY RANDOM() ";
		
		if(limit > 0)
			slimit = " LIMIT " + limit;
				
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_CATEGORY + sorderby + slimit ;
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				Category item = new Category();
				item.setCode(c.getString(c.getColumnIndex(DbSchema.COL_CATEGORY_CODE)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_CATEGORY_NAME)));
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(Category item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_CATEGORY_CODE, item.getCode());
		values.put(DbSchema.COL_CATEGORY_NAME, item.getName());
		
		return db.insert(DbSchema.TBL_CATEGORY, null, values);
	}
	
	public long update(Category item,String lastCode)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_CATEGORY_CODE, item.getCode());
		values.put(DbSchema.COL_CATEGORY_NAME, item.getName());
		
		return db.update(DbSchema.TBL_CATEGORY, values, DbSchema.COL_CATEGORY_CODE+"= '"+lastCode+"' ", null);
	}
	
	public int delete(String code)
	{
		return db.delete(DbSchema.TBL_CATEGORY, DbSchema.COL_CATEGORY_CODE + "= '" + code + "'", null);
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_CATEGORY  + 
						      " Where lower(" +DbSchema.COL_CATEGORY_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
