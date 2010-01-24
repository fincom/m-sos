package com.msos.android.manager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;

import com.msos.android.activity.SosActivity;
import com.msos.android.listener.LocationListener;

/**
 * Device Manager
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class DeviceManager {

	 // Application context
	 private Context context = null;
	 
	 // Singleton instance
	 private static DeviceManager instance = null;
	 
	 // Location listener
	 private LocationListener locationListener = null;
	 
	/**
	 * @return the singleton instance
	 */
	 public static DeviceManager getInstance(Context context){
		if (instance == null){
			instance = new DeviceManager(context);
		}
		
		return instance;
	 }
		 
	 /** Main private constructor */
	 private DeviceManager(Context context){
		 this.context = context;
	 }
	 
    /**
     * @return SIM MSISDN
     */
    public String getUniqueId(){
    	TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    	return telephonyManager.getDeviceId();
    }
    
    /**
     * Enable the location service
     */
    public boolean enableLocationService(SosActivity activity){
    	
    	LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    	locationListener = new LocationListener(activity);
    	
    	
    	// If GPS is enabled
    	if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
    		locationListener.setCurrentLocation(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
    		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    		
    		return true;
    	
    	// Else Best-effort
    	} else {
    		String bestProvider = lm.getBestProvider(new Criteria(), true);
    		locationListener.setCurrentLocation(lm.getLastKnownLocation(bestProvider));
    		lm.requestLocationUpdates(bestProvider, 0, 0, locationListener);
    		
    		return false;
    	}
    }
    
    /**
     * unregisterLocationService
     */
    public void unregisterLocationService(){
    	LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    	lm.removeUpdates(locationListener);
    }

    
    /**
     * Return the current Address
     * 
     * @return
     */
    public String getAddress(){
    	
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        Location location = LocationListener.getCurrentLocation();
        
        if (location != null){
			try {
				List<Address> addresses = geoCoder.getFromLocation(
						location.getLatitude(), 
						location.getLongitude(), 1);
	
	            String address = "";
	            if (addresses != null && addresses.size() > 0)
	            {
	                for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); i++){
	                	address += addresses.get(0).getAddressLine(i) + "\n";
	                }
	                
	                address += addresses.get(0).getLocality()+ "\n";
	                address += addresses.get(0).getCountryName()+ "\n";
	                return address;
	            }
	            
			} catch (IOException e) {}
        }
			
        return "Adresse inconnue";
    }
    
    /**
     * Return the current locality
     * 
     * @return
     */
    public String getLocality(){
    	
        Geocoder geoCoder = new Geocoder( context, Locale.getDefault());
        Location location = LocationListener.getCurrentLocation();
       
        if (location != null){
			try {
				List<Address> addresses = geoCoder.getFromLocation(
					location.getLatitude(), 
					location.getLongitude(), 1);
	
		        if (addresses.size() > 0) 
		        {
		        	 return addresses.get(0).getLocality();
		        }
	        
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        return "Locatité inconnue";
    }
}
