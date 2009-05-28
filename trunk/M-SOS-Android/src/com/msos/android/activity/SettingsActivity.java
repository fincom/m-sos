package com.msos.android.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.msos.android.manager.DeviceManager;
import com.msos.android.utils.RestClient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Settings Activity : This activity manage the application's settings 
 * 
 * @author Ludovic Toinel
 */
public class SettingsActivity extends PreferenceActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName(getString(R.string.app_name));
        addPreferencesFromResource(R.xml.preferences);
    }

    /**
     * Starts the PreferencesActivity for the specified user.
     *
     * @param context The application's environment.
     */
    public static void show(Context context) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

	@Override
	protected void onStop() {
		super.onStop();
		

		 List<Object> parameter = new ArrayList<Object>();
		 parameter.add(DeviceManager.getInstance(this).getUniqueId());
		 

		 parameter.add(uniqueId);
		 parameter.add(location.getLatitude());
		 parameter.add(location.getLongitude());
		 JSONObject result = RestClient.call("http://www.m-sos.com/json/User", "signup", 1, parameter);

		 broadcastMessageSent = result.getBoolean("result");
		
	}
    
    
    
}