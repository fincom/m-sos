package com.msos.android.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.msos.android.log.Tag;

import android.util.Log;

/**
 * Rest client for the M-SOS platform
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class GoogleRestClient extends RestClient{
	
	 /** 
	  * Broadcast the alert using the M-SOS platform 
	  */
	 public static JSONObject getElements(String category, String latitude, String longitude, String latitudeSpan, String longitudeSpan){

		 try {
			 JSONObject result = RestClient.get("http://ajax.googleapis.com/ajax/services/search/local?v=1.0&q=category:"+category+"&sll="+latitude+","+longitude+"&sspn="+latitudeSpan+","+longitudeSpan+"&rsz=large");
			 
			 if (result == null){
				 Log.w(Tag.MSOS,"Google LocalSearch error : No result found");
			 } else if (200 == result.getInt("responseStatus")){
				return result.getJSONObject("responseData");
			 } else {
				 Log.w(Tag.MSOS,"Google LocalSearch error : "+ result.getInt("responseDetails"));
			 }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
		 
	 }
	 
}
