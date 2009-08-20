package com.msos.android.receiver;

import com.msos.android.activity.SosActivity;
import com.msos.android.log.Tag;
import com.msos.android.service.FallDetectService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * SosReceiver
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class SosReceiver extends BroadcastReceiver {
	
	 /* the intent source*/ 
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED"; 
    
	 /**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	public void onReceive(Context context, Intent intent) {
		 
	     if (intent.getAction().equals(ACTION)){
		 
	       boolean enableFallDetection = SosActivity.getPreferences(context).getBoolean("sos.falldetection.enable", false);
	       
	       // if Fall detection is enabled we start the service
	       if (enableFallDetection){
		       context.startService(new Intent(context, FallDetectService.class));
		       Log.d(Tag.MSOS, "Fall detection service lauched");
	       } else {
	    	   Log.d(Tag.MSOS, "Fall detection disabled");
	       }
	  }
	 }
}


