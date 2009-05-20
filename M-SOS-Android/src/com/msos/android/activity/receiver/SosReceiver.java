package com.msos.android.activity.receiver;

import com.msos.android.activity.SosActivity;
import com.msos.android.activity.service.FallDetectService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * SosReceiver
 * 
 * @author Ludovic Toinel
 */
public class SosReceiver extends BroadcastReceiver {

	 /* the intent source*/ 
    static final String ACTION = "android.intent.action.BOOT_COMPLETED"; 
    
	 /**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	public void onReceive(Context context, Intent intent) {
		 
	     if (intent.getAction().equals(ACTION)){
		 
	       boolean enableFallDetection = SosActivity.getPreferences(context).getBoolean("sos.falldetection.enable", false);
	       
	       // if Fall detection is enabled we start the service
	       if (enableFallDetection){
		       context.startService(new Intent(context, FallDetectService.class));
		       Log.d("SosReceiver.onReceive", "Fall detection service lauched");
	       } else {
	    	   Log.d("SosReceiver.onReceive", "Fall detection disabled");
	       }
	  }
	 }
}


