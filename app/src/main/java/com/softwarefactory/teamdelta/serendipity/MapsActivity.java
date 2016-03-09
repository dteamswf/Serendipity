package com.softwarefactory.teamdelta.serendipity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //Static GPS coordinates for specific places in Italy
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
        // Here we add a few markers to various places for demo purposes
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
        // Move the camera to given location and zoom in to specific level
        mMap.moveCamera(CameraUpdateFactory.newLatLng(BOLZANO));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
    }
}
