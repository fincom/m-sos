package com.msos.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.msos.android.R;
import com.msos.android.beans.Notification;
import com.msos.android.beans.Profil;
import com.msos.android.manager.DeviceManager;
import com.msos.android.net.SosRestClient;

/**
 * Settings Activity : This activity manage the application's settings 
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
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
		SharedPreferences share = getPreferenceManager().getSharedPreferences();
		String uniqueId = DeviceManager.getInstance(this).getUniqueId();
		
		Profil profil = new Profil();
		profil.setFirstname(share.getString("sos.firstname", ""));
		profil.setLastname(share.getString("sos.lastname", ""));
		profil.setPhoneNumber(share.getString("sos.phonenumber", ""));
		profil.setBloodGroup(share.getString("sos.bloodgroup", ""));
		
		Notification notification = new Notification();
		SosRestClient.signup(uniqueId, profil, notification, "FR_fr");
	}
    
    
    
}