package com.onoy.child.service;

import com.onoy.child.utils.Constants;
import com.onoy.child.utils.Shared;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
public class ServiceReceiver extends BroadcastReceiver {
	private String TAG = "ONOY SERVICE";
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d(TAG,"onReceive");
    	Shared.initialize(context);
    	if(Shared.read(Constants.SETTING_STARTUP, "0").equals("1"))
    	{
    		if (!AppService.serviceRunning) {
            	Intent service = new Intent(context, AppService.class);
                service.setAction(AppService.STARTFOREGROUND_ACTION);
                context.startService(service);
                Log.d(TAG,"Start App Serive");
                AppService.serviceRunning = true;
            }
            else
            	Log.d(TAG,"Not Start App Serive");
    	}
        
        
        Log.d(TAG,"end onReceive");
        
    }
}