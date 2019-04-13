package com.onoy.child.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Typeface;

public final class Shared
{
	private static ContextWrapper instance;
	private static SharedPreferences pref;
	public static Typeface ArialRounded;
	public static String SERVER_URL = "http://trionoputra.com/kampus/lockedu/index.php/api/mobile/";
	public static void initialize(Context base)
	{
		instance = new ContextWrapper(base);
		pref = instance.getSharedPreferences("com.onoy.child", Context.MODE_PRIVATE);
		ArialRounded = Typeface.createFromAsset(instance.getAssets(),"font/ARLRDBD.TTF");
	}
	public static void write(String key, String value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String read(String key)
	{
		return Shared.read(key, null);
	}
	
	public static String read(String key, String defValue)
	{
		return pref.getString(key, defValue);
	}
	
	public static void clear()
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
	
	public static void clear(String key)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public static Context getContext()
	{
		return instance.getBaseContext();
	}
	
	
	
}

