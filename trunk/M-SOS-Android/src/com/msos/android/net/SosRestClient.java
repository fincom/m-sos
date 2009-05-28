package com.msos.android.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.msos.android.businessobject.Notification;
import com.msos.android.businessobject.Profil;
import com.msos.android.typesafeenum.AlertType;

/**
 * Rest client for the M-SOS platform
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class SosRestClient extends RestClient{

	 /** 
	  * Broadcast the alert using the M-SOS platform 
	  */
	 public static boolean createAlert(String uniqueId, AlertType alertType, double latitude, double longitude, boolean isVictime){
		 try {
			 if (latitude != 0 && longitude != 0){

				 JSONObject params = new JSONObject();
				 params.put("uniqueId", uniqueId);
				 params.put("alertType", alertType.getValue());
				 params.put("latitude", String.valueOf(latitude));
				 params.put("longitude",  String.valueOf(longitude));
			     params.put("twittNotify", String.valueOf(false));
			     
				 if (isVictime){
					 params.put("actAs", "M");
				 } else {
					 params.put("actAs", "T");
				 }
				 
				 JSONObject result = RestClient.call("http://www.m-sos.com/json/Alert", "createAlert", 1, params);
	
				 return result.getBoolean("result");
			 }
			 
		} catch (JSONException e) {
			e.printStackTrace();
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
			 JSONObject result = RestClient.call("http://www.m-sos.com/json/User", "signup", 1, params);

			 return result.getBoolean("result");
			 
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	 }
	
}
