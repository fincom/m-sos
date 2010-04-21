package com.msos.android.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.msos.android.beans.Notification;
import com.msos.android.beans.Profil;
import com.msos.android.log.Tag;
import com.msos.android.typesafeenum.AlertType;

/**
 * Rest client for the M-SOS platform
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class SosRestClient extends RestClient{

	// M-SOS Server URI
	private static final String SERVER_URI = "http://www.m-sos.com/json/";
	
	 /** 
	  * Broadcast the alert using the M-SOS platform 
	  */
	 public static boolean createAlert(String uniqueId, AlertType alertType, Location location, boolean isVictime){
		 try {
			 if (location != null){

				 JSONObject params = new JSONObject();
				 params.put("uniqueId", uniqueId);
				 params.put("alertType", alertType.getValue());
				 params.put("latitude", location.getLatitude());
				 params.put("longitude",  location.getLongitude());
			     params.put("twittNotify", String.valueOf(false));
			     
				 if (isVictime){
					 params.put("actAs", "M");
				 } else {
					 params.put("actAs", "T");
				 }
				 
				 // Call the service
				 Log.i(Tag.MSOS,params.toString());
				 JSONObject result = RestClient.post(SERVER_URI+"Alert", "createAlert", 1, params);
				 Log.i(Tag.MSOS,result.toString());
				  
				 return result.getBoolean("result");
				 
			 } else {
				 Log.i(Tag.MSOS,"Location is null, couldn't send an alert");
			 }
			 
		} catch (JSONException e) {
			Log.e(Tag.MSOS, "JSON error", e);
		} catch (Exception e) {
			Log.e(Tag.MSOS, "Exception", e);
		}
		
		return false;
		 
	 }
	 
	 
	 /**
	  * Signup method
	  * 
	  * $notifications = array("sms"=>phoneNumber, "twitter"=>Account, alert=>Account)
	  * $informations = array("firstName"=>FirstName, "lastName"=>LastName, "phoneNumber"=>PhoneNumber, "postalCode"=>PostalCode, "bloodGroup"=>BloodGroup)
	  * 
	  * @param uniqueId
	  * @return
	  */
	public static boolean signup(String uniqueId,Profil information, Notification notification, String lang){
		 try { 
			 JSONObject params = new JSONObject();
			 
			 // Unique ID
			 params.put("uniqueId",uniqueId);
			 
			 // Information
			 JSONObject informationJSON = new JSONObject();
			 informationJSON.put("firstName", information.getFirstname());
			 informationJSON.put("lastName", information.getLastname());
			 informationJSON.put("phoneNumber", information.getPhoneNumber());
			 informationJSON.put("postalCode", information.getPostalCode());
			 informationJSON.put("bloodGroup", information.getBloodGroup());
			 params.put("informations",informationJSON);
			 
			 // Notifications
			 JSONObject notificationJSON = new JSONObject();
			 notificationJSON.put("sms", notification.getSms());
			 notificationJSON.put("twitter", notification.getTwitter());
			 params.put("notifications",notificationJSON);
			 
			 // Lang
			 params.put("lang",lang);
			 
			 // Call
			 Log.i(Tag.MSOS,params.toString());
			 JSONObject result = RestClient.post(SERVER_URI+"User", "signup", 1, params);
			 Log.i(Tag.MSOS,result.toString());
			 
			 return result.getBoolean("result");
			 
		} catch (JSONException e) {
			Log.e(Tag.MSOS, "JSON error", e);
		} catch (Exception e) {
			Log.e(Tag.MSOS, "Exception", e);
		}
		return false;
	 }
	
}
