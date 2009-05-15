package com.msos.android.sos.manager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.msos.android.sos.activity.R;
import com.msos.android.sos.activity.SosActivity;
import com.msos.android.sos.listener.SosLocationListener;
import com.msos.android.sos.typesafeenum.AlertType;
import com.msos.android.sos.utils.RestClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.telephony.gsm.SmsManager;

/**
 * Alert Manager : This class is responsible to contact emergencies
 * 
 * @author Ludovic Toinel
 */
public class AlertManager {

	 private Context context = null;
	 private static AlertManager instance = null;
	 
	 // Flags
	 private boolean broadcastMessageSent = false;
	 private boolean notificationSmsSent = false;
	 
	/**
	 * @return the singleton instance
	 */
	public static AlertManager getInstance(Context context){
		if (instance == null){
			instance = new AlertManager(context);
		}
		return instance;
	}
	 
	 /** Main constructor */
	 private AlertManager(Context context){
		 this.context = context;
	 }
	 
	 private void broadcastAlert(AlertType alertType){
		 try {
			 List<Object> parameter = new ArrayList<Object>();
			 Location currentLocation = SosLocationListener.getCurrentLocation();
			 parameter.add(alertType.getValue());
			 parameter.add("231321");
			 parameter.add(currentLocation.getLatitude());
			 parameter.add(currentLocation.getLongitude());
			 JSONObject result = RestClient.call("http://www.m-sos.com/json/Alert", "createAlert", 1, parameter);

			 if (result.getBoolean("result")){
				 // Todo
			 }
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }

    
	/**
	 * Main alert method
	 */
	public void alert(final AlertType alertType, final boolean isVictime){
		
		// Handler
		final Handler uiThreadCallback = new Handler();
				
		// Run in Thread
		final Runnable runInUIThread = new Runnable() {
			 public void run() {
				 StringBuffer message = new StringBuffer();
				 
				 // Bradcast Message Status
				 if (broadcastMessageSent){
					 message.append(context.getString(R.string.message_sos_sent));
				 } else {
					 message.append(context.getString(R.string.message_sos_not_sent));
				 }
				 
				 if (notificationSmsSent){
					 message.append(context.getString(R.string.message_notification_sms_sent));
				 }
				 
				 addNotification(context.getString(R.string.app_name),message.toString());
			 }
		};
		
		// Send the alert message in background
		new Thread() {
		 @Override public void run() {
			 broadcastAlert(alertType);
			 if (isVictime){
				 sendAlertSMS(alertType);
			 }
			 uiThreadCallback.post(runInUIThread);
		 }
		}.start();

		
		// Call Emergency
		Uri phoneNumber = getEmergencyNumber(alertType);
		
		// Launch the new itent
		Intent itent = new Intent(Intent.ACTION_DIAL,phoneNumber);
		context.startActivity(itent);
	}

	/**
	 * Get the emergency number
	 * 
	 * @param alertType
	 * @return
	 */
	private Uri getEmergencyNumber(AlertType alertType) {
		String phoneNumber;
		if (AlertType.ALERT_ACCIDENT.equals(alertType)){
			phoneNumber = "18";
		} else if(AlertType.ALERT_FIRE.equals(alertType)) {
			phoneNumber = "18";
		} else if(AlertType.ALERT_DOMESTIC.equals(alertType)) {
			phoneNumber = "15";
		} else if(AlertType.ALERT_PERSON.equals(alertType)) {
			phoneNumber = "15";
		} else {
			phoneNumber = "112";
		}
		
		return  Uri.parse("tel:"+phoneNumber);
	}
	
	
	/**
	 * Add a new notification
	 */
	private void addNotification(String title, String message){

		// Create a new itent
		Intent notificationIntent = new Intent(context, SosActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		// Create a new notification
		Notification notification = new Notification(R.drawable.icon, "S.O.S", System.currentTimeMillis());
		notification.setLatestEventInfo(context.getApplicationContext(), title, message, contentIntent);
		notification.defaults |= Notification.DEFAULT_SOUND;
		
		// Notify the manager
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(3,notification);
	}
	
    /**
     * Sends an SMS message to another device
     * 
     * @param phoneNumber
     * @param message
     */
    private void sendAlertSMS(AlertType alertType)
    {       
    	SharedPreferences preferences = SosActivity.getPreferences(context);
    	boolean smsNotificationEnabled = preferences.getBoolean("sos.notification.sms.enable", false);
    	String phoneNumber = preferences.getString("sos.notification.sms.phonenumber", null);
    	
    	// If the notifications are enabled and the phone number has been set
    	if (smsNotificationEnabled && phoneNumber != null){

    		String message =  context.getString(R.string.message_sms_alert);
	        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, SosActivity.class), 0);                
	        SmsManager sms = SmsManager.getDefault();
	        sms.sendTextMessage(phoneNumber, null, message.toString(), pi, null); 
	        
	        notificationSmsSent = true;
    	}
    }   

    /**
     * Sends an SMS message to another device
     * 
     * @param phoneNumber
     * @param message
     */
    public void call()
    {       
    	SharedPreferences preferences = SosActivity.getPreferences(context);
    	boolean callEnabled = preferences.getBoolean("sos.falldetection.autocall.enable", false);
    	String phoneNumber = preferences.getString("sos.falldetection.autocall.phonenumber", null);
    	
    	// If the notifications are enabled and the phone number has been set
    	if (callEnabled && phoneNumber != null){

            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setSpeakerphoneOn(true);
            
    		// Launch the new itent
    		Intent itent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNumber));
    		itent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		context.startActivity(itent);
    		

    	}
    }   
}
