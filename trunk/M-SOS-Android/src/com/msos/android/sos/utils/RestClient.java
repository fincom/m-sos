package com.msos.android.sos.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * A simple Rest Client
 * 
 * @author Ludovic Toinel
 */
public class RestClient {

	private static String TAG = "RestClient";
	
	/**
	 * @param is
	 * @return
	 */
	private static String convertStreamToString(InputStream is) {
	        /*
	         * To convert the InputStream to String we use the BufferedReader.readLine()
	         * method. We iterate until the BufferedReader return null which means
	         * there's no more data to read. Each line will appended to a StringBuilder
	         * and returned as String.
	         */
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();
	 
	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
	 
	 
	/**
     * @param mUrl The URL of location tracking server.
     * @param mLocationJSON The location data with time in JSON format.
     */
    public static JSONObject call(String mUrl, String methodName, int id, List<Object> parameters) {
      
    	HttpClient httpClient = new DefaultHttpClient();
        HttpPost postMethod = new HttpPost(mUrl);
        
        try {
        	// Prepare parameters
        	HttpParams httpParams = new BasicHttpParams();
        	JSONObject jsonObject = new JSONObject();

        	// Set Params
        	JSONArray jsonArray = new JSONArray();
        	int i = 0;
        	for (Iterator<Object> iterator = parameters.iterator(); iterator.hasNext();) {
        		jsonArray.put(iterator.next());
				i++;
			}
        	
        	// Set JSON Object
        	jsonObject.put("params", jsonArray);
        	jsonObject.put("method", methodName);
        	jsonObject.put("id", id);
        	
        	// Create the request
        	StringEntity se = new StringEntity(jsonObject.toString());
        	postMethod.setEntity(se);
            Log.i(TAG,"Parameters: "+jsonObject.toString());
            postMethod.setParams(httpParams);
            HttpResponse response = httpClient.execute(postMethod);
            
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
 
            if (entity != null) {
 
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                Log.i(TAG,"Response: "+result);
 
                // A Simple JSONObject Creation
                JSONObject json = new JSONObject(result);
                return json;
            }
            
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        } finally {
            postMethod.abort();
        }
        
        return null;
    }
}
