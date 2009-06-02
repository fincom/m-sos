package com.msos.android.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Rest client for the M-SOS platform
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class GoogleRestClient extends RestClient{

	private static String TAG = "M-SOS.GoogleRestClient";
	
	 /** 
	  * Broadcast the alert using the M-SOS platform 
	  */
	 public static JSONArray getElements(String category, String latitude, String longitude, String latitudeSpan, String longitudeSpan){

		 try {
			 JSONObject result = RestClient.get("http://ajax.googleapis.com/ajax/services/search/local?v=1.0&q=category:"+category+"&sll="+latitude+","+longitude+"&sspn="+latitudeSpan+","+longitudeSpan+"&rsz=large");
			 if (200 == result.getInt("responseStatus")){
				return result.getJSONArray("results");
			 } else {
				 Log.w(TAG,"Google LocalSearch error : "+ result.getInt("responseDetails"));
			 }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
		 
	 }
	 
}
