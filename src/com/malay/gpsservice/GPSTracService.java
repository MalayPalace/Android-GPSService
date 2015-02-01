package com.malay.gpsservice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GPSTracService extends Service implements LocationListener {

	// DECLARE ALL THE VARIABLES
	Location location = null; // location
	LocationManager locationManager;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled=false;

	String msg = "CAUTION! If kept open, can consume lots of battery";
	// FOR FOREGROUND_ID
	int FORE_ID = 1335;

	double latitude; // latitude
	double longitude; // longitude

	// The minimum distance to change Updates in meters
	final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Intent noty_intent = new Intent(this,
				com.malay.gpsservice.MainActivity.class);
		noty_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, noty_intent,
				0);

		Notification n = new Notification.Builder(this)
				.setContentTitle("GPS Serice is running...")
				.setContentText(msg).setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pIntent).setAutoCancel(true).setOngoing(true)
				.build();

		startForeground(FORE_ID, n);

		try {
			locationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			 // First get location from Network Provider
			if(isNetworkEnabled){
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d("Network","Network");
				if(locationManager!=null){
					location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if(location!=null){
						latitude=location.getLatitude();
						longitude=location.getLongitude();
					}
				}
			}

			if (!isGPSEnabled) {
			} else {
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							Toast.makeText(this,
									"Location Listener on GPS started...",
									Toast.LENGTH_SHORT).show();
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			Toast.makeText(this,
					"Some Error occur while starting Location Listener",
					Toast.LENGTH_SHORT).show();
			ex.printStackTrace();
		}
		return (START_STICKY);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (locationManager != null) {
			locationManager.removeUpdates((LocationListener) this);
			locationManager = null;
		}
		stopForeground(true);
		Toast.makeText(this, "Location Listener on GPS Stopped...",
				Toast.LENGTH_SHORT).show();
		super.onDestroy();
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