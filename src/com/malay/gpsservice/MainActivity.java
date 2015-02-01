package com.malay.gpsservice;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener{

	Context mcontext=this;

	boolean isGpsEnabled=false;
	boolean isNetworkEnabled=false;
	boolean canGetLocation=false;
	
	protected LocationManager locManager;
	Location loc=null;
	double latitude;
	double longitude;
	double accuracy;
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE=20;
	private static final long MIN_TIME_BETWEEN_UPDATE=1000*60;
	
	double latitudet;
	double longitudet;
	double accuracyt;
	TextView tview;
	TextView tlat;
	TextView tlon;
	TextView tacc;
	String lat;
	String lon;
	String acc;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loc=getLocation();
		tview = (TextView) findViewById(R.id.textView1);
		
		try {
			getGPSLoc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		//Refresh Button
		if (item.getItemId() == R.id.ref) {
			getLocation();
			getGPSLoc();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void getGPSLoc() {
		if (latitude != 0.0 && longitude != 0.0) {
			tview.setText("Location Fixed");
		} else {
			tview.setText("Waiting for Location...");
		}
	if (canGetLocation) {
		latitudet=(double)Math.round(latitude*1000000)/1000000;
		tlat = (TextView) findViewById(R.id.lat);
		lat = Double.toString(latitudet);
		tlat.setText(lat);

		longitudet=(double)Math.round(longitude*1000000)/1000000;
		tlon = (TextView) findViewById(R.id.lon);
		lon = Double.toString(longitudet);
		tlon.setText(lon);

		accuracyt=(double)Math.round(accuracy*100)/100;
		tacc = (TextView) findViewById(R.id.acc);
		acc = Double.toString(accuracyt);
		tacc.setText(acc);
	}
}
	
	public Location getLocation(){
		try{
			locManager=(LocationManager)mcontext.getSystemService(LOCATION_SERVICE);
			
			isGpsEnabled=locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if(isGpsEnabled || isNetworkEnabled){
				this.canGetLocation=true;
				
				 // First get location from Network Provider
				if(isNetworkEnabled){
					locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, (LocationListener) this);
					Log.d("Network","Network");
					if(locManager!=null){
						loc=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if(loc!=null){
							latitude=loc.getLatitude();
							longitude=loc.getLongitude();
							accuracy=loc.getAccuracy();
						}
					}
				}
				
				// if GPS Enabled get lat/long using GPS Services
				if(isGpsEnabled){
					locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, (LocationListener) this);
					Log.d("GPS Enabled","GPS Enabled");
					if(locManager!=null){
						loc=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if(loc!=null){
							latitude=loc.getLatitude();
							longitude=loc.getLongitude();
							accuracy=loc.getAccuracy();
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return loc;
	}
		
	public void startServ(View vw){
		Intent gps=new Intent(this,GPSTracService.class);
		this.startService(gps);
	}

	public void stopServ(View vw){
		Intent gps=new Intent(this,GPSTracService.class);
		this.stopService(gps);
	}


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
