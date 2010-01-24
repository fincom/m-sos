package com.msos.android.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.msos.android.R;
import com.msos.android.listener.LocationListener;
import com.msos.android.log.Tag;
import com.msos.android.manager.DeviceManager;
import com.msos.android.net.GoogleRestClient;

/**
 * Map Activity : This class overrides the default Google Map Activity
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
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
		mapView.setBuiltInZoomControls(true);
		
		//mapView.displayZoomControls(true);
		
		// Initialize the zoom level
		if (LocationListener.getCurrentGeoPoint() != null){
			mc.setCenter(LocationListener.getCurrentGeoPoint());
		}
		
		mc.setZoom(17);

		//---Add a location marker---
		MapOverlay mapOverlay = new MapOverlay();
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);        

		mapView.invalidate();
		
		
	}
	
	
	public void drawElements(MapView mapView, Canvas canvas, String element){
		
		if (mapView == null){
			return;
		}
		try {
			
			GeoPoint mapCenter = mapView.getMapCenter();
			
			// Get latitude & longitude
			String latitude = String.valueOf(mapCenter.getLatitudeE6() / 1E6);
			String longitude = String.valueOf(mapCenter.getLongitudeE6() / 1E6);
			
			// Get latitude span & longitude span
			String latitudeSpan = String.valueOf(mapView.getLatitudeSpan() / 1E6);
			String longitudeSpan = String.valueOf(mapView.getLongitudeSpan() / 1E6);
			
			// Get elements
			JSONObject elements = GoogleRestClient.getElements(element, latitude, longitude, latitudeSpan, longitudeSpan);
			
			JSONArray results = elements.getJSONArray("results");
			if (results != null && results.length() != 0){
					
					for (int i = 0; i < elements.length(); i++) {
	
						JSONObject result = results.getJSONObject(i);
						
				        //---translate the GeoPoint to screen pixels---
				        Point screenPts = new Point();
				        GeoPoint p = new GeoPoint((int)(result.getDouble("lat")*1E6),(int) (result.getDouble("lng")*1E6));
				        mapView.getProjection().toPixels(p, screenPts);
			
				        //---add the marker---
				        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.placemark);         
				        canvas.drawBitmap(bmp, screenPts.x-25, screenPts.y-60, null);    
				        
					}
			}
		
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Re-center the icon on the map
	 */
	public void refreshMapLocation(){
		Log.d(Tag.MSOS,"Refresh map location");
		
	    p = LocationListener.getCurrentGeoPoint();

		// Animate the map on the point
		if (p != null && mc !=null){
			mc.animateTo(p);
		} else {
			Log.d(Tag.MSOS,"Couldn't refresh location on the map");
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
	        
	       // drawElements(mapView,canvas,"Hospitals");
	        
	        return true;
	    }
		
	    @Override
	    public boolean onTouchEvent(MotionEvent event, MapView mapView) 
	    {   
	        //---when user lifts his finger---
	        if (event.getAction() == 1) {                

	        	String address = DeviceManager.getInstance(getParent()).getAddress();
		        Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();

	            return true;
	            
	        } else {
	            return false;
	        }
	    }        
	}
}
