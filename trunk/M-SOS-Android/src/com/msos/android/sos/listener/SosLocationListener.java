package com.msos.android.sos.listener;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.msos.android.sos.activity.SosActivity;

/**
 * SosLocationListener
 * 
 * @author Ludovic Toinel
 */
public class SosLocationListener implements LocationListener 
  {
		// Current location
		private static Location currentLocation = null;
		
		// Parent activity
		private SosActivity sosActivity = null;
		
		/**
		 * Constructor
		 * 
		 * @param location
		 * @param activity
		 */
		public SosLocationListener(Location location,SosActivity activity){
			
			currentLocation = location;
			this.sosActivity = activity;
			
			if (currentLocation != null){
				Log.d("SosLocationListener.constructor","Location found");
				sosActivity.refreshMapLocation();
			}
		}
		
		/**
		 * @return the current GeoPoint
		 */
		public static GeoPoint getCurrentGeoPoint(){
			
			if (currentLocation != null){
				return new GeoPoint(
		            (int) (currentLocation.getLatitude() * 1E6), 
		            (int) (currentLocation.getLongitude() * 1E6));
			} else {
				return null;
			}
		}
		
		public static String getLocation(){
			String location = "Lat: "+ currentLocation.getLatitude() +"\nLong: "+currentLocation.getLongitude();
			return location;
		}
		
		/**
		 * @return the current latitude
		 */
		public static double getCurrentLatitude(){
			return currentLocation.getLatitude();
		}
		
		/**
		 * @return the current longitude
		 */
		public static double getCurrentLongitude(){
			return currentLocation.getLongitude();
		}
		
		
		/**
		 * @return the current location
		 */
		public static Location getCurrentLocation(){
			return currentLocation;
		}
		
        /**
         * @see android.location.LocationListener#onLocationChanged(android.location.Location)
         */
        public void onLocationChanged(Location location) {
        	Log.d("SosLocationListener.onLocationChanged","Location changed");
           	if (location != null) {
           		currentLocation = location;
           		sosActivity.refreshMapLocation();
        	} else {
        		Log.d("SosLocationListener.onLocationChanged","Location is null");
        	}
        }

		/**
		 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
		 */
		public void onProviderDisabled(String provider) {
			// Noting
		}

		/**
		 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
		 */
		public void onProviderEnabled(String provider) {
			// Noting
		}

		/**
		 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
		 */
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
			// Nothing
		}
  }        