package com.msos.android.manager;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.msos.android.activity.SosActivity;
import com.msos.android.listener.SosLocationListener;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.util.Log;

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
    	return telephonyManager.getLine1Number();
    }
    
    /**
     * Enable the location service
     */
    public boolean enableLocationService(SosActivity activity){
    	Log.d("enableLocationService", "Start");
    	LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

    	// If GPS is enabled
    	if (isGpsEnabled(lm)){
    		locationListener = new SosLocationListener(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER),activity);
    		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    		return true;
    	} else {
    		 locationListener = new SosLocationListener(lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER),activity);
    		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
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
     * @param lm The Location Manager
     * @return true if the GPS is enabled
     */
    public boolean isGpsEnabled(LocationManager lm){
    	List<String> locationProvidersEnabled = lm.getProviders(true);
     	for (Iterator<String> iterator = locationProvidersEnabled.iterator(); iterator.hasNext();) {
     		String provideName = iterator.next();
     		if (LocationManager.GPS_PROVIDER.equals(provideName)) return true;
     	}
     	return false;
    }
    

    
    /**
     * Return the current Address
     * 
     * @return
     */
    public String getAddress(){
    	
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());

		try {
			List<Address> addresses = geoCoder.getFromLocation(
			        SosLocationListener.getCurrentLatitude(), 
			        SosLocationListener.getCurrentLongitude(), 1);

            String add = "";
            if (addresses.size() > 0) 
            {
                for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); i++){
                   add += addresses.get(0).getAddressLine(i) + "\n";
                }
                
                add += addresses.get(0).getLocality()+ "\n";
                add += addresses.get(0).getCountryName()+ "\n";
                return add;
            }

            
		} catch (IOException e) {
			e.printStackTrace();
		}
			
        return "inconnue";
    }
    
    /**
     * Return the current locality
     * 
     * @return
     */
    public String getLocality(){
    	
        Geocoder geoCoder = new Geocoder( context, Locale.getDefault());
	
		try {
			List<Address> addresses = geoCoder.getFromLocation(
			    SosLocationListener.getCurrentLatitude(), 
			    SosLocationListener.getCurrentLongitude(), 1);

	        if (addresses.size() > 0) 
	        {
	        	 return addresses.get(0).getLocality();
	        }
        
		} catch (IOException e) {
			e.printStackTrace();
		}

        return "inconnue";
    }
}
