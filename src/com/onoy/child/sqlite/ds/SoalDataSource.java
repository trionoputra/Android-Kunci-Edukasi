package com.onoy.child.sqlite.ds;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.onoy.child.entity.Question;
import com.onoy.child.sqlite.DbSchema;

public class SoalDataSource {
	
	private SQLiteDatabase db;
	public SoalDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_SOAL,null,null);
	}
	
	public Question get(String code) {
		 
		Question item = new Question();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_SOAL  + 
						       " Where " +DbSchema.COL_SOAL_ID + " = '"+code+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				item.setId(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_ID)));
				item.setPertanyaan(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PERTANYAAN)));
				item.setGambar(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_GAMBAR)));
				item.setPlihanA(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PILIHAN_A)));
				item.setPlihanB(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PILIHAN_B)));
				item.setPlihanC(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PILIHAN_C)));
				item.setPlihanD(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PILIHAN_D)));
				item.setJawaban(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_JAWABAN)));
				item.setIdPelajaran(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_ID_PELAJARAN)));
				item.setIdKelas(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_ID_KELAS)));
				item.setTipe(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_TIPE)));
				item.setNamaPelajaran(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_NAMA_PELAJARAN)));
				item.setNamaKelas(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_NAMA_KELAS)));
				
			} while (c.moveToNext());
		}
		return item;
	}
	
	public ArrayList<Question> getAll(){
		return this.getAll(false,0);
	}
	
	public ArrayList<Question> getAll(boolean isRandom,int limit) {
		 
		ArrayList<Question> items = new ArrayList<Question>();
		
		String sorderby = "";
		String slimit = "";
		
		if(isRandom)
			sorderby = " ORDER BY RANDOM() ";
		
		if(limit > 0)
			slimit = " LIMIT " + limit;
				
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_SOAL + sorderby + slimit ;
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				Question item = new Question();
				item.setId(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_ID)));
				item.setPertanyaan(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PERTANYAAN)));
				item.setGambar(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_GAMBAR)));
				item.setPlihanA(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PILIHAN_A)));
				item.setPlihanB(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PILIHAN_B)));
				item.setPlihanC(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PILIHAN_C)));
				item.setPlihanD(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_PILIHAN_D)));
				item.setJawaban(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_JAWABAN)));
				item.setIdPelajaran(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_ID_PELAJARAN)));
				item.setIdKelas(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_ID_KELAS)));
				item.setTipe(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_TIPE)));
				item.setNamaPelajaran(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_NAMA_PELAJARAN)));
				item.setNamaKelas(c.getString(c.getColumnIndex(DbSchema.COL_SOAL_NAMA_KELAS)));
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(Question item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_SOAL_ID, item.getId());
		values.put(DbSchema.COL_SOAL_PERTANYAAN, item.getPertanyaan());
		values.put(DbSchema.COL_SOAL_PILIHAN_A, item.getPlihanA());
		values.put(DbSchema.COL_SOAL_PILIHAN_B, item.getPlihanB());
		values.put(DbSchema.COL_SOAL_PILIHAN_C, item.getPlihanC());
		values.put(DbSchema.COL_SOAL_PILIHAN_D, item.getPlihanD());
		values.put(DbSchema.COL_SOAL_JAWABAN, item.getJawaban());
		values.put(DbSchema.COL_SOAL_ID_PELAJARAN, item.getIdPelajaran());
		values.put(DbSchema.COL_SOAL_ID_KELAS, item.getIdKelas());
		values.put(DbSchema.COL_SOAL_TIPE, item.getTipe());
		values.put(DbSchema.COL_SOAL_NAMA_PELAJARAN, item.getNamaPelajaran());
		values.put(DbSchema.COL_SOAL_NAMA_KELAS, item.getNamaKelas());
		
		if(cekCode(item.getId()))
			return db.insert(DbSchema.TBL_SOAL, null, values);
		else
			return db.update(DbSchema.TBL_SOAL, values, DbSchema.COL_SOAL_ID+"= '"+item.getId()+"' ", null);
	}
	
	public long update(Question item,String lastCode)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_SOAL_ID, item.getId());
		values.put(DbSchema.COL_SOAL_PERTANYAAN, item.getPertanyaan());
		values.put(DbSchema.COL_SOAL_PILIHAN_A, item.getPlihanA());
		values.put(DbSchema.COL_SOAL_PILIHAN_B, item.getPlihanB());
		values.put(DbSchema.COL_SOAL_PILIHAN_C, item.getPlihanC());
		values.put(DbSchema.COL_SOAL_PILIHAN_D, item.getPlihanD());
		values.put(DbSchema.COL_SOAL_JAWABAN, item.getJawaban());
		values.put(DbSchema.COL_SOAL_ID_PELAJARAN, item.getIdPelajaran());
		values.put(DbSchema.COL_SOAL_ID_KELAS, item.getIdKelas());
		values.put(DbSchema.COL_SOAL_TIPE, item.getTipe());
		values.put(DbSchema.COL_SOAL_NAMA_PELAJARAN, item.getNamaPelajaran());
		values.put(DbSchema.COL_SOAL_NAMA_KELAS, item.getNamaKelas());
		
		return db.update(DbSchema.TBL_SOAL, values, DbSchema.COL_SOAL_ID+"= '"+lastCode+"' ", null);
	}
	
	public int delete(String code)
	{
		return db.delete(DbSchema.TBL_SOAL, DbSchema.COL_SOAL_ID + "= '" + code + "'", null);
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_SOAL  + 
						      " Where lower(" +DbSchema.COL_SOAL_ID + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
