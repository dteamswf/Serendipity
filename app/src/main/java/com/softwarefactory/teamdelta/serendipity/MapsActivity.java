package com.softwarefactory.teamdelta.serendipity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*
*  Created by Riku Suomela 2016
*
* This class integrates the Goole Maps API to the application. Information about saved recordings
* should be passed here for it to be possible to view those on the map as markers.
*
* TODO: This class provides information about the GPS proximity for the applications alert system to work.
*/
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //Static GPS coordinates for specific places in Italy, dummy data
    static final LatLng BOLZANO = new LatLng(46.4983, 11.3548);
    static final LatLng SARENTINO = new LatLng(46.6412, 11.3541);
    static final LatLng AUER = new LatLng(46.3481, 11.2990);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Here the Google maps is manipulated and configured once it is launched in the app.
     * If the device doesn't have google services, the user is prompted to install those
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Disable Maps toolbar (navigation and marker)
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // Here we get the user location and set the camera zoom to it
        mMap.setMyLocationEnabled(true);
        Location userLocation = mMap.getMyLocation();
        LatLng myLocation = null;
        if (userLocation != null) {
            myLocation = new LatLng(userLocation.getLatitude(),
                    userLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
            //10));
            // Move the camera to given location and zoom in to specific level
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(BOLZANO));
            // mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        }
        // Here we add a few markers to various places for demo purposes, hard coded dummy data
        // Add a marker in Bolzano, Italy
        Marker bolzano = mMap.addMarker(new MarkerOptions()
                .position(BOLZANO)
                .title("Soundtrack #1")
                .snippet("The story of this soundtrack..."));
        bolzano.showInfoWindow();
        // Add a marker in Sarentino, Italy
        Marker sarentino = mMap.addMarker(new MarkerOptions()
                .position(SARENTINO)
                .title("Soundtrack #2")
                .snippet("The story of this soundtrack..."));
        // Add a marker in Auer, Italy
        Marker auer = mMap.addMarker(new MarkerOptions()
                .position(AUER)
                .title("Soundtrack #3")
                .snippet("The story of this soundtrack..."));

    }
}
