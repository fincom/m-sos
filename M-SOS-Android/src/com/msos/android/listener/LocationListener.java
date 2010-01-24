package com.msos.android.listener;


import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.msos.android.activity.SosActivity;
import com.msos.android.log.Tag;

/**
 * Location listener
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class LocationListener implements android.location.LocationListener 
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
		public LocationListener(SosActivity activity){
			this.sosActivity = activity;
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
		
		/**
		 * Display the human readable current location
		 * 
		 * @return
		 */
		public static String getLocation(){
			String location;
			
			if (currentLocation != null){
				 location = "Lat: "+ currentLocation.getLatitude() +"\nLong: "+currentLocation.getLongitude();
			} else {
				 location = "Location inconnue";
			}
			return location;
		}
		
		
		/**
		 * @return the current location
		 */
		public static Location getCurrentLocation(){
			return currentLocation;
		}
		
		/**
		 * Set the current location
		 */
		public void setCurrentLocation(Location location){
			if (location != null){
				currentLocation = location;
           		sosActivity.refreshMapLocation();
			}
		}
		
        /**
         * @see android.location.LocationListener#onLocationChanged(android.location.Location)
         */
        public void onLocationChanged(Location location) {
        	Log.d(Tag.MSOS,"Location changed");
        	setCurrentLocation(location);
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
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Nothing
		}
  }        