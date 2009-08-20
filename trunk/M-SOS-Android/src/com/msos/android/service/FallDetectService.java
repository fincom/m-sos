package com.msos.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.msos.android.R;
import com.msos.android.activity.SosActivity;
import com.msos.android.log.Tag;
import com.msos.android.manager.AlertManager;

/**
 * This service detect when a person fall
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class FallDetectService extends Service implements SensorEventListener {
	
	// Sensor
    private SensorManager mSensorMgr = null;

    // Media player
    private static MediaPlayer mp = null;
    
    // Force detected
    private static boolean fallDetected = false;
    
    // Background thread
    private static Thread thread = null;
    
    // Last acceleration magnitude
    private float lastAccelMag;
    
    // Sensibility Level
    private float sensibilityLevel;
    
    @Override
    public void onCreate() {
         super.onCreate();
            
            // Android sensor Manager
            mSensorMgr = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
            Sensor sensor = mSensorMgr.getSensorList(SensorManager.SENSOR_ACCELEROMETER).get(0);
            
            if (!mSensorMgr.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)){
                Log.e(Tag.MSOS, "No suited sensor found");
                stopSelf();
            } else {
                Log.i(Tag.MSOS, "SensorListener found");
            }
            
            sensibilityLevel = Float.parseFloat(SosActivity.getPreferences(getApplicationContext()).getString("sos.falldetection.sensibility", "120"));
         
   	}

    	
    @Override
	public void onDestroy() {
		super.onDestroy();
		mSensorMgr.unregisterListener(this);
	}


	/**
     * @see android.hardware.SensorListener#onSensorChanged(int, float[])
     */
    public void onSensorChanged(SensorEvent event) {
    
    	
    	if(event.sensor.getType() == SensorManager.SENSOR_ACCELEROMETER && !fallDetected){
    	      
    		
    		  // Acceleration magnitude
    		  float accelMag = (event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2]);
    	       
    	 	  //Log.i("FallDetectService.onSensorChanged", String.valueOf(values[0]*values[0]) + "|" +  String.valueOf(values[1]*values[1]) + "|" +  String.valueOf(values[2]*values[2]) );
	    	   
    	      if (((lastAccelMag - accelMag) > sensibilityLevel) && !fallDetected)
    	      {
    	    	   	  fallDetected = true;
    	              Log.v(Tag.MSOS,"Force Detected");

    	              // Play alarm
    	              if (mp == null || !mp.isPlaying()){

    	                  // Start Activity
    	                  Intent intent = new Intent(getApplicationContext(),SosActivity.class);
    	                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	                  startActivity(intent);
	        	          
    	                  // Call the person
    	                  thread = new Thread() {
    	                	  MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
    	                	 
    	                	  
    	                	  public void run() {
    	                		 try {
    	                			mp.setLooping(true); 
    	        	              	mp.start();
    	        	              	sleep(30000);
    	        	              	mp.stop();

	    	    	                AlertManager.getInstance(getApplicationContext()).call();

								 } catch (InterruptedException e) {
									 if (mp != null)
									  mp.stop();
								 }
								
    	                	  }
    	                	  
    	          			};
    	          			thread.start();
    	              }
    	       } 
    	    
    	      lastAccelMag = accelMag;

    	 }
    }

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	/**
	 * Stop the alarm
	 */
	public static void stopAlarm(){

		if (thread != null){
			thread.stop();
			thread.interrupt();
			thread = null;
		}
		fallDetected = false;
		
	}
	
	
	/**
	 * Fall detected
	 */
	public static boolean isFallDetected(){
		return fallDetected;
	}


	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing
	}


}
