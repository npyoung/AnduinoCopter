package org.anduinocopter;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener, SensorEventListener
{
    private TextView latitudeField, longitudeField, altitudeField;
    private TextView headingField, pitchField, rollField;
    private LocationManager locationManager;
    private String provider;
    private SensorManager senseManager;
    private float[] accel;
    private float[] magnet;
    private static final String TAG = "MainActivity";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        accel = new float[3];
        magnet = new float[3];
        
        latitudeField = (TextView) findViewById(R.id.Lat);
        longitudeField = (TextView) findViewById(R.id.Lon);
        altitudeField = (TextView) findViewById(R.id.Alt);
        headingField = (TextView) findViewById(R.id.Head);
        pitchField = (TextView) findViewById(R.id.Pitch);
        rollField = (TextView) findViewById(R.id.Roll);
        
        // Location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        updateLocation(location);
        
        // Position
        senseManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        registerListeners();
    }
    
    @Override
    public void onDestroy() {
        unregisterListeners();
        super.onDestroy();
    } 
    
    // Location callbacks
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}
    
    // Sensor callbacks
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            updatePosition(event);
        }
    }
    
    // Private methods
    
    private void registerListeners() {
        locationManager.requestLocationUpdates(provider, 0, 0, this);
        senseManager.registerListener(this,
                senseManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        senseManager.registerListener(this,
                senseManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }
    
    private void unregisterListeners() {
        senseManager.unregisterListener(this);
    }
    
    private void updateLocation(Location location) {
        if(location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            double alt = location.getAltitude();
            latitudeField.setText(String.valueOf(lat));
            longitudeField.setText(String.valueOf(lon));
            altitudeField.setText(String.valueOf(alt));
        } else {
            latitudeField.setText("Provider not available");
            longitudeField.setText("Provider not available");
            altitudeField.setText("Provider not available");
        }
    }
    
    private void updatePosition(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accel = event.values;
            Log.d(TAG,String.format("Accelerometer updated to %f %f %f",accel[0],accel[1],accel[2]));
        } else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnet = event.values;
            Log.d(TAG,String.format("Magnetic field updated to %f %f %f",magnet[0],magnet[1],magnet[2]));
        }
        if(accel != null && magnet != null) {
            float[] R = new float[16];
            float[] I = new float[16];
            if(SensorManager.getRotationMatrix(R, I, accel, magnet)) {
                Log.d(TAG, "Got a rotation matrix");
                float[] vals = new float[3];
                SensorManager.getOrientation(R, vals);
                double heading = Math.toDegrees(vals[0]);
                double pitch = -1*Math.toDegrees(vals[1]) ;
                double roll = Math.toDegrees(vals[2]);
                headingField.setText(String.valueOf(heading));
                pitchField.setText(String.valueOf(pitch));
                rollField.setText(String.valueOf(roll));
                Log.d(TAG, "Set the orientation");
            } else {
                Log.d(TAG,"Did not get a rotation matrix");
            }
        }
    }
}
