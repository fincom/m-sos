package com.msos.android.activity;

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
}