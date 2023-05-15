package com.example.battlecards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

//Location[gps 22.436865,114.029846 hAcc=8.0 et=+4d13h45m18s893ms alt=76.72840312233177 vAcc=14.489536 vel=0.0 sAcc=0.20027263 {Bundle[{satellites=16, maxCn0=30, meanCn0=22}]}]
public class GPSearch extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView longitude_tv;
    TextView latitude_tv;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    GPS gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        String game = intent.getStringExtra("game");
        latitude_tv = findViewById(R.id.latitudeTV);
        longitude_tv = findViewById(R.id.longitudeTV);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        System.out.println("after permission check");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc != null) {
            latitude_tv.setText("Latitude: " + loc.getLatitude());
            longitude_tv.setText("Longitude: " + loc.getLongitude());
        }
        gps = new GPS(game, getApplicationContext());
        gps.start();
        if(loc != null)
        gps.update(loc.getLatitude(), loc.getLongitude());
    }

    @Override
    public void onLocationChanged(Location location) {
//        txtLat = (TextView) findViewById(R.id.textview1);
//        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        latitude_tv.setText("Latitude: " + location.getLatitude());
        longitude_tv.setText("Longitude: " + location.getLongitude());
        this.gps.update(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.gps.end();
    }
}