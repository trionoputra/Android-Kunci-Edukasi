package com.onoy.child.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.onoy.child.LoginActivity;
import com.onoy.child.MainActivity;
import com.onoy.child.R;
import com.onoy.child.WelcomeActivity;
import com.onoy.child.entity.ApkInfo;
import com.onoy.child.sqlite.DatabaseHelper;
import com.onoy.child.sqlite.DatabaseManager;
import com.onoy.child.sqlite.ds.AppsDataSource;
import com.onoy.child.utils.Constants;
import com.onoy.child.utils.Shared;

public class AppService extends Service{
	private String TAG = "ONOY SERVICE";
	private mThread mthread;
	public boolean isRunning = false;
	
	public static final int WAITING_FOR_OPEN_APPS = 1;
	public static final int APP_CLOSE_AND_LOCK_OPEN = 2;
	public static final int TAKS_COMPLETE_AND_CONTINUE_APPS = 3;
	public static int currentState = WAITING_FOR_OPEN_APPS;  
	
	public static String STARTFOREGROUND_ACTION = "com.onoy.child.service.action.startforeground";
	public static String STOPFOREGROUND_ACTION = "com.onoy.child.service.action.stopforeground";
	public static String MAIN_ACTION = "com.onoy.child.service.action.main";
	
	public static int FOREGROUND_SERVICE = 101;
	public static boolean serviceRunning = false;
	public static boolean hasUpdate = false;
	
	private RunningAppProcessInfo lastRunningAppProcessInfo;
	
	private  ArrayList<ApkInfo> applist;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Shared.initialize(getBaseContext());
		mthread  = new mThread();
		DatabaseManager.initializeInstance(new DatabaseHelper(this));
		
		getApplist();
	}
	
	private void getApplist()
	{
		SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		AppsDataSource DS = new AppsDataSource(db);
		applist = DS.getAll();
		DatabaseManager.getInstance().closeDatabase();
		hasUpdate = false;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent.getAction() != null)
		{
			if (intent.getAction().equals(AppService.STARTFOREGROUND_ACTION)) {
			
				Intent notificationIntent = new Intent(this, MainActivity.class);
				notificationIntent.setAction(AppService.MAIN_ACTION);
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
				 
				Notification notification = new NotificationCompat.Builder(this)
				 .setContentTitle("Kunci Endukasi")
				 .setTicker("Kunci Endukasi  is Running")
				 .setContentText("running")
				 .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
				 .setContentIntent(pendingIntent)
				 .setOngoing(true)
				 .build();
				
				Log.d(TAG,"Show notification");
				startForeground(AppService.FOREGROUND_SERVICE,notification);
				if(!isRunning)
				{
					mthread.start();
			        isRunning = true;
			    }
				
			}
			
			else if (intent.getAction().equals(AppService.STOPFOREGROUND_ACTION)) {
				stopForeground(true);
				stopSelf();
				if(isRunning)
				{
					 mthread.interrupt();
					 isRunning = false;
			    } 
			}
		}
		

		Log.d(TAG,isRunning ? "start service" : "stop service");
		
		return START_STICKY;
		//return super.onStartCommand(intent, flags, startId);
	}
	
	public static boolean isForeground(Context ctx, String myPackage){
	    ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
	    List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(1); 

	    ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
	    if(componentInfo.getPackageName().equals(myPackage)) {
	        return true;
	    }       
	    return false;
	}
	
	private void getRunningApps()
	{
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();

     	Log.d(TAG,"waiting for open apps");
     	Log.d(TAG,"LAST STATE : " + currentState);
     	boolean appHasClose = true;
     	
     	
     	
        for ( ActivityManager.RunningAppProcessInfo appProcess: runningAppProcessInfo )
        {
        	for (int i = 0; i < applist.size(); i++) 
        	{
        		if (appProcess.processName.equals(applist.get(i).getPname())) 
        		{
        			 if ( appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && isForeground(getApplicationContext(),appProcess.processName))
        			 {
        				 
                         if(currentState == WAITING_FOR_OPEN_APPS )
                         {
                         	Log.d(TAG,"open apps ");
                         	lastRunningAppProcessInfo = appProcess;
                            Intent dialogIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
                         	startActivity(dialogIntent);
                         	
                         }
                         
                         appHasClose = false;
        			 }
        			 
        		}
			}
        	
        	if (((appProcess.processName.equals(Constants.PACKAGE_SETTING) && Shared.read(Constants.SETTING_LOCK_SETTING,"0").equals("1") ) || (appProcess.processName.equals(Constants.PACKAGE_INSTALLER) && Shared.read(Constants.SETTING_PACKAGE_INSTALLER,"0").equals("1") )) && isForeground(getApplicationContext(),appProcess.processName))        		
        	{
        		if(currentState == WAITING_FOR_OPEN_APPS)
                {
                	Log.d(TAG,"open apps ");
                	lastRunningAppProcessInfo = appProcess;
                    Intent dialogIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    dialogIntent.putExtra(Constants.IS_OPEN_FORM_SERVICE, true);
                	dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
                	startActivity(dialogIntent);
                	appHasClose = false;
                	
                }
        	}
        }
        
        
        if(AppService.currentState == AppService.TAKS_COMPLETE_AND_CONTINUE_APPS)
    	{
        	if(lastRunningAppProcessInfo != null)
            {
        		try {
        			Thread.sleep(5000);
        			if(appHasClose && !isForeground(getApplicationContext(),lastRunningAppProcessInfo.processName))
        			{
        				Log.d(TAG,"APP CLOSE");
        				currentState = WAITING_FOR_OPEN_APPS;
        			}
    				
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} 
            }
    	}
        
        	
	}
	
	class mThread extends Thread{
        final long DELAY = 100;
        @Override
        public void run(){          
            while(isRunning){
                try {
                	
                	if(hasUpdate)
                		getApplist();
                	
                	getRunningApps();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }
    }
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 if(isRunning)
		 {
			 mthread.interrupt();
			 isRunning = false;
	     } 
	}

}
