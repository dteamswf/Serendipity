package com.softwarefactory.teamdelta.serendipity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softwarefactory.teamalpha.serendipity.R;

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
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
