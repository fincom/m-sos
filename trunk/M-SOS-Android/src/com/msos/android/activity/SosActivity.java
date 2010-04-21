package com.msos.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.ToggleButton;
import android.widget.TabHost.TabSpec;

import com.msos.android.R;
import com.msos.android.log.Tag;
import com.msos.android.manager.AlertManager;
import com.msos.android.manager.DeviceManager;
import com.msos.android.service.FallDetectService;
import com.msos.android.typesafeenum.AlertType;
import com.nullwire.trace.ExceptionHandler;

/**
 * SOS Activity : This class manage all the user events
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class SosActivity extends MapActivity{

	private MediaPlayer mp = null;
	
    /**
     * Called when the activity is first created.
     *  
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(Tag.MSOS, "SOS Activity Create");
        
        // Remote Exception handler
        ExceptionHandler.register(this,"http://labs.geeek.org/android/stacktrace.php");
        	
        // Enable GPS
        if(!DeviceManager.getInstance(this).enableLocationService(this)){
    		displayInfoBox(getString(R.string.warning),getString(R.string.message_gps_disabled));
        }
        
        // Set the main content view
        setContentView(R.layout.main);
     
        // Initialize the tabs
        initTabs();
	
        // Initialize the Listener
		initAlertButtonListener();
		initToolsButtonListener();
		
		// Initialize the map
		initMap();
	
    }


	/**
     * Called when the activity is destroyed
     * 
     * @see com.google.android.maps.MapActivity#onDestroy()
     */
    @Override 
    protected void onDestroy() {
    	
    	super.onDestroy();
    }
    
    
	@Override
	protected void onStop() {
		
		super.onStop();
		
		// Stop alarm
		FallDetectService.stopAlarm();
		
		// Stop sound
		if (mp != null){
			mp.stop();
		}
				
		// Stop location register 
		DeviceManager.getInstance(this).unregisterLocationService();
		
		// Start or stop service
		boolean enableFallDetection = getPreferences(this).getBoolean("sos.falldetection.enable", false);
		   
		// if Fall detection is enabled we start the service
		if (enableFallDetection){
			 this.startService(new Intent(this, FallDetectService.class));
		     Log.d(Tag.MSOS, "Fall detection service lauched");
		} else {
			 this.stopService(new Intent(this, FallDetectService.class));
			 Log.d(Tag.MSOS, "Fall detection disabled");
		}
	}


	/**
	 * @return the shared preference of the application
	 */
	public static SharedPreferences getPreferences(Context context){
		
		SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Activity.MODE_PRIVATE);
		return preferences;
	}
	
    /**
     * Launch an alert
     * @param alertType
     */
    public void launchAlert(AlertType alertType, boolean isVictime){
    	AlertManager.getInstance(this).alert(alertType, isVictime);
    }
    
	 
	/**
	 * Display an info Box
	 * @param message
	 */
    protected void displayInfoBox(String title, String message){
		
		AlertDialog infoBox = new AlertDialog.Builder(this).create();
		infoBox.setTitle(title);
		infoBox.setCancelable(true);
		infoBox.setCanceledOnTouchOutside(true);
		infoBox.setMessage(message);
	    
	    // Button listener
		infoBox.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
	      public void onClick(DialogInterface dialog, int which) {
	        return;
	      } });
		
		infoBox.show();
	}
    
	/**
	 * Display a context dialog
	 * 
	 * @param alertType
	 */
	protected void displayContextDialog(final AlertType alertType){
		
		final CharSequence[] items = {getString(R.string.item_victime), getString(R.string.item_temoin)};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_incident_context);
		builder.setCancelable(true);
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        String context = items[item].toString();
		        
		        boolean isVictime = false;
		        if (context.equals(getString(R.string.item_victime))){
		        	isVictime = true;
		        }
		        launchAlert(alertType,isVictime);
		    }
		});
		
		// We create the dialog and display it
		AlertDialog alert = builder.create();
		alert.setButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		    	dialog.dismiss(); 
		      }});
		alert.show();
	}

	
	/**
	 * Initialize the button listeners on the alert panel
	 */
	protected void initAlertButtonListener() {
		
		// Action Button Accident
        final Button buttonAccident = (Button) findViewById(R.id.button_accident);
        buttonAccident.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	displayContextDialog(AlertType.ALERT_ACCIDENT);
            }
        });
        
        // Action Button Fire
        final Button buttonFire = (Button) findViewById(R.id.button_fire);
        buttonFire.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	displayContextDialog(AlertType.ALERT_FIRE);
            }
        });
        
        // Action Button Person
        final Button buttonPerson= (Button) findViewById(R.id.button_person);
        buttonPerson.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	displayContextDialog(AlertType.ALERT_SANTE);
            }
        });
        
        // Action Button Person
        final Button buttonDomestic= (Button) findViewById(R.id.button_domestic);
        buttonDomestic.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	displayContextDialog(AlertType.ALERT_DOMESTIC);
            }
        });
	}
	
	/**
	 * Initialize the button listeners on the Tools panel
	 */
	protected void initToolsButtonListener() {
		
		// Action Button Accident
        final ToggleButton buttonCardiac = (ToggleButton) findViewById(R.id.button_cardiac);
        buttonCardiac.setOnClickListener(new Button.OnClickListener() {
        
        	
            public void onClick(View v) {
        	   if (buttonCardiac.isChecked()) {
            		mp = MediaPlayer.create(getApplicationContext(), R.raw.bip);
            		mp.setLooping(true);
            		mp.start();
        	   } else {
        		   	mp.stop();
		       }
            }
            
        });
        
	}
	
	/**
	 * Initialize the UI Tabs
	 */
	protected void initTabs() {
		//build the tabs
        TabHost tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();

		//create tab1
        TabSpec tab1 = tabs.newTabSpec("tab1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator(null, getResources().getDrawable(R.drawable.tab_alert));   
        tabs.addTab(tab1);
		
		//create tab2
        TabSpec tab2 = tabs.newTabSpec("tab2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator(null, getResources().getDrawable(R.drawable.tab_map));
        tabs.addTab(tab2);
        
		//create tab3
        TabSpec tab3 = tabs.newTabSpec("tab3");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator(null, getResources().getDrawable(R.drawable.tab_tools));
        tabs.addTab(tab3);

		//set the currently selected tab
		tabs.setCurrentTab(0);
	}
	
	
    /**
     * Called when the menu button is pressed
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    
    /**
     *  Handles when menu options are selected
     *  
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
         
         	 case R.id.options_menu_settings :
         		SettingsActivity.show(this);
         		return true;
         		
         	 // About
         	 case R.id.options_menu_about :
         		
         		String version = "";
         		try {
         			PackageManager pm = getPackageManager();
         			PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
         			version = pi.versionName;
         		} catch (NameNotFoundException e) { }
    			
         		displayInfoBox(getString(R.string.app_name)+ " " + version, getString(R.string.message_about));
         		return true;
         	
             // Quit
	         case R.id.options_menu_quit :
	             finish();
	             return true;
	     }
    	 return false;
    }
	
}
