package com.onoy.child.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	public DatabaseHelper(Context context) {
		super(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DbSchema.CREATE_TBL_APPS);
		db.execSQL(DbSchema.CREATE_TBL_SOAL);
		db.execSQL(DbSchema.CREATE_TBL_CATEGORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(DbSchema.DROP_TBL_APPS);
		db.execSQL(DbSchema.DROP_TBL_SOAL);
		db.execSQL(DbSchema.DROP_TBL_CATEGORY);
	}
}
