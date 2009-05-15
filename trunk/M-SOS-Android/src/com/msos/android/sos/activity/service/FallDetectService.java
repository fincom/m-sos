package com.msos.android.sos.activity.service;

import com.msos.android.sos.activity.R;
import com.msos.android.sos.activity.SosActivity;
import com.msos.android.sos.manager.AlertManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/**
 * This service detect when a person fall
 * 
 * @author Ludovic Toinel
 */
public class FallDetectService extends Service implements SensorListener{

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
	            if (!mSensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER,SensorManager.SENSOR_DELAY_NORMAL)) {
	                Log.e("FallDetectService.onCreate", "No suited sensor found");
	                stopSelf();
	            } else {
	                Log.i("FallDetectService.onCreate", "SensorListener found");
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
        public void onSensorChanged(int sensor, float[] values){
        
        	if(sensor == SensorManager.SENSOR_ACCELEROMETER && !fallDetected){
        	      
        		  // Calcul de l'acceleration
        		  float accelMag = (values[0]*values[0]+values[1]*values[1]+values[2]*values[2]);
        	       
        	 	  //Log.i("FallDetectService.onSensorChanged", String.valueOf(values[0]*values[0]) + "|" +  String.valueOf(values[1]*values[1]) + "|" +  String.valueOf(values[2]*values[2]) );
    	    	   
        	      if (((lastAccelMag - accelMag) > sensibilityLevel) && !fallDetected)
        	      {
        	    	   	  fallDetected = true;
        	              Log.v("FallDetectService.onSensorChanged","Force Detected");

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
		 * @see android.hardware.SensorListener#onAccuracyChanged(int, int)
		 */
		public void onAccuracyChanged(int sensor, int accuracy) {
			// Nothing
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
}
