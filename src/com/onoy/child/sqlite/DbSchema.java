package com.onoy.child.sqlite;

public interface DbSchema {
	
	String DB_NAME = "com_onoy_child.db";
	int DB_VERSION = 1;
	
	String TBL_APPS = "apps";	
	String COL_APPS_CODE = "id";
	String COL_APPS_NAME = "name";
	String COL_LOCK_TYPE = "lock_type";
	
	String CREATE_TBL_APPS = "CREATE TABLE "
								+ TBL_APPS
								+ "(" 
									+ COL_APPS_CODE  + " TEXT PRIMARY KEY," 
									+ COL_APPS_NAME + " TEXT," 
									+ COL_LOCK_TYPE + " TEXT" 
								+ ");";

	String TBL_SOAL = "soal";	
	String COL_SOAL_ID = "id";
	String COL_SOAL_PERTANYAAN = "pertanyaan";
	String COL_SOAL_GAMBAR= "gamber";
	String COL_SOAL_PILIHAN_A = "plihanA";
	String COL_SOAL_PILIHAN_B = "plihanB";
	String COL_SOAL_PILIHAN_C = "plihanC";
	String COL_SOAL_PILIHAN_D = "plihanD";
	String COL_SOAL_JAWABAN = "jawaban";
	String COL_SOAL_ID_PELAJARAN = "idPelajaran";
	String COL_SOAL_ID_KELAS = "idKelas";
	String COL_SOAL_NAMA_PELAJARAN = "namaPelajaran";
	String COL_SOAL_NAMA_KELAS = "namaKelas";
	String COL_SOAL_TIPE = "tipe";
	
	String CREATE_TBL_SOAL = "CREATE TABLE "
								+ TBL_SOAL
								+ "(" 
									+ COL_SOAL_ID  + " TEXT PRIMARY KEY,"
									+ COL_SOAL_PERTANYAAN + " TEXT," 
									+ COL_SOAL_GAMBAR + " TEXT," 
									+ COL_SOAL_PILIHAN_A + " TEXT," 
									+ COL_SOAL_PILIHAN_B + " TEXT," 
									+ COL_SOAL_PILIHAN_C + " TEXT," 
									+ COL_SOAL_PILIHAN_D + " TEXT," 
									+ COL_SOAL_JAWABAN + " TEXT," 
									+ COL_SOAL_ID_PELAJARAN + " TEXT," 
									+ COL_SOAL_ID_KELAS + " TEXT,"
									+ COL_SOAL_NAMA_PELAJARAN + " TEXT," 
									+ COL_SOAL_NAMA_KELAS + " TEXT,"
									+ COL_SOAL_TIPE + " TEXT" 
								+ ");";
	
	
	String TBL_CATEGORY = "categori";	
	String COL_CATEGORY_CODE = "id";
	String COL_CATEGORY_NAME = "name";
	
	String CREATE_TBL_CATEGORY = "CREATE TABLE "
								+ TBL_CATEGORY
								+ "(" 
									+ COL_CATEGORY_CODE  + " TEXT PRIMARY KEY," 
									+ COL_CATEGORY_NAME + " TEXT" 
								+ ");";
													
	String DROP_TBL_APPS = "DROP TABLE IF EXISTS "+ TBL_APPS;
	String DROP_TBL_SOAL = "DROP TABLE IF EXISTS "+ TBL_SOAL;
	String DROP_TBL_CATEGORY = "DROP TABLE IF EXISTS "+ TBL_CATEGORY;
	
}
