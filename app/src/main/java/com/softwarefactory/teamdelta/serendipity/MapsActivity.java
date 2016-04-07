package com.softwarefactory.teamdelta.serendipity;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.Maps;

/*
*  Created by Riku Suomela 2016
*
* This class integrates the Goole Maps API to the application. Information about saved recordings
* should be passed here for it to be possible to view those on the map as markers.
*
* The template for this class is provided by Github user Daniel Nugent (16.11.2015)
*
* Most of the proximity alert system code has been adopted from https://www.javacodegeeks.com/2011/01/android-proximity-alerts-tutorial.html with own modifications and deletions
*
*/
public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;

    private static final long POINT_RADIUS = 200; // in Meters
    private static final String PROX_ALERT_INTENT = "com.softwarefactory.teamdelta.serendipity.MapsActivity";
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
    private static final long PROX_ALERT_EXPIRATION = -1;
    private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
    private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";
    BroadcastReceiver myBroadCast;

    LatLng latLng;
    GoogleMap mMap;
    Marker currLocationMarker;
    SupportMapFragment mFragment;


    // The map fragment is fetched here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //TODO: Develop a fix to avoid multiple in  stances of the same fragment to be ran at the same time
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        mMap = gMap;
        // This and the other similar checks in the source code are due to new Google policies
        // according to which user permissions should be double checked when certain permissions
        // are required, even though the permissions were given upon installation of the app by the user.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // We disable the toolbar but enable myLocation button from it
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMyLocationEnabled(true);
        //Build an instance of the Google API Client
        buildGoogleApiClient();
        // Check if an instance of the client is already running or not and act accordingly.
        // This avoids multiple running clients at the same time and thus, redundant data and processing.
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    // Here the Google Api Client gets built
    // More info at https://developers.google.com/android/guides/api-client
    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // When the user has a connection to the GPS, the following functionality is executed
    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        // Mandatory permission check #2...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    // If the connection to GPS is suspended, the user is notified with a toast
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    // If the connection to GPS fails, the user is notified with a toast
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    // From this method we can send information about the location change and compare this with
    // whatever other locations.
    // Toasts update in real time and show the lat/lng coordinates of the users updated positions.
    // This should ultimately be moved to LOG level.
    @Override
    public void onLocationChanged(Location location) {
    Log.d("OnLocationChanged", "Called");
        //place marker at current position
        //mMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = mMap.addMarker(markerOptions);
        Log.d("MapsActivity: ", "Coordinates " + latLng);
        Toast.makeText(this, "Location Changed:" + "\n" + latLng, Toast.LENGTH_SHORT).show();

        // Zooming to users current position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    private void addProximityAlert(double latitude, double longitude) {

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        // Mandatory permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.addProximityAlert(
                latitude, // the latitude of the central point of the alert region
                longitude, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );

        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        myBroadCast = new ProximityIntentReceiver();
        registerReceiver(myBroadCast, filter);

    }

    // Method for saving the last known user GPS location as a proximity alert to the systems SharedPrefences
    private void saveProximityAlertPoint() {
        // Mandatory permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location==null) {
            Toast.makeText(this, "No last known location. Aborting...",
                    Toast.LENGTH_LONG).show();
            return;
        }
        saveCoordinatesInPreferences((float) location.getLatitude(),
                (float) location.getLongitude());
        addProximityAlert(location.getLatitude(), location.getLongitude());
        // We add a marker to the position where an alert was saved
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("Last saved location"));

    }

    // Method for saving desired coordinates in SharedPreferences
    private void saveCoordinatesInPreferences(float latitude, float longitude) {
        SharedPreferences prefs =
                this.getSharedPreferences(getClass().getSimpleName(),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putFloat(POINT_LATITUDE_KEY, latitude);
        prefsEditor.putFloat(POINT_LONGITUDE_KEY, longitude);
        prefsEditor.commit();
    }

    // Method for retrieving saved coordinates from SharedPreferences
    private Location retrievelocationFromPreferences() {
        SharedPreferences prefs =
                this.getSharedPreferences(getClass().getSimpleName(),
                        Context.MODE_PRIVATE);
        Location location = new Location("POINT_LOCATION");
        location.setLatitude(prefs.getFloat(POINT_LATITUDE_KEY, 0));
        location.setLongitude(prefs.getFloat(POINT_LONGITUDE_KEY, 0));
        return location;
    }

    // Button for adding markers to the map and saving that location to memory
    public void markerClick(View view){
        saveProximityAlertPoint();

    }
    // onStop method for the activity to remove intent receivers and handle other exiting actions
    @Override
    protected void onStop() {
        if(myBroadCast != null) {
            unregisterReceiver(myBroadCast);
        }
        super.onStop();
    }

}