package com.msos.android.sos.activity;

import java.util.List;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.MapView.LayoutParams;
import com.msos.android.sos.listener.SosLocationListener;

/**
 * Map Activity : This class overrides the default Google Map Activity
 * 
 * @author Ludovic Toinel
 */
public abstract class MapActivity extends com.google.android.maps.MapActivity{

	// Instance attributes
	private MapView mapView = null;
	private MapController mc = null;
	private GeoPoint p = null;
	

    /**
     * Called when the activity is destroyed
     * 
     * @see com.google.android.maps.MapActivity#onDestroy()
     */
    @Override 
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    
    
    /**
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        switch (keyCode) 
        {
            case KeyEvent.KEYCODE_3:
                mc.zoomIn();
                break;
            case KeyEvent.KEYCODE_1:
                mc.zoomOut();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }  
    
    
	/**
	 * Initialize the Map
	 */
	public void initMap(){
		
		// Get the main element
		mapView = (MapView) findViewById(R.id.mapview1);
		mc  = mapView.getController();
		
		// Add a zoom
		LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  
		View zoomView = mapView.getZoomControls(); 
		zoomLayout.removeAllViews();
		zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)); 
		mapView.displayZoomControls(true);
		
		// Initialize the zoom level
		mc.setCenter(SosLocationListener.getCurrentGeoPoint());
		mc.setZoom(17);

		//---Add a location marker---
		MapOverlay mapOverlay = new MapOverlay();
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);        

		mapView.invalidate();
	}
	
	/**
	 * Re-center the icon on the map
	 */
	public void refreshMapLocation(){
		Log.d("MainActivity.refreshMapLocation","Refresh map location");
		
	    p = SosLocationListener.getCurrentGeoPoint();

		// Animate the map on the point
		if (p != null && mc !=null){
			mc.animateTo(p);
		} else {
			Log.d("MainActivity.refreshMapLocation","Couldn't refresh location on the map");
		}
	}
	

	/**
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	protected boolean isRouteDisplayed() {
		return false;
	}
 
	/**
	 * Map overlay
	 */
	class MapOverlay extends com.google.android.maps.Overlay
	{
		@Override
	    public boolean draw(Canvas canvas, MapView mapView, 
	    boolean shadow, long when) 
	    {
	        super.draw(canvas, mapView, shadow);                   

	        if (p != null){
		        //---translate the GeoPoint to screen pixels---
		        Point screenPts = new Point();
		        mapView.getProjection().toPixels(p, screenPts);
	
		        //---add the marker---
		        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.placemark);         
		        canvas.drawBitmap(bmp, screenPts.x-25, screenPts.y-60, null);     
	        }
	        return true;
	    }
		
	    @Override
	    public boolean onTouchEvent(MotionEvent event, MapView mapView) 
	    {   
	        //---when user lifts his finger---
	        if (event.getAction() == 1) {                

	        	//String address = DeviceManager.getInstance(getParent()).getAddress();
		        //Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();
	        	String location = SosLocationListener.getLocation();
	        	Toast.makeText(getBaseContext(), location, Toast.LENGTH_SHORT).show();
	        	
	            return true;
	            
	        } else {
	            return false;
	        }
	    }        
	}
}
