package com.ilink.proximity;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.cast.TextTrackStyle;
import com.ilink.RegisterSimpleActivity;

public class ProximityAlertService extends Service implements LocationListener {
    public static final String LATITUDE_INTENT_KEY = "LATITUDE_INTENT_KEY";
    public static final String LONGITUDE_INTENT_KEY = "LONGITUDE_INTENT_KEY";
    public static final String RADIUS_INTENT_KEY = "RADIUS_INTENT_KEY";
    private static final String TAG = "ProximityAlertService";
    private boolean inProximity;
    private double latitude;
    private LocationManager locationManager;
    private double longitude;
    private float radius;

    public void onCreate() {
        super.onCreate();
        this.locationManager = (LocationManager) getSystemService("location");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean z = false;
        Location bestLocation = null;
        this.latitude = intent.getDoubleExtra(LATITUDE_INTENT_KEY, Double.MIN_VALUE);
        this.longitude = intent.getDoubleExtra(LONGITUDE_INTENT_KEY, Double.MIN_VALUE);
        this.radius = intent.getFloatExtra(RADIUS_INTENT_KEY, Float.MIN_VALUE);
        for (String provider : this.locationManager.getProviders(false)) {
            Location location = this.locationManager.getLastKnownLocation(provider);
            if (bestLocation == null) {
                bestLocation = location;
            } else if (location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }
        if (bestLocation != null) {
            if (getDistance(bestLocation) <= this.radius) {
                z = true;
            }
            this.inProximity = z;
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.locationManager.requestLocationUpdates(RegisterSimpleActivity.KEY_NETWORK, 0, 0.0f, this);
        }
        return 1;
    }

    public void onLocationChanged(Location location) {
        float distance = getDistance(location);
        Intent intent;
        if (distance <= this.radius && !this.inProximity) {
            this.inProximity = true;
            Log.i(TAG, "Entering Proximity");
            intent = new Intent(ProximityPendingIntentFactory.PROX_ALERT_INTENT);
            intent.putExtra("entering", true);
            sendBroadcast(intent);
        } else if (distance <= this.radius || !this.inProximity) {
            String provider;
            float distanceFromRadius = Math.abs(distance - this.radius);
            float locationEvaluationDistance = (distanceFromRadius - location.getAccuracy()) / 2.0f;
            Toast.makeText(getBaseContext(), "roaming state and make sure signal correct! , (distanceFromRadius):" + distanceFromRadius + " (locationEvaluationDistance):" + locationEvaluationDistance, 1).show();
            this.locationManager.removeUpdates(this);
            float updateDistance = Math.max(TextTrackStyle.DEFAULT_FONT_SCALE, locationEvaluationDistance);
            Toast.makeText(getBaseContext(), "updateDistance: " + updateDistance, 1).show();
            if (distanceFromRadius <= location.getAccuracy() || "gps".equals(location.getProvider())) {
                provider = "gps";
            } else {
                provider = RegisterSimpleActivity.KEY_NETWORK;
            }
            if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                this.locationManager.requestLocationUpdates(provider, 0, updateDistance, this);
            }
        } else {
            this.inProximity = false;
            Log.i(TAG, "Exiting Proximity");
            intent = new Intent(ProximityPendingIntentFactory.PROX_ALERT_INTENT);
            intent.putExtra("entering", false);
            sendBroadcast(intent);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.locationManager.removeUpdates(this);
            Toast.makeText(getBaseContext(), "Stoping the service!", 1).show();
        }
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    private float getDistance(Location location) {
        float[] results = new float[1];
        Location.distanceBetween(this.latitude, this.longitude, location.getLatitude(), location.getLongitude(), results);
        return results[0];
    }
}
